package com.xlong.data.net;

import com.google.gson.Gson;


/**
 * 
 * Created by xlong on 2022/11/23
 */
public class BaseModel<T> {
    private int code;
    private String msg;
    private int pagesize = 30; // 默认值
    private String endid;
    private Object param; // 冗余字段

    private T datas;

    public void setDatas(T datas) {
        this.datas = datas;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getDatas() {
        return datas;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public String getEndid() {
        return endid;
    }

    public void setEndid(String endid) {
        this.endid = endid;
    }

    public Object getParam() {
        return param;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public static String toJson(BaseModel baseModel) {
        return new Gson().toJson(baseModel);
    }
}
