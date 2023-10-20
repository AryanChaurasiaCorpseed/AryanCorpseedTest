package com.corpseed.entity.newsentity;

import com.corpseed.entity.User;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "news_category")
public class NewsCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotBlank(message = "Please enter news category title !!")
	private String title;
	
	@NotBlank(message = "Please enter news category slug !!")
	private String slug;
	
	@Column(length = 2)
	private String displayStatus="1";
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "posted_user_id",nullable = false)
	private User user;
	
	@OneToMany(mappedBy = "newsCategory",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<News> news=new ArrayList<>();

	public NewsCategory() {
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<News> getNews() {
		return news;
	}

	public void setNews(List<News> news) {
		this.news = news;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
	
	
}
