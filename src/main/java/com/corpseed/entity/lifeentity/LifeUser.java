package com.corpseed.entity.lifeentity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "life_user")
public class LifeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "life_user_category",
            inverseJoinColumns = @JoinColumn(name = "life_category_id",referencedColumnName = "id"),
            joinColumns = @JoinColumn(name = "life_user_id", referencedColumnName = "id")
    )
    private List<LifeCategory> lifeCategories;

    @Column(name = "picture_name")
    private String pictureName;

    @NotBlank(message = "Title is required !!")
    @NotEmpty(message = "Title is required !!")
    @NotNull(message = "Title is required !!")
    @Column(columnDefinition = "TINYTEXT")
    private String title;

    @NotBlank(message = "Title is required !!")
    @NotEmpty(message = "Title is required !!")
    @NotNull(message = "Title is required !!")
    @Column(unique = true)
    private String slug;

    @NotBlank(message = "Summary is required !!")
    @NotEmpty(message = "Summary is required !!")
    @NotNull(message = "Summary is required !!")
    @Column(columnDefinition = "TEXT")
    private String summary;

    @NotBlank(message = "Description is required !!")
    @NotEmpty(message = "Description is required !!")
    @NotNull(message = "Description is required !!")
    @Column(columnDefinition = "LONGTEXT" )
    private String description;

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

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<LifeCategory> getLifeCategories() {
        return lifeCategories;
    }

    public void setLifeCategories(List<LifeCategory> lifeCategories) {
        this.lifeCategories = lifeCategories;
    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return "LifeUser{" +
                "id=" + id +
                ", pictureName='" + pictureName + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
