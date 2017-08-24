package com.camp.share.core.enhance;

import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by wushengsheng on 2017/6/18.
 */
public class Result<T> {
    private String code = "500";
    private boolean success = false;
    private Map<String, T> data = null;
    private String msg = "";

    public Result() {
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result();
        r.setCode("200");
        r.setSuccess(true);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> fail(String code, String msg) {
        Result<T> r = new Result();
        r.setSuccess(false);
        r.setMsg(msg);
        r.setCode(code);
        return r;
    }

    public String getCode() {
        return this.code;
    }

    public Result<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public Result<T> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public T getData() {
        if (this.data != null && !this.data.isEmpty()) {
            Iterator var2 = this.data.keySet().iterator();
            if (var2.hasNext()) {
                String key = (String)var2.next();
                return this.data.get(key);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Result<T> setData(T data) {
        Map<String, T> map = Maps.newHashMap();
        map.put("result", data);
        this.data = map;
        return this;
    }

    public Result<T> setData(String key, T data) {
        Map<String, T> map = Maps.newHashMap();
        map.put(key, data);
        this.data = map;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}

