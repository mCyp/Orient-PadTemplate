package com.orient.padtemplate.base.rx;

/**
 * Author WangJie
 * Created on 2018/9/4.
 */
public class BaseException extends Throwable {

    String result;

    String msg;

    public BaseException(String result, String msg) {
        this.result = result;
        this.msg = msg;
    }

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
}
