package com.xlong.data.model

/**
 * 我的-首页-视频数量信息
 * Create by xlong 2022/11/28
 */
data class MyInfoModel(val id: String? = null, val pose_lib_num: Int = 0,//姿势库
                       val collect_num: Int = 0,//收藏
                       val share_num: Int = 0,//分享
                       val gift_num: Int = 0,//礼物
                       val video_num: Int = 0,//视频
                       val compare_num: Int = 0,//AI比对
                       val analysis_num: Int = 0,//AI分析
                       val report_num: Int = 0)//AI报告
