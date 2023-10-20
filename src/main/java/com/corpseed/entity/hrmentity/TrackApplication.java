package com.corpseed.entity.hrmentity;

import com.corpseed.entity.documententity.DocumentVerification;
import com.corpseed.entity.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class TrackApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 100)
	private String uuid;
		
	@Column(length = 20)
	private String date;
	
	@Column(length = 20)
	private String time;
	
	@Column(length = 2)
	private String status="1";
	
	@Column(length = 100)
	private String addedByUuid;
	
	@OneToOne
	@JoinColumn(name = "jobAppId")
	private JobApplication jobApplication;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 deleted, 2 not deleted'")
	private int deleteStatus=2;
	
	@OneToOne(mappedBy = "trackApplication",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private HrScreening hrScreening;
	
	@OneToOne(mappedBy = "trackApplication",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private TechnicalRound technicalRound;
	
	@OneToOne(mappedBy = "trackApplication",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private HrAndTechnical hrAndTechnical;
	
	@OneToOne(mappedBy = "trackApplication",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private DocumentVerification documentVerification;
	
	@OneToOne(mappedBy = "trackApplication",cascade = CascadeType.REMOVE,orphanRemoval = true)
	private OfferLetter offerLetter;
	/* for interviewer id */
	@ManyToOne
	@JoinColumn(name="interviewerId")
	private User user;
	
	@Column(length = 1,columnDefinition = "integer default 2 COMMENT '1 email sended, 2 email not sended'")
	private int hrEmailStatus=2;
	
	@Column(length = 20)
	private String postDate;

	public TrackApplication() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TrackApplication(long id, String uuid, String date, String time, String status,
			String addedByUuid, JobApplication jobApplication, int deleteStatus, HrScreening hrScreening,
			TechnicalRound technicalRound, HrAndTechnical hrAndTechnical, DocumentVerification documentVerification,
			OfferLetter offerLetter, User user) {
		super();
		this.id = id;
		this.uuid = uuid;
		
		this.date = date;
		this.time = time;
		this.status = status;
		this.addedByUuid = addedByUuid;
		this.jobApplication = jobApplication;
		this.deleteStatus = deleteStatus;
		this.hrScreening = hrScreening;
		this.technicalRound = technicalRound;
		this.hrAndTechnical = hrAndTechnical;
		this.documentVerification = documentVerification;
		this.offerLetter = offerLetter;
		this.user = user;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public JobApplication getJobApplication() {
		return jobApplication;
	}

	public void setJobApplication(JobApplication jobApplication) {
		this.jobApplication = jobApplication;
	}

	public HrScreening getHrScreening() {
		return hrScreening;
	}

	public void setHrScreening(HrScreening hrScreening) {
		this.hrScreening = hrScreening;
	}

	public TechnicalRound getTechnicalRound() {
		return technicalRound;
	}

	public void setTechnicalRound(TechnicalRound technicalRound) {
		this.technicalRound = technicalRound;
	}

	public HrAndTechnical getHrAndTechnical() {
		return hrAndTechnical;
	}

	public void setHrAndTechnical(HrAndTechnical hrAndTechnical) {
		this.hrAndTechnical = hrAndTechnical;
	}

	public DocumentVerification getDocumentVerification() {
		return documentVerification;
	}

	public void setDocumentVerification(DocumentVerification documentVerification) {
		this.documentVerification = documentVerification;
	}

	public OfferLetter getOfferLetter() {
		return offerLetter;
	}

	public void setOfferLetter(OfferLetter offerLetter) {
		this.offerLetter = offerLetter;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddedByUuid() {
		return addedByUuid;
	}

	public void setAddedByUuid(String addedByUuid) {
		this.addedByUuid = addedByUuid;
	}

	public int getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(int deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	public int getHrEmailStatus() {
		return hrEmailStatus;
	}
	public void setHrEmailStatus(int hrEmailStatus) {
		this.hrEmailStatus = hrEmailStatus;
	}
	public String getPostDate() {
		return postDate;
	}
	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}
	
}
