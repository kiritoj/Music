package com.example.music.bean

import com.google.gson.annotations.SerializedName

/**
 * Created by tk on 2019/8/22
 * 歌单分类
 */
class CategoryBean {

    /**
     * code : 200
     * msg : OK
     * timestamp : 1566381617865
     * data : {"all":{"imgId":0,"activity":false,"resourceCount":1000,"name":"全部歌单","type":0,"category":4,"hot":false,"resourceType":0},"sub":[{"imgId":0,"activity":false,"resourceCount":1000,"name":"流行","type":0,"category":1,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"影视原声","type":0,"category":4,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"华语","type":0,"category":0,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"清晨","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"怀旧","type":0,"category":3,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"摇滚","type":0,"category":1,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"夜晚","type":0,"category":2,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"清新","type":0,"category":3,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"ACG","type":0,"category":4,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"欧美","type":1,"category":0,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"儿童","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"学习","type":0,"category":2,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":821,"name":"民谣","type":0,"category":1,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"浪漫","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"日语","type":1,"category":0,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"工作","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"电子","type":0,"category":1,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"校园","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"性感","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"韩语","type":1,"category":0,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"午休","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"游戏","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"伤感","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"舞曲","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":623,"name":"粤语","type":1,"category":0,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"小语种","type":1,"category":0,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"下午茶","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"70后","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"说唱","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"治愈","type":0,"category":3,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"轻音乐","type":0,"category":1,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"80后","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"放松","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"地铁","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"爵士","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"90后","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"驾车","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"孤独","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"感动","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"运动","type":0,"category":2,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"网络歌曲","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"乡村","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"兴奋","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"KTV","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"旅行","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"R&B/Soul","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"古典","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"快乐","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"散步","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"经典","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"翻唱","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"安静","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"民族","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"酒吧","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"思念","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"吉他","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"英伦","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"金属","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"钢琴","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"朋克","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"器乐","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"榜单","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"蓝调","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"雷鬼","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"00后","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"世界音乐","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"拉丁","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"另类/独立","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"New Age","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":904,"name":"古风","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"后摇","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"Bossa Nova","type":1,"category":1,"hot":false,"resourceType":0}],"code":200,"categories":{"0":"语种","1":"风格","2":"场景","3":"情感","4":"主题"}}
     */

    var code: Int = 0
    var msg: String? = null
    var timestamp: Long = 0
    var data: DataBean? = null

    class DataBean {
        /**
         * all : {"imgId":0,"activity":false,"resourceCount":1000,"name":"全部歌单","type":0,"category":4,"hot":false,"resourceType":0}
         * sub : [{"imgId":0,"activity":false,"resourceCount":1000,"name":"流行","type":0,"category":1,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"影视原声","type":0,"category":4,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"华语","type":0,"category":0,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"清晨","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"怀旧","type":0,"category":3,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"摇滚","type":0,"category":1,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"夜晚","type":0,"category":2,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"清新","type":0,"category":3,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"ACG","type":0,"category":4,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"欧美","type":1,"category":0,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"儿童","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"学习","type":0,"category":2,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":821,"name":"民谣","type":0,"category":1,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"浪漫","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"日语","type":1,"category":0,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"工作","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"电子","type":0,"category":1,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"校园","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"性感","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"韩语","type":1,"category":0,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"午休","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"游戏","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"伤感","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"舞曲","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":623,"name":"粤语","type":1,"category":0,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"小语种","type":1,"category":0,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"下午茶","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"70后","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"说唱","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"治愈","type":0,"category":3,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"轻音乐","type":0,"category":1,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"80后","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"放松","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"地铁","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"爵士","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"90后","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"驾车","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"孤独","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"感动","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"运动","type":0,"category":2,"hot":true,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"网络歌曲","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"乡村","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"兴奋","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"KTV","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"旅行","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"R&B/Soul","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"古典","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"快乐","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"散步","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"经典","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"翻唱","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"安静","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"民族","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"酒吧","type":1,"category":2,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"思念","type":1,"category":3,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"吉他","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"英伦","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"金属","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"钢琴","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"朋克","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"器乐","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"榜单","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"蓝调","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"雷鬼","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"00后","type":1,"category":4,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"世界音乐","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"拉丁","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"另类/独立","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"New Age","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":904,"name":"古风","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"后摇","type":1,"category":1,"hot":false,"resourceType":0},{"imgId":0,"activity":false,"resourceCount":1000,"name":"Bossa Nova","type":1,"category":1,"hot":false,"resourceType":0}]
         * code : 200
         * categories : {"0":"语种","1":"风格","2":"场景","3":"情感","4":"主题"}
         */

        var all: AllBean? = null
        var code: Int = 0
        var categories: CategoriesBean? = null
        var sub: List<SubBean>? = null

        class AllBean {
            /**
             * imgId : 0
             * activity : false
             * resourceCount : 1000
             * name : 全部歌单
             * type : 0
             * category : 4
             * hot : false
             * resourceType : 0
             */

            var imgId: Int = 0
            var isActivity: Boolean = false
            var resourceCount: Int = 0
            var name: String? = null
            var type: Int = 0
            var category: Int = 0
            var isHot: Boolean = false
            var resourceType: Int = 0
        }

        class CategoriesBean {
            /**
             * 0 : 语种
             * 1 : 风格
             * 2 : 场景
             * 3 : 情感
             * 4 : 主题
             */

            @SerializedName("0")
            var `_$0`: String? = null
            @SerializedName("1")
            var `_$1`: String? = null
            @SerializedName("2")
            var `_$2`: String? = null
            @SerializedName("3")
            var `_$3`: String? = null
            @SerializedName("4")
            var `_$4`: String? = null
        }

        class SubBean {
            /**
             * imgId : 0
             * activity : false
             * resourceCount : 1000
             * name : 流行
             * type : 0
             * category : 1
             * hot : true
             * resourceType : 0
             */

            var imgId: Int = 0
            var isActivity: Boolean = false
            var resourceCount: Int = 0
            var name: String? = null
            var type: Int = 0
            var category: Int = 0
            var isHot: Boolean = false
            var resourceType: Int = 0
        }
    }
}
