package com.corpseed.entity.lifeentity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "life_category")
public class LifeCategory {

    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;

//    @ManyToMany(targetEntity = LifeUser.class)
//    private List<LifeUser> lifeUserList=new ArrayList<>();

    private String title;

    @Column(unique = true)
    private String slug;

    private String createdAt;

    private String updatedAt;

    @Column(length = 2)
    private String displayStatus;

    @Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
    private int deleteStatus=2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

   /* public List<LifeUser> getLifeUserList() {
        return lifeUserList;
    }

    public void setLifeUserList(List<LifeUser> lifeUserList) {
        this.lifeUserList = lifeUserList;
    }*/

    @Override
    public String toString() {
        return "LifeCategory{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", displayStatus='" + displayStatus + '\'' +
                '}';
    }
}
