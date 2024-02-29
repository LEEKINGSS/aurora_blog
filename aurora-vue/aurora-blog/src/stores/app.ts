import { defineStore } from 'pinia'
import { i18n } from '@/locales/index'
import cookies from 'js-cookie'
import nProgress from 'nprogress'
import 'nprogress/nprogress.css'

nProgress.configure({
  showSpinner: false,
  trickleSpeed: 100,
  parent: '#loading-bar-wrapper'
})

const setTheme = (theme: string) => {
  if (theme === 'theme-dark') {
    document.body.classList.remove('theme-light')
    document.body.classList.add('theme-dark')
  } else {
    document.body.classList.remove('theme-dark')
    document.body.classList.add('theme-light')
  }
}

export const useAppStore = defineStore('appStore', {
  state: () => {
    return {
      themeConfig: {
        theme: cookies.get('theme') ? String(cookies.get('theme')) : 'theme-dark',
        profile_shape: 'circle-avatar',
        feature: true,
        gradient: {
          color_1: '#24c6dc',
          color_2: '#5433ff',
          color_3: '#ff0099'
        },
        header_gradient_css: 'linear-gradient(130deg, #24c6dc, #5433ff 41.07%, #ff0099 76.05%)',
        background_gradient_style: {
          background: 'linear-gradient(130deg, #24c6dc, #5433ff 41.07%, #ff0099 76.05%)',
          '-webkit-background-clip': 'text',
          '-webkit-text-fill-color': 'transparent',
          '-webkit-box-decoration-break': 'clone',
          'box-decoration-break': 'clone'
        }
      },
      appLoading: false,
      websiteConfig: '' as any,
      viewCount: 0,
      articleCount: 0,
      talkCount: 0,
      categoryCount: 0,
      tagCount: 0,
      NPTimeout: -1,
      loadingTimeout: -1,
      aurora_bot_enable: true
    }
  },
  actions: {
    loadStyle() {
      let element = document.querySelector('#app .app-wrapper .app-container') as HTMLElement
      if (element) {
        let marginValue = window.getComputedStyle(element).getPropertyValue('margin')
        if (marginValue == '0px') {
          // 修改全局margin值
          element.style.margin = '0 auto'
          // 修改背景颜色
          element.style.backgroundColor = 'var(--background-primary-note)'
        }
        let maxScreen = document.querySelector(' .lg\\:max-w-screen-2xl') as HTMLElement
        if (maxScreen) {
          // 修改屏幕最大宽度
          maxScreen.style.maxWidth = '1536px'
        }
        let padding = document.querySelector(' .lg\\:px-8') as HTMLElement
        if (padding) {
          // 修改padding值
          padding.style.paddingLeft = '2rem'
          padding.style.paddingRight = '2rem'
        }
        let position = document.querySelector('.header-container') as HTMLElement
        if (position) {
          // 修改页面布局
          position.style.position = 'relative'
          position.style.width = '100%'
          position.style.left = '0'
        }
        let playBox = document.querySelector('.play_box') as HTMLElement
        if (playBox) {
          // 修改页面布局
          playBox.style.minWidth = '300px'
          //隐藏其余标签
          let progress = document.querySelector('.progress') as HTMLElement
          if (progress) {
            progress.style.display = ''
          }
          let timeShow = document.querySelector('.time_show') as HTMLElement
          if (timeShow) {
            timeShow.style.display = ''
          }
          let tool = document.querySelector('.tool') as HTMLElement
          if (tool) {
            tool.style.display = ''
          }
          let musicInfo = document.querySelector('.music_info') as HTMLElement
          if (musicInfo) {
            musicInfo.style.display = ''
          }
        }

      }
    },
    loadNoteStyle() {
      let element = document.querySelector('#app .app-wrapper .app-container') as HTMLElement
      if (element) {
        // 修改全局margin值
        element.style.margin = '0'
        // 修改背景颜色
        element.style.backgroundColor = 'var(--background-primary-note)'
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
      let playBox = document.querySelector('.play_box') as HTMLElement
      if (playBox) {
        // 修改页面布局
        playBox.style.minWidth = '50px'
        //隐藏其余标签
        let progress = document.querySelector('.progress') as HTMLElement
        if (progress) {
          progress.style.display = 'none'
        }
        let timeShow = document.querySelector('.time_show') as HTMLElement
        if (timeShow) {
          timeShow.style.display = 'none'
        }
        let tool = document.querySelector('.tool') as HTMLElement
        if (tool) {
          tool.style.display = 'none'
        }
        let musicInfo = document.querySelector('.music_info') as HTMLElement
        if (musicInfo) {
          musicInfo.style.display = 'none'
        }
      }

    },
    changeLocale(locale: string) {
      cookies.set('locale', locale, { expires: 7 })
      i18n.global.locale = locale
    },
    initializeTheme(mode: string) {
      setTheme(mode)
    },
    toggleTheme(isDark?: boolean) {
      this.themeConfig.theme =
        isDark === true || this.themeConfig.theme === 'theme-light' ? 'theme-dark' : 'theme-light'
      cookies.set('theme', this.themeConfig.theme, { expires: 7 })
      setTheme(this.themeConfig.theme)
    },
    startLoading() {
      if (this.appLoading === true) return
      if (this.NPTimeout !== -1) clearTimeout(this.NPTimeout)
      if (this.loadingTimeout !== -1) clearTimeout(this.loadingTimeout)
      nProgress.start()
      this.appLoading = true
    },
    endLoading() {
      this.NPTimeout = <any>setTimeout(() => {
        nProgress.done()
      }, 100)

      this.loadingTimeout = <any>setTimeout(() => {
        this.appLoading = false
      }, 300)
    }
  }
})
