<script setup lang="js">
import Typed from '@/styles/source/js/typed.min.js'
import { defineComponent } from 'vue'
export default defineComponent({
  name: 'Screen',
  mounted() {
    const options = {
      strings: ['Think like an artist, develop like an artisan', '艺术家思维去思考问题，工匠创造精神去开发'],
      typeSpeed: 90,
      loop: true,
      loopCount: Infinity,
      backSpeed: 20,
    };

    const typed = new Typed(this.$refs.typedElement, options);
  }
})
</script>
<template>
  <div class="screen-gradient-after">
    <div class="screen-gradient-content">
      <div class="screen-gradient-content-inside">
        <div class="bold-underline-links screen-gradient-sponsor">
          <p>
            <span class="typed-text" :id="screen" ref="typedElement"></span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

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