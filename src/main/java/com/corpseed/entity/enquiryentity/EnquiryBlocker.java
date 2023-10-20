package com.corpseed.entity.enquiryentity;

import javax.persistence.*;

@Entity
@Table(name = "enquiry_blocker")
public class EnquiryBlocker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String mobile;

    private String email;

    @Column(name = "date_time")
    private String dateTime;

    public EnquiryBlocker() {
        super();
    }

    public EnquiryBlocker(String mobile, String email, String dateTime) {
        this.mobile = mobile;
        this.email = email;
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
