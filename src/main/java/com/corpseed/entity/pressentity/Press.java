package com.corpseed.entity.pressentity;

import com.corpseed.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "press")
public class Press {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Please enter press title !!")
    private String title;

    @NotBlank(message = "Please enter slug !!")
    @Column(unique = true)
    private String slug;

    private String image;

    @NotBlank(message = "Please enter press summary !!")
    @Column(columnDefinition = "TEXT")
    private String summary;

    @NotBlank(message = "Please provide press description !!")
    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @NotBlank(message = "Please enter meta title !!")
    @Column(name = "meta_title")
    private String metaTitle;

    @NotBlank(message = "Please enter meta keyword !!")
    @Column(columnDefinition = "TEXT",name = "meta_keyword")
    private String metaKeyword;

    @NotBlank(message = "Please provide meta description !!")
    @Column(columnDefinition = "TEXT",name = "meta_description")
    private String metaDescription;

    @Column(length = 2)
    private String displayStatus="1";

    @Column(length = 20,name = "post_date")
    private String postDate;

    @Column(length = 20,name = "modify_date")
    private String modifyDate;

    private long visited=0;

    @Column(name = "delete_status",length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
    private int deleteStatus=2;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "author_id",nullable = false)
    private User user;

    @ManyToOne(targetEntity = PressCategory.class)
    @JoinColumn(name = "press_category_id",nullable = false)
    private PressCategory pressCategory;

    public Press() {
        super();
        // TODO Auto-generated constructor stub
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaKeyword() {
        return metaKeyword;
    }

    public void setMetaKeyword(String metaKeyword) {
        this.metaKeyword = metaKeyword;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(String modifyDate) {
        this.modifyDate = modifyDate;
    }

    public long getVisited() {
        return visited;
    }

    public void setVisited(long visited) {
        this.visited = visited;
    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PressCategory getPressCategory() {
        return pressCategory;
    }

    public void setPressCategory(PressCategory pressCategory) {
        this.pressCategory = pressCategory;
    }
}
