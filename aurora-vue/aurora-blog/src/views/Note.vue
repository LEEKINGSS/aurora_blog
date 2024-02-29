<template>
  <div>
    <ul>
      <template v-if="haveNotes === true">
        <li v-for="note in notes" :key="note.id">
          <NoteCard :data="note" @tabChange="handleTabChange" @tagChange="handleTagChange"></NoteCard>
        </li>
      </template>
    </ul>
    <template v-if="loadMore === true">
      <div id="container">
        <div id="primary"></div>
        <div id="pager"><a href="#" class="more" @click.prevent="pageChangeHanlder">加载更多</a></div>
      </div>
    </template>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, toRefs, reactive } from 'vue'
import { NoteCard } from '@/components/NoteCard'
import { useAppStore } from '@/stores/app'
import { useUserStore } from '@/stores/user'
import { useNoteStore } from '@/stores/note'
import api from '@/api/api'
import markdownToHtml from '@/utils/markdown'

export default defineComponent({
  name: 'Note',
  components: {
    NoteCard
  },
  setup() {
    const userStore = useUserStore()
    const noteStore = useNoteStore()
    const appStore = useAppStore()
    const pagination = reactive({
      size: 10,
      total: 0,
      current: 1
    })
    const reactiveData = reactive({
      haveNotes: false,
      loadMore: true
    })
    onMounted(() => {
      fetchNotes()
      appStore.loadNoteStyle()
    })
    const fetchNotes = () => {
      pagination.current = userStore.page
      reactiveData.haveNotes = false
      api
        .getNotes({
          current: pagination.current,
          size: pagination.size
        })
        .then(({ data }) => {
          if (data.flag) {
            data.data.records.forEach((item: any) => {
              item.noteContent = markdownToHtml(item.noteContent)
                .replace(/<\/?[^>]*>/g, '')
                .replace(/[|]*\n/, '')
                .replace(/&npsp;/gi, '')
            })
            prePageChangeHanlder(data)
            noteStore.allNotes = data.data.records
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
          collectionId: collectionId
        })
        .then(({ data }) => {
          if (data.flag) {
            data.data.records.forEach((item: any) => {
              item.noteContent = markdownToHtml(item.noteContent)
                .replace(/<\/?[^>]*>/g, '')
                .replace(/[|]*\n/, '')
                .replace(/&npsp;/gi, '')
            })
            prePageChangeHanlder(data)
            noteStore.allNotes = data.data.records
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
            prePageChangeHanlder(data)
            noteStore.allNotes = data.data.records
            pagination.total = data.data.count
            reactiveData.haveNotes = true
          }
        })
    }
    /**
     * 对笔记进行分页操作
     */
    const prePageChangeHanlder = (data: any) => {
      if (pagination.current * pagination.size > data.data.count) {
        noteStore.notes = data.data.records.slice(0, data.data.count)
        reactiveData.loadMore = false
      } else {
        noteStore.notes = data.data.records.slice(0, pagination.current * pagination.size)
      }
    }
    const pageChangeHanlder = () => {
      const notes = noteStore.allNotes
      pagination.current++
      if (pagination.current * pagination.size > pagination.total) {
        noteStore.notes = notes.slice(0, pagination.total)
        reactiveData.loadMore = false
      } else {
        noteStore.notes = notes.slice(0, pagination.current * pagination.size)
      }
    }

    const handleTabChange = (collectionId: any) => {
      fetchNotesByCollectionId(collectionId)
    }
    const handleTagChange = (tagId: any) => {
      fetchNotesByTagId(tagId)
    }
    return {
      ...toRefs(reactiveData),
      ...toRefs(noteStore.$state),
      handleTabChange,
      handleTagChange,
      pageChangeHanlder
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

<!-- 通过@import语法将.styl文件导入到当前组件的样式中 -->
<style lang="stylus" scoped>
@import '@/styles/source/css/obsidian.styl';
/* 其他样式 */
</style>