<template>
  <nav class="items-center flex-1 hidden lg:flex">
    <ul class="flex flex-row list-none px-6 text-white index-css">
      <li
        class="not-italic font-medium text-xs h-full relative flex flex-col items-center justify-center cursor-pointer text-center py-4 px-2"
        v-for="route in routes"
        :key="route.path">
        <div
          class="nav-link text-sm block px-1.5 py-0.5 rounded-md relative uppercase cursor-pointer"
          @click="pushPage(route.path)"
          v-if="route.children && route.children.length === 0"
          :data-menu="route.name">
          <span class="relative z-50" v-if="$i18n.locale === 'cn' && route.i18n.cn">
            {{ route.i18n.cn }}
          </span>
          <span class="relative z-50" v-else-if="$i18n.locale === 'en' && route.i18n.en">
            {{ route.i18n.en }}
          </span>
          <span class="relative z-50" v-else>{{ route.name }}</span>
        </div>
        <Dropdown
          @command="pushPage"
          hover
          v-else
          class="nav-link text-sm block px-1.5 py-0.5 rounded-md relative uppercase">
          <span class="relative z-50" v-if="$i18n.locale === 'cn' && route.i18n.cn">
            {{ route.i18n.cn }}
          </span>
          <span class="relative z-50" v-else-if="$i18n.locale === 'en' && route.i18n.en">
            {{ route.i18n.en }}
          </span>
          <span class="relative z-50" v-else>{{ route.name }}</span>
          <DropdownMenu>
            <DropdownItem v-for="sub in route.children" :key="sub.path" :name="sub.path">
              <span class="relative z-50" v-if="$i18n.locale === 'cn' && sub.i18n.cn">
                {{ sub.i18n.cn }}
              </span>
              <span class="relative z-50" v-else-if="$i18n.locale === 'en' && sub.i18n.en">
                {{ sub.i18n.en }}
              </span>
              <span class="relative z-50" v-else>{{ sub.name }}</span>
            </DropdownItem>
          </DropdownMenu>
        </Dropdown>
      </li>
    </ul>
    <div class="flex flex-row player">
      <Music v-if="!isMobile"/>
    </div>
  </nav>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, reactive, toRefs } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { Dropdown, DropdownMenu, DropdownItem } from '@/components/Dropdown'
import { isExternal } from '@/utils/validate'
import config from '@/config/config'
import api from '@/api/api'
import { useAppStore } from '@/stores/app'
import { Music } from '@/components/Music'
import { useCommonStore } from '@/stores/common'


export default defineComponent({
  name: 'Navigation',
  components: { Dropdown, DropdownMenu, DropdownItem, Music },
  setup() {
    const { t, te } = useI18n()
    const router = useRouter()
    const appStore = useAppStore()
    const commonStore = useCommonStore() // 是否为手机端
    const pushPage = (path: string): void => {
      if (!path) return
      if (isExternal(path)) {
        window.location.href = path
      } else {
        router.push({
          path: path
        })
      }
    }
    const reactiveData = reactive({
      albums: [] as any
    })
    onMounted(() => {
      api.getAlbums().then(({ data }) => {
        reactiveData.albums = data.data
      })
    })
    const openPhotoAlbum = (id: any): void => {
      router.push('/photos/' + id)
    }
    return {
      isMobile: computed(() => commonStore.isMobile),
      ...toRefs(reactiveData),
      routes: config.routes,
      pushPage,
      openPhotoAlbum,
      te,
      t
    }
  }
})
</script>

<style lang="scss" scoped>
.nav-link {
  @apply hover:text-ob-bright;
  &:hover {
    &:before {
      @apply opacity-60;
    }
  }

  &:before {
    @apply absolute rounded-lg opacity-0 transition bg-ob-deep-800 z-40;
    content: '';
    top: -4px;
    left: -4px;
    width: calc(100% + 8px);
    height: calc(100% + 8px);
  }
}

.index-css {
  color: var(--text-note-header)
}
.player{
  padding-left: 25%;
}
</style>
