package com.server.hostel.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicKeyResult {
    String errcode = "";
    String errmsg = "";
    String publicKey = "";

    public PublicKeyResult(String errcode, String errmsg, String publicKey){
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.publicKey = publicKey;
    }
}
