package com.server.hostel.entity;

import lombok.Getter;
import lombok.Setter;

/*** 数据文件信息 */
@Getter
@Setter
public class UploadDataSm2Info {
    //加密数据
    private String data;
    //加密数据
    private String data_type;
    //账号
    private String accountNo;
    //公钥
    private String publicKey;
    //数据包流水ID
    private String serial;
    //接收时间
    private String receiver_time;
    //令牌
    private String token;

}
