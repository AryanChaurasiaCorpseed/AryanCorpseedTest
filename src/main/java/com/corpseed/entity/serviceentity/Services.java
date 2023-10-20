package com.corpseed.entity.serviceentity;

import com.corpseed.entity.*;
import com.corpseed.entity.couponentity.CouponServices;
import com.corpseed.entity.footerentity.FooterService;
import com.corpseed.entity.orderentity.Orders;
import com.corpseed.entity.productentity.Product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
public class Services {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true,length = 100)
	private String uuid;
	
	@NotBlank(message = "Please enter service title !!")
	@Column(length = 100)
	private String title; 
	
	@NotBlank(message = "Please enter slug !!")
	@Column(length = 100)
	private String slug;

	@Column(name = "video_url")
	private String videoUrl;
	@NotBlank(message = "please enter service name")
	@Column(length = 200)
	private String serviceName;
	
	@NotBlank(message = "Please write about service !!")
	@Column(columnDefinition = "TEXT")
	private String summary;
	
	@NotBlank(message = "Please enter meta title !!")
	@Column(length = 200)
	private String metaTitle;
	
	@NotBlank(message ="Please enter meta keyword !!")
	@Column(columnDefinition = "TEXT")
	private String metaKeyword;
	
	@NotBlank(message = "Please enter meta description !!")
	@Column(columnDefinition = "TEXT")
	private String metaDescription;
	
	@Column(length = 500)
	private String videoLink="NA";
	
	@Column(length = 200)
	private String legalGuide="NA";
	
	@Column(length = 50)
	private String ratingUser="0";
	
	@Column(length = 5)
	private String ratingValue="0";
	
	@Column(length = 50)
	private String ratingDefaultUser="0";
	
	@Column(length = 5)
	private String ratingDefaultValue="0";
	
	@Column(length = 2)
	private String displayStatus="1";
	
	@Column(length = 2)
	private String showHomeStatus="1";
	
	@Column(length = 2)
	private String showMenuStatus;
	
	@Column(length = 20)
	private String postDate;
	
	@Column(length = 20)
	private String modifyDate;
	
	@Column(length = 100)
	private String addedByUUID;
	
	private long visited=0;
	
	@Column(length = 50)
	@NotBlank(message = "Product number can't be blank.")
	private String productNo;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@ManyToOne(targetEntity = Category.class)
	@JoinColumn(name="categoryId")
	private Category category;
	
	@OneToOne(mappedBy = "services",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
	private HotTags hotTags;
	
	@OneToMany(mappedBy = "services",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<ServiceBlogs> serviceBlogs=new ArrayList<>();
	
	@OneToMany(mappedBy = "services",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<ServicePackage> servicePackage=new ArrayList<>();
		
	@OneToMany(mappedBy = "services",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<ServiceFaq> serviceFaq=new ArrayList<>();

	@OneToMany(mappedBy = "services",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<ServiceDetails> serviceDetails=new ArrayList<>();
	
	@OneToMany(mappedBy = "services",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<Orders> orders=new ArrayList<>();
	
	@OneToMany(mappedBy = "services",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private List<CouponServices> couponService=new ArrayList<>();
	
	@OneToOne(mappedBy = "services",cascade = CascadeType.REMOVE,orphanRemoval = true,fetch = FetchType.LAZY)
	private FooterService footerService;
	
	@OneToMany(mappedBy = "services",cascade = CascadeType.
			REMOVE,orphanRemoval = true)
	private List<ServiceBrands> serviceBrands=new ArrayList<>();
	
	@OneToMany(mappedBy = "services",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Testimonial> testmonials=new ArrayList<>();

	@OneToMany(mappedBy = "services",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<SubService> subServiceList=new ArrayList<>();

	@OneToMany(mappedBy = "services",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<ServiceContact> serviceContacts=new ArrayList<>();

	@Column(length = 1,columnDefinition = "integer default 1 COMMENT '1 show, 2 hide'")
	private int stepStatus=1;

	@OneToMany(mappedBy = "services",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Product> productList=new ArrayList<>();

	@OneToOne(mappedBy = "services",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
	private ServiceState serviceState;

	@OneToOne(mappedBy = "services",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
	private ServiceCity serviceCity;

	@OneToMany(mappedBy = "services",cascade = CascadeType.ALL,orphanRemoval = true)
	private List<StateCityService> stateCityServices=new ArrayList<>();
	
	public Services() {
		super();
		// TODO Auto-generated constructor stub
	}


	public long getVisited() {
		return visited;
	}
	public void setVisited(long visited) {
		this.visited = visited;
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
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

	public String getVideoLink() {
		return videoLink;
	}

	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}

	public String getLegalGuide() {
		return legalGuide;
	}

	public void setLegalGuide(String legalGuide) {
		this.legalGuide = legalGuide;
	}

	public String getRatingUser() {
		return ratingUser;
	}

	public void setRatingUser(String ratingUser) {
		this.ratingUser = ratingUser;
	}

	public String getRatingValue() {
		return ratingValue;
	}

	public void setRatingValue(String ratingValue) {
		this.ratingValue = ratingValue;
	}

	public String getDisplayStatus() {
		return displayStatus;
	}

	public void setDisplayStatus(String displayStatus) {
		this.displayStatus = displayStatus;
	}

	public String getShowHomeStatus() {
		return showHomeStatus;
	}

	public void setShowHomeStatus(String showHomeStatus) {
		this.showHomeStatus = showHomeStatus;
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

	public String getAddedByUUID() {
		return addedByUUID;
	}

	public void setAddedByUUID(String addedByUUID) {
		this.addedByUUID = addedByUUID;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public HotTags getHotTags() {
		return hotTags;
	}

	public void setHotTags(HotTags hotTags) {
		this.hotTags = hotTags;
	}

	public List<ServiceBlogs> getServiceBlogs() {
		return serviceBlogs;
	}

	public void setServiceBlogs(List<ServiceBlogs> serviceBlogs) {
		this.serviceBlogs = serviceBlogs;
	}

	public List<ServicePackage> getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(List<ServicePackage> servicePackage) {
		this.servicePackage = servicePackage;
	}

	public List<ServiceFaq> getServiceFaq() {
		return serviceFaq;
	}

	public void setServiceFaq(List<ServiceFaq> serviceFaq) {
		this.serviceFaq = serviceFaq;
	}

	public List<ServiceDetails> getServiceDetails() {
		return serviceDetails;
	}

	public void setServiceDetails(List<ServiceDetails> serviceDetails) {
		this.serviceDetails = serviceDetails;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}
	public List<CouponServices> getCouponService() {
		return couponService;
	}
	public void setCouponService(List<CouponServices> couponService) {
		this.couponService = couponService;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}


	public String getRatingDefaultUser() {
		return ratingDefaultUser;
	}


	public void setRatingDefaultUser(String ratingDefaultUser) {
		this.ratingDefaultUser = ratingDefaultUser;
	}


	public String getRatingDefaultValue() {
		return ratingDefaultValue;
	}


	public void setRatingDefaultValue(String ratingDefaultValue) {
		this.ratingDefaultValue = ratingDefaultValue;
	}


	public String getShowMenuStatus() {
		return showMenuStatus;
	}


	public void setShowMenuStatus(String showMenuStatus) {
		this.showMenuStatus = showMenuStatus;
	}


	public int getDeleteStatus() {
		return deleteStatus;
	}


	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}


	public FooterService getFooterService() {
		return footerService;
	}


	public void setFooterService(FooterService footerService) {
		this.footerService = footerService;
	}


	public List<ServiceBrands> getServiceBrands() {
		return serviceBrands;
	}


	public void setServiceBrands(List<ServiceBrands> serviceBrands) {
		this.serviceBrands = serviceBrands;
	}

	public List<Testimonial> getTestmonials() {
		return testmonials;
	}

	public void setTestmonials(List<Testimonial> testmonials) {
		this.testmonials = testmonials;
	}

	public List<SubService> getSubServiceList() {
		return subServiceList;
	}

	public void setSubServiceList(List<SubService> subServiceList) {
		this.subServiceList = subServiceList;
	}

	public int getStepStatus() {return stepStatus;}

	public void setStepStatus(int stepStatus) {this.stepStatus = stepStatus;}

	public List<ServiceContact> getServiceContacts() {
		return serviceContacts;
	}

	public void setServiceContacts(List<ServiceContact> serviceContacts) {
		this.serviceContacts = serviceContacts;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public ServiceState getServiceState() {
		return serviceState;
	}

	public void setServiceState(ServiceState serviceState) {
		this.serviceState = serviceState;
	}

	public ServiceCity getServiceCity() {
		return serviceCity;
	}

	public void setServiceCity(ServiceCity serviceCity) {
		this.serviceCity = serviceCity;
	}

	public List<StateCityService> getStateCityServices() {
		return stateCityServices;
	}

	public void setStateCityServices(List<StateCityService> stateCityServices) {
		this.stateCityServices = stateCityServices;
	}
}
