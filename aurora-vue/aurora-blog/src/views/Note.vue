<template>
  <ul>
    <template v-if="haveNotes === true">
      <li v-for="note in notes" :key="note.id">
        <NoteCard :data="note" @tabChange="handleTabChange" @tagChange="handleTagChange"></NoteCard>
      </li>
    </template>
  </ul>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, toRefs, toRef, reactive } from 'vue'
import { Feature, FeatureList } from '@/components/Feature'
import { ArticleCard, HorizontalArticle } from '@/components/ArticleCard'
import { Title } from '@/components/Title'
import { Sidebar, Profile, RecentComment, TagBox, Notice, WebsiteInfo } from '@/components/Sidebar'
import { NoteCard } from '@/components/NoteCard'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { useNoteStore } from '@/stores/note'
import { useCollectionStore } from '@/stores/collection'
import { useCategoryStore } from '@/stores/Category'
import { useI18n } from 'vue-i18n'
import Paginator from '@/components/Paginator.vue'
import api from '@/api/api'
import markdownToHtml from '@/utils/markdown'

export default defineComponent({
  name: 'Note',
  components: {
    NoteCard
  },
  setup() {
    const appStore = useAppStore()
    const userStore = useUserStore()
    const noteStore = useNoteStore()
    const collectionStore = useCollectionStore()
    const { t } = useI18n()
    const pagination = reactive({
      size: 10,
      total: 0,
      current: 1
    })
    const reactiveData = reactive({
      haveNotes: false
    })
    onMounted(() => {
      fetchCollections()
      fetchNotes()
      loadStyle()
    })
    const loadStyle = () => {
      let element = document.querySelector('#app .app-wrapper .app-container') as HTMLElement
      if (element) {
        // 修改全局margin值
        element.style.margin = '0'
      }
      let maxScreen = document.querySelector(' .lg\\:max-w-screen-2xl') as HTMLElement
      if (maxScreen) {
        // 修改屏幕最大宽度
        maxScreen.style.maxWidth = '100%'
      }
      let padding = document.querySelector(' .lg\\:px-8') as HTMLElement
      if (padding) {
        // 修改padding值
        padding.style.padding = '0'
      }
      let position = document.querySelector('.header-container') as HTMLElement
      if (position) {
        // 修改页面布局
        position.style.position = 'absolute'
        position.style.width = '80%'
        position.style.left = '9%'
      }
    }
    const fetchCollections = () => {
      collectionStore.collections = []
      api.getAllCollections().then(({ data }) => {
        collectionStore.collections.push(...data.data)
      })
    }
    const fetchNotes = () => {
      pagination.current = userStore.page
      reactiveData.haveNotes = false
      api
        .getNotes({
          current: pagination.current,
          size: pagination.size
        })
        .then(({ data }) => {
          console.log(data)
          if (data.flag) {
            data.data.records.forEach((item: any) => {
              item.noteContent = markdownToHtml(item.noteContent)
                .replace(/<\/?[^>]*>/g, '')
                .replace(/[|]*\n/, '')
                .replace(/&npsp;/gi, '')
            })
            noteStore.notes = data.data.records
            pagination.total = data.data.count
            reactiveData.haveNotes = true
          }
        })
    }
    /**
     * 通过合集id获取文章信息
     * @param collectionId
     */
    const fetchNotesByCollectionId = (collectionId: any) => {
      reactiveData.haveNotes = false
      api
        .getNotesByCollectionId({
          current: pagination.current,
          size: pagination.size,
          collectionId: collectionId
        })
        .then(({ data }) => {
          console.log(data)
          if (data.flag) {
            data.data.records.forEach((item: any) => {
              item.noteContent = markdownToHtml(item.noteContent)
                .replace(/<\/?[^>]*>/g, '')
                .replace(/[|]*\n/, '')
                .replace(/&npsp;/gi, '')
            })
            noteStore.notes = data.data.records
            pagination.total = data.data.count
            reactiveData.haveNotes = true
          }
        })
    }
    /**
     * 通过标签id获取文章信息
     */
    const fetchNotesByTagId = (tagId: any) => {
      reactiveData.haveNotes = false
      api
        .getNotesByTagId({
          current: pagination.current,
          size: pagination.size,
          tagId: tagId
        })
        .then(({ data }) => {
          if (data.flag) {
            data.data.records.forEach((item: any) => {
              item.noteContent = markdownToHtml(item.noteContent)
                .replace(/<\/?[^>]*>/g, '')
                .replace(/[|]*\n/, '')
                .replace(/&npsp;/gi, '')
            })
            noteStore.notes = data.data.records
            pagination.total = data.data.count
            reactiveData.haveNotes = true
          }
        })
    }
    /**
     * 对笔记进行分页操作
     */
    const pageChangeHanlder = (notes: any) => {
      if (pagination.current * pagination.size > pagination.total) {
        noteStore.notes = notes.slice(0, pagination.total)
      } else {
        noteStore.notes = notes.slice(0, pagination.current * pagination.size)
      }
      pagination.current++
    }
    const handleTabChange = (collectionId: any) => {
      fetchNotesByCollectionId(collectionId)
    }
    const handleTagChange = (tagId: any) => {
      fetchNotesByTagId(tagId)
      pageChangeHanlder(noteStore.notes)
    }
    return {
      ...toRefs(reactiveData),
      ...toRefs(noteStore.$state),
      handleTabChange,
      handleTagChange
    }
  }
})
</script>

<style lang="scss">
.home-article {
  .article-content {
    p {
      overflow: hidden;
      text-overflow: ellipsis;
      display: -webkit-box;
      -webkit-line-clamp: 5;
      -webkit-box-orient: vertical;
    }

    .article-footer {
      margin-top: 13px;
    }
  }
}
</style>
