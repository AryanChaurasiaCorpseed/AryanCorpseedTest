$(document).ready(function(){
	////// preloader 
	$('#preloader').addClass('fade-up');
	$('body').removeClass('scroll-lock');
    ///// Sidebar-toggle
    var sidebarToggle = $('.burger-icon');
    $(sidebarToggle).click(function(){
		$('#sidebar').toggleClass('collapsed');
		$('main').toggleClass('sidebar-sm')
		$(this).children().toggleClass('la-bars la-arrow-left')
	})
	
	///// sidebar collapse menu 
	var collapseToggle = $('.collapse-toggle');
    $(collapseToggle).click(function(e                                                                                            ){
		if(!$('#sidebar').hasClass('collapsed')) {
			e.preventDefault();
			$(this).toggleClass('show');
			$(this).siblings().next().slideToggle('show');
			$(this).next().slideToggle(300);
			$(this).parent().addClass('active').siblings().removeClass('active');
			$(".collapse-menu").removeClass("show");
		}
	})
	$(window).scroll(function(){
		var scroll = $(window).scrollTop();
		(scroll > 50) ? $('header').addClass('header-fixed') : $('header').removeClass('header-fixed');
	})

//	$('#multi-select').multiselect();

	$("#stateId").change(function (e){
		let stateId=$(this).val();
		if(stateId!=null&&stateId!="")
		$.ajax({
			type : "GET",
			url : "/admin/services/citybystate",
			dataType : "HTML",
			data : {stateId:stateId},
			success : function(response){
				if(Object.keys(response).length!=0){
					$("#cityId").empty();
					$("#cityId").append("<option value=''>Select City</option>");
					response = JSON.parse(response);
					$.each(response,function(key,value){
						$("#cityId").append("<option value='"+key+"'>"+value+"</option>");
					});
				}
			}
		})
		else{
			$("#cityId").empty();
			$("#cityId").append("<option value=''>Select City</option>");
		}
	})

});

var Container = $('.msfield');
var listItems = $('.listItems');
var nextBtn 	= $('.next');
var nextBtn 	= $('.previous');
var submitBTn	= $('.submit');

$(Container).eq(0).css({'opacity': '1', 'z-index': '9'});
$(".next").click(function(){
		var number = $(this).parent().index();
	 	$(this).parent().css({'opacity': '0', 'z-index': '0'});
		$(this).parent().next().css({'opacity': '1', 'z-index': '9'});
		$(listItems).eq(number).addClass("active");
} )
$(".previous").click(function(){
		var number = $(this).parent().index();
	 	$(this).parent().css({'opacity': '0', 'z-index': '0'});
		$(this).parent().prev().css({'opacity': '1', 'z-index': '9'});
		$(listItems).eq(number - 1).removeClass("active");		
} )
$(".submit").click(function(){
	return false;
})


$('.list-group-item-action').click(function() {
	$(this).addClass('list-group-item-success');
  })

  /// add new document line
  $('.add-new-document').click(function() {
	  var newDoc = $('.document-add:last').clone();
	  $(newDoc).insertAfter(".document-add:last");
	  e.preventDefault();

	});
  /// add new service line
  $('.add-new-service').click(function() {

	  var newDoc = $('.service-add:last').clone();
	  $(newDoc).insertAfter(".service-add:last");
	  e.preventDefault();

	});
  /// add new service line
  $('.add-new-faq').click(function() {
	  var newDoc = $('.faq-add:last').clone();
	  $(newDoc).insertAfter(".faq-add:last");
	});
  $('.add-new-step').click(function() {

	  var newDoc = $('.step-add:last').clone();
	  $(newDoc).insertAfter(".step-add:last");
	  e.preventDefault();

	});
	
function showSteps() {
	$('.show-step').slideDown();
  }
function showFaq() {
	$('.show-faq').slideDown();
  }
function showCheck() {
	$('.show-checklist').slideDown();
  }
function showService() {
	$('.show-services').slideDown();
  }
///////// materiasl waves 
Waves.attach('.waves', ['waves-button', 'waves-float']);
Waves.init();

/* menu sliding */
function menuSliding() {
	$('.dropdown').on('show.bs.dropdown', function (e) {

			if ($(window).width() > 320) {
				$(this).find('.dropdown-menu').first().stop(true, true).slideDown(200);

			} else {
				$(this).find('.dropdown-menu').first().stop(true, true).show(200);
			}
		}

	);
	$('.dropdown').on('hide.bs.dropdown', function (e) {
		if ($(window).width() > 320) {
			$(this).find('.dropdown-menu').first().stop(true, true).slideUp(200);
		} else {
			$(this).find('.dropdown-menu').first().stop(true, true).hide(200);
		}
	});

}
$(document).ready(function(){
    $(function() {
        menuSliding();
    })
})

function convertToSlug(str){
	str = str.replace(/[`~!@#$%^&*()_\-+=\[\]{};:'"\\|\/,.<>?\s]/g, ' ').toLowerCase();
	str = str.replace(/^\s+|\s+$/gm,'');
	str = str.replace(/\s+/g, '-');	
  	//document.getElementById("slug").innerHTML= str;
  	document.getElementById("slug").value = str;
}

function convertToUserSlug(){
	let str=$("#firstName").val()+"-"+$("#lastName").val();
	str = str.replace(/[`~!@#$%^&*()_\-+=\[\]{};:'"\\|\/,.<>?\s]/g, ' ').toLowerCase();
	str = str.replace(/^\s+|\s+$/gm,'');
	str = str.replace(/\s+/g, '-');
	//document.getElementById("slug").innerHTML= str;
	document.getElementById("slug").value = str;
}

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
function processApplication(uuid,name,position,data,dept){
	if(data=="2"){
	$.ajax({
		type : "GET",
		url : "/hrm/job-application/update/",
		dataType : "HTML",
		data : {uuid:uuid,name:name},
		success : function(response){
			location.reload();						
		}
	});	
	}else if(data=="1"){
		$("#Department").val(dept);		
		fillDepartmentUser(dept)
				
		var message="Dear <b>"+name+"</b>,<br><br><p>Thank you for your application to the <b>"+position+"</b> role at <b>Corpseed ITES Pvt. Ltd.</b></p><p>We would like to invite you to interview for the <b>"+position+"</b> role with <span style='font-weight:600'>[Interviewer Name]</span>.</p>"
		+"<p>The interview will last 2 hours in total.</p><p>Please find the schedule below:</p>"
		 +"<p>Date: <span style='font-weight:600'>[Date]</span></p><p>Time: <span style='font-weight:600'>[Time]</span></p>"
		 +"<p>Mode: <span style='font-weight:600'>[Mode]</span></p>"
		 +"<p>Address: A-154A, 2nd Floor, A Block, Sector 63, Noida, Uttar Pradesh - 201301</p>"
		 +"<p>Please visit https://www.corpseed.com/ to know more about Corpseed.</p>"
		 +"<p>Reach out to us at hr@corpseed.com for any queries or rescheduling the interview.</p><p>Sincerely,<br/><b>(HR Team)</b></p>Corpseed ITES Pvt. Ltd.<br/>A-154A, 2nd Floor, A Block, Sector 63, Noida, Uttar Pradesh - 201301<br/>+91-7558-640-644<br/>hello@corpseed.com</p>";
		$("#Message").val(message);   
		CKEDITOR.instances['Message'].setData(message);
		$("#JobAppUuid").val(uuid);
		$("#InterviewModal").modal("show");
	}else if(data=="3"){
		if(confirm("Want to reset this application?")){
	 $.ajax({
		type : "GET",
		url : "/hrm/job-application/reset/",
		dataType : "HTML",
		data : {uuid:uuid,status:"3"},
		success : function(response){
			location.reload();						
		}
	});}else location.reload();
	}else if(data=="4"){
		if(confirm("Want to hold this application?")){
			 $.ajax({
				type : "GET",
				url : "/hrm/job-application/reset/",
				dataType : "HTML",
				data : {uuid:uuid,status:"4"},
				success : function(response){
					location.reload();						
				}
			});
	}else{
		location.reload();
	}
	}
}
function updateStatus(uuid,status){
	if(confirm("Want to proceed this action ?")){
		 $.ajax({
			type : "GET",
			url : "/hrm/job-application/update-status/",
			dataType : "HTML",
			data : {uuid:uuid,status:status},
			success : function(response){
				location.reload();						
			}
		});
	}else{
		location.reload();
	}
}
function fillDepartmentUser(dept){
	$.ajax({
		type : "GET",
		url : "/hrm/job-application/interviewer/",
		dataType : "HTML",
		data : {dept:dept},
		success : function(data){	
			$("#Interviewer").empty();
			$("#Interviewer").append(data);					
		}
	});
	
}
function sendInvitationmail(){
	var date=$("#DateForInterview").val();
	var time=$("#TimeForInterview").val();
	var interviewer=$("#Interviewer").val();
	var interviewMode=$("#InterviewMode").val();
	
	if(date==""){
	$("#DateForInterview").css("color","red");
		return false;
	}else{
		$("#DateForInterview").css("color","black");
	}
	if(time==""){
	$("#TimeForInterview").css("color","red");
		return false;
	}else{
		$("#TimeForInterview").css("color","black");
	}
	if(interviewer==""){
	$("#Interviewer").css("color","red");
		return false;
	}else{
		$("#Interviewer").css("color","black");
	}
	if(interviewMode==""){
	$("#InterviewMode").css("color","red");
		return false;
	}else{
		$("#InterviewMode").css("color","black");
	}
	
	var uuid=$("#JobAppUuid").val();
	var message=$("#Message").val();
	
	$("#messageBar").html("Please wait,I'M sending invitation for interview..... !!");
	$("#messageBar").attr("style", "display:block;");
	setTimeout(function(){ 
		$("#messageBar").attr("style", "display:none");
	}, 5000);
		
	var x=interviewer.split("#");
	interviewerName=x[1];
	$("button[name=interviewSubmit]").attr("disabled","disabled"),
	$.ajax({
		type : "POST",
		url : "/hrm/job-application/interview-schedule",
		dataType : "HTML",
		data : {
				date:date,
				time:time,
				uuid:uuid,
				message:message,
				interviewerName:interviewerName,
				interviewMode:interviewMode,
				id:x[0]
		},
		success : function(response){
			$("button[name=interviewSubmit]").removeAttr("disabled","disabled");
			if(response=="pass"){
				$("#InterviewModal").modal("hide");
				$("#messageBar").html("Successfully interview schedule on "+date+" at "+time+"  !!");
				$("#messageBar").attr("style", "display:block;");
				setTimeout(function(){ 
					location.reload();
				}, 5000);
			}else{
				$("#messageBar").html("Something went wrong, Please contact to admin  !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
								
		}
	});
}
function setDate(date){
	var content=CKEDITOR.instances['Message'].getData();	
	content=content.replace("[Date]", date);	
	CKEDITOR.instances['Message'].setData(content);
}
function tConvert (time) {
  // Check correct time format and split into components
  time = time.toString ().match (/^([01]\d|2[0-3])(:)([0-5]\d)(:[0-5]\d)?$/) || [time];

  if (time.length > 1) { // If time format correct
    time = time.slice (1);  // Remove full string match value
    time[5] = +time[0] < 12 ? ' AM' : ' PM'; // Set AM/PM
    time[0] = +time[0] % 12 || 12; // Adjust hours
  }
  return time.join (''); // return adjusted time or original string
}
function setTime(time){
	time=tConvert(time);	
//	var t=time.split(":");
//	if(Number(t[0])<=12)time=time+" AM";
//	else time=time+" PM";
	console.log(time);
	var content=CKEDITOR.instances['Message'].getData();
	content=content.replace("[Time]", time);	
	CKEDITOR.instances['Message'].setData(content);	
}
function setInterviewMode(mode){
	var content=CKEDITOR.instances['Message'].getData();
	content=content.replace("[Mode]", mode);	
	CKEDITOR.instances['Message'].setData(content);
}
function uploadNewDocument(ChooseNewFile,ChoosedOldFile){
	$("#"+ChoosedOldFile).hide();
	$("#"+ChooseNewFile).removeClass("d-none");
}
function uploadCandidateFile(id){
	var name="file"+id;
	$("#"+name).click();
}
function uploadDocument(id){
	$.ajax({
    url: "/candidate/uploadFile",
    type: "POST",
    data: new FormData($("#form"+id)[0]),
    enctype: 'multipart/form-data',
    processData: false,
    contentType: false,
    cache: false,
    success: function (response) {		
	  $("#doc"+id).html(response);
    },
    error: function () {
      // Handle upload error
      // ...
    }
  });
}
function rejectDocument(id,uuid){
	$("#action"+id).hide();
	$.ajax({
		type : "GET",
		url : "/hrm/track-application/reject-document",
		dataType : "HTML",
		data : {
				uuid:uuid
		},
		success : function(response){
			if(response=="pass"){
				$("#doc"+id).html("NA");				
			}else{
				$("#action"+id).show();
				$("#messageBar").html("Something went wrong, Please contact to admin  !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
								
		}
	});
}
function goBack() {
  window.history.back();
}

function updateFormStatus(uuid,data){
	var id="CheckBox"+data;
	var status="2";
	if($("#"+id).prop('checked') == true){
		status="1";
	}
		$.ajax({
		type : "GET",
		url : "/admin/industry/details/form-update",
		dataType : "HTML",
		data : {
				uuid : uuid,
				status : status
		},
		success : function(response){
			if(response=="fail"){
				$("#messageBar").html("Something went wrong, Please try-again later !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
								
		}
	});
}

function setFormStatus(uuid,data){
	var id="CheckBox"+data;
	var status="2";
	if($("#"+id).prop('checked') == true){
		status="1";
	}
		$.ajax({
		type : "GET",
		url : "/admin/services/details/update-form-status",
		dataType : "HTML",
		data : {
				uuid : uuid,
				status : status
		},
		success : function(response){
			if(response=="fail"){
				$("#messageBar").html("Something went wrong, Please try-again later !!");
				$("#messageBar").attr("style", "display:block;color:red;");
				setTimeout(function(){ 
					$("#messageBar").attr("style", "display:none");
				}, 5000);
			}
								
		}
	});
}

function filterByCategory(url){
	window.location = url;
}

function disableMe(){
	$(".disableMePlease").attr("disabled","disabled");
}
 function filter(){
	if ($('.filter_menu').is(':visible')){
		$(".filter_menu").slideUp();
	}else{
		$(".filter_menu").slideDown();
	}	
}
 $(document).click(function (e) {
              if (!$(e.target).closest('.trackfilter, .filter_menu ').length) {
             $('.filter_menu ').stop(true).slideUp();
            }
          });

function pushOnBitrix(uuid){
	 var enqUuid="";
	 var len=uuid.length;
	 for (i=0; i<len; i++) {
    	enqUuid+=uuid[i];    
    if(i!=len-1)enqUuid+="#";
  }
if(enqUuid!=""){  
	$.ajax({
		type : "POST",
		url : "/enquiry/push-bitrix24",
		dataType : "HTML",
		data : {enqUuid:enqUuid},
		success : function(response){
								
		}
	});
}
}
function getCategoryService(category){
	if(category!=null&&category!='')
	$.ajax({
		type : "GET",
		url : "/admin/master/category-service",
		dataType : "HTML",
		data : {category:category},
		success : function(response){
			if(Object.keys(response).length!=0){
			response = JSON.parse(response);	
			var serviceSelect = $('#ServicesId');	
			serviceSelect.empty();
			 $.each(response,function(key,value){
				var option = new Option(value, key, false, false);
    			serviceSelect.append(option).trigger('change');			 	 			 
		     });
			}				
		}
	});
}
  function uploadFile(file){
  	data = new FormData();
      data.append("file", file);

      $.ajax({
          data: data,
          type: "POST",
          url: "/summernote/upload", //replace with your url
          cache: false,
          contentType: false,
          processData: false,
          success: function(url) {
        	  if(url=="exist"){
        		  alert("File already exist !!")
        	  }else{
              	$('#description').summernote("insertImage", url);
        	  }
          }
      });
  }
function toogle(){
$(".sidebar").animate({left: '0'},"fast");
 }

function closebtn(){
$(".sidebar").animate({left: '-250'}, "fast");
 }
 function setInterviewer(data){
	var d=data.split("#");
	var content=CKEDITOR.instances['Message'].getData();
	content=content.replace("[Interviewer Name]", d[1]);	
	CKEDITOR.instances['Message'].setData(content);
}

$("#ServiceFormData").validate({
	rules:{
	selectService: "required"},
	messages:{		
	    selectService: "Please select services..",
	},
	submitHandler:function(e){
		var services=$("#selectService").val();
		var serviceLink="";
		for(var i=0;i<services.length;i++){
			serviceLink+=services[i];
			if(services.length-1!=i)serviceLink+="#";
		}		
		$("#getServiceLink").val("<span class='serviceView'>"+serviceLink+"</span>");	
	}
});

$("#UploadFormData").validate({
	rules:{
	file: "required"},
	messages:{		
	    file: "Please select file..",
	},
	submitHandler:function(e){
		$("button[name=uploadFormBtn]").attr("disabled","disabled"),
		
	$.ajax({
	    url: "/ckeditor/uploadFile",
	    type: "POST",
	    data: new FormData($("#UploadFormData")[0]),
	    enctype: 'multipart/form-data',
	    processData: false,
	    contentType: false,
	    cache: false,
	    success: function (response) {
		  $("button[name=uploadFormBtn]").removeAttr("disabled","disabled");	
		  $("#getFileLink").val("pdfview="+response+"=pdfview");			
	    },
	    error: function () {
	      alert("Something went wrong");
	    }
	  });
	}
});

function copyLink(){
	var copyText = document.getElementById("getFileLink");
	  copyText.select();
	  copyText.setSelectionRange(0, 99999)
	  document.execCommand("copy");
	  CKEDITOR.instances['description'].insertText($("#getFileLink").val().trim());
}
function copyCode(){
	var copyText = document.getElementById("getServiceLink");
	  copyText.select();
	  copyText.setSelectionRange(0, 99999)
	  document.execCommand("copy");
	  CKEDITOR.instances['description'].insertHtml($("#getServiceLink").val().trim());
}
// CKEDITOR.instances['description'].insertHtml($("#getFileLink").val().trim());   
function enquiryForm(){
	 CKEDITOR.instances['description'].insertHtml('<span class="formView">--------------Blog Contact Form-------------</span>');
}       
function showFeedback(id){
	$.ajax({
		type : "GET",
		url : "/admin/feedback/view/"+id,
		dataType : "HTML",
		data : {},
		success : function(response){
			$("#feedbackBody").html(response);
			$("#feedbackView").modal("show");				
		}
	});	
}
$("#ProductFormData").validate({
	rules:{
		selectService: "required"},
	messages:{
		selectService: "Please select Product..",
	},
	submitHandler:function(e){
		var products=$("#selectProduct").val();
		var productLink="";
		for(var i=0;i<products.length;i++){
			productLink+=products[i];
			if(products.length-1!=i)productLink+="#";
		}
		$("#getProductLink").val("<span class='productView'>"+productLink+"</span>");
	}
});
function copyProductCode(){
	var copyText = document.getElementById("getProductLink");
	copyText.select();
	copyText.setSelectionRange(0, 99999)
	document.execCommand("copy");
	CKEDITOR.instances['description'].insertHtml($("#getProductLink").val().trim());
}