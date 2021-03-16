package com.server.express.entity;

public class PublicKeyResult {
    String errcode = "";
    String errmsg = "";
    String publicKey = "";

    public PublicKeyResult(String errcode, String errmsg, String publicKey){
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.publicKey = publicKey;
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
        return publicKey;
    }

    public void setToken(String publicKey) {
        this.publicKey = publicKey;
    }
}
