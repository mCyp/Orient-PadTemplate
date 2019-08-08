package com.orient.padtemplate.core.data.model;

/**
 * Author WangJie
 * Created on 2018/4/16.
 */

public class BaseModel<T> {
    private String result;
    private String msg;
    private T data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
