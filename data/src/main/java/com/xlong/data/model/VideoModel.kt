package com.xlong.data.model

/**
 * 视频信息
 * Create by xlong 2022/11/25
 */
data class VideoModel(var id: String? = null, var vid: String? = null, //视频vid
                      var uid: String? = null, //用户ID
                      var name: String? = null, //用户昵称
                      var avatar: String? = null, //用户头像
                      var cover: String? = null, //封面
                      var title: String? = null, //标题
                      var playurl: String? = null, //播放地址
                      var duration: Long = 0L,//视频时长 单位s
                      var compare_num: Int = 0,//比对次数
                      var is_heart: Int = 0, //是否点赞
                      var heart_num: Int = 0, //点赞数量
                      var is_collect: Int = 0, //是否收藏
                      var collect_num: Int = 0, //收藏数量
                      var share_num: Int = 0, //分享数量
                      var comment_num: Int = 0, //评论数量
                      var isfollow: Int = 0)//是否关注
