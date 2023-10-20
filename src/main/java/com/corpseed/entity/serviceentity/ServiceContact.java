package com.corpseed.entity.serviceentity;

import com.corpseed.entity.User;
import com.corpseed.entity.serviceentity.Services;

import javax.persistence.*;

@Entity
@Table(name = "service_contact")
public class ServiceContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(targetEntity = Services.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id",nullable = false)
    private Services services;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "post_date")
    private String postDate;

    @Column(name = "modify_date")
    private String modifyDate;

    @Column(name = "display_status")
    private int displayStatus=1;

    @Column(name = "delete_status")
    private int deleteStatus=2;
    public ServiceContact() {
        super();
    }

    public long getId() {
        return id;
    }

    public Services getServices() {
        return services;
    }

    public User getUser() {
        return user;
    }

    public String getPostDate() {
        return postDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public int getDisplayStatus() {
        return displayStatus;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public void setDisplayStatus(int displayStatus) {
        this.displayStatus = displayStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

}
