package com.corpseed.entity.productentity;


import com.corpseed.entity.Category;
import com.corpseed.entity.User;
import com.corpseed.entity.serviceentity.Services;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 255)
    @NotBlank(message = "Please enter product name !!")
    private String name;

    @Column(length = 255)
    @NotBlank(message = "Please enter product title !!")
    private String title;

    @NotBlank(message = "Please enter slug !!")
    @Column(length = 255 ,unique = true)
    private String slug;

    @Column(length = 255 )
    private String image;

    @NotBlank(message = "Please enter product summary !!")
    @Column(columnDefinition = "TEXT")
    private String summary;

    @NotBlank(message = "Please provide product description !!")
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @NotBlank(message = "Please enter meta title !!")
    @Column(length = 255)
    private String metaTitle;

    @NotBlank(message = "Please enter meta keyword !!")
    @Column(columnDefinition = "TEXT")
    private String metaKeyword;

    @NotBlank(message = "Please provide meta description !!")
    @Column(columnDefinition = "TEXT")
    private String metaDescription;

    @Column(length = 50)
    private String ratingUser="0";

    @Column(length = 5)
    private String ratingValue="0";

    @Column(length = 2)
    private String displayStatus;

    @Column(length = 20)
    private String postDate;

    @Column(length = 20)
    private String modifyDate;

    private long visited=0;

    @ManyToOne(targetEntity = Category.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    //Mapping with category class
    @ManyToOne(targetEntity = Services.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id",nullable = false)
    private Services services;

    @ManyToOne(targetEntity = User.class,fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id",nullable = false)
    private User user;

    @Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
    private int deleteStatus=2;

    public Product() { }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getImage() {
        return image;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public String getMetaKeyword() {
        return metaKeyword;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public String getRatingUser() {
        return ratingUser;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public String getPostDate() {      return postDate;    }

    public String getModifyDate() {
        return modifyDate;
    }

    public long getVisited() { return visited;  }

    public Category getCategory() { return category;    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public void setMetaKeyword(String metaKeyword) {
        this.metaKeyword = metaKeyword;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public void setRatingUser(String ratingUser) {
        this.ratingUser = ratingUser;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public void setVisited(long visited) {
        this.visited = visited;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Services getServices() {
        return services;
    }

    public User getUser() {
        return user;
    }

    public void setServices(Services services) {
        this.services = services;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
