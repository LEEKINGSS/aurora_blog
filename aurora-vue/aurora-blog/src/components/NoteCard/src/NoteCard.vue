<template>
  <div class="article-header-wrapper">
    <div class="article-header">
      <div class="article-covers animated fadeIn" :style="{
      animationDelay: '600ms',
      animationDuration: '1.2s',
      backgroundImage: 'radial-gradient(ellipse closest-side,var(--background-image), var(--background-primary-note)), url(' + note.noteCover + ')'
    }">
      </div>
      <div class="else">
        <p class="animated fadeInDown">
          <a href="#" v-if="note.collectionName"
             @click.prevent="$emit('tabChange',note.collectionId)"><b>「 </b>{{ note.collectionName }}<b> 」</b></a>
          {{ t('settings.shared-on') }} {{ t(`settings.months[${new Date(note.createTime).getMonth()}]`) }}
          {{ new Date(note.createTime).getDate() }}, {{ new Date(note.createTime).getFullYear() }}
        </p>
        <h3 class="post-title animated fadeInDown">
          <a class="posttitle" href="#" :title="note.noteTitle" @click.prevent="toNote">{{ note.noteTitle }}</a>
        </h3>
        <p class="post-count animated fadeInDown">
          <span>
            <b class="iconfont icon-text2"></b>
            <i>文章字数</i>
            {{ note.noteCount }}
          </span>
          <span>
            <b class="iconfont icon-timer__s"></b>
            <i>阅读约需</i>
            {{ note.noteTime }}
          </span>
        </p>
        <ul v-if="note.tags && note.tags.length > 0" class="animated fadeInDown post-tags-list" itemprop="keywords">
          <li v-for="tag in note.tags" :key="tag.id" class="animated fadeInDown post-tags-list-item"><a
            class="animated fadeInDown post-tags-list-link"
            href="#" rel="tag" @click.prevent="$emit('tagChange',tag.id)">{{ tag.tagName }}</a></li>
        </ul>
        <div class="md-content animated fadeIn">
          <p>{{ note.noteContent }}</p>
          <span class="read-more"><a class="posttitle" href="#" title="查看全文" @click.prevent="toNote">查看全文</a>
                <b class="iconfont icon-arrow-right- gradient-text"></b></span>
        </div>
      </div>
    </div>
  </div>
  <Screen :data="note.noteQuotes"></Screen>
</template>

<script lang="ts">
import { defineComponent, toRefs, getCurrentInstance } from 'vue'
import { Screen } from '@/components/NoteCard'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import emitter from '@/utils/mitt'

export default defineComponent({
  name: 'NoteCard',
  components: { Screen },
  emits: ['tabChange', 'tagChange'],
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
            message: '该文章受密码保护,请登录后访问',
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
      note: toRefs(props).data,
      handleAuthorClick,
      toNote,
      t
    }
  }
})
</script>

<!-- 通过@import语法将.styl文件导入到当前组件的样式中 -->
<style lang="stylus" scoped>
@import '@/styles/source/css/obsidian.styl';
/* 其他样式 */
</style>

<style lang="css" scoped>
/* 其他样式 */
@import '@/styles/source/css/animate.min.css';
@import '@/styles/source/css/font_1429596_nzgqgvnmkjb.css';
</style>