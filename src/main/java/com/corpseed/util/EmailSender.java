package com.corpseed.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corpseed.entity.Mail;
import com.corpseed.serviceImpl.emailserviceimpl.MailServiceImpl;

@Component
public class EmailSender {
		
	@Autowired
	private MailServiceImpl mailService;

	public boolean sendmail(String destmailid,String cc,String subject,String message,String attachName) {
		boolean flag=false;
		try {
			Mail mail = new Mail();
	        mail.setMailFrom("update@corpseed.com");
	        mail.setMailTo(destmailid);
	        
	        if(cc!=null&&cc.length()>0&&!cc.equalsIgnoreCase("empty")) {
	        	String x[]=cc.split(",");
	        	List<String> list=new ArrayList<>();
	        	for (String c : x) {
					list.add(c);
				}
	        	if(!list.isEmpty()) {
	        		mail.setMailCc(list);
	        	}
	        }
	        
	        mail.setMailSubject(subject);
	        mail.setMailContent(message);
	        this.mailService.sendEmail(mail);
	        flag=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
