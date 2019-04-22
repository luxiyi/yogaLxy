package com.woniu.yago.pojo;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

/**
 * @Description: java类作用描述
 * @Author: 路边
 * @time: 2019/4/16 10:07
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Proxy(lazy = false)
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private Integer userId;
    @Column(name = "user_pwd")
    private String userPwd;
    @Column
    private String salt;
    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "user_phone")
    private String userPhone;
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "user_wechat")
    private String userWechat;
    @Column(name = "user_qq")
    private String userQq;
    @Column(name = "user_nickname")
    private String userNickname;
    @Column(name = "user_headimg")
    private String userHeadimg;
    @Column
    private Float longitude;
    @Column
    private Float latitude;
    @Column(name = "user_location")
    private String userLocation;
    @Column(name = "temp_name")
    private String tempName;
    @Column(name = "real_name")
    private String realName;
    @Column
    private String idcard;
    @Column(name = "user_level")
    private Integer userLevel;
    @Column(name = "user_score")
    private Integer userScore;
    @Column(name = "user_privacy")
    private Integer userPrivacy;
    @Column(nullable=false,name = "active",columnDefinition = "int default 0")
    private Integer active;
    @Column(name = "user_verify_code")
    private String userVerifyCode;
    @Column(name = "user_flag")
    private Integer userFlag;

//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }
//
//    public String getUserPwd() {
//        return userPwd;
//    }
//
//    public void setUserPwd(String userPwd) {
//        this.userPwd = userPwd;
//    }
//
//    public String getSalt() {
//        return salt;
//    }
//
//    public void setSalt(String salt) {
//        this.salt = salt;
//    }
//
//    public Integer getRoleId() {
//        return roleId;
//    }
//
//    public void setRoleId(Integer roleId) {
//        this.roleId = roleId;
//    }
//
//    public String getUserPhone() {
//        return userPhone;
//    }
//
//    public void setUserPhone(String userPhone) {
//        this.userPhone = userPhone;
//    }
//
//    public String getUserEmail() {
//        return userEmail;
//    }
//
//    public void setUserEmail(String userEmail) {
//        this.userEmail = userEmail;
//    }
//
//    public String getUserWechat() {
//        return userWechat;
//    }
//
//    public void setUserWechat(String userWechat) {
//        this.userWechat = userWechat;
//    }
//
//    public String getUserQq() {
//        return userQq;
//    }
//
//    public void setUserQq(String userQq) {
//        this.userQq = userQq;
//    }
//
//    public String getUserNickname() {
//        return userNickname;
//    }
//
//    public void setUserNickname(String userNickname) {
//        this.userNickname = userNickname;
//    }
//
//    public String getUserHeadimg() {
//        return userHeadimg;
//    }
//
//    public void setUserHeadimg(String userHeadimg) {
//        this.userHeadimg = userHeadimg;
//    }
//
//    public Float getLongitude() {
//        return longitude;
//    }
//
//    public void setLongitude(Float longitude) {
//        this.longitude = longitude;
//    }
//
//    public Float getLatitude() {
//        return latitude;
//    }
//
//    public void setLatitude(Float latitude) {
//        this.latitude = latitude;
//    }
//
//    public String getUserLocation() {
//        return userLocation;
//    }
//
//    public void setUserLocation(String userLocation) {
//        this.userLocation = userLocation;
//    }
//
//    public String getTempName() {
//        return tempName;
//    }
//
//    public void setTempName(String tempName) {
//        this.tempName = tempName;
//    }
//
//    public String getRealName() {
//        return realName;
//    }
//
//    public void setRealName(String realName) {
//        this.realName = realName;
//    }
//
//    public String getIdcard() {
//        return idcard;
//    }
//
//    public void setIdcard(String idcard) {
//        this.idcard = idcard;
//    }
//
//    public Integer getUserLevel() {
//        return userLevel;
//    }
//
//    public void setUserLevel(Integer userLevel) {
//        this.userLevel = userLevel;
//    }
//
//    public Integer getUserScore() {
//        return userScore;
//    }
//
//    public void setUserScore(Integer userScore) {
//        this.userScore = userScore;
//    }
//
//    public Integer getUserPrivacy() {
//        return userPrivacy;
//    }
//
//    public void setUserPrivacy(Integer userPrivacy) {
//        this.userPrivacy = userPrivacy;
//    }
//
//    public Integer getActive() {
//        return active;
//    }
//
//    public void setActive(Integer active) {
//        this.active = active;
//    }
//
//    public String getUserVerifyCode() {
//        return userVerifyCode;
//    }
//
//    public void setUserVerifyCode(String userVerifyCode) {
//        this.userVerifyCode = userVerifyCode;
//    }
//
//    public Integer getUserFlag() {
//        return userFlag;
//    }
//
//    public void setUserFlag(Integer userFlag) {
//        this.userFlag = userFlag;
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "userId=" + userId +
//                ", userPwd='" + userPwd + '\'' +
//                ", salt='" + salt + '\'' +
//                ", roleId=" + roleId +
//                ", userPhone='" + userPhone + '\'' +
//                ", userEmail='" + userEmail + '\'' +
//                ", userWechat='" + userWechat + '\'' +
//                ", userQq='" + userQq + '\'' +
//                ", userNickname='" + userNickname + '\'' +
//                ", userHeadimg='" + userHeadimg + '\'' +
//                ", longitude=" + longitude +
//                ", latitude=" + latitude +
//                ", userLocation='" + userLocation + '\'' +
//                ", tempName='" + tempName + '\'' +
//                ", realName='" + realName + '\'' +
//                ", idcard='" + idcard + '\'' +
//                ", userLevel=" + userLevel +
//                ", userScore=" + userScore +
//                ", userPrivacy=" + userPrivacy +
//                ", active=" + active +
//                ", userVerifyCode='" + userVerifyCode + '\'' +
//                ", userFlag=" + userFlag +
//                '}';
//    }
}