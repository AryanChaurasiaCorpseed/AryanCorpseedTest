package com.corpseed.entity;

public class Api {
	private final String state;
	private final String ctype;
	private final Double amount;
	private final Double normal;
	private final Double moaCharge;
	private final Double aoaCharge;
	private final Double stampDutyAmt;
	private final Double stampDutyAoaAmt;
	private final Double stampDutyMoaAmt;
	

	private double minVal(double a, double b){
		if(a<=b) {
			return a;
		}else{
			return b;
		}
	}
	
	private double maxVal(double a, double b){
		if(a>=b) {
			return a;
		}else{
			return b;
		}
	}

	public Api(String state, String ctype, Double amount, Double normal, Double moaCharge, Double aoaCharge, Double stampDutyAmt, Double stampDutyAoaAmt, Double stampDutyMoaAmt){
		
		String companyType = ctype;
		double capitalAmt = amount;
		double normalFee = 0;
		double moaFee = 0;
		double aoaFee = 0;
		double stampDuty = 0;
		double stampDutyAoa = 0;
		double stampDutyMoa = 0;
		
		if(capitalAmt<=1000000){
			normalFee=0;
		}else{
			normalFee=500;
		}
		
		if(companyType.equals("Public Limited Company")){
			if(capitalAmt<=1000000){
				moaFee = 0;
			}else if(capitalAmt<=5000000){
				moaFee = 36000+300*((capitalAmt-1000000)/10000);
			}else if(capitalAmt<=10000000){
				moaFee = 156000+100*((capitalAmt-5000000)/10000);
			}else{
				moaFee=206000+75*((capitalAmt-10000000)/10000);
			}
		}else if(companyType.equals("Private Limited Company")){
			if(capitalAmt<=5000000){
				moaFee = 0;
			}else if(capitalAmt<=10000000){
				moaFee = 156000+100*((capitalAmt-5000000)/10000);;
			}else{
				moaFee=206000+75*((capitalAmt-10000000)/10000);
			}
		}else{
			if(capitalAmt<=5000000){
				moaFee = 0;
			}else if(capitalAmt<=10000000){
				moaFee = 156000+100*((capitalAmt-5000000)/10000);;
			}else{
				moaFee=206000+75*((capitalAmt-10000000)/10000);
			}
		}
		
		if(capitalAmt<=1000000){
			aoaFee=0;
		}else if(capitalAmt<=2499999){
			aoaFee=400;
		}else if(capitalAmt<=9999999){
			aoaFee=500;
		}else{
			aoaFee=600;
		}
		
		if(state.equals("Delhi")){
			stampDuty = 10;
			stampDutyAoa=minVal(0.0015*capitalAmt,2500000);
			stampDutyMoa = 200;
		}else if(state.equals("Haryana")){
			if(capitalAmt<=100000){
				stampDutyAoa=60;
			}else{
				stampDutyAoa=120;
			}
			stampDuty = 15;
			stampDutyMoa = 60;
		}else if(state.equals("Maharastra")){
			if(capitalAmt<500000){
				stampDutyAoa=1000;
			}else{
				stampDutyAoa=1000*(capitalAmt/500000);
			}
			stampDuty = 100;
			stampDutyMoa = 200;
		}else if(state.equals("Orissa")){
			stampDuty = 10;
			stampDutyAoa=300;
			stampDutyMoa = 300;
		}else if(state.equals("Andhra Pradesh")){
			if(0.0015*capitalAmt>500000){
				stampDutyAoa=500000;
			}else{
				stampDutyAoa=maxVal(0.0015*capitalAmt,1000);
			}
			stampDuty = 20;
			stampDutyMoa = 500;
		}else if(state.equals("Telangana")){
			if(0.0015*capitalAmt>500000){
				stampDutyAoa=500000;
			}else{
				stampDutyAoa=maxVal(0.0015*capitalAmt,1000);
			}
			stampDuty = 20;
			stampDutyMoa = 500;
		}else if(state.equals("Bihar")){
			if(0.0015*capitalAmt>500000){
				stampDutyAoa=500000;
			}else{
				stampDutyAoa=maxVal(0.0015*capitalAmt,1000);
			}
			stampDuty = 100;
			stampDutyMoa = 500;
		}else if(state.equals("Jharkhand")){
			stampDuty = 5;
			stampDutyAoa=105;
			stampDutyMoa = 63;
		}else if(state.equals("Jammu & Kashmir")){
			if(capitalAmt<=100000){
				stampDutyAoa=150;
			}else{
				stampDutyAoa=300;
			}
			stampDuty = 10;
			stampDutyMoa = 150;
		}else if(state.equals("Tamil Nadu")){
			stampDuty = 20;
			stampDutyAoa=300;
			stampDutyMoa = 200;
		}else if(state.equals("Puducherry")){
			stampDuty = 10;
			stampDutyAoa=300;
			stampDutyMoa = 200;
		}else if(state.equals("Assam")){
			stampDuty = 15;
			stampDutyAoa=310;
			stampDutyMoa = 200;
		}else if(state.equals("Meghalaya")){
			stampDuty = 10;
			stampDutyAoa=300;
			stampDutyMoa = 100;
		}else if(state.equals("Manipur")){
			stampDuty = 10;
			stampDutyAoa=150;
			stampDutyMoa = 100;
		}else if(state.equals("Nagaland")){
			stampDuty = 10;
			stampDutyAoa=150;
			stampDutyMoa = 100;
		}else if(state.equals("Tripura")){
			stampDuty = 10;
			stampDutyAoa=150;
			stampDutyMoa = 100;
		}else if(state.equals("Arunachal Pradesh")){
			stampDuty = 10;
			stampDutyAoa=500;
			stampDutyMoa = 200;
		}else if(state.equals("Mizoram")){
			stampDuty = 10;
			stampDutyAoa=150;
			stampDutyMoa = 100;
		}else if(state.equals("Kerala")){
			if(capitalAmt<=1000000){
				stampDutyAoa=2000;
			}else if(capitalAmt<=2500000){
				stampDutyAoa=5000;
			}else if(capitalAmt>2500000){
				stampDutyAoa=0.0015*capitalAmt;
			}else{
				stampDutyAoa=0;
			}
			stampDuty = 25;
			stampDutyMoa = 1000;
		}else if(state.equals("Lakshadweep")){
			stampDuty = 25;
			stampDutyAoa=1000;
			stampDutyMoa = 500;
		}else if(state.equals("Madhya Pradesh")){
			if(capitalAmt*0.0015>500000){
				stampDutyAoa=500000;
			}else{
				stampDutyAoa=maxVal(capitalAmt*0.0015,5000);
			}
			stampDuty = 50;
			stampDutyMoa = 2500;
		}else if(state.equals("Chattisgarh")){
			if(capitalAmt*0.0015>500000){
				stampDutyAoa=500000;
			}else{
				stampDutyAoa=maxVal(capitalAmt*0.0015,1000);
			}
			stampDuty = 10;
			stampDutyMoa = 500;
		}else if(state.equals("Rajasthan")){
			stampDuty = 10;
			stampDutyAoa=0.005*capitalAmt;
			stampDutyMoa = 500;
		}else if(state.equals("Punjab")){
			if(capitalAmt<=100000){
				stampDutyAoa=5000;
			}else{
				stampDutyAoa=10000;
			}
			stampDuty = 25;
			stampDutyMoa = 5000;
		}else if(state.equals("Himachal Pradesh")){
			if(capitalAmt<=100000){
				stampDutyAoa=60;
			}else{
				stampDutyAoa=120;
			}
			stampDuty = 3;
			stampDutyMoa = 60;
		}else if(state.equals("Chandigarh")){
			stampDuty = 3;
			stampDutyAoa=1000;
			stampDutyMoa = 500;
		}else if(state.equals("Uttar Pradesh")){
			stampDuty = 10;
			stampDutyAoa=500;
			stampDutyMoa = 500;
		}else if(state.equals("Uttarakhand")){
			stampDuty = 10;
			stampDutyAoa=500;
			stampDutyMoa = 500;
		}else if(state.equals("West Bengal")){
			stampDuty = 10;
			stampDutyAoa=300;
			stampDutyMoa = 60;
		}else if(state.equals("Karnataka")){
			stampDuty = 20;
			stampDutyAoa=1000*(capitalAmt/1000000);
			stampDutyMoa = 1000;
		}else if(state.equals("Gujarat")){
			stampDuty = 20;
			stampDutyAoa=minVal(0.005*capitalAmt,500000);
			stampDutyMoa = 100;
		}else if(state.equals("Dadra & Nagar Havelli")){
			stampDuty = 1;
			stampDutyAoa=25;
			stampDutyMoa = 15;
		}else if(state.equals("Goa")){
			stampDuty = 50;
			stampDutyAoa=1000*(capitalAmt/500000);
			stampDutyMoa = 150;
		}else if(state.equals("Daman & Diu")){
			stampDuty = 20;
			stampDutyAoa=1000*(capitalAmt/500000);
			stampDutyMoa = 150;
		}else if(state.equals("Andaman & Nicobar")){
			stampDuty = 20;
			stampDutyAoa=300;
			stampDutyMoa = 200;
		}else {
			stampDuty = 0;
			stampDutyAoa=0;
			stampDutyMoa = 0;
		}
		
		double total = normalFee+moaFee+aoaFee+stampDuty+stampDutyAoa+stampDutyMoa;
		
		this.state = state;
		this.ctype = ctype;
		this.amount = total;
		this.normal = normalFee;
		this.moaCharge = moaFee;
		this.aoaCharge = aoaFee;
		this.stampDutyAmt = stampDuty;
		this.stampDutyAoaAmt = stampDutyAoa;
		this.stampDutyMoaAmt = stampDutyMoa;
	}

	public String getState() {
		return state;
	}
	
	public String getCtype() {
		return ctype;
	}

	public Double getAmount() {
		return amount;
	}
	
	public Double getNormal() {
		return normal;
	}
	
	public Double getMoaCharge() {
		return moaCharge;
	}
	
	public Double getAoaCharge() {
		return aoaCharge;
	}
	
	public Double getStampDutyAmt() {
		return stampDutyAmt;
	}
	
	public Double getStampDutyAoaAmt(){
		return stampDutyAoaAmt;
	}
	
	public Double getStampDutyMoaAmt(){
		return stampDutyMoaAmt;
	}
}