package com.kanouakira.vueblog.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private int status;// 200正常,非200表示异常
    private String message;
    private Object data;

    public static Result succ(Object data){
        return(succ(200,"操作成功", data));
    }

    public static Result succ(int status, String message, Object data){
        Result r = new Result();
        r.setStatus(status);
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static Result fail(String msg){
        return fail(400,msg,null);
    }

    public static Result fail(int status, String message, Object data){
        Result r = new Result();
        r.setStatus(status);
        r.setMessage(message);
        r.setData(data);
        return r;
    }
}
