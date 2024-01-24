import { defineStore } from 'pinia'
import { AuroraDia, DiaConfig } from '@/utils/aurora-dia'
import cookies from "js-cookie";

export const useDiaStore = defineStore('diaStore', {
  state: () => {
    return {
      dia: new AuroraDia(),
      aurora_bot: {
        enable: true,
        locale: cookies.get('locale') ? String(cookies.get('locale')) : 'cn',
        bot_type: 'dia'
      }
    }
  },
  actions: {
    initializeBot(configs: DiaConfig): void {
      this.dia.installSoftware(configs)
      this.dia.on()
    }
  }
})

