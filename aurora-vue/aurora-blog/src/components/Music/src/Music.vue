<template>
  <div
    class="not-italic font-medium text-xs relative flex flex-col items-center justify-center cursor-pointer text-center py-4 px-2">
    <!-- 音乐源 -->
    <audio :src="music[index]" hidden="true" controls ref="music"></audio>
    <!-- 音乐播放器 -->
    <div class="play_box">
      <!-- 音乐播放器背景 -->
      <div class="play_bc">
        <img :src="head[index]" alt="">
      </div>
      <!-- 播放组件 -->
      <div class="state">
        <img class="head" :src="head[index]" alt="">
        <div class="state_set iconfont icon-bofang" @click="musicPlaySet"></div>
      </div>
      <!-- 进度条组件 -->
      <div class="progress" @mousedown="musicMoveFa" @mouseup="musicMoveUp">
        <div class="last iconfont icon-shangyishou" @click="musicLast"></div>
        <div class="next iconfont icon-xiayishou" @click="musicNext"></div>
        <div class="line">
          <div class="pros" @mousedown="musicMove">
            <div class="Anchor"></div>
          </div>
        </div>
      </div>
      <!-- 时间组件 -->
      <div class="time_show">00:00 / 00:00</div>
      <!-- 音量组件 -->
      <div class="tool">
        <div class="volume iconfont icon-volumeMiddle" @mouseenter="volumeSet" @click="volumeBtn"
             @mouseleave="volumeLeave">
          <div class="volume_range">
            <input type="range" class="range" @focus="volumePro">
          </div>
        </div>
      </div>
      <!-- 音乐信息组件 -->
      <div class="music_info">{{ name[index] }}</div>
    </div>
  </div>
</template>

<script setup lang="js">
import { defineComponent, nextTick, onMounted, ref } from 'vue'

export default defineComponent({
  name: 'Music',
  data() {
    return {
      f1: false,
      f2: false,
      i1: 0,
      time: null,
      time2: null,
      time3: null,
      time4: null,
      nowtime: 0,
      currtime: 0,
      head: ['https://liking.oss-cn-wulanchabu.aliyuncs.com/aurora/music/head/44434fff4ae331f5531ac64bfd970ed.jpg',
        'https://liking.oss-cn-wulanchabu.aliyuncs.com/aurora/music/head/eac6f07b1e0fb2912e9116ec2d4cf06.jpg',
        'https://liking.oss-cn-wulanchabu.aliyuncs.com/aurora/music/head/4d0320b9d24f499acf1d635131dae92.png'
      ],
      music: ['https://liking.oss-cn-wulanchabu.aliyuncs.com/aurora/music/music/Call%20your%20name%20Gv-%E6%B3%BD%E9%87%8E%E5%BC%98%E4%B9%8B.320.mp3',
        'https://liking.oss-cn-wulanchabu.aliyuncs.com/aurora/music/music/Dave%20-%20%E6%AF%8F%E5%BD%93%E6%88%91.mp3',
        'https://liking.oss-cn-wulanchabu.aliyuncs.com/aurora/music/music/UnicornPhantom%20-%20%E7%8B%AC%E8%A7%92.mp3'
      ],
      name: ['Call your name',
        '每当我',
        '独角'
      ],
      x: 0,
      index: 0
    }
  },
  mounted() {
    const {
      stateSet,
      musicOBJ,
      proLine
    } = this.setupLocalVar()
    this.time3 = setInterval(() => {
      if (musicOBJ.ended) {
        proLine.style.width = '0px'
        stateSet.className = 'state_set iconfont icon-bofang'
        this.nowtime = 0
        clearInterval(this.time)
        clearInterval(this.time2)
        clearInterval(this.time3)
        clearInterval(this.time4)
        this.f1 = false
      }
    }, 1000)
  },
  methods: {
    setupLocalVar() {
      // 需要使用到的元素对象
      let stateSet = document.querySelector('.state_set')
      let musicHead = document.querySelector('.head')
      let musicOBJ = document.querySelector('audio')
      let proLine = document.querySelector('.pros')
      let timeshow = document.querySelector('.time_show')
      let musicLineMoveFa = document.querySelector('.progress')
      let musicLineMove = document.querySelector('.pros')
      let volumeBtn = document.querySelector('.volume')
      let volumeBar = document.querySelector('.volume_range')
      let volumePro = document.querySelector('.range')
      let musicLast = document.querySelector('.last')
      let musicNext = document.querySelector('.next')
      let musicBOXbc = document.querySelector('.play_bc > img')
      let musicInfoShow = document.querySelector('.music_info')
      return {
        stateSet,
        musicHead,
        musicOBJ,
        proLine,
        timeshow,
        musicLineMoveFa,
        musicLineMove,
        volumeBtn,
        volumeBar,
        volumePro,
        musicLast,
        musicNext,
        musicBOXbc,
        musicInfoShow
      }
    },
    musicPlaySet() {
      const audio = ref(null)
      const { stateSet, musicOBJ, musicHead } = this.setupLocalVar()
      // 点击暂停的时候
      if (this.f1) {
        this.musicStop()
        //点击开始的时候
      } else {
        stateSet.className = 'state_set iconfont icon-24gf-pause2'
        nextTick(() => {
          musicOBJ.play()
        })
        this.musicTime()
        this.time = setInterval(() => {
          this.i1++
          musicHead.style.transform = `rotate(${this.i1}deg)`
        }, 20)
        this.f1 = true
      }
    },
    // 音乐停止状态
    musicStop() {
      const { stateSet, musicOBJ } = this.setupLocalVar()
      stateSet.className = 'state_set iconfont icon-bofang'
      musicOBJ.pause()
      clearInterval(this.time)
      clearInterval(this.time2)
      clearInterval(this.time4)
      this.f1 = false
    },
    // 音乐对象的时间的显示方法
    musicTime() {
      const { musicOBJ, proLine, timeshow } = this.setupLocalVar()
      let time = Math.floor(musicOBJ.duration)
      let minute = Math.floor(time / 60) + ''
      minute = minute < 10 ? '0' + minute : minute
      let second = time % 60 + ''
      second = second < 10 ? '0' + second : second
      this.time4 = setInterval(() => {
        let muscurrtime = musicOBJ.currentTime
        let currminute = Math.floor(muscurrtime / 60) + ''
        currminute = currminute < 10 ? '0' + currminute : currminute
        let currsecond = Math.floor(muscurrtime % 60) + ''
        currsecond = currsecond < 10 ? '0' + currsecond : currsecond
        timeshow.innerText = `${currminute}:${currsecond} / ${minute}:${second}`

      }, 1)
      this.time2 = setInterval(() => {
        let movetime = 130 / time
        this.nowtime += movetime
        proLine.style.width = this.nowtime + 'px'
      }, 1000)
    },
    musicMove() {
      clearInterval(this.time)
      clearInterval(this.time2)
      clearInterval(this.time4)
    },
    musicMoveFa() {
      const { musicOBJ, proLine } = this.setupLocalVar()
      clearInterval(this.time)
      clearInterval(this.time2)
      clearInterval(this.time4)
      this.x = event.clientX - (window.innerWidth - 130) / 2 - 350
      console.log(this.x)
      proLine.style.width = this.x + 'px'
      this.currtime = this.x / (130 / musicOBJ.duration)
    },
    musicMoveUp() {
      const { musicOBJ, proLine, musicLineMoveFa, stateSet, musicHead } = this.setupLocalVar()
      musicLineMoveFa.onmousemove = null
      musicOBJ.currentTime = this.currtime
      proLine.style.width = this.currtime * (130 / musicOBJ.duration) + 'px'
      this.nowtime = this.currtime * (130 / musicOBJ.duration)
      this.musicTime()
      nextTick(() => {
        musicOBJ.play()
      })
      stateSet.className = 'state_set iconfont icon-24gf-pause2'
      this.time = setInterval(() => {
        this.i1++
        musicHead.style.transform = `rotate(${this.i1}deg)`
      }, 20)
      this.f1 = true
    },
    volumeSet() {
      const { volumePro, volumeBar } = this.setupLocalVar()
      volumePro.autofocus = true
      volumePro.defaultValue = 100
      volumePro.step = 1
      volumePro.max = 100
      volumePro.min = 0
      volumeBar.style.height = '100px'
      volumeBar.style.padding = '5px'
      volumeBar.style.top = '50px'
    },
    volumeBtn() {
      const { volumePro, volumeBtn, musicOBJ } = this.setupLocalVar()
      if (this.f2) {
        volumePro.disabled = true
        volumeBtn.className = 'volume iconfont icon-volumeDisable'
        musicOBJ.muted = true
        this.f2 = false
      } else {
        volumePro.disabled = false
        volumeBtn.className = 'volume iconfont icon-volumeMiddle'
        musicOBJ.muted = false
        this.f2 = true
      }
    },
    volumeLeave() {
      const { volumeBar } = this.setupLocalVar()
      volumeBar.style.height = '0px'
      volumeBar.style.padding = '0px'
      volumeBar.style.top = '0px'
    },
    volumePro() {
      const { volumePro, musicOBJ, volumeBtn } = this.setupLocalVar()
      volumeBtn.onclick = null
      this.onchange = function() {
        musicOBJ.volume = this.value / 100
        console.log(this.value)
        if (this.value === 0) {
          volumeBtn.className = 'volume iconfont icon-volumeDisable'
          this.f2 = false
        }
      }
    },
    musicLast() {
      const { musicOBJ, musicBOXbc, musicInfoShow } = this.setupLocalVar()
      if (this.index > 0) {
        this.index--
      } else {
        this.index = this.music.length - 1
      }
      // musicOBJ.src = this.music[this.index]
      // musicBOXbc.src = this.head[this.index]
      // musicInfoShow.innerText = this.name[this.index]
      this.nowtime = 0
      this.musicStop()
    },
    musicNext() {
      const { musicOBJ, musicBOXbc, musicInfoShow } = this.setupLocalVar()
      if (this.index < this.music.length - 1) {
        this.index++
      } else {
        this.index = 0
      }
      // musicOBJ.src = this.music[this.index]
      // musicBOXbc.src = this.head[this.index]
      // musicInfoShow.innerText = this.name[this.index]
      this.nowtime = 0
      this.musicStop()
    }
  }
})
</script>

<style scoped lang="css">
@import '@/styles/music/css/css.css';
@import '@/styles/music/css/icon.css';

.play_box {
  min-width: 300px;
}
</style>