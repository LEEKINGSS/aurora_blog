import { defineStore } from 'pinia'
export const useNoteStore = defineStore('noteStore', {
  state: () => {
    return {
      featuredNotes: [] as any,
      notes: [] as any,
      collections: [] as any,
      archives: [] as any,
      allNotes: [] as any
    }
  },
  actions: {
  }
})
