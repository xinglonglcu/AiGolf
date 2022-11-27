package com.xlong.data.net

import com.xlong.data.model.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 *
 * Create by xlong 2022/11/23
 */
interface BasicService {

    @GET("${mVersion}mod=user&ac=login")
    fun login(@Query("mobile") mobile: String, @Query("pwd") pwd: String): Observable<BaseModel<AccountModel>>

    @GET("${mVersion}mod=props&ac=filter&startid=1669185792855e63ab&client=2&height=2242&stepid=2&anon_id=3429dd7c610d10608d5bd67b071adf72&package=com.bokecc.dance&compileabi=64-Bit&operator=46001&cpu=Qualcomm_Technologies%2C_Inc_SM4350&dic=old_oem&div=8.8.8.0930-debug&dpi=480&ver=v2&diu5=c2be1c74f28446ae33b334de1e7f0bb9&oaid=&time=1669186299578&uuid=75538033b5786b3a&gtcid=52a0538cf44ff866c89fedc28e261444&netop=%E8%81%94%E9%80%9A&width=1080&xinge=&version=8.8.8.0930-debug&all_startid=16691857928542ff04&sdkversion=12&channel=old_oem&height2=2400&bucketlist=app_log_switch-off%2Capp_record_oritation-off%2C0%2Ccommunity_homepage-old%2Cdance_feed_ad-new%2Cdownload_ad_head-new%2Cdownload_ad_tail-new%2Cdownload_dance_play-old%2Cdownload_definition-old%2Cdownload_list_rank-old%2Cdownload_play_optimize-old%2Cdownload_split_tab-new%2Cdownload_video_cache-new%2Cdownpage_hide_send_new-new1%2Cdownpage_video_fold-old%2Cfavpage_add_feed-old%2Cfeed_plaque_ad_new2-old_equal%2Cfeed_rank_tab-old%2Cfeed_video_tag-old%2Cfollowdance_throwingend-new%2Cfollowdance_video_content-old_equal%2Chigh_definition_down-old%2Chistorypage_add_feed-old%2Chome_top_button-new%2Chomepage_feed_style-old%2Chomepage_tab-old%2Chomerec_preview_new-old%2Cicon_red_dot-new%2Cjump_home_multitype-new%2Cjump_home_video_rec-new%2Cjump_video_turn-new%2C-1%2Clogin_optimize-new%2Cmember_block-new2%2Cnew_feedui_optimize-old%2Cnewuser_feed_button-old%2Cnewuser_interest_label2-old%2Cnewuser_kingkong-new%2Cnewuser_login_guide-new%2Cnewuser_masking-old%2Cplayad_noheadad_v3-old%2Cplaypage_dadian_exp-new%2Cplaypage_danceteach-old_equal%2Cplaypage_plaque_ad_new-old%2Cplaypage_to_exercise-new1%2Cpreload_ad-new%2Cpreload_end_ad-new%2Cpreload_feed_ad-new%2Cpreload_plaque_ad-new%2Cpreload_playpage_ad-new%2Cpreload_quit_plaque_ad-new%2Cpush_sdk-new%2Cquit_plaque_ad-new%2Cranklist_entrance_exp-old%2Crecord_config-new%2Cremove_ad_frame-new%2Csearch_result_page_optimize-old%2Csearch_resultpage_tab-new%2Csend_revision-new%2Cshare_flowers-new%2Cshare_no_adv-old%2Cshow_direction_v2-old%2Cspace_ui-old%2Ctab_icon-old_3%2Cvideo_cache-new%2Cvideo_result_rank-old%2Cvideo_tag1-old%2Cvideo_tag2-old%2Cvip_buy_first-old%2Cvip_buy_name-new%2Cvip_intro-old%2Cvip_series_course-old%2Cvip_tab_change-new%2Cvip_video-old%2Cvip_video_dv-old%2Cvip_video_section-old%2Cvisitor_vip-new2&smallvideo=1&nettype=WIFI&isvirtualapp=0&hash=728744897cede5be7d7920b709b05e73")
    fun getRecordFilters(): Observable<BaseModel<List<LutFilterModel>>>

    @GET("${mVersion}mod=gcw_team&ac=check_is_bind")
    fun check(): Observable<BaseModel<Object>>

    @GET("${mVersion}mod=gcw_team&ac=check_is_bind")
    fun getVideoList(@Query("page") page: Int): Observable<BaseModel<List<VideoModel>>>

    @GET("${mVersion}mod=gcw_team&ac=check_is_bind")
    fun getMyCollectList(@Query("page") page: Int): Observable<BaseModel<List<VideoModel>>>

    @GET("${mVersion}mod=gcw_team&ac=check_is_bind")
    fun getPoseLibList(@Query("page") page: Int): Observable<BaseModel<List<VideoModel>>>

    @GET("${mVersion}mod=gcw_team&ac=check_is_bind")
    fun getFollowList(@Query("page") page: Int, @Query("key") key: String): Observable<BaseModel<List<FollowModel>>>

    @GET("${mVersion}mod=gcw_team&ac=check_is_bind")
    fun actionFollow(@Query("suid") suid: String, @Query("follow") follow: Int): Observable<BaseModel<Any>>

    @GET("${mVersion}mod=gcw_team&ac=check_is_bind")
    fun getMyGift(): Observable<BaseModel<MyGiftModel>>

    companion object {
        const val mVersion = "api.php?"
    }
}