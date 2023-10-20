package com.corpseed.entity;

import java.util.Date;
import java.util.List;

public class Mail {
	private String mailFrom;
	 
    private String mailTo;
 
    private List<String> mailCc;
 
    private String mailBcc;
 
    private String mailSubject;
 
    private String mailContent;
 
    private String contentType;
 
    private String attachments;
 
    public Mail() {
        contentType = "text/html";
    }
 
    public String getContentType() {
        return contentType;
    }
 
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
 
    public String getMailBcc() {
        return mailBcc;
    }
 
    public void setMailBcc(String mailBcc) {
        this.mailBcc = mailBcc;
    } 
 
    public List<String> getMailCc() {
		return mailCc;
	}

	public void setMailCc(List<String> mailCc) {
		this.mailCc = mailCc;
	}

	public String getMailFrom() {
        return mailFrom;
    }
 
    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }
 
    public String getMailSubject() {
        return mailSubject;
    }
 
    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }
 
    public String getMailTo() {
        return mailTo;
    }
 
    public void setMailTo(String mailTo) {
        this.mailTo = mailTo;
    }
 
    public Date getMailSendDate() {
        return new Date();
    }
 
    public String getMailContent() {
        return mailContent;
    }
 
    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }
 
    public String getAttachments() {
        return attachments;
    }
 
    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }
}
