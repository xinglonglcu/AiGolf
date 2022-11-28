package com.xlong.data.model

/**
 * 我的-首页-视频按时间集合
 * Create by xlong 2022/11/28
 */
data class MyVideoModel(val id: String? = null,
                        val type: Int = 1,//1-视频，2-AI分析，3-AI对比，4-AI报告
                        val time: String? = null, //创建时间
                        val list: List<VideoModel>? = null)

enum class MyVideoType(val type: Int) {
    MYVIDEO(1), AI_ANALYSIS(2), AI_COMPARE(3), AI_REPORT(4),
}