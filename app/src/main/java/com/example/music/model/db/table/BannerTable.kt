package com.example.music.model.db.table

import org.litepal.crud.LitePalSupport

/**
 * Created by tk on 2019/8/20
 * @param imageUrl 轮播图url
 * @param targetId 歌曲/mv/专辑id
 * @param targetType 轮播图跳转类型
 *                   1.歌曲（如果是vip专享只有试听）
 *                   3000.网页
 *                   10.专辑
 *                   1004.mv
 *                   1000.歌单
 *                   999.无跳转
 * @param url 网页跳转链接
 * @param typeTitle 类型：独家、vip专享
 */
class BannerTable(
                  val adDispatchJson: Any? = null,
                  val adLocation: Any? = null,
                  val adSource: Any? = null,
                  val adid: Any? = null,
                  val encodeId: String? = null,
                  val event: Any? = null,
                  val exclusive: Boolean? = null,
                  val extMonitor: Any? = null,
                  val extMonitorInfo: Any? = null,
                  val imageUrl: String,
                  val monitorBlackList: Any? = null,
                  val monitorClick: Any? = null,
                  val monitorClickList: Any? = null,
                  val monitorImpress: Any? = null,
                  val monitorImpressList: Any? = null,
                  val monitorType: Any? = null,
                  val program: Any? = null,
                  val scm: String? = null,
                  val song: Any? = null,
                  val targetId: Long = 0,
                  val targetType: Int = 999,
                  val titleColor: String? = null,
                  val typeTitle: String = "",
                  val url: String = "",
                  val video: Any? = null
) : LitePalSupport()

