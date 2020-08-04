package com.server.express;

/*** 收件人信息 */
public class Receiver {
    /*** 姓名 */
    private String username;
    /*** 手机号 */
    private String phone;
    /*** 收件区号 */
    private String cityCode;
    /*** 民族 */
    private String nation;
    /*** 性别 */
    private int gender;
    /*** 收件人证件类型 */
    private int idcardType;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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