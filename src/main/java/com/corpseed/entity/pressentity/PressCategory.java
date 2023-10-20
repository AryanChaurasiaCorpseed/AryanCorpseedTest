package com.corpseed.entity.pressentity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "press_category")
public class PressCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Please enter press category title !!")
    private String title;

    @NotBlank(message = "Please enter press category slug !!")
    @Column(unique = true)
    private String slug;

    @Column(length = 2,name = "display_status")
    private String displayStatus="1";

    @Column(length = 20,name = "post_date")
    private String postDate;

    @Column(length = 20,name = "modify_date")
    private String modifyDate;

    @Column(name = "delete_status",length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
    private int deleteStatus=2;

    @OneToMany(mappedBy = "pressCategory",cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Press> press=new ArrayList<>();

    public PressCategory() {
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

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public List<Press> getPress() {
        return press;
    }

    public void setPress(List<Press> press) {
        this.press = press;
    }
}
