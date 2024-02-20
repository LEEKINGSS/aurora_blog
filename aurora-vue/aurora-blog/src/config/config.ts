import api from '@/api/api'
import {reactive} from "vue";

const reactiveData = reactive({
    albums: [] as any
});

interface route {
    name: string,
    path: string,
    i18n: {
        cn: string,
        en: string
    },
    children: Array<route>
}

// 查询相册中的子相册
function getConfigDate() {
    let routes: Array<route> = [
        {
            name: 'Home',
            path: '/',
            i18n: {
                cn: '首页',
                en: 'Home'
            },
            children: []
        },
        {
            name: 'Note',
            path: '/note',
            i18n: {
                cn: '笔记',
                en: 'Note'
            },
            children: []
        },
        {
            name: 'Talks',
            path: '/talks',
            i18n: {
                cn: '说说',
                en: 'talks'
            },
            children: []
        },
        {
            name: 'About',
            path: '/about',
            i18n: {
                cn: '关于',
                en: 'About'
            },
            children: []
        },
        {
            name: 'Archives',
            path: '/archives',
            i18n: {
                cn: '归档',
                en: 'Archives'
            },
            children: []
        },
        {
            name: 'Tags',
            path: '/tags',
            i18n: {
                cn: '标签',
                en: 'Tags'
            },
            children: []
        },
        {
            name: 'Message',
            path: '/message',
            i18n: {
                cn: '留言',
                en: 'Message'
            },
            children: []
        },
        {
            name: 'Friends',
            path: '/friends',
            i18n: {
                cn: '友链',
                en: 'Friends'
            },
            children: []
        },
        {
            name: 'PhotoAlbums',
            path: '/photos',
            i18n: {
                cn: '相册',
                en: 'PhotoAlbums'
            },
            children: [
                {
                    name: 'PhotoAlbums',
                    path: '/photos',
                    i18n: {
                        cn: '相册',
                        en: 'PhotoAlbums'
                    },
                    children: []
                }
            ]
        }
    ]
    api.getAlbums().then(res => {
        reactiveData.albums = res.data;
        for (const item of reactiveData.albums.data) {
            const route = {
                name: item.albumName,
                path: `/photos/${item.id}`,
                i18n: {
                    cn: item.albumName,
                    en: item.albumName
                },
                children: []
            }
            routes[8].children.push(route);
            routes[8].children.shift();
        }
    });
    return routes;
}
export default {
    captcha: {
        TENCENT_CAPTCHA: '#'
    },
    qqLogin: {
        QQ_APP_ID: '102068044',
        QQ_REDIRECT_URI: 'https://blog.liking.world/oauth/login/qq'
    },
    routes: getConfigDate()
}