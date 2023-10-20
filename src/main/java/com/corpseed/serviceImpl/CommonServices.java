package com.corpseed.serviceImpl;

import com.corpseed.entity.orderentity.OrderNoGenerator;
import com.corpseed.entity.ReceiptGenerator;
import com.corpseed.entity.User;
import com.corpseed.serviceImpl.orderserviceimpl.OrderNoGeneratorService;
import com.github.opendevl.JFlat;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

@Service
public class CommonServices {

	@Autowired
	private UserServices userService;
	
	@Autowired
	private ReceiptGeneratorService receiptService;
	
	@Autowired
	private OrderNoGeneratorService orderNoService;
	
	@Autowired
	private CommonServices commonService;

	@Autowired
	private Environment env;
	
	private double orderAmount=0;

    public String getUserRole(Principal principal) {
		return userService.isUserExist(principal.getName()).getRole();
	}
	
	public User getLoginedUser(Principal principal) {
		return userService.isUserExist(principal.getName());
	}
	
	public String getUUID() {
		UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        
        return uuidAsString.replace("-","");
	}	
	
	public long getUniqueCode() {
		return (long) Math.floor(Math.random() * 9000000000L) ;
	}
	
//	public String getIpAddress(){
//		String hostAddress="";
//		try {
//			InetAddress IP=InetAddress.getLocalHost();
//			hostAddress = IP.getHostAddress();						
//		}catch(UnknownHostException ue) {
//			ue.printStackTrace();
//		}
//		return hostAddress;
//	}
	
	public String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("x-forwarded-for");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
	public String getToday() {
		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();		   
	    return dtf1.format(now);
	}
	
	public String timeBetweenDates(String first) {
		SimpleDateFormat dtf = new SimpleDateFormat("dd MM yyyy");
		long daysBetween=0;
		String time="Today";		   
	    String last=getToday();
	    try {
	    	first=first.substring(8, 10)+" "+first.substring(5, 7)+" "+first.substring(0, 4);
	    	last=last.substring(8, 10)+" "+last.substring(5, 7)+" "+last.substring(0, 4);
	    	
	        Date date1 = dtf.parse(first);
	        Date date2 = dtf.parse(last);
	        long diff = date2.getTime() - date1.getTime();
	        daysBetween=TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    if(daysBetween>=365) {
	    	long year=daysBetween/365;
	    	if(year>1) {
	    		time=year+" Years ago";
	    	}else {
	    		time=year+" Year ago";
	    	}
	    	
	    }else if(daysBetween>=30) {
	    	long month=daysBetween/30;
	    	if(month>1) {
	    		time=month+" Months ago";
	    	}else {
	    		time=month+" Month ago";
	    	}
	    }else {
	    	if(daysBetween>1) {
	    		time=daysBetween+" Days ago";
	    	}else if(daysBetween==1) {
	    		time="Yesterday";
	    	}else if(daysBetween==0){
	    		time="Today";
	    	}
	    }
	    
		return time;
	}
	
	public String getReceiptNo() {
		//GETTING RECEIPT NO
		ReceiptGenerator rcp=this.receiptService.getLastNo();
		String sn=null;
		if(rcp!=null)sn=rcp.getReceiptNo();
		String newSn="";
		if(sn==null) {
			sn="01";
			newSn="1";
		}
		else {
			sn=(Long.parseLong(sn)+1)+"";
			newSn=sn;
			if(sn.length()==1)sn+="0"+sn;						
		}
		this.receiptService.addReceiptNo(newSn,this.commonService.getToday());
		
		return "1303"+sn;
	}
	
	public String getOrderNo(long serviceId) {
		String today=this.commonService.getToday();
		OrderNoGenerator orderNo=this.orderNoService.getLastOrderNo(today);
		String sn=null;
		if(orderNo!=null)sn=orderNo.getOrderNo();
		String newOrderNo="";
		if(sn==null) {
			newOrderNo="1";
			sn="C001"+today.substring(8)+today.substring(5,7)+serviceId;
		}else {
			sn=(Long.parseLong(sn)+1)+"";
			newOrderNo=sn;
			if(sn.length()==1)sn="C00"+sn;
			else if(sn.length()==2)sn="C0"+sn;
			else sn="C"+sn;
			sn+=today.substring(8)+today.substring(5,7)+serviceId;
		}
		
		this.orderNoService.addOrderNo(new OrderNoGenerator(0, newOrderNo, today));
		return sn;
	}

	
	public void sumAmount(double parseDouble) {
		orderAmount+=parseDouble;
	}

	public void setZero() {
		orderAmount=0;
	}

	public double getOrderAmount() {
		// TODO Auto-generated method stub
		return orderAmount;
	}

	public String dateBeforeDays(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -days);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date = format1.format(cal.getTime());
		
		return date;
	}
	
	public String currentWeekPassedSunday() {
		LocalDate today = LocalDate.now();

	    LocalDate sunday = today.with(previousOrSame(SUNDAY));
	    
	    return sunday.toString();
	}
	public String currentWeekMonday() {
		LocalDate today = LocalDate.now();

	    LocalDate monday = today.with(previousOrSame(MONDAY));
	    
	    return monday.toString();
	}
	
	public String dateBeforeDaysFromDate(int days,String dateMinus) {
		String date="";
		try {
		Calendar cal = Calendar.getInstance();
		Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(dateMinus);  
		cal.setTime(date1);
		cal.add(Calendar.DATE, -days);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		date = format1.format(cal.getTime());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public String dateAfterDays(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +days);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String date = format1.format(cal.getTime());
		
		return date;
	}
	
	public String getDay() {
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());		 
	}
	public int callGetUrl(String GET_URL) {
		int responseCode=400;
		try {
//			System.out.println(GET_URL);
			URL obj = new URL(GET_URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
			responseCode = con.getResponseCode();
//			System.out.println("GET Response Code :: " + responseCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseCode;
	}
	public int callPostURL(String POST_URL,String POST_PARAMS) {
		int responseCode=400;
		try {
		URL obj = new URL(POST_URL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

        // For POST only - START
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        responseCode = httpURLConnection.getResponseCode();
//        System.out.println("POST Response Code :: " + responseCode);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return responseCode;
	}
	@SuppressWarnings("unused")
	public int callPostURLForJSON(String POST_URL,String POST_PARAMS) {
		int responseCode=400;
		try {
		URL obj = new URL(POST_URL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) obj.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

        // For POST only - START
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        responseCode = httpURLConnection.getResponseCode();
        
      BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
      String inputLine;
      StringBuffer response = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
      	response.append(inputLine);
      }
      in.close();
        
        //first way to convert

        JFlat flatMe = new JFlat(response.toString());

        //get the 2D representation of JSON document
        List<Object[]> json2csv = flatMe.json2Sheet().getJsonAsSheet();

        //write the 2D representation in csv format
        flatMe.write2csv(env.getProperty("bitrix24")+"orderLines.csv");
      
        //second way to convert

//        JSONObject output=new JSONObject(response.toString());
//        JSONArray docs = output.getJSONArray("result");
//
//        File file=new File("src/main/resources/orderLines.csv");
//        String csv = CDL.toString(docs);
//        FileUtils.writeStringToFile(file, csv);
        
        
       
		}catch(Exception e) {			
			e.printStackTrace();
		}
		return responseCode;
	}

	public String getTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");  
		   LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	public String getTimeBeforeMinutes(int minutes) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now.minusMinutes(minutes));
	}

	public String getDateTime() {
		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf1.format(now);
	}
	
	public Date getGMTDate(String date1) {
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		format1.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date date = null;
		try {
		    date = format1.parse(date1);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
		return date;
	}

	public String getBrowser(HttpServletRequest req) {
		UserAgent userAgent=UserAgent.parseUserAgentString(req.getHeader("User-Agent"));
		OperatingSystem operatingSystem = userAgent.getOperatingSystem();
	    Browser browser = userAgent.getBrowser();
	    String browserName = browser.getName();
	    Version browserVersion = userAgent.getBrowserVersion();
		return browserName+" : "+browserVersion+", OS : "+operatingSystem;
	}
	
	public String convertTo12Hours(String time) {
		
		try {
			SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
			SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
		    Date _24HourDt = _24HourSDF.parse(time);
		    time=_12HourSDF.format(_24HourDt);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	
	
//	private final String LOCALHOST_IPV4 = "127.0.0.1";
//	private final String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";
	
	public String getIpAddress(HttpServletRequest request) {
		/*
		 * String ipAddress = request.getHeader("X-Forwarded-For");
		 * if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
		 * ipAddress = request.getHeader("Proxy-Client-IP"); }
		 * 
		 * if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
		 * ipAddress = request.getHeader("WL-Proxy-Client-IP"); }
		 * 
		 * if(StringUtils.isEmpty(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
		 * ipAddress = request.getRemoteAddr(); if(LOCALHOST_IPV4.equals(ipAddress) ||
		 * LOCALHOST_IPV6.equals(ipAddress)) { try { InetAddress inetAddress =
		 * InetAddress.getLocalHost(); ipAddress = inetAddress.getHostAddress(); } catch
		 * (UnknownHostException e) { e.printStackTrace(); } } }
		 * 
		 * if(!StringUtils.isEmpty(ipAddress) && ipAddress.length() > 15 &&
		 * ipAddress.indexOf(",") > 0) { ipAddress = ipAddress.substring(0,
		 * ipAddress.indexOf(",")); }
		 * 
		 * return ipAddress;
		 */
		
		String xForwardedForHeader = request.getHeader("X-Forwarded-For");
	    if (xForwardedForHeader == null) {
	        return request.getRemoteAddr();
	    } else {
	        // As of https://en.wikipedia.org/wiki/X-Forwarded-For
	        // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
	        // we only want the client
	        return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
	    }
	}
}
