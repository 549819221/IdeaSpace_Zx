package com.server.filesizesync.entity;

/*** 快递员信息 */
public class ExpressStaff {
    /*** 姓名 */
    private String username;
    /*** 快递员工号 */
    private String expressStaffNo;
    /*** 手机号 */
    private String phone;
    /*** 民族 */
    private String nation;
    /*** 性别 */
    private int gender;
    /*** 证件类型 */
    private int idcardType;
    /*** 证件号 */
    private String idcardNo;
    /*** 邮编 */
    private String postalCode;
    /*** 地址 */
    private String address;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getExpressStaffNo() {
        return expressStaffNo;
    }

    public void setExpressStaffNo(String expressStaffNo) {
        this.expressStaffNo = expressStaffNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getIdcardType() {
        return idcardType;
    }

    public void setIdcardType(int idcardType) {
        this.idcardType = idcardType;
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
