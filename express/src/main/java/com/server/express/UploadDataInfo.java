package com.server.express;
/*** 数据文件信息 */
public class UploadDataInfo {
    /*** 数据文件名称 */
    private String fileName;
    /*** 数据文件 hash 值 */
    private String hash;
    /*** 密钥 ID */
    private String keyId;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
