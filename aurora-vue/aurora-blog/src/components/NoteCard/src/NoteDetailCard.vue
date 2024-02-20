<template>
  <div class="article-container">
    <div class="article" @click="toNote" >
      <div class="article-thumbnail">
        <img v-if="note.noteCover" v-lazy="note.noteCover" alt="" />
        <img v-else src="@/assets/default-cover.jpg" />
        <span class="thumbnail-screen" :style="gradientBackground" />
      </div>
      <div class="article-content">
        <span>
          <b v-if="note.collectionName">
            {{ note.collectionName }}
          </b>
          <ob-skeleton v-else tag="b" height="20px" width="35px" />
          <ul v-if="note.tags && note.tags.length > 0">
            <li v-for="tag in note.tags" :key="tag.id">
              <em># {{ tag.tagName }}</em>
            </li>
          </ul>
          <ul v-else-if="note.tags && note.tags.length <= 0">
            <li>
              <em># {{ t('settings.default-tag') }}</em>
            </li>
          </ul>
          <ul v-else>
            <ob-skeleton v-if="!note.tags" :count="2" tag="li" height="16px" width="35px" />
          </ul>
        </span>
        <h1 class="article-title" v-if="note.noteTitle" @click.stop="tonote" data-dia="article-link">
          <a>
            <span> {{ note.noteTitle }}</span>
            <svg-icon v-if="note.status == 2" icon-class="lock" class="lock-svg" />
          </a>
        </h1>
        <ob-skeleton v-else tag="h1" height="3rem" />
        <p v-if="note.noteContent">{{ note.noteContent }}</p>
        <ob-skeleton v-else tag="p" :count="5" height="16px" />
        <div class="article-footer" v-if="note.author && note.createTime">
          <div class="flex flex-row items-center">
            <img
              class="hover:opacity-50 cursor-pointer"
              :src="note.author.avatar || ''"
              alt="author avatar"
              @click.stop="handleAuthorClick(note.author.website)" />
            <span class="text-ob-dim">
              <strong
                class="text-ob-normal pr-1.5 hover:text-ob hover:opacity-50 cursor-pointer"
                @click.stop="handleAuthorClick(note.author.website)">
                {{ note.author.nickname }}
              </strong>
              {{ t('settings.shared-on') }} {{ t(`settings.months[${new Date(note.createTime).getMonth()}]`) }}
              {{ new Date(note.createTime).getDate() }}, {{ new Date(note.createTime).getFullYear() }}
            </span>
          </div>
        </div>
        <div class="article-footer" v-else>
          <div class="flex flex-row items-center mt-6">
            <ob-skeleton class="mr-2" height="28px" width="28px" :circle="true" />
            <span class="text-ob-dim mt-1">
              <ob-skeleton height="20px" width="150px" />
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, toRefs, getCurrentInstance } from 'vue'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import emitter from '@/utils/mitt'
import SvgIcon from '@/components/SvgIcon/index.vue'

export default defineComponent({
  name: 'NoteDetailCard',
  components: { SvgIcon },
  props: ['data'],
  setup(props) {
    const proxy: any = getCurrentInstance()?.appContext.config.globalProperties
    const appStore = useAppStore()
    const userStore = useUserStore()
    const router = useRouter()
    const { t } = useI18n()
    const handleAuthorClick = (link: string) => {
      if (link === '') link = window.location.href
      window.open(link)
    }
    const toNote = () => {
      let isAccess = false
      userStore.accessNotes.forEach((item: any) => {
        if (item == props.data.id) {
          isAccess = true
        }
      })
      if (props.data.status === 2 && isAccess == false) {
        if (userStore.userInfo === '') {
          proxy.$notify({
            title: 'Warning',
            message: '该笔记受密码保护,请登录后访问',
            type: 'warning'
          })
        } else {
          emitter.emit('changeNotePasswordDialogVisible', props.data.id)
        }
      } else {
        router.push({ path: '/notes/' + props.data.id })
      }
    }
    return {
      gradientBackground: computed(() => {
        return { background: appStore.themeConfig.header_gradient_css }
      }),
      note: toRefs(props).data,
      handleAuthorClick,
      toNote,
      t
    }
  }
})
</script>

<style lang="scss" scoped>
.article-title:hover {
  cursor: default;
}
</style>
