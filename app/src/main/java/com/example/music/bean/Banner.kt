package com.example.music.bean

import com.example.music.db.table.BannerTable

/**
 * Created by tk on 2019/8/19
 */
class Banner {

    /**
     * code : 200
     * msg : OK
     * timestamp : 1566227925367
     * data : [{"backgroundUrl":"http://p1.music.126.net/Lu4Gn6nskEcEtCaxN8OHgQ==/109951164305133992.jpg","picUrl":"http://p1.music.126.net/XFGTolOA4knoDuTjctkSHg==/109951164305143125.jpg","monitorType":"","targetId":"0","monitorImpress":"","targetType":"3000","monitorClick":"","url":"https://music.163.com/store/newalbum/detail?id=80982712"},{"backgroundUrl":"http://p1.music.126.net/dPOSGbfcX_UBqy6nSIoe8w==/109951164304358842.jpg","picUrl":"http://p1.music.126.net/eNy8Wi1GdIf_bSvHKZgHOg==/109951164304365040.jpg","monitorType":"","targetId":"0","monitorImpress":"","targetType":"3000","monitorClick":"","url":"https://music.163.com/store/newalbum/detail?id=80440087"},{"backgroundUrl":"http://p1.music.126.net/YoAP-mmMik1OtEr0GM7bJQ==/109951164302346843.jpg","picUrl":"http://p1.music.126.net/xlsuFiLe68hLlzFHdeC7uA==/109951164302342994.jpg","monitorType":"","targetId":"0","monitorImpress":"","targetType":"3000","monitorClick":"","url":"https://music.163.com/store/newalbum/detail?id=80915351"},{"backgroundUrl":"http://p1.music.126.net/rTKsuOPezAQyp-UzVTxnUQ==/109951164304371972.jpg","picUrl":"http://p1.music.126.net/cfHZQ40S6_ark3NLO2gQjw==/109951164304381122.jpg","monitorType":"","targetId":"1383011509","monitorImpress":"","targetType":"1","monitorClick":"","url":"/song?id=1383011509"},{"backgroundUrl":"http://p1.music.126.net/6HdP-Ace-S-CH9BrNI_x2g==/109951164304391288.jpg","picUrl":"http://p1.music.126.net/4jZ1L1qECEOsikW2JKsyHQ==/109951164304393164.jpg","monitorType":"","targetId":"1384132585","monitorImpress":"","targetType":"1","monitorClick":"","url":"/song?id=1384132585"},{"backgroundUrl":"http://p1.music.126.net/VQaq2gukO7xfnvk_hu5Ahg==/109951164304394822.jpg","picUrl":"http://p1.music.126.net/P5kVS0ideN1GKUcsGokZQw==/109951164304398733.jpg","monitorType":"","targetId":"1384830303","monitorImpress":"","targetType":"1","monitorClick":"","url":"/song?id=1384830303"},{"backgroundUrl":"http://p1.music.126.net/E38GncrwMOVq4xBkCtrx7A==/109951164304412030.jpg","picUrl":"http://p1.music.126.net/JeYhYCeKfvXrJzwspIO8ug==/109951164304408155.jpg","monitorType":"","targetId":"1370897787","monitorImpress":"","targetType":"1","monitorClick":"","url":"/song?id=1370897787"},{"backgroundUrl":"http://p1.music.126.net/e5_ybwXkzfqcuHcbAez4Kg==/109951164305958440.jpg","picUrl":"http://p1.music.126.net/tsJc9HlvNNSJCrp-zKNglw==/109951164305963303.jpg","monitorType":"","targetId":"81016236","monitorImpress":"","targetType":"10","monitorClick":"","url":"/album?id=81016236"}]
     */

    var code: Int = 0
    var msg: String? = null
    var timestamp: Long = 0
    var data: List<BannerTable>? = null

}
