////// sticky nav bar ////////
let interval;
$('.search-toggle, .search-close').click(function () {
	$('#search-cont').toggleClass('active');
});

$(document).ready(function () {
  $('#preloader').fadeOut("slow");
  $('body').removeClass('scroll-lock');
 // console.log('Preloader ended')
});

function mySearch(){
	$('#search-cont').toggleClass('active'); 
	if($('#search-cont').hasClass('active')){
		$("#ServiceBlogSearch").focus(); 
		setPopularBlogAndService();	
	}else{$("#homeServiceBlogSearch").html("");}
}
function setPopularBlogAndService(){
	$.ajax({
		type : "GET",
		url : "/search/popular-services-blogs",
		dataType : "HTML",
		data : {},
		success : function(response){
			if(Object.keys(response).length!=0){
				$("#homeServiceBlogSearch").html(response);
			}
		}
	});
}
function showHideApplyCoupon(data){ 
	if(data==""){
		$("#CheckBoxCoupon,#CrossBoxCoupon").hide()
	}else{
		$("#CheckBoxCoupon").show()
		$("#CrossBoxCoupon").hide()
	}
}
function consultNow(catId){
	$("#CategoryId").val(catId);
	$("#ConsultNowModal").modal("show");
}

function saveNewsVisitedRecord(slug,title){
	var slug=$("#"+slug).val();
	var title=$("#"+title).val();
	$.ajax({
		type : "POST",
		url : "/enquiry/news/visitor",
		dataType : "HTML",
		data : {		
			slug:slug,
			title:title		
		},
		success : function(){
		}
	});
}

function saveVisitedRecord(type,id,titleId){
	var uuid=$("#"+id).val();
	var title=$("#"+titleId).val();
//	console.log(uuid+"\n"+title);
	$.ajax({
		type : "POST",
		url : "/enquiry/visitor/save",
		dataType : "text/HTML",
		data : {	
			type:type,	
			uuid:uuid,
			title:title,	
			location:window.location.href			
		},
		success : function(response){						
		}
	});
}
function applyCoupon(){
	var coupon=$("#ApplyCouponBox").val();
	var ServiceUuid=$("#ServiceUuid").val();
	var array ="";
	$("input:checkbox[name=packageId]:checked").each(function(){
    	array=array+$(this).val()+",";
	});
	$.ajax({
		type : "GET",
		url : "/service/cart/coupon",
		dataType : "HTML",
		data : {
			coupon : coupon,
			array : array,
			ServiceUuid : ServiceUuid
			
		},
		success : function(response){
			if(response!="invalid"){
				$("#ApplyCouponBox").attr("readonly",true);
				$("#CheckBoxCoupon").hide()
				$("#CrossBoxCoupon").show()	
				var x=response.split("#");
				$("#finalInr").html(x[1]);
				$("#finalDiscount").html(x[0]);
				$("#DiscountFinal").removeClass("disp-none");
			}else if(response=="invalid"){
				$("#DiscountFinal").addClass("disp-none");
				$("#messageBar").html("Either Coupon Expired or Invalid Coupon, Please enter a valid coupon !!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
						$("#ApplyCouponBox").val("");
				}, 6000);
			}else if(response=="error"){
				$("#messageBar").html("Service Doesn't exist, Please contact to admin !!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
				}, 6000);				
			}			
		}
	});
	
}
function removeCoupon(){
	var coupon=$("#ApplyCouponBox").val();
	var ServiceUuid=$("#ServiceUuid").val();
	var array ="";
	$("input:checkbox[name=packageId]:checked").each(function(){
    	array=array+$(this).val()+",";
	});
	$.ajax({
		type : "GET",
		url : "/service/cart/coupon-remove",
		dataType : "HTML",
		data : {
			coupon : coupon,
			array : array,
			ServiceUuid : ServiceUuid
			
		},
		success : function(response){
			if(response!="invalid"){
				$("#ApplyCouponBox").val("");
				$("#ApplyCouponBox").attr("readonly",false);
				$("#CheckBoxCoupon,#CrossBoxCoupon").hide()	
				$("#DiscountFinal").addClass("disp-none");
				
				var x=response.split("#");
				$("#finalInr").html(response);
				$("#finalDiscount").html("0.0");
			}else if(response=="invalid"){				
				$("#messageBar").html("Something went wrong, Please try-again later !!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);
			}
		}
	});		
}

$('.blog-text').find('img').first().hide();

var date=new Date();  
var day=date.getDate();  
var month=date.getMonth()+1;  
var year=date.getFullYear();
var fullDate = year+"-"+month+"-"+day;



////////// scroller spy blog page!

$('.main-content-nav').scrollspy({ target: '#mainnav' })

function slugify(content){
	return content. toLowerCase(). replace(/ /g,'-'). replace(/[^\w-]+/g,'');
}

$(document).ready(function(){
	$("#ServiceBlogSearch").on("keyup", function(){
	var str = $(this).val().toLowerCase();
	if(str!=''){
    	$.ajax({
			type : "GET",
			url : "/search/service-industry-blog/"+str,
			dataType : "HTML",
			data : {},
			success : function(response){
				$("#homeServiceBlogSearch").html(response);
    		}});
    		}else{
    			$("#homeServiceBlogSearch").html('');
    		}   	
	});

	
	$("#home-search").on("keyup", function(){
	var str = $(this).val().toLowerCase();
	var sqValue='';
	if(str!=''){
    	$.ajax({
    			type : "GET",
    			url : "/search/"+str,
    			dataType : "HTML",
    			data : {},
    			success : function(response){
    				if(Object.keys(response).length!=0){
    				response = JSON.parse(response);		
    				 $.each(response,function(key,value){
    				 	var finalres = "<li><a href='/service/"+key+"'>"+value+"</a></li>";
					  	sqValue=sqValue+finalres;    					 
    			     });
    			     $("#homesearch").html("<ul>"+sqValue+"</ul>");
    				}else{
    				$("#homesearch").html('');
    				}
    			}
    		}); 
    		}else{
    		$("#homesearch").html('');
    		}   	
	});
});

$(document).ready(function(){
	$("#blog-search").on("keyup", function(){
    	var str = $(this).val().toLowerCase();
    	if(str.length>3){
	    	xmlhttp = null;
	    	var url = "/blogs/"+str;
		    if (window.XMLHttpRequest) {
		    	xmlhttp = new XMLHttpRequest();
		    }
		    xmlhttp.onreadystatechange = function(){
	    	    if(xmlhttp.readyState == 4 && xmlhttp.status == 200){
	            	if(xmlhttp.responseText != null){
	            		var words = xmlhttp.responseText;
	            		if(words.length>1){
	            			$('#blogsearch').html(words);
				        }
	            	}
		        }
	    	}
	    	xmlhttp.open("GET", url, false);
	    	xmlhttp.send();
	    }else{
	    	$('#blogsearch').html("");
	    }
	});
});

$(document).ready(function(){
	
	$("#city").on("keyup", function(){
    	var str = $(this).val().toLowerCase();
    	if(str.length>0){    	
		var city_search="";
		$.ajax({
			url: "/city/"+str,
			type: 'GET',
			dataType: 'json',
			data: {},
			contentType: 'application/json',
			mimeType: 'application/json',
			success: function (data) {
				$.each(data, function (index, article) {
					city_search+='<li><a href="javascript:void(0)" onclick="$(\'#city\').val(\''+article+'\');$(\'#citysearch\').hide();">'+article+'</a></li>';
				});
			$('#citysearch').html("<ul>"+city_search+"</ul>");
			$('#citysearch').show();
			},
			error:function(data,status,er) {
				alert("error: "+data+" status: "+status+" er:"+er);
			}
	});
	}else{
		 $('#citysearch').html('');
		$('#citysearch').hide();
	}
	});
});

function cityclose(){
	var listItems = document.querySelectorAll("ul #cityhtml a");
	listItems.forEach(function(item){
		item.onclick = function(e){
			$('#citysearch').hide();
			document.getElementById("city").value = this.innerText;
			
		}
	});
}

$("#SearchLawUpdate").validate({
	rules:{
	fromDate: "required",
	toDate: "required",
	department: "required"},
	messages:{
		fromDate: "Please select from-date !!",	    
	    toDate: "Please select to-date !!",
	},

});

function validateEmail(emailId,button){
	var email=$("#"+emailId).val();
	const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
if(email==""||re.test(String(email).toLowerCase())==false){
		$("#"+emailId).val("");
		$("#messageBar").html("Invalid email address,please enter a valid email address !!");
		$("#messageBar").attr("style", "display:block;color:red;");
		setTimeout(function(){ 
			$("#messageBar").attr("style", "display:none");
	}, 6000);
		return false;
	}
}

$("#bookMeeting").validate({
	rules:{
	name: "required",
	email: "required",
	mobile:{required:!0,minlength:10,maxlength:15},
	city: "required",
	dateTime: "required",
	message: "required"
	},
	messages:{
		name: "Please enter your full name",
		email: "Please enter email",
		city: "Please enter city",
		dateTime: "Please select date time",
		messge: "Please describe meeting purpose.."
	},
	submitHandler:function(e){		
//		$("#bookMeeting").submit();
		var mobile=window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber();
		$("#mobile").val(mobile);
		$("#mobileotpverifynumber").val(mobile);
		let name=$("input[name=name]").val();
		sendOtp("resendOTP","resendOTPTime",name);
		$("#exampleModalOtp").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal\",\"mobile\")'>Change</a>");
		$("#otpModal").modal("show");
	}
});

$("#ChatBoxValidation").validate({
	rules:{
	chatName: "required",
	chatMobile:{required:!0,minlength:10,maxlength:15}},
	messages:{
		chatName: "Please enter your full name",
	},
	submitHandler:function(e){
		var mobile=$("#chatMobile").val();
		$("#mobileotpverifynumber").val(mobile);
		sendOtpBlog("resendOTPChat","resendOTPTimeChat",mobile);			
		$("#exampleModalOtpChat").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModalChat\",\"chatMobile\")'>Change</a>");
		$("#otpModalChat").modal("show");
	}
});

function startChat(mobile,otp){
	$('.chat-mail').addClass('hide');
    $('.chat-body').removeClass('hide');
    $('.chat-input').removeClass('hide');
    $('.chat-header-option').removeClass('hide');
	$("#HelloName").html($("#chatName").val());	
	$.ajax({
		type : "GET",
		url : "/chat_boat/category",
		dataType : "HTML",
		data : {
			mobile:mobile,
			otp:otp
		},
		success : function(response){
			if(Object.keys(response).length!=0){
				
				response = JSON.parse(response);
				$("#ChatTyping").hide();
				 $.each(response,function(key,value){				 
					$(''+
						'<div class="chat-option you category" onclick="getPrint(\''+key+'\',\''+value+'\',\'category\')">'+value+'</div>'
						).insertBefore("#AppendChatData");	
								 
			     });
				
			}
		}
	});
}
function getCategoryAgain(){
	$(".chat-body").animate({ scrollTop: $('.chat-body').prop("scrollHeight")}, 1000);
	$(".anothercategory").remove();
	$.ajax({
		type : "GET",
		url : "/chat_boat/category",
		dataType : "HTML",
		data : {},
		success : function(response){
			if(Object.keys(response).length!=0){
				
				response = JSON.parse(response);
				$("#ChatTyping").hide();
				 $.each(response,function(key,value){				 
					$(''+
						'<div class="chat-option you category" onclick="getPrint(\''+key+'\',\''+value+'\',\'category\')">'+value+'</div>'
						).insertBefore("#AppendChatData");
			     });
			}
		}
	});
}
function getPrint(key,value,type){
	$(".category").remove();
	$(''+
	'<div class="chat-bubble me">'+value+'</div>'
	).insertBefore("#AppendChatData");
	$("#ChatTyping").show();
	setTimeout(function(){ 
		if(type=="category"){
			$(''+
			'<div class="chat-bubble you">Great! Please choose type of Business Licenses ?</div>'
			).insertBefore("#AppendChatData");
			getPrintServices(key);
		}else if(type=="service"){
			$("#ServiceUuid").val(key);
			var name=$("#HelloName").html();
			var GreatMsgId=$("#GreatMsgId").val();
			var pos="scroll-position"+GreatMsgId;
			$(''+
			'<div class="chat-bubble you '+pos+'">Let our advisors assist you with quotation and invoice.</div>'+
			'<div class="chat-bubble you">Thanks '+name+', let me quickly arrange a callback for you.</div>'+
			'<div class="chat-bubble you">Our expert adviser will connect you shortly. You can also contact our 24x7 Support team at 7558-640-644 or info@corpseed.com.</div>'+
			'<div class="chat-bubble you"><img src="/images/hang.gif" style="max-width:100%"/><br/> Just hang in there! We are on our way to help you out.</div>'+
			'<div class="chat-option you anothercategory" onclick="getCategoryAgain()">Book another service</div>'+
			'<br/><div class="chat-option you anothercategory" onclick="endChat()">End Chat</div>'
			).insertBefore("#AppendChatData");
			$("#ChatTyping").hide();
//			$(".chat-body").animate({ scrollTop: $('.chat-body').prop("scrollHeight")}, 1000);
			$("#GreatMsgId").val(Number(GreatMsgId)+1);			 
			scrollChat(pos);
			saveThisEnquiry(key);
		}			
	}, 1000);
	
}
function scrollChat(pos){
//	console.log(pos);
	var container = $('.chat-body');
	var scrollTo = $("."+pos);
	
	var position = scrollTo.offset().top-container.offset().top+container.scrollTop();
		  
	// Animating scrolling effect
	container.animate({
	    scrollTop: position
	}); 
}
function endChat(){
	$(".chat-bot-icon").click();
}
function saveThisEnquiry(serviceUuid){
	var categoryUuid=$("#CategoryUuid").val();
	var message=$("#ChatTypingText").val();	
	var name=$("#chatName").val();	
	var email=$("#chatEmail").val();
	var mobile=$("#chatMobile").val();
	var otp=$("#OTPChatVerify798456T").val();
	// console.log("otp chat===="+otp);
	if(serviceUuid!="NA"){
		message="Enquiry from chat-boat."
	}
		$.ajax({
		type : "POST",
		url : "/chat_boat/save-enquiry",
		dataType : "HTML",
		data : {
			otp:otp,
			categoryUuid : categoryUuid,
			serviceUuid : serviceUuid,
			name : name,
			email : email,
			mobile : mobile,
			message : message,
			location:window.location.href
		},
		success : function(){
			if(serviceUuid=="NA"){
				$("#ChatTypingText").val('');
				endChat();
				$("#success_tic").modal("show");					
			}		
		}
	});
}
function getPrintServices(key){
	$("#ChatTyping").hide();
	$("#ChatTypingText").hide();
	$(".input-action-icon").hide();
	$("#CategoryUuid").val(key);
	$.ajax({
		type : "GET",
		url : "/chat_boat/category/services",
		dataType : "HTML",
		data : {
			key:key
		},
		success : function(response){
			if(Object.keys(response).length!=0){
			response = JSON.parse(response);
			$("#ChatTyping").hide();
			var position="";
			var pos="";
			var k=0;
			 $.each(response,function(key,value){
				if(k==0){position=key;pos=key;k=1;}else{position="";}
			 	$(''+
			'<div class="chat-option you category '+position+'" onclick="getPrint(\''+key+'\',\''+value+'\',\'service\')">'+value+'</div>'
				).insertBefore("#AppendChatData");				 
		     });
//			$(".chat-body").animate({ scrollTop: $('.chat-body').prop("scrollHeight")}, 1000);
//			console.log("Going to scroll this position="+pos);
			scrollChat(pos);
			}
		}
	});
}
$("#cartEnquiry").validate({
	rules:{
	name: "required",
	email:{required: true, email: true},
	mobile:{required:!0,minlength:10,maxlength:15}},
	city: "required",
	messages:{
		name: "Please enter your full name",
	    email: {
	      required: "Please enter your email id to contact you",
	      email: "Your email address must be in the format of name@domain.com"
	    },
	    city: "Please search your city and select",
	},
	submitHandler:function(e){
		$("button[name=submit]").attr("disabled","disabled"),
		$.ajax({
			url:"/enquiry/cart-enquiry",
			data:{
				type:$("#type").val(),
				name:$("#name").val(),
				email:$("#email").val(),
				mobile:$("#mobile").val(),
				serviceId:$("#type_id").val(),
				message:$("#message").val(),
				city:$("#city").val(),
				status:1,
				ipAddress:$("#ip").val(),
				postdate:fullDate,
				modifydate:fullDate,
				location:window.location.href
			},
			type:"GET",
			dataType:"HTML",
			success:function(e){
				if(e!==""){
					location.href = e;
				}
			}
		})
	}
});
$("#BlogEnquiry1").validate({
	rules:{
	name: "required",
	email:{required: true, email: true},
	mobile:{required:!0,minlength:10,maxlength:15}},
	city: "required",
	messages:{
		name: "Please enter your full name",
	    email: {
	      required: "Please enter your email id to contact you",
	      email: "Your email address must be in the format of name@domain.com"
	    },
	    city: "Please search your city and select",
	},
	submitHandler:function(e){
		$("button[name=blogSubmit1]").attr("disabled","disabled"),
	
		$.ajax({
			url:"/enquiry/saveblogenquiry",
			data:{				
				name:$("#name").val(),
				email:$("#email").val(),
				mobile:window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber(),				
				message:$("#message").val(),
				pageurl:$("#PageUrl").val(),
				city:$("#city").val(),
				postDate:$("#postDate").val(),
				CategoryId:$("#CategoryId").val(),
				modifyDate:$("#modifyDate").val()
			},
			type:"POST",
			dataType:"HTML",
			success:function(e){
			$("button[name=blogSubmit1]").removeAttr("disabled");
				if(e=="pass"){
					$("#BlogEnquiry1")[0].reset();					
					$("#messageBar").html("Thanks for enquiry, our advisory team will contact you soon");
					$("#messageBar").attr("style", "display:block");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
					}, 5000);
				}else if(e=="invalid"){
					$("#messageBar").html("Invalid email address,please enter a valid email address !!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
					}, 5000);
				}
			}
		})
	}
});
function saveBlogEnquiry(nameId,emailId,mobileId){
	var name=$("#"+nameId).val();
//	var email=$("#"+emailId).val();	
	var mobile=window.intlTelInputGlobals.getInstance(document.querySelector("#"+mobileId)).getNumber();
	$("#dynamicBlogMobId").val(mobileId);
	$("#dynamicBlogNameId").val(nameId);
	if(name==null||name==""){
		$("#messageBar").html("Please enter name !!");
		$("#messageBar").attr("style", "display:block;color:red;");
		$("#messageBar").fadeOut(5000);
		return false;
	}

	if(mobile==null||mobile==""){
		$("#messageBar").html("Please enter valid mobile number !!");
		$("#messageBar").attr("style", "display:block;color:red;");
		$("#messageBar").fadeOut(5000);
		return false;
	}	
		sendOtpBlog('resendOTP2','resendOTPTime2',mobile);
		$("#mobileotpverifynumber").val(mobile);
		$("#exampleModalOtp2").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal2\",\""+mobileId+"\")'>Change</a>");
		$("#otpModal2").modal("show");
	
}
function saveBlogFormEnquiry(otp){
	$("button[name=blogSubmit]").attr("disabled","disabled");
	var mobileId=$("#dynamicBlogMobId").val();
	var nameId=$("#dynamicBlogNameId").val();
	$.ajax({
		type : "POST",
		url : "/enquiry/saveblogenquiry",
		dataType : "HTML",
		data : {
			otp:otp,
			name:$("#"+nameId).val(),
			email:'NA',
			mobile:$("#"+mobileId).val(),				
			message:$("#message").val(),
			pageurl:$("#PageUrl").val(),
			city:$("#city").val(),
			postDate:$("#postDate").val(),
			CategoryId:$("#CategoryId").val(),
			modifyDate:$("#modifyDate").val()
		},
		success : function(e){
			$("button[name=blogSubmit]").removeAttr("disabled");			
			if(e=="pass"){	
				$("#"+nameId).val('');
//				$("#"+emailId).val('');
				$("#"+mobileId).val('');				
				$("#whatsApp").val("1");			
				$("#success_tic").modal("show");
			}else if(e=="invalid"){
				$("#messageBar").html("Invalid email address,please enter a valid email address !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
		}
	});
}
$("#BlogEnquiry").validate({
	rules:{
	name1: "required",
	email1:{required: true, email: true},
	mobile1:{required:!0,minlength:10,maxlength:15}},
	city: "required",
	messages:{
		name1: "Please enter your full name",
	    email1: {
	      required: "Please enter your email id to contact you",
	      email1: "Your email address must be in the format of name@domain.com"
	    },
	    city: "Please search your city and select",
	},
	submitHandler:function(e){
		var mobile=window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber();
		sendOtp('resendOTP1','resendOTPTime1');
		$("#mobileotpverifynumber").val(mobile);
		$("#exampleModalOtp1").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal1\",\"mobile1\")'>Change</a>");
		$("#otpModal1").modal("show");
	}
});

function blogEnquiry(otp){
	$("button[name=blogSubmit]").attr("disabled","disabled"),
	
	$.ajax({
		type : "POST",
		url : "/enquiry/saveblogenquiry",
		dataType : "HTML",
		data:{
			otp:otp,
			name:$("#name1").val(),
			email:$("#email1").val(),
			mobile:window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber(),				
			message:$("#message").val(),
			pageurl:$("#PageUrl").val(),
			city:$("#city").val(),
			postDate:$("#postDate").val(),
			CategoryId:$("#CategoryId").val(),
			modifyDate:$("#modifyDate").val()
		},
		success:function(e){
		$("button[name=blogSubmit]").removeAttr("disabled");
			if(e=="pass"){
				$("#BlogEnquiry")[0].reset();					
				$("#whatsApp").val("1");			
				$("#success_tic").modal("show");
			}else if(e=="invalid"){
				$("#messageBar").html("Invalid email address,please enter a valid email address !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
		}
	})
}

$("#industryEnquiry").validate({
	rules:{
	name: "required",
	email:{required: true, email: true},
	mobile:{required:!0,minlength:10,maxlength:14}},
	city: "required",
	message: "required",
	messages:{
		name: "Please enter your full name",
	    email: {
	      required: "Please enter your email id to contact you",
	      email: "Your email address must be in the format of name@domain.com"
	    },
	    city: "Please search your city and select",
		message: "Please enter message",
	},
	submitHandler:function(e){
		var mobile=window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber();
		sendOtp('resendOTP1','resendOTPTime1');
		$("#mobileotpverifynumber").val(mobile);
		$("#exampleModalOtp1").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal1\",\"inEnqMobile\")'>Change</a>");
		$("#otpModal1").modal("show");
	}
});

function industryEnquiry(otp){
	$("button[name=inenqsubmit]").attr("disabled","disabled"),
	$.ajax({
		url:"/enquiry/industry",
		data:{
			otp:otp,
			name:$("#name").val(),
			email:$("#email").val(),
			mobile:window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber(),				
			city:$("#city").val(),
			message:$("#message").val(),
			pageUrl:$("#PageUrl").val(),
			postDate:$("#postDate").val(),
			modifyDate:$("#modifyDate").val(),
			industryId:$("#IndustryId").val(),
			location:window.location.href
		},
		type:"POST",
		dataType:"HTML",
		success:function(e){
		$("button[name=inenqsubmit]").removeAttr("disabled");
			if(e=="pass"){
				$("#industryEnquiry")[0].reset();		
				$("#whatsApp").val("1");			
				$("#success_tic").modal("show");
			}else if(e=="invalid"){					
				$("#messageBar").html("Invalid email address,please enter a valid email address !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
		}
	})
}

function changeYourCa(otp){
	$.ajax({
		url:"/enquiry/change-your-ca",
		data:{
			otp:otp,
			name:$("#name").val(),
			email:$("#email").val(),
			mobile:window.intlTelInputGlobals.getInstance(mobile_intel).getNumber(),
			city:$("#city").val(),
			postDate:$("#postDate").val(),
			modifyDate:$("#modifyDate").val(),
			packTime:$("#packTime").val(),
			packPrice:$("#packPrice").val(),
			packageName:$("#packageName").val(),
			location:window.location.href
		},
		type:"POST",
		dataType:"HTML",
		success:function(e){
			$("button[name=submit_enquiry]").removeAttr("disabled");
			if(e=="pass"){
				$("#ChangeYourCA").modal("hide");
				$("#Change_Your_CA")[0].reset(),
					$("#messageBar").html("Thanks for enquiry, our advisory team will contact you soon");
				$("#messageBar").attr("style", "display:block");
				setTimeout(function(){
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
		}
	})
}

$("#Change_Your_CA").validate({
	rules:{
	name: "required",
	email:{required: true, email: true},
	mobile:{required:!0,minlength:10,maxlength:15}},
	city: "required",
	messages:{
		name: "Please enter your full name",
	    email: {
	      required: "Please enter your email id to contact you",
	      email: "Your email address must be in the format of name@domain.com"
	    },
	    city: "Please search your city and select",
	},
	submitHandler:function(e){
		$("button[name=submit_enquiry]").attr("disabled","disabled");
		var mobile=window.intlTelInputGlobals.getInstance(mobile_intel).getNumber();
		$("#mobile").val(mobile);
		$("#mobileotpverifynumber").val(mobile);
		sendOtp("resendOTP","resendOTPTime");
		$("#exampleModalOtp").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal\",\"mobile\")'>Change</a>");
		$("#otpModal").modal("show");
	}
});
$("#contactUsEnquiry").validate({
	rules:{
	name: "required",
	email:{required: true, email: true},
	mobile:{required:!0,minlength:10,maxlength:15}},
	city: "required",
	message: "required",
	messages:{
		name: "Please enter your full name",
	    email: {
	      required: "Please enter your email id to contact you",
	      email: "Your email address must be in the format of name@domain.com"
	    },
	    city: "Please search your city and select",
	    message: "Please enter message",
	},
	submitHandler:function(e){
		var mobile=window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber();	
 		sendOtp("resendOTP","resendOTPTime");
		$("#mobileotpverifynumber").val(mobile);
		 $("#exampleModalOtp").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal\",\"mobile\")'>Change</a>");
 		$("#otpModal").modal("show");
	}
});
function saveContactUsEnquiry(otp){
$("button[name=submit]").attr("disabled","disabled"),
	
		$.ajax({
			url:"/enquiry/contact-us",
			data:{
				otp:otp,
				name:$("#name").val(),
				email:$("#email").val(),
				mobile:window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber(),			
				city:$("#city").val(),
				message:$("#message").val(),
				postDate:$("#postDate").val(),
				modifyDate:$("#modifyDate").val(),
				location:window.location.href
			},
			type:"POST",
			dataType:"HTML",
			success:function(e){
			$("button[name=submit]").removeAttr("disabled");
				if(e=="pass"){
					$("#contactUsEnquiry")[0].reset(),
					$("button[name=submit]").removeAttr("disabled");
					$("#success_tic").modal("show");
				}else if(e=="invalid"){					
					$("#messageBar").html("Invalid email address,please enter a valid email address !!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
					}, 5000);
				}
				
			}
		});
}
$("#serviceEnquiry").validate({
	rules:{
	name: "required",
	email:{required: true, email: true},
	mobile:{required:!0,minlength:10,maxlength:15}},
	city: "required",
	messages:{
		name: "Please enter your full name",
	    email: {
	      required: "Please enter your email id to contact you",
	      email: "Your email address must be in the format of name@domain.com"
	    },
	    city: "Please search your city and select",
	},
	submitHandler:function(e){
		var mobile=window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber();
		sendOtp('resendOTP1','resendOTPTime1');
		$("#mobileotpverifynumber").val(mobile);
		$("#exampleModalOtp1").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal1\",\"serEnqMobile\")'>Change</a>");
		$("#otpModal1").modal("show");
	}
});

function serviceEnquiry(otp){
	$("button[name=servEnqSubmit]").attr("disabled","disabled"),
//	console.log("Service enquiry calldddddddd");
	$.ajax({
		url:"/service/enquiry",
		data:{
			otp:otp,
			slug:$("#Slug").val(),
			url:$("#PageUrl").val(),
			name:$("#name").val(),
			email:$("#email").val(),
			mobile:window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber(),				
			city:$("#city").val(),
			postDate:$("#postDate").val(),
			categoryId:$("#CategoryId").val(),
			serviceId:$("#ServiceId").val(),
			message:$("#Message").val(),
			modifyDate:$("#modifyDate").val()
		},
		type:"POST",
		dataType:"HTML",
		success:function(e){
			$("button[name=servEnqSubmit]").removeAttr("disabled");
			if(e=="pass"){					
				$("#serviceEnquiry")[0].reset();					
				$("#whatsApp").val("1");			
				$("#success_tic").modal("show");
				
			}else if(e=="invalid"){
				$("#messageBar").html("Something went wrong, Please try-again later !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
		}
	})
}

$("#newsSubscribe").validate({
	rules:{
	newsEmail:{required: true, email: true}
	},
	messages:{
		newsEmail: {
	      required: "Please enter your email id",
	      newsEmail: "Your email address must be in the format of name@domain.com"
	    }
	},
	submitHandler:function(e){
		$("button[name=newsSubmit]").attr("disabled","disabled"),
		$.ajax({
			url:"/subscribe",
			data:{
				email:$("#newsEmail").val()				
			},
			type:"POST",
			dataType:"HTML",
			success:function(e){
			$("button[name=newsSubmit]").removeAttr("disabled");
				if(e=="pass"){
					location.href="/subscribe/thanks";
				}else if(e=="invalid"){
					$("#messageBar").html("Invalid email address , Please enter correct email.");
					$("#messageBar").attr("style", "display:block");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
					}, 5000);
				}else if(e=="duplicate"){					
					$("#messageBar").html("You are alredy subscribed,You will notify on upcoming updates.");
					$("#messageBar").attr("style", "display:block");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
					}, 5000);
				}else if(e=="fail"){
					$("#messageBar").html("Something went wrong , Please try-again later.");
					$("#messageBar").attr("style", "display:block");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
					}, 5000);
				}else if(e=="empty"){
					$("#messageBar").html("Please enter a valid email address to subscribe.");
					$("#messageBar").attr("style", "display:block");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
					}, 5000);
				}
			}
		});
	}
});

$("#StartUpGuideEnquiry").validate({
	rules:{
	pname: "required",
	pemail:{required: true, email: true},
	pmobile:{required:!0,minlength:10,maxlength:15}},	
	messages:{
		pname: "Please enter your full name",
	    pemail: {
	      required: "Please enter your email id to contact you",
	      email: "Your email address must be in the format of name@domain.com"
	    }
	},
	submitHandler:function(e){
		$("button[name=guidesubmit]").attr("disabled","disabled");
		var mobile=$("#mobile").val();
		$("#mobile").val(mobile);
		$("#mobileotpverifynumber").val(mobile);
		sendOtp("resendOTP","resendOTPTime");
		$("#exampleModalOtp").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal\",\"mobile\")'>Change</a>");
		$("#otpModal").modal("show");

	}
});

function startUpGuide(otp){
	$.ajax({
		url:"/enquiry/save",
		data:{
			otp:otp,
			name:$("#pname").val(),
			email:$("#pemail").val(),
			mobile:$("#mobile").val(),
			url:$("#url").val(),
			postDate:$("#postDate").val(),
			modifyDate:$("#modifyDate").val()
		},
		type:"POST",
		dataType:"HTML",
		success:function(e){
			$("button[name=guidesubmit]").removeAttr("disabled");
			if(e=="pass"){
				$("#StartUpGuideEnquiry")[0].reset(),
					$("#messageBar").html("Thank your for your valuable time.");
				$("#messageBar").attr("style", "display:block");
//					$("#DownloadLeagelGuide").click();
				setTimeout(function(){
					$("#messageBar").attr("style", "display:none");
					$("#DownloadLegalFile")[0].click();
				}, 3000);
				$('#StartUpModal').modal('hide');  //To hide
			}else if(e=="invalid"){
				$("#messageBar").html("Invalid email address,please enter a valid email address !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
		}
	})
}

$("#CallBackSubmit").validate({
	rules:{
	pmobile:{required:!0,minlength:10,maxlength:15}},	
	submitHandler:function(e){	
		var mobile=$("#mobile").val();	
		sendOtp("resendOTP","resendOTPTime");
		$("#mobileotpverifynumber").val(mobile);
		$("#exampleModalOtp").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal\",\"mobile\")'>Change</a>");
		$("#otpModal").modal("show");		
	}
});
function calBackSubmit(otp){
	console.log("calBackSubmit otp=="+otp);
$.ajax({
		url:"/enquiry/callback",
		type:"POST",
		dataType:"HTML",
		data:{
			"otp":otp,
			"name":$("#enq_name").val(),
			"email":$("#enq_email").val(),
			"whatsApp":$("#whatsApp").val(),
			"mobile":window.intlTelInputGlobals.getInstance(mobile_intel).getNumber(),
			"page":$("#page").val(),				
			"location":window.location.href
		},
		success:function(e){
			console.log(e);
			if(e=="pass"){
				$("#CallBackSubmit")[0].reset();				
				$("#whatsApp").val("1");
				$("#success_tic").modal("show");
			}
		},error:function(xhr,status,error){
			console.log("error : "+JSON.parse(xhr.responseText));
	    }
	})
}

$("#partnerEnquiry").validate({
	rules:{
	pname: "required",
	pemail:{required: true, email: true},
	pmobile:{required:!0,minlength:10,maxlength:15}},
	poccupation: "required",
	pmessage: "required",
	customCheck: "required",
	messages:{
		pname: "Please enter your full name",
	    pemail: {
	      required: "Please enter your email id to contact you",
	      email: "Your email address must be in the format of name@domain.com"
	    }
	},
	submitHandler:function(e){
		var mobile=$("#mobile").val();	
		sendOtp("resendOTP","resendOTPTime");
		$("#mobileotpverifynumber").val(mobile);
		$("#exampleModalOtp").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal\",\"mobile\")'>Change</a>");
		$("#otpModal").modal("show");	
	}
});

function savePartnerEnquiry(otp){
$("button[name=psubmit]").attr("disabled","disabled"),
	$.ajax({
		url:"/enquiry/savepartnerenquiry",
		data:{
			"otp":otp,
			"name":$("#pname").val(),
			"email":$("#pemail").val(),
			"mobile":$("#mobile").val(),
			"message":$("#poccupation").val()+" - "+$("#pmessage").val(),								
			"postDate":$("#postDate").val(),
			"modifyDate":$("#modifyDate").val(),
			"location":window.location.href
		},
		type:"POST",
		dataType:"HTML",
		success:function(e){
		$("button[name=psubmit]").removeAttr("disabled");
			if(e=="pass"){
				$("#partnerEnquiry")[0].reset();
				$("#exampleModal").modal("hide");  //To hide
				$("#success_tic").modal("show")
			}else if(e=="invalid"){					
				$("#messageBar").html("Invalid email address,please enter a valid email address !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
		}
	})	
}

$("#consultNowEnquiry").validate({
	rules:{
	consultname: "required",
	consultemail:{required: true, email: true},
	mobile:{required:!0,minlength:10,maxlength:15}},
	consultcity: "required",
	consultmessage: "required",
	messages:{
		consultname: "Please enter your full name",
	    consultname: {
	      required: "Please enter your email id to contact you",
	      email: "Your email address must be in the format of name@domain.com"
	    }
	},
	submitHandler:function(e){
		var mobile=$("#mobile").val();
		sendOtp('resendOTP','resendOTPTime');
		$("#mobileotpverifynumber").val(mobile);
		$("#exampleModalOtp").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal\",\"mobile\")'>Change</a>");
		$("#otpModal").modal("show");
	}
});
function saveConsultNow(otp){
	$("button[name=consultsubmit]").attr("disabled","disabled"),
	$.ajax({
		url:"/enquiry/consult-now",
		data:{
			otp:otp,
			name:$("#consultname").val(),
			email:$("#consultemail").val(),
			mobile:$("#mobile").val(),
			city:$("#consultcity").val(),
			message:$("#consultmessage").val(),
			CategoryId:$("#CategoryId").val(),							
			postDate:$("#consultpostDate").val(),
			modifyDate:$("#consultmodifyDate").val(),
			location:window.location.href
		},
		type:"POST",
		dataType:"HTML",
		success:function(e){
		$("button[name=consultsubmit]").removeAttr("disabled");
			if(e=="pass"){
				$("#ConsultNowModal").modal("hide");
				$("#consultNowEnquiry")[0].reset();					
				$("#success_tic").modal("show");
				$('#exampleModal').modal('hide');  //To hide
			}else if(e=="invalid"){
				$("#messageBar").html("Invalid email address,please enter a valid email address !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
		}
	})
}
$("#calcFind").validate({
	rules:{
	state: "required",
	ctype: "required",
	capitalAmt:{required:!0,minlength:5,number:!0,maxlength:15}},
	messages:{
		state: "Please choose state",
	    ctype: "Please choose company type",
		capitalAmt: "Please enter total capital amount."
	},
	submitHandler:function(e){
		//$("button[name=submit]").attr("disabled","disabled"),
		$.ajax({
			url:"/api",
			data:{
				action:"postData",
				state:$("#state").val(),
				ctype:$("#ctype").val(),
				capitalAmt:$("#capitalAmt").val()
			},
			type:"GET",
			dataType:"HTML",
			success:function(data){
				var myObj = JSON.parse(data);
				//$('#userTable').html(data);
				$('#states').html(myObj.state);
				$('#ctypes').html(myObj.ctype);
				$('#normal').html(myObj.normal);
				$('#moa').html(myObj.moaCharge);
				$('#aoa').html(myObj.aoaCharge);
				$('#stamp').html(myObj.stampDutyAmt);
				$('#stampAoa').html(myObj.stampDutyAoaAmt);
				$('#stampMoa').html(myObj.stampDutyMoaAmt);
				$('#total').html(myObj.amount);
			}
		})
	}
});


////// sticky nav bar ////////
$(document).ready(function () {
  $('#preloader').fadeOut("slow");
  $('body').removeClass('scroll-lock');  
})

////////// scroller spy blog page!
$('.main-content-nav').scrollspy({ target: '#mainnav' })


////// sticky nav bar ////////
$(document).ready(function () {
  $(window).scroll(function () {
    var scroll = $(window).scrollTop();
    if (scroll >= 30) {
      $("nav.navbar,.mobilecontact").addClass("scroll");      
    } else {
      $("nav.navbar,.mobilecontact").removeClass("scroll");
    }
  });

  ///////// nav open
  $(".fixed-top .navbar-toggler").on("click", function(){
    $('#main-nav').toggleClass("show");
    $(this).toggleClass('collapsed');
    $('body').toggleClass("scroll-lock");
  });
    
	$('.search-toggle, .search-close').click(function (){
		$('#search-cont').toggleClass('active');
	});
  // close button 
  $(".btn-close").click(function(e){
    $(".navbar-collapse").removeClass("show");
    $("body").removeClass("scroll-lock");
  }); 
  ///// init tooltip data
  $(function () {
    $('[data-toggle="tooltip"]').tooltip();
  })
  
});



//////////////// cards service ///////////
$(document).ready(function () {
  var service =   $('.service-cont .service');

  $(service).click(function() {
      if(!$(this).hasClass("active")){
       // console.log("Click menu Service!");
        $(this).siblings(".service").hide().delay(500);
        $('#close').addClass('show');
        $(this).addClass("active");
        $(this).children('.service-menu').addClass("open");
        $('#v-all-service').hide();
        $('html, body').animate({
          scrollTop: $('#services').offset().top - 85
        }, 500);
        setServiceHeight();
      }
  })

  $('#close').click(function(){
      // $(service).removeClass(".active");      
      $(this).removeClass('show');
      $(".service.active").removeClass("active");
      $(".service-menu.open").removeClass("open");
      $('.service').show();
      $('#v-all-service').show();
      revertHeight();
  })

  $('.tb-select').click(function(){
    $('.tb-select').removeClass('active');
    $(this).addClass('active')
  })

  function closeMenu($this) {
    $(".service").removeClass("active");
    $(".service-menu").removeClass("open");
  }
  
  

  function setServiceHeight($this){
   // console.log(service.height());
    let vHeight = $('.service.active').outerHeight();
    service.parent().height(vHeight);
  }
  function revertHeight() {
    service.parent().height('auto');
    }

});

/////////////// sliding menu
/* menu sliding */
function menuSliding() {
	$('.dropper').on('show.bs.dropdown', function (e) {

			if ($(window).width() > 20) {
				$(this).find('.dropdown-menu').first().stop(true, true).slideDown(300);
        
			} else {
				$(this).find('.dropdown-menu').first().stop(true, true).show();
			}
		}

	);
	$('.dropper').on('hide.bs.dropdown', function (e) {
		if ($(window).width() > 20) {
			$(this).find('.dropdown-menu').first().stop(true, true).slideUp(300);
		} else {
			$(this).find('.dropdown-menu').first().stop(true, true).hide();
    }
    
    $('.mega-dropdown .dropdown').removeClass('active').first().addClass('active');
	});
}
$(function() {
    menuSliding();
})

/////////// stop menu from closing //////////////////
$(document).on('click', '.dropdown-menu', function (e) {
  e.stopPropagation();
});


//////// addd active on hover mega link ///////// 
$(document).ready(function(){
    if ($(window).width() > 1024) {
      $('.mega-dropdown .dropdown').on('click', function(){
        $(this).addClass('active').siblings().removeClass('active');
      }).on('click', function(){
        $(this).addClass('active').siblings().removeClass('active');
      })
    }

});

////////////////// mouse hover .//////////////////////
//////// addd active on hover mega link ///////// 
$(document).ready(function(){
  if ($(window).width() > 1024) {
    $('.drop-downtoggle').on('mouseenter', function(e) {
      if($(this).next().hasClass('show')){
        return;
      }
//     else{$(this).click();}
    })
  }
})


//////////// mega menu /////////////
$(document).ready(function(){	
  $('.mega-tab-link').hover(function(){
    $('.mega-tab-link').removeClass('active');
    $(this).delay(50).addClass('active');
  })
  

// mega menu 
})
function addIndustryEnquiry(uuid,id){
	$("#iDetailsUuid").val(uuid);
	$("#iDetailsId").val(id);
	var name=$("#name"+id).val();
	var mobile=$("#mobile"+id).val();
	if(name==null||name==""){
		$("#name"+id).addClass("is-invalid");
		return false;
	}else{
		$("#name"+id).removeClass("is-invalid");
	}
	if(mobile==null||mobile==""||mobile.length<10){
		$("#mobile"+id).addClass("is-invalid");
		return false;
	}else{
		$("#mobile"+id).removeClass("is-invalid");
	}
	sendOtpBlog('resendOTP2','resendOTPTime2',mobile);
	$("#mobileotpverifynumber").val(mobile);
	$("#exampleModalOtp2").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal2\",\"mobile"+id+"\")'>Change</a>");
	$("#otpModal2").modal("show");
}
function industryDetailsEnquiry(otp){
	var uuid=$("#iDetailsUuid").val();
	var id=$("#iDetailsId").val();
	var name=$("#name"+id).val();
	var mobile=$("#mobile"+id).val();
	if(name!=null&&name!=""&&mobile!=null&&mobile!=""){
	
	$(".submit-validation").attr("disabled","disabled");
	$.ajax({
		type : "POST",
		url : "/enquiry/industry-details",
		dataType : "HTML",
		data : {
			otp:otp,
			uuid : uuid,
			name :name,
			mobile : mobile,
			location:window.location.href
		},
		success : function(response){
			$(".submit-validation").removeAttr("disabled");
			if(response=="fail"){
				$("#messageBar").html("Something went wrong, Please try-again later !!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
				}, 5000);
			}else if(response=="pass"){
				$("#name"+id).val('');
				$("#mobile"+id).val('');
				$("#success_tic").modal("show");
			}
		}
	});
	}
}
function addServiceEnquiry(uuid,id){ 
	$("#sDetailsUuid").val(uuid);
	$("#sDetailsId").val(id);
	var name=$("#name"+id).val();
	var mobile=$("#mobile"+id).val();
	if(name==null||name==""){
		$("#name"+id).addClass("is-invalid");
		return false;
	}else{
		$("#name"+id).removeClass("is-invalid");
	}
	if(mobile==null||mobile==""||mobile.length<10){
		$("#mobile"+id).addClass("is-invalid");
		return false;
	}else{
		$("#mobile"+id).removeClass("is-invalid");
	}
	
	sendOtpBlog('resendOTP2','resendOTPTime2',mobile);
	$("#mobileotpverifynumber").val(mobile);
	$("#exampleModalOtp2").html("Please enter the OTP sent to "+mobile+"&nbsp;<a href='javascript:void(0)' onclick='changeNumber(\"otpModal2\",\"mobile"+id+"\")'>Change</a>");
	$("#otpModal2").modal("show");
}
function serviceDetailsEnquiry(otp){
	var uuid=$("#sDetailsUuid").val();
	var id=$("#sDetailsId").val();
	var name=$("#name"+id).val();
	var mobile=$("#mobile"+id).val();
	if(name!=null&&name!=""&&mobile!=null&&mobile!=""){
	$(".submit-validation").attr("disabled","disabled");
	$.ajax({
		type : "POST",
		url : "/enquiry/service-details",
		dataType : "HTML",
		data : {
			otp:otp,
			uuid : uuid,
			name :name,
			mobile : mobile,
			location:window.location.href
		},
		success : function(response){
			$(".submit-validation").removeAttr("disabled");
			if(response=="fail"){
				$("#messageBar").html("Something went wrong, Please try-again later !!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
				}, 5000);
			}else if(response=="pass"){
				$("#name"+id).val('');
				$("#mobile"+id).val('');
				$("#success_tic").modal("show");
			}
		}
	});
	}
}
function addTranding(key){
	$.ajax({
		type : "GET",
		url : "/enquiry/tranding-search",
		dataType : "HTML",
		data : {key:key
		},
		success : function(response){			
		}
	});
}
function disableMe(){
	$(".disableMePlease").attr("disabled","disabled");
}
function showFirst(id){
	setTimeout(function(){
	$("#"+id).addClass("active");
	}, 50);
}
function get_hostname() {
	var url=location.href;
    var m = url.match(/^http:\/\/[^/]+/);
    return m ? m[0] : null;
}

$( "#cookie-consent" ).click(function() {
  $.ajax({
		type : "GET",
		url : "/allow-cookie",
		dataType : "HTML",
		data : {},
		success : function(response){			
		}
	});
});
function filterJob(type){
    $('html, body').animate({
        scrollTop: $("#job-filter").offset().top-180
    });
  
	if(type=="sales")$("#Sales-Job").click();
	else if(type=="delivery")$("#Delivery-Job").click();
	else if(type=="hr")$("#HR-Job").click();
	else if(type=="marketing")$("#Marketing-Job").click();
	else if(type=="it")$("#It-Job").click();
	else if(type=="legal")$("#Legal-Job").click();
}
function scrollDiv(div){
 $('html, body').animate({
        scrollTop: $("#"+div).offset().top-130
    });
    
	$(".list-group-item-action").removeClass("active");
	$("."+div).addClass("active");    
}

$("#Complaint_Box").validate({
	rules:{
	iepno:"required",
	cname: "required",
	cemail:{required: true, email: true},
	cmobile:{required:!0,minlength:10,maxlength:15}},
	cmessage: "required",
	messages:{
		iepno: "Enter valid invoice/estimate/project number.!!",
		cname: "Enter your full name.!!",
	    email: {
	      required: "Please enter your email id to contact you",
	      email: "Your email address must be in the format of name@domain.com"
	    },
	    cmobile:"Enter mobile number.!!",
	    cmessage: "Please describe your concern. !!",
	},
	submitHandler:function(e){
		$("button[name=raiseComplaint]").attr("disabled","disabled"),
		$.ajax({
		type : "POST",
		url : "/support/raise-complaint",
		dataType : "HTML",
		data : {
			iepno:$("input[name='iepno']").val(),
			name:$("input[name='cname']").val(),
			mobile:$("input[name='cmobile']").val(),
			email:$("input[name='cemail']").val(),
			message:$("textarea[name='cmessage']").val(),
			postDate:$("input[name='cpostDate']").val(),			
		},
		success : function(response){			;
			$("button[name=raiseComplaint]").removeAttr("disabled","disabled");	
			if(response=="pass"){
				$("#raiseSupport").modal("hide");
				$("#Complaint_Box")[0].reset();
				$("#messageBar").html("Successfully your query submitted, Our team will contact you soon !!");
				$("#messageBar").attr("style", "display:block;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
				}, 8000);
			}else if(response=="invalid"){
				$("#messageBar").html("Please enter a valid invoive/estimate/project no. for identification...... !!");
				$("#messageBar").attr("style", "color:red;");
				$("#messageBar").attr("style", "display:block;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
				}, 8000);
			}else{
				$("#messageBar").html("Something went wrong, Please try-again later !!");
				$("#messageBar").attr("style", "color:red!important;display:block;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");
				}, 8000);
			}	
		}
	});
	}
});
function hideAllTab(){	
	$(".mega-dropdown.mega-dropdown-service .nav-item.dropdown .dropdown-mega-nav").addClass("d-none");
}
//$( ".nav-link.dropdown-mega" ).click(function() {
//	 if($(window).width() < 768)$(this).next().toggleClass("d-none");
//});
$( ".nav-link.dropdown-mega" ).click(function() {
	if(window.matchMedia('(max-width: 768px)').matches) $(this).next().toggleClass("d-none");
});

$("#fb_share,#fb_share1").on("click",function(){
	// 	var url='https://www.facebook.com/sharer/sharer.php?href=' + encodeURIComponent(location.href);
    var fbpopup = window.open("https://www.facebook.com/sharer/sharer.php?u="+encodeURIComponent(location.href)+"", "pop", "width=600, height=400, scrollbars=no");
    return false;
});
$("#whatsApp_share,#whatsApp_share1").on("click",function(){
    window.open("https://api.whatsapp.com/send?text="+encodeURIComponent(location.href)+"", "pop", "width=800, height=600, scrollbars=no");
    return false;
});	
$("#linkdin_share,#linkdin_share1").on("click",function(){
    window.open("https://www.linkedin.com/sharing/share-offsite/?url="+encodeURIComponent(location.href), "pop", "width=800, height=600, scrollbars=no");
    return false;
});		
$("#email_share,#email_share1").on("click",function(){
    window.open("mailto:?body="+encodeURIComponent(location.href));
    return false;
});

function sharePress(type,slug){
	if(type=="facebook"){
		window.open("https://www.facebook.com/sharer/sharer.php?u="+encodeURIComponent(location.href+"/"+slug)+"", "pop", "width=600, height=400, scrollbars=no");
		return false;
	}else if(type=="whatsapp"){
		window.open("https://api.whatsapp.com/send?text="+encodeURIComponent(location.href+"/"+slug)+"", "pop", "width=800, height=600, scrollbars=no");
		return false;
	}else if(type=="linkedin"){
		window.open("https://www.linkedin.com/sharing/share-offsite/?url="+encodeURIComponent(location.href+"/"+slug), "pop", "width=800, height=600, scrollbars=no");
		return false;
	}else if(type=="email"){
		window.open("mailto:?body="+encodeURIComponent(location.href+"/"+slug));
		return false;
	}
}
$(document).ready(function(){
	$("#mobile,#mobile1").keypress(function(evt){
	   evt = (evt) ? evt : window.event;
	
		var charCode = (evt.which) ? evt.which : evt.keyCode;
	
		if (charCode > 31 && (charCode < 48 || charCode > 57)) {
		    return false;
		}
		return true;
	});

});
$(document).ready(function(){
  if ($(window).width() > 1024) {
   $('.nav-link.arrow.drop-downtoggle').removeAttr( "data-toggle" )
  }
})
$(document).ready(function(){
  if ($(window).width() > 767) {
   $('.nav-link.arrow.drop-downtoggle').attr( "data-toggle" )
  }
})
function playVideo(frame,video){
	$("#"+frame).hide();
    $("#"+video).show();
}

function updateWhatsApp(id){
	if($('#'+id).is(':checked')){
		$("#whatsApp").val("1");
	}else{$("#whatsApp").val("2");}}
	
//checking entered character is number or not.

function isNumberKey(evt){

   var charCode = (evt.which) ? evt.which : evt.keyCode;

   if (charCode != 46 && charCode > 31 

     && (charCode < 48 || charCode > 57))

      return false;
   return true;
}

//only digit allow

function isNumber(evt)

{

	evt = (evt) ? evt : window.event;

	var charCode = (evt.which) ? evt.which : evt.keyCode;

	if (charCode > 31 && (charCode < 48 || charCode > 57)) {

	    return false;

	}

	return true;

}
$(".toc_title").click(function(){
  $(".main-toclist").slideToggle();
});

$(document).bind("contextmenu",function(e) {
 e.preventDefault();
});

document.onkeydown = function(e) {
if(e.keyCode == 123) {
return false;
}
if(e.ctrlKey && e.keyCode == 'E'.charCodeAt(0)){
return false;
}
if(e.ctrlKey && e.shiftKey && e.keyCode == 'I'.charCodeAt(0)){
return false;
}
if(e.ctrlKey && e.shiftKey && e.keyCode == 'C'.charCodeAt(0)){
return false;
}
if(e.ctrlKey && e.shiftKey && e.keyCode == 'J'.charCodeAt(0)){
return false;
}
if(e.ctrlKey && e.keyCode == 'U'.charCodeAt(0)){
return false;
}
if(e.ctrlKey && e.keyCode == 'S'.charCodeAt(0)){
return false;
}
if(e.ctrlKey && e.keyCode == 'H'.charCodeAt(0)){
return false;
}
if(e.ctrlKey && e.keyCode == 'A'.charCodeAt(0)){
return false;
}
if(e.ctrlKey && e.keyCode == 'F'.charCodeAt(0)){
return false;
}
if(e.ctrlKey && e.keyCode == 'E'.charCodeAt(0)){
return false;
}
}
function changeNumber(model,mobile){
	$("#"+model).modal("hide");
	$("#"+mobile).focus();
}
$(document).ready(function(){	
	$("#otpModal,#enquiryOtpModal").on('shown.bs.modal', function () {
        $(this).find('#OTP1').focus();
		$(this).find('#OTPENQUIRY1').focus();
    });

	$("#consultNowVerifyOtp").click(function(){
		var otp1=$("#OTP1").val();
		var otp2=$("#OTP2").val();
		var otp3=$("#OTP3").val();
		var otp4=$("#OTP4").val();
		var otp=otp1+otp2+otp3+otp4;
		
		if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
			$("#messageBar").html("Please enter 4 digit OTP..");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);
			return false;
		}
		verifyOtp(otp,"otpModal","verifyOtpInput","consult_now");
	})

	$("#buttonVerifyOtpContact").click(function(){
		var otp1=$("#OTP1").val();
		var otp2=$("#OTP2").val();
		var otp3=$("#OTP3").val();
		var otp4=$("#OTP4").val();
		var otp=otp1+otp2+otp3+otp4;
		
		if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
			$("#messageBar").html("Please enter 4 digit OTP..");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);
			return false;
		}
		verifyOtp(otp,"otpModal","verifyOtpInput","contact_us");
	})

	$("#buttonVerifyOtpCareer").click(function(){
		var otp1=$("#OTP1").val();
		var otp2=$("#OTP2").val();
		var otp3=$("#OTP3").val();
		var otp4=$("#OTP4").val();
		var otp=otp1+otp2+otp3+otp4;
		
		if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
			$("#messageBar").html("Please enter 4 digit OTP..");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);
			return false;
		}
		verifyOtp(otp,"otpModal","verifyOtpInput","career");
	})
    
	$("#buttonVerifyOtpMeeting").click(function(){
		var otp1=$("#OTP1").val();
		var otp2=$("#OTP2").val();
		var otp3=$("#OTP3").val();
		var otp4=$("#OTP4").val();
		var otp=otp1+otp2+otp3+otp4;
		
		if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
			$("#messageBar").html("Please enter 4 digit OTP..");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);
			return false;
		}
		verifyOtp(otp,"otpModal","verifyOtpInput","meeting");
	})    

	$("#buttonVerifyOtpPartner").click(function(){
		var otp1=$("#OTP1").val();
		var otp2=$("#OTP2").val();
		var otp3=$("#OTP3").val();
		var otp4=$("#OTP4").val();
		var otp=otp1+otp2+otp3+otp4;
		
		if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
			$("#messageBar").html("Please enter 4 digit OTP..");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);
			return false;
		}
		verifyOtp(otp,"otpModal","verifyOtpInput","partner");
	})
    
	$("#buttonVerifyOtp").click(function(){
		var otp1=$("#OTP1").val();
		var otp2=$("#OTP2").val();
		var otp3=$("#OTP3").val();
		var otp4=$("#OTP4").val();
		var otp=otp1+otp2+otp3+otp4;
		
		if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
			$("#messageBar").html("Please enter 4 digit OTP..");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);
			return false;
		}
		verifyOtp(otp,"otpModal","verifyOtpInput","callback");
	})

	$("#buttonVerifyOtpStartUpGuide").click(function(){
		var otp1=$("#OTP1").val();
		var otp2=$("#OTP2").val();
		var otp3=$("#OTP3").val();
		var otp4=$("#OTP4").val();
		var otp=otp1+otp2+otp3+otp4;

		if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
			$("#messageBar").html("Please enter 4 digit OTP..");
			$("#messageBar").attr("style", "display:block;color:red;");
			setTimeout(function(){
				$("#messageBar").attr("style", "display:none");
			}, 6000);
			return false;
		}
		verifyOtp(otp,"otpModal","verifyOtpInput","statrtUpGuide");
	})

	$("#buttonVerifyOtpChangeCA").click(function(){
		var otp1=$("#OTP1").val();
		var otp2=$("#OTP2").val();
		var otp3=$("#OTP3").val();
		var otp4=$("#OTP4").val();
		var otp=otp1+otp2+otp3+otp4;

		if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
			$("#messageBar").html("Please enter 4 digit OTP..");
			$("#messageBar").attr("style", "display:block;color:red;");
			setTimeout(function(){
				$("#messageBar").attr("style", "display:none");
			}, 6000);
			return false;
		}
		verifyOtp(otp,"otpModal","verifyOtpInput","changeYourCA");
	})
	
	$("#otpModal1").on('shown.bs.modal', function () {
        $(this).find('#OTP11').focus();
    });
    $("#otpModal2").on('shown.bs.modal', function () {
        $(this).find('#OTP111').focus();
    });
    $("#otpModalChat").on('shown.bs.modal', function () {
        $(this).find('#OTPChat1').focus();
    });
	$("#buttonVerifyOtp1").click(function(){
		var otp1=$("#OTP11").val();
		var otp2=$("#OTP22").val();
		var otp3=$("#OTP33").val();
		var otp4=$("#OTP44").val();
		var otp=otp1+otp2+otp3+otp4;
		
		if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
			$("#messageBar").html("Please enter 4 digit OTP..");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);
			return false;
		}
		verifyOtp(otp,"otpModal1","verifyOtpInput1","industry");
	});
	$("#buttonVerifyOtp2").click(function(){
	var otp1=$("#OTP11").val();
	var otp2=$("#OTP22").val();
	var otp3=$("#OTP33").val();
	var otp4=$("#OTP44").val();
	var otp=otp1+otp2+otp3+otp4;
	
	if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
		$("#messageBar").html("Please enter 4 digit OTP..");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");						
			}, 6000);
		return false;
	}
	verifyOtp(otp,"otpModal1","verifyOtpInput1","service");
  })
$("#buttonVerifyOtp3").click(function(){
	var otp1=$("#OTP11").val();
	var otp2=$("#OTP22").val();
	var otp3=$("#OTP33").val();
	var otp4=$("#OTP44").val();
	var otp=otp1+otp2+otp3+otp4;
	
	if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
		$("#messageBar").html("Please enter 4 digit OTP..");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");						
			}, 6000);
		return false;
	}
	verifyOtp(otp,"otpModal1","verifyOtpInput1","blog");
  })
  
  $("#buttonVerifyOtp4").click(function(){
	var otp1=$("#OTP111").val();
	var otp2=$("#OTP222").val();
	var otp3=$("#OTP333").val();
	var otp4=$("#OTP444").val();
	var otp=otp1+otp2+otp3+otp4;
	
	if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
		$("#messageBar").html("Please enter 4 digit OTP..");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");						
			}, 6000);
		return false;
	}
	verifyOtp(otp,"otpModal2","verifyOtpInput2","blogForm");
  })
  
   $("#buttonVerifyOtp5").click(function(){
	var otp1=$("#OTP111").val();
	var otp2=$("#OTP222").val();
	var otp3=$("#OTP333").val();
	var otp4=$("#OTP444").val();
	var otp=otp1+otp2+otp3+otp4;
	
	if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
		$("#messageBar").html("Please enter 4 digit OTP..");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");						
			}, 6000);
		return false;
	}
	verifyOtp(otp,"otpModal2","verifyOtpInput2","serviceDetails");
  })

  $("#buttonVerifyOtp6").click(function(){
	var otp1=$("#OTP111").val();
	var otp2=$("#OTP222").val();
	var otp3=$("#OTP333").val();
	var otp4=$("#OTP444").val();
	var otp=otp1+otp2+otp3+otp4;
	
	if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
		$("#messageBar").html("Please enter 4 digit OTP..");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");						
			}, 6000);
		return false;
	}
	verifyOtp(otp,"otpModal2","verifyOtpInput2","industryDetails");
  })
 
 $("#buttonVerifyOtpChat").click(function(){
	var otp1=$("#OTPChat1").val();
	var otp2=$("#OTPChat2").val();
	var otp3=$("#OTPChat3").val();
	var otp4=$("#OTPChat4").val();
	var otp=otp1+otp2+otp3+otp4;
	
	if(otp1==null||otp1==''||otp2==null||otp2==''||otp3==null||otp3==''||otp4==null||otp4==''){
		$("#messageBar").html("Please enter 4 digit OTP..");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");						
			}, 6000);
		return false;
	}
	verifyOtp(otp,"otpModalChat","verifyOtpInputChat","chat");
  })
  
})
function verifyOtp(otp,model,modelform,type){
	var mobile=$("#mobileotpverifynumber").val();
	$.ajax({
		type : "GET",
		url : "/enquiry/verify-otp/"+mobile+"/"+otp,
		dataType : "HTML",
		data : {},
		success : function(response){
			//console.log("response="+response);
			if(response=="pass"){	
				//console.log("type==="+type);
				
				$("#"+model).modal("hide");
				$("#"+modelform)[0].reset();
				
				if(type=="callback"){
					calBackSubmit(otp);
				}else if(type=="industry"){
					industryEnquiry(otp);
				}else if(type=="service"){
					serviceEnquiry(otp);
				}else if(type=="blog"){
					blogEnquiry(otp);
				}else if(type=="blogForm"){
					saveBlogFormEnquiry(otp);
				}else if(type=="serviceDetails"){
					serviceDetailsEnquiry(otp);
				}else if(type=="industryDetails"){
					industryDetailsEnquiry(otp);
				}else if(type=="chat"){
					$("#OTPChatVerify798456T").val(otp);
					startChat(mobile,otp);
				}else if(type=="meeting"){
					$("#varifyedOTP").val(otp);
					$("#meetingBtn").prop("disabled",true);
					$("#bookMeeting")[0].submit();
				}else if(type=="partner"){
					savePartnerEnquiry(otp);
				}else if(type=="career"){
					$("#varifyedOTP").val(otp);
					$("#add_application_btn").prop("disabled",true);
         	        $("#add_application")[0].submit();
				}else if(type=="contact_us"){
					saveContactUsEnquiry(otp);
				}else if(type=="consult_now"){
					saveConsultNow(otp);
				}else if(type=="changeYourCA"){
					changeYourCa(otp);
				}else if(type=="statrtUpGuide"){
					startUpGuide(otp);
				}else if(type=="globalEnquiry"){
					globalEnquiry(otp);
				}
				stopTimer();
								
			}else{
				$("#messageBar").html("Please enter valid otp..!!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);
			}
		},error: function(xhr, status, error) {
			//console.log(xhr);
			//console.log(status);
			console.log(error);
		}
	});
}	
function sendOtp(resendOTP,resendOTPTime,name){
	var mobile=$("#mobile").val();
	if(mobile==null||mobile=="")
		mobile=window.intlTelInputGlobals.getInstance(mobile_intel1).getNumber();
		
	$("#"+resendOTP).addClass("d-none");	
	$("#"+resendOTPTime).show();	
	$.ajax({
		type : "POST",
		url : "/enquiry/send-otp/"+mobile,
		dataType : "HTML",
		data : {},
		success : function(response){	
			if(response=="pass"){
				stopTimer();
				startTimer(60,resendOTP,resendOTPTime);
			}else{
				$("#messageBar").html("Something went wrong, Please try-again later..!!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);				
			}
		}
	});
}

function sendOtpBlog(resendOTP,resendOTPTime,mobile,name){
	if(mobile==null||mobile==""||mobile=="NAA"){
		var id=$("#sDetailsId").val();
		mobile=$("#mobile"+id).val();
	}
	if(mobile==null||mobile==""||mobile=="industry"){
		var id=$("#iDetailsId").val();
		mobile=$("#mobile"+id).val();
	}
	if(mobile==null||mobile==""||mobile=="chat"){
		mobile=$("#chatMobile").val();
	}
	if(mobile==null||mobile==""||mobile=="NA"){
		var mobileId=$("#dynamicBlogMobId").val();
		mobile=$("#"+mobileId).val();		
	}	
	
	
	$("#"+resendOTP).addClass("d-none");	
	$("#"+resendOTPTime).show();	
	$.ajax({
		type : "POST",
		url : "/enquiry/send-otp/"+mobile,
		dataType : "HTML",
		data : {name:name,location:window.location.href },
		success : function(response){	
			if(response=="pass"){
				stopTimer();
				startTimer(60,resendOTP,resendOTPTime);
			}else{
				$("#messageBar").html("Something went wrong, Please try-again later..!!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);				
			}
		}
	});
}

function startTimer(seconds,resendOTP,resendOTPTime){
	console.log(seconds+"#"+resendOTP+"#"+resendOTPTime);
	interval = setInterval(function() {
    seconds--;
    $("#"+resendOTPTime).text(seconds);
    if (seconds == 0) {
      // Display a login box
      clearInterval(interval);
      $("#"+resendOTPTime).hide();
  	  $("#"+resendOTP).removeClass("d-none");
    }
  }, 1000);
}
function stopTimer(){
	clearInterval(interval);
}
$('.digit-group').find('input').each(function() {
	$(this).attr('maxlength', 1);
	$(this).on('keyup', function(e) {
		var parent = $($(this).parent());
		
		if(e.keyCode === 8 || e.keyCode === 37) {
			var prev = parent.find('input#' + $(this).data('previous'));
			
			if(prev.length) {
				$(prev).select();
			}
		} else if((e.keyCode >= 48 && e.keyCode <= 57) || (e.keyCode >= 65 && e.keyCode <= 90) || (e.keyCode >= 96 && e.keyCode <= 105) || e.keyCode === 39) {
			var next = parent.find('input#' + $(this).data('next'));
			
			if(next.length) {
				$(next).select();
			} else {
				if(parent.data('autosubmit')) {
					parent.submit();
				}
			}
		}
	});
});

$("#otpModal input,#otpModal1 input,#otpModal2 input,#enquiryOtpModal input").bind("paste", function(e){
   var pastedData = e.originalEvent.clipboardData.getData('text');
   var x=pastedData.split("");
	$("#OTP1,#OTP11,#OTP111,#OTPChat1,#OTPENQUIRY1").val(x[0]);
	$("#OTP2,#OTP22,#OTP222,#OTPChat2,#OTPENQUIRY2").val(x[1]);
	$("#OTP3,#OTP33,#OTP333,#OTPChat3,#OTPENQUIRY3").val(x[2]);
	$("#OTP4,#OTP44,#OTP444,#OTPChat4,#OTPENQUIRY4").val(x[3]);
	$("#OTP4,#OTP44,#OTP444,#OTPChat4,#OTPENQUIRY4").focus();
});
$(document).ready(function(){
	$('.news_review .reactions .reaction').click(function() {	  
	    $(this).toggleClass('active').siblings('.reaction').hide();
	    $(this).addClass("active");
	    var value=$(".news_review .reactions .reaction.active .reaction__label").html();
	    if(value!="Excellent"){
	    	$("#ratingModal").modal("show");
	    }else{
			$("#success_feedback").modal("show");
			$("textarea[name='ratingReviewComment']").val("Excellent : No need comment");
			$("#ratingForm").submit();
	    }
	});
	$("#closeRating").click(function(){
	    $(".news_review .reactions .reaction").removeClass("active");
	    $(".news_review .reactions .reaction").show();
	});	
	
	$("#ratingModal").on('shown.bs.modal', function () {
        $(this).find('#ratingReviewComment').focus();
    });
    
    $(".feather.feather-message-square").click(function(){
		var audio = document.getElementById("chatAudioSound");
        audio.play();
	});
})
$("#ratingForm").validate({
	rules:{
	ratingReviewComment: "required"},
	messages:{
		ratingReviewComment: "Please enter comment..."},
	submitHandler:function(e){
	$("button[name=ratingbtn]").attr("disabled","disabled"),
		$.ajax({
		type : "POST",
		url : "/feedback/"+$("input[name='feedbackType']").val(),
		dataType : "HTML",
		data : {
			comment :$("textarea[name='ratingReviewComment']").val(),
			ratingValue : $(".news_review .reactions .reaction.active .reaction__label").html(),
			location:window.location.href 		
		},
		success : function(response){			;
			$("button[name=ratingbtn]").removeAttr("disabled","disabled");	
			if(response=="pass"){	
				$("#ratingModal").modal("hide");	
				$("#success_feedback").modal("show");
			  }else{
				$("#messageBar").html("Something went wrong, Please try-again later..!!");
					$("#messageBar").attr("style", "display:block;color:red;");
					setTimeout(function(){ 
						$("#messageBar").attr("style", "display:none");						
				}, 6000);	
			}	
		}
	});	
	}
});
$(document).ready(function(){$('.scroll-lock').bind("cut copy paste",function(e) {e.preventDefault();});});
