package com.example.music.bean

/**
 * Created by tk on 2019/9/1
 * 根据歌曲id获得的歌曲bean类
 */
class MusicBean {

    /**
     * code : 200
     * msg : OK
     * timestamp : 1567348415583
     * data : {"privileges":[{"st":0,"flag":0,"subp":1,"fl":999000,"fee":0,"dl":128000,"cp":1,"preSell":false,"cs":false,"toast":false,"maxbr":128000,"id":37239038,"pl":128000,"sp":7,"payed":0}],"code":200,"songs":[{"no":1,"copyright":2,"fee":0,"mst":9,"pst":0,"pop":90,"dt":179957,"rtype":0,"s_id":0,"rtUrls":[],"id":37239038,"st":0,"cd":"01","publishTime":1388505600004,"cf":"","mv":0,"al":{"picUrl":"http://p2.music.126.net/oc_p98jXIWPLjVAS-y-7kQ==/3421680187176215.jpg","name":"最新热歌慢摇102","tns":[],"id":3159845,"pic":3421680187176215},"l":{"br":128000,"fid":0,"size":2880199,"vd":-2},"cp":0,"alia":[],"djId":0,"ar":[{"name":"Various Artists","tns":[],"alias":[],"id":104700}],"ftype":0,"t":0,"v":20,"name":"当红警遇上Nobody","mark":524288}]}
     */

    var code: Int = 0
    var msg: String? = null
    var timestamp: Long = 0
    var data: DataBean? = null

    class DataBean {
        /**
         * privileges : [{"st":0,"flag":0,"subp":1,"fl":999000,"fee":0,"dl":128000,"cp":1,"preSell":false,"cs":false,"toast":false,"maxbr":128000,"id":37239038,"pl":128000,"sp":7,"payed":0}]
         * code : 200
         * songs : [{"no":1,"copyright":2,"fee":0,"mst":9,"pst":0,"pop":90,"dt":179957,"rtype":0,"s_id":0,"rtUrls":[],"id":37239038,"st":0,"cd":"01","publishTime":1388505600004,"cf":"","mv":0,"al":{"picUrl":"http://p2.music.126.net/oc_p98jXIWPLjVAS-y-7kQ==/3421680187176215.jpg","name":"最新热歌慢摇102","tns":[],"id":3159845,"pic":3421680187176215},"l":{"br":128000,"fid":0,"size":2880199,"vd":-2},"cp":0,"alia":[],"djId":0,"ar":[{"name":"Various Artists","tns":[],"alias":[],"id":104700}],"ftype":0,"t":0,"v":20,"name":"当红警遇上Nobody","mark":524288}]
         */

        var code: Int = 0
        var privileges: List<PrivilegesBean>? = null
        var songs: List<SongsBean>? = null

        class PrivilegesBean {
            /**
             * st : 0
             * flag : 0
             * subp : 1
             * fl : 999000
             * fee : 0
             * dl : 128000
             * cp : 1
             * preSell : false
             * cs : false
             * toast : false
             * maxbr : 128000
             * id : 37239038
             * pl : 128000
             * sp : 7
             * payed : 0
             */

            var st: Int = 0
            var flag: Int = 0
            var subp: Int = 0
            var fl: Int = 0
            var fee: Int = 0
            var dl: Int = 0
            var cp: Int = 0
            var isPreSell: Boolean = false
            var isCs: Boolean = false
            var isToast: Boolean = false
            var maxbr: Int = 0
            var id: Int = 0
            var pl: Int = 0
            var sp: Int = 0
            var payed: Int = 0
        }

        class SongsBean {
            /**
             * no : 1
             * copyright : 2
             * fee : 0
             * mst : 9
             * pst : 0
             * pop : 90.0
             * dt : 179957
             * rtype : 0
             * s_id : 0
             * rtUrls : []
             * id : 37239038
             * st : 0
             * cd : 01
             * publishTime : 1388505600004
             * cf :
             * mv : 0
             * al : {"picUrl":"http://p2.music.126.net/oc_p98jXIWPLjVAS-y-7kQ==/3421680187176215.jpg","name":"最新热歌慢摇102","tns":[],"id":3159845,"pic":3421680187176215}
             * l : {"br":128000,"fid":0,"size":2880199,"vd":-2}
             * cp : 0
             * alia : []
             * djId : 0
             * ar : [{"name":"Various Artists","tns":[],"alias":[],"id":104700}]
             * ftype : 0
             * t : 0
             * v : 20
             * name : 当红警遇上Nobody
             * mark : 524288
             */

            var no: Int = 0
            var copyright: Int = 0
            var fee: Int = 0
            var mst: Int = 0
            var pst: Int = 0
            var pop: Double = 0.toDouble()
            var dt: Int = 0
            var rtype: Int = 0
            var s_id: Int = 0
            var id: Int = 0
            var st: Int = 0
            var cd: String? = null
            var publishTime: Long = 0
            var cf: String? = null
            var mv: Int = 0
            var al: AlBean? = null
            var l: LBean? = null
            var cp: Int = 0
            var djId: Int = 0
            var ftype: Int = 0
            var t: Int = 0
            var v: Int = 0
            var name: String? = null
            var mark: Int = 0
            var rtUrls: List<*>? = null
            var alia: List<*>? = null
            var ar: List<ArBean>? = null

            class AlBean {
                /**
                 * picUrl : http://p2.music.126.net/oc_p98jXIWPLjVAS-y-7kQ==/3421680187176215.jpg
                 * name : 最新热歌慢摇102
                 * tns : []
                 * id : 3159845
                 * pic : 3421680187176215
                 */

                var picUrl: String? = null
                var name: String? = null
                var id: Int = 0
                var pic: Long = 0
                var tns: List<*>? = null
            }

            class LBean {
                /**
                 * br : 128000
                 * fid : 0
                 * size : 2880199
                 * vd : -2.0
                 */

                var br: Int = 0
                var fid: Int = 0
                var size: Int = 0
                var vd: Double = 0.toDouble()
            }

            class ArBean {
                /**
                 * name : Various Artists
                 * tns : []
                 * alias : []
                 * id : 104700
                 */

                var name: String? = null
                var id: Int = 0
                var tns: List<*>? = null
                var alias: List<*>? = null
            }
        }
    }
}
