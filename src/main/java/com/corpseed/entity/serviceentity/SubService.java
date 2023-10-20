package com.corpseed.entity.serviceentity;

import com.corpseed.entity.serviceentity.Services;

import javax.persistence.*;

@Entity
@Table(name = "sub_service")
public class SubService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "parent_service_uuid")
    private String parentServiceUuid;

    @Column(name = "parent_service_slug")
    private String parentServiceSlug;

    @ManyToOne(targetEntity = Services.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id",nullable = false)
    private Services services;

    private String image;

    @Column(name = "post_date")
    private String postDate;

    @Column(name = "modify_date")
    private String modifyDate;

    @Column(name = "display_status",columnDefinition = "integer default 1 COMMENT '1 show, 2 not show'")
    private int displayStatus;

    @Column(name = "delete_status",length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
    private int deleteStatus;

    public SubService() {
        super();
    }


    public long getId() {
        return id;
    }

    public Services getServices() {
        return services;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParentServiceUuid() {
        return parentServiceUuid;
    }

    public String getParentServiceSlug() {
        return parentServiceSlug;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setServices(Services services) {
        this.services = services;
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

    public void setParentServiceUuid(String parentServiceUuid) {
        this.parentServiceUuid = parentServiceUuid;
    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public void setParentServiceSlug(String parentServiceSlug) {
        this.parentServiceSlug = parentServiceSlug;
    }
}
