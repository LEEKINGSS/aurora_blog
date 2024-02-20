import { defineStore } from 'pinia'
export const useNoteStore = defineStore('noteStore', {
  state: () => {
    return {
      topNote: '' as any,
      featuredNotes: [] as any,
      notes: [] as any,
      collections: [] as any,
      archives: [] as any
    }
  },
  actions: {
  }
})
