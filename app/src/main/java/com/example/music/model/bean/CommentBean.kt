package com.example.music.model.bean

/**
 * Created by tk on 2019/9/5
 * 歌曲、歌单评论bean类
 */
data class CommentBean(
    //val code: Int,
    val comments: List<Comment>,
    val hotComments: List<Comment>
    //val isMusician: Boolean,
    //val more: Boolean,
    //val moreHot: Boolean,
    //val topComments: List<Any>,
    //val total: Int,
    //val userId: Int
)

data class Comment(
    //val beReplied: List<Any>,
    //val commentId: Int,
    //val commentLocationType: Int,
    val content: String,
    //val decoration: Decoration,
    //val expressionUrl: Any,
    //val liked: Boolean,
    val likedCount: Int,
    //val parentCommentId: Int,
    //val pendantData: PendantDataX,
    //val repliedMark: Any,
    //val showFloorComment: Any,
    //val status: Int,
    val time: Long,
    val user: UserX
)

data class PendantData(
    val id: Int,
    val imageUrl: String
)

class Decoration(
)

data class User(
    val authStatus: Int,
    val avatarUrl: String,
    val expertTags: Any,
    val experts: Any,
    val liveInfo: Any,
    val locationInfo: Any,
    val nickname: String,
    val remarkName: Any,
    val userId: Int,
    val userType: Int,
    val vipRights: VipRights,
    val vipType: Int
)

data class VipRights(
    val associator: Associator,
    val musicPackage: Any,
    val redVipAnnualCount: Int
)

data class Associator(
    val rights: Boolean,
    val vipCode: Int
)

data class HotComment(
    //val beReplied: List<Any>,
    //val commentId: Int,
    //val commentLocationType: Int,
    val content: String,
    //val decoration: Any,
    //val expressionUrl: Any,
    //val liked: Boolean,
    val likedCount: Int,
    //val parentCommentId: Int,
    //val pendantData: PendantData,
    //val repliedMark: Any,
    //val showFloorComment: Any,
    //val status: Int,
    val time: Long,
    val user: User
)

data class UserX(
    //val authStatus: Int,
    val avatarUrl: String,
    //val expertTags: Any,
    //val experts: Any,
    //val liveInfo: Any,
    //val locationInfo: Any,
    val nickname: String,
    //val remarkName: Any,
    val userId: Int
    //val userType: Int
    //val vipRights: VipRightsX,
    //val vipType: Int
)

data class VipRightsX(
    val associator: Associator,
    val musicPackage: Any,
    val redVipAnnualCount: Int
)

data class PendantDataX(
    val id: Int,
    val imageUrl: String
)