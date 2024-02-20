import { defineStore } from 'pinia'

export const useCollectionStore = defineStore('collectionStore', {
  state: () => {
    return {
        collections:[] as any[]
    }
  },
  actions: {}
})
