package com.server.express.entity;

public class TokenResult {
    String errcode = "";
    String errmsg = "";
    String token = "";

    public TokenResult(String errcode,String errmsg,String token){
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
