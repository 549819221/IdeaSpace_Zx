package com.server.express.entity;

public class UploadDataResult {

    // 返回结果内容，errcode 为 0 标识上报成功，其他的均为异常结果，需要查看数据内容 
    String errcode = ""; 
    String errmsg = ""; 
    String data = "";

    public UploadDataResult(String errcode,String errmsg,String data){
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.data = data;
    }

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
