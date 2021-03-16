package com.server.express.entity;

import lombok.Getter;
import lombok.Setter;

/*** 数据文件信息 */
@Getter
@Setter
public class UploadDataSm2Info {
    //加密数据
    private String data;
    //账号
    private String accountNo;
    //公钥
    private String publicKey;
    //数据包流水ID
    private String serial;
    //令牌
    private String token;

}
