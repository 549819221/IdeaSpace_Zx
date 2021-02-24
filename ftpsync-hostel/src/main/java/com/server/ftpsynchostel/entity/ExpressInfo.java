package com.server.ftpsynchostel.entity;

import java.util.Date;

/***
 * 快递信息
 */
public class ExpressInfo {
    /*** 寄件人信息 */
    private Sender sender;
    /*** 收件人信息 */
    private Receiver receiver;
    /*** 快递员信息 */
    private ExpressStaff expressStaff;
    /*** 1.寄件；2.派件 */
    private int type;
    /*** 1.散件；2.协议件 */
    private int expressType;
    /*** 揽件/寄件/派件时间, * 格式 yyyy-MM-dd HH:mm:ss */
    private Date expressTime;
    /*** 面单号 */
    private String expressNo;
    /*** 托寄物名称 */
    private String expressGoodsName;
    /*** 面单照片 url */
    private String expressInvoicePhoto;
    /*** 面单开箱验视照片 url */
    private String expressBoxPhoto;
    /*** 经度 */
    private String expressLon;
    /*** 维度 */
    private String expressLat;
    /*** 单位编码 */
    private String placeCode;
    /*** 单位名称（分支机构、末端网点、分拨仓储中心） */
    private String placeName;
    /*** 单位地址 */
    private String placeAddress;
    /*** 单位社会信用统一代码 */
    private String placeBusinessLicense;
    /*** 单位负责人姓名 */
    private String placeManager;
    /*** 单位负责人手机号 */
    private String placeManagerPhone;
    /*** 单位负责人身份证号 */
    private String placeManagerIdcardNo;
    /*** 单位法人身份证号 */
    private String legalPersonIdcardNo;
    /*** 所属辖区编码 */
    private String policeAreaCode;
    /*** 所属辖区名称 */
    private String policeAreaName;
    /*** 单位法人姓名  */
    private String legalPersonName;
    /*** 单位法人手机号 */
    private String legalPersonPhone;


    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public ExpressStaff getExpressStaff() {
        return expressStaff;
    }

    public void setExpressStaff(ExpressStaff expressStaff) {
        this.expressStaff = expressStaff;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getExpressType() {
        return expressType;
    }

    public void setExpressType(int expressType) {
        this.expressType = expressType;
    }

    public Date getExpressTime() {
        return expressTime;
    }

    public void setExpressTime(Date expressTime) {
        this.expressTime = expressTime;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getExpressGoodsName() {
        return expressGoodsName;
    }

    public void setExpressGoodsName(String expressGoodsName) {
        this.expressGoodsName = expressGoodsName;
    }

    public String getExpressInvoicePhoto() {
        return expressInvoicePhoto;
    }

    public void setExpressInvoicePhoto(String expressInvoicePhoto) {
        this.expressInvoicePhoto = expressInvoicePhoto;
    }

    public String getExpressBoxPhoto() {
        return expressBoxPhoto;
    }

    public void setExpressBoxPhoto(String expressBoxPhoto) {
        this.expressBoxPhoto = expressBoxPhoto;
    }

    public String getExpressLon() {
        return expressLon;
    }

    public void setExpressLon(String expressLon) {
        this.expressLon = expressLon;
    }

    public String getExpressLat() {
        return expressLat;
    }

    public void setExpressLat(String expressLat) {
        this.expressLat = expressLat;
    }

    public String getPlaceCode() {
        return placeCode;
    }

    public void setPlaceCode(String placeCode) {
        this.placeCode = placeCode;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public String getPlaceBusinessLicense() {
        return placeBusinessLicense;
    }

    public void setPlaceBusinessLicense(String placeBusinessLicense) {
        this.placeBusinessLicense = placeBusinessLicense;
    }

    public String getPlaceManager() {
        return placeManager;
    }

    public void setPlaceManager(String placeManager) {
        this.placeManager = placeManager;
    }

    public String getPlaceManagerPhone() {
        return placeManagerPhone;
    }

    public void setPlaceManagerPhone(String placeManagerPhone) {
        this.placeManagerPhone = placeManagerPhone;
    }

    public String getPlaceManagerIdcardNo() {
        return placeManagerIdcardNo;
    }

    public void setPlaceManagerIdcardNo(String placeManagerIdcardNo) {
        this.placeManagerIdcardNo = placeManagerIdcardNo;
    }

    public String getLegalPersonIdcardNo() {
        return legalPersonIdcardNo;
    }

    public void setLegalPersonIdcardNo(String legalPersonIdcardNo) {
        this.legalPersonIdcardNo = legalPersonIdcardNo;
    }

    public String getPoliceAreaCode() {
        return policeAreaCode;
    }

    public void setPoliceAreaCode(String policeAreaCode) {
        this.policeAreaCode = policeAreaCode;
    }

    public String getPoliceAreaName() {
        return policeAreaName;
    }

    public void setPoliceAreaName(String policeAreaName) {
        this.policeAreaName = policeAreaName;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getLegalPersonPhone() {
        return legalPersonPhone;
    }

    public void setLegalPersonPhone(String legalPersonPhone) {
        this.legalPersonPhone = legalPersonPhone;
    }
}
