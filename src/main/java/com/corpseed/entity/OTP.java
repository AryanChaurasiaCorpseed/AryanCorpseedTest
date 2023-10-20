package com.corpseed.entity;

import javax.persistence.*;

@Entity
@Table(name = "otp")
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String mobile;

    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "date_time")
    private String dateTime;
    @Column(name = "otp_times")
    private int otpTimes;

    //true : not verifyed //false : verifyed
    private boolean status;

    private String location;

    private String name;

    @Column(name = "delivery_status",columnDefinition = "integer default 2 COMMENT '1 Delivered, 2 not delivered'")
    private int deliveryStatus;

    public OTP() {
        super();
    }

    public OTP(String mobile, String otpCode, String dateTime,int otpTimes,boolean status,
               String location,String name,int deliveryStatus) {
        this.mobile = mobile;
        this.otpCode = otpCode;
        this.dateTime = dateTime;
        this.otpTimes=otpTimes;
        this.status=status;
        this.location=location;
        this.name=name;
        this.deliveryStatus=deliveryStatus;
    }

    public long getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOtpTimes() {
        return otpTimes;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setOtpTimes(int otpTimes) {
        this.otpTimes = otpTimes;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
