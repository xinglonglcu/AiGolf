package com.xlong.data.model

/**
 * 关注
 * Create by xlong 2022/11/27
 */
data class FollowModel(val id: String? = null, val uid: String? = null, //用户ID
                       val name: String? = null, //用户名
                       val avatar: String? = null, //头像
                       val letter_num: Int = 0,//私信数量
                       var is_follow: Int = 0)//是否已关注 1-已关注，0-未关注
