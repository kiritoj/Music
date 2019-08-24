package com.example.music.db.table

import org.litepal.crud.LitePalSupport

/**
 * Created by tk on 2019/8/20
 */
class BannerTable : LitePalSupport() {
    /**
     * backgroundUrl : http://p1.music.126.net/Lu4Gn6nskEcEtCaxN8OHgQ==/109951164305133992.jpg
     * picUrl : http://p1.music.126.net/XFGTolOA4knoDuTjctkSHg==/109951164305143125.jpg
     * monitorType :
     * targetId : 0
     * monitorImpress :
     * targetType : 3000
     * monitorClick :
     * url : https://music.163.com/store/newalbum/detail?id=80982712
     */

    var backgroundUBrl: String? = null
    var picUrl: String? = null
    var monitorType: String? = null
    var targetId: String? = null
    var monitorImpress: String? = null
    var targetType: String? = null
    var monitorClick: String? = null
    var url: String? = null
}
