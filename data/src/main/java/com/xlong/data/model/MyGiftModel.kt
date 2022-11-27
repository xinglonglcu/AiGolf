package com.xlong.data.model

import java.net.IDN

/**
 * 收到的礼物
 * Create by xlong 2022/11/27
 */
data class MyGiftModel(val golf_total_num: Int = 0,//高尔夫球总数
                       val golden_total_num: Int = 0,//金球总数
                       val silver_total_num: Int = 0,//银球总数
                       val iron_pole_total_num: Int = 0,//铁杆总数
                       val wooden_pole_total_num: Int = 0,//木杆总数
                       val training_total_num: Int = 0,//训练场总数
                       val ball_ground_total_num: Int = 0,//球场总数
                       val list: List<UserGiftModel>? = null)//用户赠送

data class UserGiftModel(val id: String? = null, val name: String? = null, //用户名
                         val uid: String? = null,//用户uid
                         val avatar: String? = null, //用户头像
                         val golf_num: Int = 0,//赠送高尔夫球数量
                         val golden_num: Int = 0,//赠送金球数量
                         val silver_num: Int = 0,//赠送银球数量
                         val iron_pole_num: Int = 0,//赠送铁杆数量
                         val wooden_pole_num: Int = 0,//赠送木杆数量
                         val training_num: Int = 0,//赠送训练场数量
                         val ball_ground_num: Int = 0)//赠送球场数量
