package com.corpseed.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class EnquiryDto {

    @JsonProperty("Created_Time")
    private Date Created_Time;

    @JsonProperty("Ad_Name")
    private String Ad_Name;

    @JsonProperty("Platform")
    private String Platform;

    @JsonProperty("Full_Name")
    private String Full_Name;

    @JsonProperty("Phone_Number")
    private String Phone_Number;

    @JsonProperty("Email")
    private String Email;

    @JsonProperty("Street_Address")
    private String Street_Address;

    @JsonProperty("State")
    private String State;

    @JsonProperty("Country")
    private String Country;

    public EnquiryDto() {
    }

    public EnquiryDto(Date created_Time, String ad_Name, String platform, String full_Name, String phone_Number, String email, String street_Address, String state, String country) {
        Created_Time = created_Time;
        Ad_Name = ad_Name;
        Platform = platform;
        Full_Name = full_Name;
        Phone_Number = phone_Number;
        Email = email;
        Street_Address = street_Address;
        State = state;
        Country = country;
    }

    public Date getCreated_Time() {
        return Created_Time;
    }

    public void setCreated_Time(Date created_Time) {
        Created_Time = created_Time;
    }

    public String getAd_Name() {
        return Ad_Name;
    }

    public void setAd_Name(String ad_Name) {
        Ad_Name = ad_Name;
    }

    public String getPlatform() {
        return Platform;
    }

    public void setPlatform(String platform) {
        Platform = platform;
    }

    public String getFull_Name() {
        return Full_Name;
    }

    public void setFull_Name(String full_Name) {
        Full_Name = full_Name;
    }

    public String getPhone_Number() {
        return Phone_Number;
    }

    public void setPhone_Number(String phone_Number) {
        Phone_Number = phone_Number;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getStreet_Address() {
        return Street_Address;
    }

    public void setStreet_Address(String street_Address) {
        Street_Address = street_Address;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }


}
