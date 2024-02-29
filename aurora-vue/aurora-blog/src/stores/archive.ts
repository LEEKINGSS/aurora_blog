import { defineStore } from 'pinia'

export const useArchiveStore = defineStore('archiveStore', {
  state: () => {
    return {
      archives: [] as any
    }
  },
  actions: {}
})
