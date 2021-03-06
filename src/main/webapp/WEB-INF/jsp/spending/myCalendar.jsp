<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>	
 	<jsp:include page="/WEB-INF/jsp/include/head.jsp"></jsp:include>

	<script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script> 
	<link href='https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/main.min.css' rel='stylesheet' />
	<script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/main.min.js'></script>
	<!-- fullcalendar 언어 CDN -->
	<script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.8.0/locales-all.min.js'></script>
	<link rel="stylesheet" href="${ pageContext.request.contextPath }/resources/template/css/bootstrap.min.css">
    <!-- style CSS
		============================================ -->
    <link rel="stylesheet" href="${ pageContext.request.contextPath }/resources/template/css/style.css">

<style>
	#budgetTable {
		width : 800px;
		margin-left: 100px;
   		font-size: 24px;
		margin-bottom : 40px;
	}
	body{
 	font-family: 'hana' , verdana, san-serif;
    }
	 #main-layout {
		width : 90%;
		margin: 0 auto;
	}
	
	.main {
		width : 90%;
		display : flex;
		justify-content : center;
		margin-right: 30px;
	}
	
#title {
    	color : black;
    	font-size : 28px;
    	font-weight : bold;
/*     	border-bottom: 2px solid #a0a0a0; */
    	padding-top : 55px;
    	margin-bottom : 20px;
    	padding-bottom : 16px;
    }
 
    #menu-title {
    	font-size : 24px;
    	font-weight : bold;
    	margin-bottom : 20px;
    	
    }
    
    .title {
		padding-bottom : 0px;
	}	
	.title h2{
	    font-size: 30px;
	    margin-bottom : 60px;
	}
	
	.title h2:after {
	    height: 0;
	   	font-size: 30px;
	}
	
	section {
    	width : 1300px;
    }
    .nav-tabs>li {
	    border: 2px solid lightgray;
	    border-radius: 7px 7px 0 0;
    }
    
    .widget-tabs-list .nav.nav-tabs>li>a {
    font-size: 25px;
     padding: 8px 45px;
}

.border-box {
		padding : 30px;
		border : 2px solid #dddddd; 
		border-radius : 5px;
		margin-bottom : 30px;
		margin-top : 30px;
	}

/* 	ul.nav.nav-tabs::after {
	
		background-color :#00c292;

} */

.fc-col-header {
	background : #f5f5f5;
}
 
.fc-h-event .fc-event-title-container {
    height: 24px;
    font-size: 20px;
}

.fc-day-number {
    float: right;
    font-size: 30px;
}
.fc-toolbar .fc-button {
    color: #fff;
    border: none;
    width: 70px;
    height: 45px;
    font-size: 18px;
    background: #16c89b;
  }
  .fc .fc-toolbar-title {
    font-size: 2.6em;
    margin: 0;
}
  .fc td, .fc th {
 
    font-size: 20px;
}
.card-body, .waves-effect {
    font-size: 20px;
 }
 
 .fc .fc-daygrid-day-number {
    color: black;
  
}

.fc-direction-ltr {
    direction: ltr;
    text-align: right;
    }
    
#historyDetail {
	border-radius : 20px;
/* 	margin-left : 100px;*/
/* 	padding:10px 20px;
	padding-top: 20px; */
	margin-top : 20px;
	margin-bottom : 20px; 
    background: #fffadf; 
    height : 620px; 
}

#historyList {
	padding :20px;
	width : 400px;
	font-size : 20px; 
	 margin-left: 30px;
}

th {
	height : 43px; 
}
#calendar {
	margin-top:15px;
	padding: 20px;
 }
 
 .fc .fc-daygrid-day-bg .fc-highlight {
    z-index: 3;
    background: #fffadf;
}

.fc .fc-daygrid-day.fc-day-today {
    background-color: rgba(188,232,241,.3);
}

#date {
	padding: 10px 30px;
    border-radius: 20px 20px 0 0;
    background: #fff3b5
}

.feedback-text {
	text-align : center;
	font-weight : bold;
}
q {
	/* color : #4bc0c0; */
}
	div#div-caution >img {
	width : 40px;
	margin : 10px;
	}
	
	#div-caution {
	margin-top : 50px;
	margin-left : 180px;
	font-size : 22px;
	font-weight : bold;
	
	}
	
	#progress {
	margin-top : 50px;
	margin-left : 170px;
	margin-right : 100px;
	}
	
	.progress {
		height : 45px;
		
	}
	
	.progress-bar {
		font-weight : bold;
		font-size : 22px;
	}
	.feedback-text {
	text-align : center;
	font-weight : bold;
}
</style>

<script>
$(document).ready(function(){
	 function numberWithCommas(x) {
		    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	} 
	 
	 
	let now = new Date()
	let lastDate = new Date(now.getYear(), now.getMonth()+1,0).getDate();
	let nowDate = now.getDate();
	console.log(lastDate)
	console.log(nowDate)
	
	let restDate = lastDate - nowDate
	console.log(restDate)
	
	//한달 권장 일일 지출액
	let dailyExpense = Math.floor(${myMonthlyBudget.consumption}/lastDate)
	dailyExpenseComma = numberWithCommas(dailyExpense)
	console.log(dailyExpense)
	
	$("#dailyExpense").html(dailyExpenseComma)
	
	//지금부터 권장 지출액
	let nowDailyExpense = Math.floor(${hanaro.consumptionBalance}/restDate)
	nowDailyExpense = numberWithCommas(nowDailyExpense)
	
	//$('#nowDailyExpense').html(nowDailyExpense)
	console.log(nowDailyExpense)
	
	//이번달 예상 지출
	let nowExpense = ${myMonthlyBudget.consumption} - ${hanaro.consumptionBalance} 	//오늘까지 쓴 금액
	let avgExpenseOfDay = nowExpense / nowDate										//하루 평균 사용금액	
	let expectExpense = Math.floor(avgExpenseOfDay * lastDate)								//달 예상 지출액
	expectExpense = numberWithCommas(expectExpense)
	//$('#expectExpense').html(expectExpense)
	//console.log(nowExpense)
	//console.log(avgExpenseOfDay)
	//console.log(expectExpense)
	
	//오늘까지 권장 지출액
	let expense1= numberWithCommas(dailyExpense * nowDate)
	let expense2 = numberWithCommas(${myMonthlyBudget.consumption - hanaro.consumptionBalance})
	let progressWidth1 = dailyExpense * nowDate / ${myMonthlyBudget.consumption} * 100
	let progressWidth2 = ${(myMonthlyBudget.consumption - hanaro.consumptionBalance) /myMonthlyBudget.consumption * 100 } - progressWidth1
	
	console.log(progressWidth1)
	console.log(progressWidth2)
	text = $('#progressBarTemplate').text()
	text = text.replace(/\{width\}/gi, progressWidth1)
				.replace(/\{nowWidth\}/gi, progressWidth2)
				.replace(/\{expense1\}/gi, expense1)
				.replace(/\{expense2\}/gi, expense2)
	console.log(text)
	//$('#progress').html(text)
})
</script>
<script id="progressBarTemplate" type="text/template">
<div class="progress">
<div class="progress-bar bg-warning progress-bar-striped progress-bar-animated" role="progressbar" style="width: {width}% " aria-valuenow="30" aria-valuemin="0" aria-valuemax="100"> 권장 지출액 : {expense1} </div>
 <div class="progress-bar bg-danger progress-bar-striped progress-bar-animated" role="progressbar" style="width: {nowWidth}% " aria-valuenow="20" aria-valuemin="0" aria-valuemax="100"> 실제 지출액 : {expense2}</div>
</div>
</script>
<script>
$(document).ready(function(){

	getCalendarList()
	let calList
	function getCalendarList(){
		let userCode = ${loginMember.userCode}
		
		let data = {userCode : userCode}
		let calArr = []
		$.ajax({
			type : 'post',
			contentType : 'application/json',
			url : '${ pageContext.request.contextPath }/myCalendar/calendarList',
			data : JSON.stringify(data),
			success : function(calendarList){
				calList = calendarList
				console.log(calList)
				let json = JSON.parse(calendarList)
/* 				console.log("json !!!!! : " +json) */
				if(calendarList.length > 0){
					json.forEach(function(cal){
						calArr.push(cal)
					
					})
					console.log(calArr)
				}
	
	(function($) {
		"use strict";
			// calendar element 취득
			var calendarEl = $('#calendar')[0];
			// full-calendar 생성하기
			var calendar = new FullCalendar.Calendar(calendarEl, {
				height: '750px', // calendar 높이 설정
				expandRows: true, // 화면에 맞게 높이 재설정
				slotMinTime: '08:00', // Day 캘린더에서 시작 시간
				slotMaxTime: '20:00', // Day 캘린더에서 종료 시간
				// 해더에 표시할 툴바
				headerToolbar: {
				left: 'prev',
				center: 'title',
				right: 'next'
			}, 
				 dateClick: function(info) {
						let userCode = ${ loginMember.userCode }
	        			let date = info.dateStr	//클릭한 날짜 뽑기
	        			//alert('클릭 날짜: ' + date); 
	        	 		
	        			let url = '${pageContext.request.contextPath }/myCalendar/historyListByDate/' + date 
	        			//alert(url)
	        			
	        			let data = { userCode : userCode }
	        			
			        	$.ajax({
			        	 	type : 'post',
			        		contentType : 'application/json',
			        		url : url,
			        		data : JSON.stringify(data),
			        		
			        		success : function(historyList){
			        			console.log(historyList)
			        			let json = JSON.parse(historyList)
								
			        			let html = ''
			        			if(historyList.length > 0){
			        				
			        		 	json.forEach(function(historyByDate){
			        				let temp = $('#spendingTemplate').text()
			        					temp= temp.replace(/\{category\}/gi, historyByDate.category)
			        								.replace(/\{othersName\}/gi, historyByDate.othersName)
			        								.replace(/\{transMoney\}/gi, historyByDate.transMoney)
												        					
			        				html += temp
			        				
			        				//$('#date').text(historyByDate.transDate)
			        			}) 
			        			}
			        			$('#date').text(date.substring(5,10).replace(/-/gi,'/'))
			        			console.log(html)
			        			$('#historyList').html(html)
			        			
			        		}
			        		
			        /* 		 let html = $('#spendingTemplate').text()
			        	 		$('#spendingList').html(html)
			        	 */
			        	}) //ajax 끝
	        	},
				initialView: 'dayGridMonth', // 초기 로드 될때 보이는 캘린더 화면(기본 설정: 달)
	/* 			initialDate: '2021-09-15', // 초기 날짜 설정 (설정하지 않으면 오늘 날짜가 보인다.) */
				navLinks: true, // 날짜를 선택하면 Day 캘린더나 Week 캘린더로 링크
				editable: true, // 수정 가능?
				selectable: true, // 달력 일자 드래그 설정가능
				nowIndicator: true, // 현재 시간 마크
				dayMaxEvents: true, // 이벤트가 오버되면 높이 제한 (+ 몇 개식으로 표현)
				locale: 'ko', // 한국어 설정 

				eventAdd: function(obj) { // 이벤트가 추가되면 발생하는 이벤트
					console.log(obj);
				},
				eventChange: function(obj) { // 이벤트가 수정되면 발생하는 이벤트
					console.log(obj);
				},
				
				eventRemove: function(obj){ // 이벤트가 삭제되면 발생하는 이벤트
					console.log(obj);
				},
			/* 	eventClick : function(){
					alert('이벤트클릭')
				}, */
				select: function() { // 캘린더에서 드래그로 이벤트를 생성할 수 있다.
					
						
						/* 	
					var title = prompt('Event Title:');
					if (title) {
						calendar.addEvent({
							title: title,
							start: arg.start,
							end: arg.end,
							allDay: arg.allDay
						}) 
					} 
					*/
			/* 	calendar.unselect() */
				},
				/* eventColor : 'white', */
				events:
					calArr 
				/*
					 [
				
					{	color : 'white',
						textColor : 'blue',
						title: 'All Day Event',
						start: '2021-09-01'
					},
					{
						color : null,
						title: 'Long Event',
						start: '2021-09-07'
					},
				] 
				
				*/
			
			});
		calendar.render();
		})(jQuery);
	
			}
		})
	
	}
	

		
		$('.fc-prev-button').click( function(){ alert('dd')})
		
	
		

})
</script>

<script id="spendingTemplate" type="text/template">
	<tr>
		<th>{category}</th>
		<th>{othersName}</th>
		<th>{transMoney}</th>
	</tr>

</script>
</head>
 <!-- body -->
<body class="main-layout">
      <!-- loader  -->
 
      <!-- end loader --> 
      <!-- header -->
      <header>
 			<jsp:include page="/WEB-INF/jsp/include/header.jsp"></jsp:include>      
      </header>
      <!-- end header -->

      <div class="row main">
      <aside id="side-menu">
			<div id="diyLnb" class="on">
			<h2 class="tit">하나로통장</h2>
			<ul class="depth1">
				
						<li class="on"><a href="#//HanaBank" >통장전환</a></li>
						
						<li><a href="#//HanaBank">My하나로통장</a></li>
						
						<li><a href="#//HanaBank">대시보드</a></li>
	
				</ul>
				</div>
		</aside>
   
      <section>
			<div id="title" class="titlepage">대시보드</div>
			
			
			
			<div class="widget-tabs-list">
			<ul class="nav nav-tabs">
				<li class="active"><a href="${pageContext.request.contextPath}/spending/myCalendar">소비현황</a></li>
				<li class=""><a href="${pageContext.request.contextPath}/spending/spendingAnalysis" >카테고리별 소비</a></li>
				<li class=""><a href="${pageContext.request.contextPath}/spending/challenge">도전하기</a></li>
			</ul>
			
			
				<div class="content">
							<!-- --------------------- 내용 ------------------------ -->
							<div class="border-box">
						<div class="title col">	
							<h2 id="title-h2">내 <strong class="black">예산</strong></h2>	
						</div>
						<div id="div-table">
						<table id="budgetTable">
							<tr>
								<th width="30%">월 생활금</th>
								<th><fmt:formatNumber  value="${myMonthlyBudget.consumption}" type="number"/>원 
								<span style="font-size : 18px;">&nbsp;(권장 지출액 : 일 <span id="dailyExpense"></span>원)</span>
								</th>
							</tr>
							<tr>
								<th>남은 예산</th>
								<th><span style="font-size : 36px; color : #009b9d"><fmt:formatNumber value="${hanaro.consumptionBalance}" type="number"/></span>
								&nbsp;원 </th>
							</tr>
						</table>
						</div>
						 <div id="div-caution">
						<img src="${pageContext.request.contextPath}/resources/icon/caution3.png"/>
						예산을 맞추려면 하루에 <span id="nowDailyExpense" style="font-size : 30px"></span>&nbsp;원씩 써야해요!<br>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;이 추세로 쓴다면 이번 달 지출&nbsp; <span id="expectExpense"  style="font-size : 30px"></span>&nbsp;원 예상!
						</div>
					
						
						<div id="progress">
						<%-- progressBar --%>
						</div>
					
					</div>
						
						
						<div class="border-box">
							<div class="title col">		
								<h2 id="title-h2">소비<strong class="black"> 달력</strong></h2>				
							</div>
								
							<div id="calendar-container" class="row">
								<div id="calendar" class="col-md-8"></div>
								
								
								<div class="col-md-4"style="padding : 0; margin-top : 100px">
							
								<div id="historyDetail" >
									<h2 id="date"></h2>
									
									<table id="historyList">
									<tr>
								
									</tr>
									</table>
								</div>
								</div>
								
							</div>	
							
							
							<!-- <div id="historyDetail">
							<h2 id="date"></h2>
							<table id="historyList">
								<tr>
							
								</tr>
							</table>
							</div> -->

						</div>
						
						<div class="border-box">
							<div class="title col">		
								<h2 id="title-h2">주별/요일별<strong class="black"> 소비</strong></h2>				
							</div>
							
							
						<div class="row">
			                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
			                <div class ="feedback-text" style="font-size : 25px; padding-left : 20px;"><q> 일주일에 22.1만원 정도 써요. <br>이번주는 7.5만원 덜썼어요 </q></div>
			                    <div class="bar-chart-wp mg-t-30 chart-display-nn">
			                        <canvas height="140vh" width="180vw" id="weeklyChart"></canvas>
			                    </div>
			                </div>
			                
			                
			                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
			                <div class ="feedback-text" style="font-size : 25px;padding-left : 20px;"><q> 불 '화'를 즐기는 타입 ..? <br> 화요일 소비를 주의하세요! </q></div>
			                    <div class="bar-chart-wp mg-t-30 chart-display-nn">
			                        <canvas height="140vh" width="180vw" id="dayChart"></canvas>
			                    </div>
			                </div>
               			</div>
						
						</div>
						
						</div>
			
			
			</div>

			 <script src="${ pageContext.request.contextPath }/resources/template/js/charts/Chart.js"></script>
      	<script src="${ pageContext.request.contextPath }/resources/template/js/vendor/jquery-1.12.4.min.js"></script>
   			 <!-- bootstrap JS
		============================================ -->
  			  <script src="${ pageContext.request.contextPath }/resources/template/js/bootstrap.min.js"></script>	
      </section> 
      </div>
      <%-- footer --%>
      <footer>
         <jsp:include page="/WEB-INF/jsp/include/footer.jsp"></jsp:include>
      </footer>
      <%-- end footer --%>
      
</body>
      <!-- Javascript files--> 
  		<jsp:include page="/WEB-INF/jsp/include/javascriptFiles.jsp"></jsp:include>
<script>
(function ($) {
	 "use strict";
		
		/*----------------------------------------*/
		/*  주별 소비 chart
		/*----------------------------------------*/
	 	let week1 = ${weeklySpending.week1}
	 	let week2 = ${weeklySpending.week2}
	 	let week3 = ${weeklySpending.week3}
	 	let week4 = ${weeklySpending.week4}
	 	let week5 = ${weeklySpending.week5}
	 	let week6 = ${weeklySpending.week6}
	 
		var ctx = document.getElementById("weeklyChart");
		var weeklyChart = new Chart(ctx, {
			type: 'horizontalBar',
			data: {
				labels: ["8월 2주","8월 3주", "8월 4주", "9월 1주", "9월 2주", "9월 3주"],
				datasets: [{
					label: 'Bar Chart',
					data: [week1, week2, week3, week4, week5, week6],
					backgroundColor: 
						'rgb(75, 192, 192)'
						
					,
					borderColor: 
						'rgb(75, 192, 192)'
					
					,
					borderWidth: 1
				}]
			},
			options: {
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero:true,
							fontSize: 18,
						}
					}],
					xAxes:[{
						  ticks:{
						  beginAtZero:true,
						  fontColor:'black',
						  fontSize: 18,
						 }
						}]
				}
			}
		});
		
		/*----------------------------------------*/
		/*  요일 별 소비 chart
		/*----------------------------------------*/
		
		
		let mon = ${daySpending.mon}/4
		let tue = ${daySpending.tue}/4
		let wed = ${daySpending.wed}/4
		let thu = ${daySpending.thu}/4
		let fri = ${daySpending.fri}/5
		let sat = ${daySpending.sat}/4
		let sun = ${daySpending.sun}/4
		
		var ctx = document.getElementById("dayChart");
		var dayChart = new Chart(ctx, {
			type: 'bar',
			data: {
				labels: ['월','화','수','목','금','토','일'],
				datasets: [{
					label: 'Bar Chart',
					data: [mon, tue, wed, thu, fri, sat, sun],
					backgroundColor: 
						'rgb(75, 192, 192)'
						
					,
					borderColor: 
						'rgb(75, 192, 192)'
					
					,
					borderWidth: 1
				}]
			},
			options: {
				scales: {
					yAxes: [{
						ticks: {
							beginAtZero:true,
							fontSize: 18,
						}
					}],
					xAxes:[{
						  ticks:{
						  fontColor:'black',
						  fontSize: 18,
						 }
						}]
				}
			}
		});
		
})(jQuery); 
</script>
</html>