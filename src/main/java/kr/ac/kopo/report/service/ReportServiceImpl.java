package kr.ac.kopo.report.service;



import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import kr.ac.kopo.challenge.vo.ChallengeVO;
import kr.ac.kopo.dashBoard.dao.DashBoardDAO;
import kr.ac.kopo.dashBoard.vo.MonthlyBudgetVO;
import kr.ac.kopo.report.dao.ReportDAO;
import kr.ac.kopo.report.vo.ReportVO;
import kr.ac.kopo.spending.dao.SpendingDAO;
import kr.ac.kopo.spending.vo.SpendingInfoVO;

//@Component
@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private ReportDAO reportDAO;
	
	@Autowired
	private DashBoardDAO dashBoardDAO;
	
	@Autowired
	private SpendingDAO spendingDAO;
	
	
	//@Scheduled(fixedDelay=3000)
	public void schedulerTest() {
	
		System.out.println("test성공");
	}
	


	
	//@Scheduled(cron=" 0 9 1 * * ") //한달에 한번 오전 9시
	public void monthlyReportCreate() throws IOException {
		
		
		List<ReportVO> reportMemberList = reportDAO.reportMemberList();
		System.out.println(reportMemberList);
		
		// 정보사항 - 날짜
		Date date = new Date();
		SimpleDateFormat dateTitle = new SimpleDateFormat("YYYY년 MM월");
		SimpleDateFormat dateContent = new SimpleDateFormat("MM월");
		String nowYearMonth = dateTitle.format(date); // 오늘 연도 월
		String nowMonth = dateContent.format(date); // 오늘 월
		SimpleDateFormat yearMonthDate = new SimpleDateFormat("YYYYMM");
	
		String yearMonth = yearMonthDate.format(date);
		
		//고객정보
		String name="";
		String accountNo = "";
		
		
		//9월 달성 title 3
		
		//예산구성
		int basicBudget =0;
		int consumptionBudget =0;
		int savingBudget =0;
		
		//이번달 수입, 지출
		int incomeTotal =0;
		int spendingTotal =0;
		
		int basicSpent;
		int consumptionSpent;
		int savingSpent;
		
		int per1 = 0;
		int per2 =0;
		int per3 =0;
		
		
		
		
		SpendingInfoVO spendingInfo = new SpendingInfoVO();
		spendingInfo.setAccountNo(accountNo);
		spendingInfo.setYearMonth(yearMonth);
		
	
		List<SpendingInfoVO> spendingInfoList = spendingDAO.spendingByCategory(spendingInfo);
		
		if(reportMemberList.size() > 0) {
			int i = 0;
			
			while( i < reportMemberList.size()) {
				FileOutputStream fos = null;
				BufferedOutputStream bos = null;
				
				name = reportMemberList.get(i).getName();
				accountNo = reportMemberList.get(i).getAccountNo();
				System.out.println(name);
				System.out.println(accountNo);
				//예산구성
				MonthlyBudgetVO monthlyBudget = dashBoardDAO.myMonthlySplit(accountNo);
				basicBudget = dashBoardDAO.myMonthlyFixedExpense(accountNo).getFixedExpense();
				consumptionBudget = monthlyBudget.getConsumption();
				savingBudget = monthlyBudget.getSaving();
		
				incomeTotal = reportDAO.incomeTotal(accountNo);
				spendingTotal = reportDAO.spendingTotal(accountNo);
				MonthlyBudgetVO realSpent = reportDAO.realSpent(accountNo);
				basicSpent = realSpent.getFixedExpense();
				consumptionSpent = realSpent.getConsumption();
				savingSpent = realSpent.getSaving();
	
				
				List<ChallengeVO> challengeList = reportDAO.challengeList(accountNo);
				int penaltyTotal = 0;
				List<String> categoryTop = spendingDAO.topSpending(spendingInfo);
				
				
				
				
				
				String tranStr = "";
				
				for(int k =0; k < 3; k++) {
					
					SpendingInfoVO spending = spendingInfoList.get(k);
					
				tranStr  = "					<tr>\r\n"
				+ "					<th>TOP"+k+"</th>\r\n"
				+ "					<td>"+spending.getCategory()+"</td>\r\n"
				+ "					<td>"+spending.getSpendingMoney()+"원</td>\r\n"
				+ "				</tr>\r\n";
				};
				
				tranStr +="				</table>\r\n"
				+ "			</div>\r\n"
				+ "				<div class=\"col-md-6\">\r\n"
				+ "					<div class=\"title \" style=\"padding-bottom : 0px\">"+nowMonth+"월 잦은 지출</div>\r\n"
				+ "					<h4 style=\"font-size : 20px; margin-bottom : 30px\">습관적인 방문으로 인한 소비가 아닌지 확인하세요!</h4>\r\n"
				+ "				<table style=\"height : 160px; margin-bottom: 30px\">\r\n";
		
				
				
				
			
				String challengeStr ="";
				for(int k =0; k < challengeList.size(); k++) {
					
					ChallengeVO challenge = challengeList.get(k);
				challengeStr = "	<div class=\"col-md-6\">\r\n"
						+ "                  <div class=\"service-box account-box challenge\">\r\n"
						+ "                  <div class=\"row\">\r\n"
						+ "                   <h2 id=\"nickname\" class=\"col-md-9\"> \r\n"
						+ "                     "+ challenge.getTitle()+"\r\n"
						+ "                  </h2>\r\n"
						+ "                  <h3 class=\"col-md-2\" id=\"penalty-box\">\r\n"
						+ "                     30%\r\n"
						+ "                  </h3>\r\n"
						+ "                \r\n"
						+ "                  </div>\r\n";
						if(challenge.getNowSpending() > challenge.getSavingGoal()) {
							challengeStr+= " <div class=\"challengeResult\" style=\"background : red\">실패</div>\r\n";
						} else {
							challengeStr+= " <div class=\"challengeResult\" style=\"background : green\">성공</div>\r\n";
						};
					
						challengeStr+=" <div class=\"row\">\r\n"
						+ "                  	<div class=\"col-md-6\">지출액</div>\r\n"
						+ "                  	<div class=\"col-md-6\">목표금액</div>\r\n"
						+ "                  	\r\n"
						+ "                  </div>\r\n"
						+ "                  <div class=\"row\">\r\n"
						+ "                     <h2 id=\"balance\" class=\"col-md-6\">\r\n"
						+ "                    	 "+challenge.getNowSpending()+"\r\n"
						+ "                      </h2>\r\n"
						+ "                     <h2 id=\"balance\" class=\"col-md-6\">\r\n"
						+ "                    	 "+challenge.getSavingGoal()+"\r\n"
						+ "                      </h2>\r\n"
						+ "                  </div>\r\n"
						+ "                  </div>\r\n"
						+ "           </div>";
				}
				
		
			try {
						
				
				
				
				fos = new FileOutputStream("C:\\Users\\HP\\Desktop\\java\\spring-workspace\\Hana-Project\\src\\main\\webapp\\hanaro_report.html");
				bos = new BufferedOutputStream(fos);
				String str ="<!DOCTYPE html>\r\n"
						+ "<html>\r\n"
						+ "<head>\r\n"
						+ "<meta charset=\"UTF-8\">\r\n"
						+ "<title>Insert title here</title>\r\n"
						+ "   <script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js\"></script>\r\n"
						+ "	<script src=\"https://code.jquery.com/jquery-3.6.0.js\" integrity=\"sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=\" crossorigin=\"anonymous\"></script> \r\n"
						+ "	<link rel=\"stylesheet\" href=\"http://localhost:9998/Hana-Project/resources/template/css/bootstrap.min.css\">\r\n"
						+ "    <!-- style CSS\r\n"
						+ "		============================================ -->\r\n"
						+ "    <link rel=\"stylesheet\" href=\"http://localhost:9998/Hana-Project/resources/template/css/style.css\">\r\n"
						+ "    <link rel=\"stylesheet\" href=\"http://localhost:9998/Hana-Project/resources/css/style.css\">\r\n"
						+ "    <link rel=\"stylesheet\" href=\"http://localhost:9998/Hana-Project/resources/css/email.css\">\r\n"
						+ "\r\n"
						+ "	<script type=\"text/javascript\"> 	\r\n"
						+ "	var oldTab = [ 1, 1 ];\r\n"
						+ "		function tabShowHideFunc(t, n, b) { // t = this, n = x번째 탭, b = 탭아닌 버튼으로 탭 컨트롤시..\r\n"
						+ "			var gStr = t.href.split('.html#')[1]; 		\r\n"
						+ "		if (!gStr) {\r\n"
						+ "				gStr = t.href.split('.htm#')[1]; 			\r\n"
						+ "				if (!gStr) {\r\n"
						+ "					gStr = t.href.split('#')[1]; 			} 		}\r\n"
						+ "			gStr1 = gStr.substr(0, gStr.length - 1);\r\n"
						+ "			var gNum = (t.href).substr((t.href).length - 1, (t.href).length); \r\n"
						+ "			if (arguments[2] != undefined) {\r\n"
						+ "				if (arguments[2] == 'btn') {\r\n"
						+ "					document.getElementById(\"email\").getElementsByTagName(\"li\")[gNum - 1]\r\n"
						+ "							.getElementsByTagName(\"a\")[0].click();\r\n"
						+ "					document.documentElement.scrollTop = 0;\r\n"
						+ "					document.body.scrollTop = 0;\r\n"
						+ "				} else if (arguments[2] == 'slide') {\r\n"
						+ "					document.getElementById(\"email\").getElementsByTagName(\"li\")[3]\r\n"
						+ "							.getElementsByTagName(\"a\")[0].click();\r\n"
						+ "					setTimeout(function() {\r\n"
						+ "						var targetPos = document.getElementById(gStr).offsetTop;\r\n"
						+ "						window.scroll(0, targetPos); 				}, 200);\r\n"
						+ "				} else if (arguments[2] == 'voice') {\r\n"
						+ "					document.getElementById(\"email\").getElementsByTagName(\"li\")[3]\r\n"
						+ "							.getElementsByTagName(\"a\")[0].click();\r\n"
						+ "					setTimeout(function() {\r\n"
						+ "						var targetPos = document.getElementById(gStr).offsetTop;\r\n"
						+ "						window.scroll(0, targetPos); 				}, 200);\r\n"
						+ "				} else if (arguments[2] == 'revolg') {\r\n"
						+ "					document.getElementById(\"email\").getElementsByTagName(\"li\")[3]\r\n"
						+ "							.getElementsByTagName(\"a\")[0].click();\r\n"
						+ "					setTimeout(function() {\r\n"
						+ "						var targetPos = document.getElementById(gStr).offsetTop;\r\n"
						+ "						window.scroll(0, targetPos); 				}, 200);\r\n"
						+ "				} else if (arguments[2] == 'revol') {\r\n"
						+ "					document.getElementById(\"email\").getElementsByTagName(\"li\")[1]\r\n"
						+ "							.getElementsByTagName(\"a\")[0].click();\r\n"
						+ "					setTimeout(function() {\r\n"
						+ "						var targetPos = document.getElementById(gStr).offsetTop;\r\n"
						+ "						window.scroll(0, targetPos); 				}, 200);\r\n"
						+ "				} else if (arguments[2] == 'minus') {\r\n"
						+ "					document.getElementById(\"email\").getElementsByTagName(\"li\")[3]\r\n"
						+ "							.getElementsByTagName(\"a\")[0].click();\r\n"
						+ "					setTimeout(function() {\r\n"
						+ "						var targetPos = document.getElementById(gStr).offsetTop;\r\n"
						+ "						window.scroll(0, targetPos); 				}, 200);\r\n"
						+ "				} 		} else {\r\n"
						+ "				t.parentNode.className = \"on col-md-4\";\r\n"
						+ "				document.getElementById(gStr1 + gNum).style.display = \"block\";\r\n"
						+ "				if (oldTab[n - 1] != gNum) {\r\n"
						+ "					t.parentNode.parentNode.getElementsByTagName(\"li\")[oldTab[n - 1] - 1].className = \"col-md-4\";\r\n"
						+ "					document.getElementById(gStr1 + oldTab[n - 1]).style.display = \"none\";\r\n"
						+ "				} 			oldTab[n - 1] = gNum; 		\r\n"
						+ "				} 	\r\n"
						+ "			}\r\n"
						+ "\r\n"
						+ "		</script> \r\n"
						+ "		<script> 	\r\n"
						+ "		$(document).ready(function() {\r\n"
						+ "			$(\".ac-label\").click(function(e) { 			\r\n"
						+ "				e.preventDefault();\r\n"
						+ "				$check = $(this).prev(); 			\r\n"
						+ "				if ($check.prop('checked'))\r\n"
						+ "					$check.prop(\"checked\", false); 			\r\n"
						+ "				else\r\n"
						+ "					$check.prop(\"checked\", true); 		\r\n"
						+ "				}); \r\n"
						+ "\r\n"
						+ "			});\r\n"
						+ "	</script> \r\n"
						+ "\r\n"
						+ "</head>\r\n"
						+ "<body>\r\n"
						+ "<div class=\"div-body container\">\r\n"
						+ "\r\n"
						+ "<div class=\"header-div\"> 			\r\n"
						+ "	<img style=\"height : 120px;\" src=\"hanalogo.png\">\r\n"
						+ "</div>\r\n"
						+ "<div class=\"content\" id=\"content\" >\r\n"
						+ "	\r\n"
						+ "	<h1 id=\"h1\">"+yearMonth+"<span style=\"font-size : 18pt\"> &nbsp;&nbsp;&nbsp;하나로통장 월간 금융 리포트 </span></h1> \r\n"
						+ "	\r\n"
						+ "	<!-- 탭 선택 -->\r\n"
						+ "	<ul class=\"mailGuideTab tab3 emailTab row\" id=\"email\">\r\n"
						+ "	\r\n"
						+ "		<li class=\"on col-md-4\"><a href=\"#email01\" onclick=\"tabShowHideFunc(this,1); return false;\">요약</a></li>\r\n"
						+ "		<li class=\"col-md-4\"><a href=\"#email02\" onclick=\"tabShowHideFunc(this,1); return false;\">09월 소비</a></li>\r\n"
						+ "		<li class=\"col-md-4\"><a href=\"#email03\" onclick=\"tabShowHideFunc(this,1); return false;\">도전 현황</a></li>\r\n"
						+ "	</ul>\r\n"
						+ "	\r\n"
						+ "	<!----------------------------------------- 첫번째 탭 ---------------------------------------------->\r\n"
						+ "	<div class=\"select-div\" id=\"email01\" style=\"display: block;\">\r\n"
						+ "	\r\n"
						+ "		<span class=\"span-big\">"+name+"</span> \r\n"
						+ "		하나은행을 이용해주셔서 감사합니다.<br>\r\n"
						+ nowMonth +"월 하나로 통장 금융 리포트입니다.\r\n"
						+ "		\r\n"
						+ "		<div id=\"accountInfo\">\r\n"
						+ "			상품명 : 하나로 통장 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;계좌번호 : " +accountNo+"</div>\r\n"
						+ "	     <div class=\"title\"> 예산 구성</div>\r\n"
						+ "	     	\r\n"
						+ "	     	<div class=\"row\">\r\n"
						+ "	     	<div class=\"col-md-6 piechart\">\r\n"
						+ "			   <canvas height=\"140vh\" width=\"180vw\" id=\"pieChart\"></canvas>\r\n"
						+ "			</div>\r\n"
						+ "			<div class=\"col-md-6\">\r\n"
						+ "					<div style=\"margin-left : 30px;\">\r\n"
						+ "					2021년 9월 수입 :" +incomeTotal+"원<br>\r\n"
						+ "					2021년 9월 지출 :"+ spendingTotal+"원\r\n"
						+ "					</div>\r\n"
						+ "				<table style=\"width : 100%; height : 230px; margin-top : 15px; border :1px solid #dddddd\">\r\n"
						+ "					<tr style=\"background : #dddddd\">\r\n"
						+ "					<td>분류</td>\r\n"
						+ "					<td>실제 지출/저축 금액</td>\r\n"
						+ "					<td>전달 대비<td>\r\n"
						+ "					</tr>\r\n"
						+ "					<tr>\r\n"
						+ "					<td>고정지출</td>\r\n"
						+ "					<td>"+basicSpent+"원</td>\r\n"
						+ "					<td>" + per1 +"</td>\r\n"
						+ "					</tr>\r\n"
						+ "					<tr>\r\n"
						+ "					<td>생활비</td>\r\n"
						+ "					<td>"+consumptionSpent+"원</td>\r\n"
						+ "					<td> "+per2+"</td>\r\n"
						+ "					</tr>\r\n"
						+ "					<tr>\r\n"
						+ "					<td>비상금</td>\r\n"
						+ "					<td>"+savingSpent+"원</td>\r\n"
						+ "					<td>"+ per3+"</td>\r\n"
						+ "					</tr>\r\n"
						+ "				</table>\r\n"
						+ "				\r\n"
						+ "			</div>\r\n"
						+ "	     	</div>\r\n"
						+ "	     	 <div class=\"title\"> 예산 비교</div>\r\n"
						+ "	 		<div id=\"menu-title\"> 내 또래 평균 예산</div>\r\n"
						+ "\r\n"
						+ "				    <!-- Bar Chart area End-->\r\n"
						+ "				    <div class=\"bar-chart-area\">\r\n"
						+ "				        <div class=\"container\">\r\n"
						+ "				       		<div class=\"row\">\r\n"
						+ "				             	\r\n"
						+ "				                <div class=\"col-md-6\">\r\n"
						+ "				                    <div class=\"bar-chart-wp sm-res-mg-t-30 chart-display-nn\">\r\n"
						+ "				                        <canvas height=\"110vh\" width=\"180vw\" id=\"barchart1\"></canvas>\r\n"
						+ "				                    </div>\r\n"
						+ "				                </div>\r\n"
						+ "				                 \r\n"
						+ "				                  <div class =\"feedback-text col-md-4\" style=\"font-size : 25px; padding-top : 80px;\"> \r\n"
			
						
						+ "				                  </div>\r\n"
						+ "				       \r\n"
						+ "				            </div>\r\n"
						+ "			\r\n"
						+ "				        </div>\r\n"
						+ "				    </div>\r\n"
						+ "				    <!-- Bar Chart area End-->\r\n"
						+ "												 \r\n"
						+ "					<div id=\"menu-title\"> 비슷한 급여 사용자 평균 예산 </div>\r\n"
						+ "				 								  <div class=\"bar-chart-area\">\r\n"
						+ "				        <div class=\"container\">\r\n"
						+ "				           <div class=\"row\">\r\n"
						+ "				         \r\n"
						+ "				                <div class=\"col-md-6\">\r\n"
						+ "				                    <div class=\"bar-chart-wp sm-res-mg-t-30 chart-display-nn\">\r\n"
						+ "				                        <canvas height=\"110vh\" width=\"180vw\" id=\"barchart2\"></canvas>\r\n"
						+ "				                    </div>\r\n"
						+ "				                </div>\r\n"
						+ "				                <div class =\"feedback-text col-md-4\" style=\"font-size : 25px; padding-top : 80px;\"> \r\n"
				
						+ "				                  </div>\r\n"
						+ "				            </div>\r\n"
						+ "				 	\r\n"
						+ "				        </div>\r\n"
						+ "				    </div>\r\n"
						+ "\r\n"
						+ "\r\n"
						+ "	</div>\r\n"
						+ "	\r\n"
					
						+ "	<div class=\"select-div\" id=\"email02\" style=\"display: none;\">\r\n"
						+ "		\r\n"

						+ "	        </div>\r\n"
						+ "	      </div>\r\n"
						+ "		\r\n"
						+ "		\r\n"
						+ "		\r\n"
						+ "		<div class=\"row\">\r\n"
						+ "		<div class=\"col-md-6\">\r\n"
				
					+"<div class=\"title \" style=\"padding-bottom : 0px\">"+nowMonth+"월 큰 지출</div>\r\n"
						+ "				<h4 style=\"font-size : 20px; margin-bottom : 30px \">충동적인 소비가 아니었는지 되돌아보세요!</h4>\r\n"
						+ "				<table style=\"text-align : left; border-right : 2px solid #dddddd; height : 160px\">\r\n"			;	
				
					str +=tranStr;
					str += "	\r\n"
						+ "</div>\r\n"
						+ "	<!--------------------------------------------------------------------------------------->\r\n"
						+ "	<div class=\"select-div\" id=\"email03\" style=\"display: none;\">  	\r\n"
						+ "		<div class=\"title\"> 09월 도전 결과 </div>\r\n"
						+ "			\r\n"
						+ "           <h2 class=\"challengeTotal\">"+nowMonth+"월 도전하기 패널티로 <br>\r\n"
						+ "           <br>비상금에 총"+penaltyTotal+"원이 저축되었습니다.</h2>\r\n"
						+ "		<div class=\"service\">  \r\n"
						+ "	      <div class=\"row\">\r\n";
						
					str += challengeStr;

					str+= "	           </div>         \r\n"
						+ "           </div>\r\n"
						+ "\r\n"
						+ "           \r\n"
						+ "	</div>\r\n"
						+ "</div>\r\n"
						+ "</div>\r\n"
						+ "</body>\r\n"
						+ "\r\n"
						+ "	<script src=\"http://localhost:9998/Hana-Project/resources/template/js/plugins.js\"></script>\r\n"
						+ " <script src=\"http://localhost:9998/Hana-Project/resources/template/js/charts/Chart.js\"></script>\r\n"
						+ "	<script src=\"http://localhost:9998/Hana-Project/resources/template/js/vendor/jquery-1.12.4.min.js\"></script>\r\n"
						+ "	<script src=\"http://localhost:9998/Hana-Project/resources/template/js/bootstrap.min.js\"></script>	\r\n"
						+ "	<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js\"></script>\r\n"
						+ "	<script src=\"https://code.jquery.com/jquery-3.3.1.slim.min.js\"></script>\r\n"
						+ "	<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js\"></script>\r\n"
						+ "	<script src=\"https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js\"></script>\r\n"
						+ "</html>";
				
				bos.write(str.getBytes()); // Byte형으로만 넣을 수 있음
				System.out.println("완료");
				
				bos.flush();
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			ReportVO reportMember = reportMemberList.get(i);
			monthlyReportExecute(reportMember);
			i++;
		}
			 
		}
		
	}
	

	public void monthlyReportExecute(ReportVO reportMember) {
		String name = reportMember.getName();
		Date date = new Date();
		SimpleDateFormat dateContent = new SimpleDateFormat("MM월");
		String setfrom = "dpfls0106@naver.com";
		String tomail = "bubi0106@naver.com";
		String title = name + "님"+ dateContent +" 하나로통장 금융리포트입니다";
		String content = "내용입니다";
		
		String filename = "C:\\Users\\HP\\Desktop\\java\\spring-workspace\\Hana-Project\\src\\main\\webapp\\hanaro_report.html";
		
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			
			messageHelper.setFrom(setfrom); // 보내는사람 생략하면 정상작동을 안함
			messageHelper.setTo(tomail); // 받는사람 이메일
			messageHelper.setSubject(title); // 메일제목은 생략이 가능하다
			messageHelper.setText(content); // 메일 내용
			
			
			// 파일첨부
			FileSystemResource fsr = new FileSystemResource(filename);
			messageHelper.addAttachment("hanaro_report.html", fsr);
			
			messageHelper.setText("<!DOCTYPE HTML>\r\n" + "<html lang=\"ko\">\r\n" + "<head>\r\n"
					+ "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
					+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
					+ "<title>메일</title>\r\n" + "</head>\r\n" + "<body class=\"ko_KR \">\r\n"
					+ "	<div class=\"viewWrap\" id=\"previewContent\">\r\n"
					+ "		<div class=\"coverWrap\">\r\n" + "			<div id=\"readFrame\">\r\n"
					+ "				<div\r\n"
					+ "					style=\"max-width: 710px; min-width: 320px; margin: 0 auto; background-color: #fff; text-align: left;\">\r\n"
					+ "					<!-- header-->\r\n"
					+ "					<div style=\"height: 47px; padding: 0 0 20px 0; margin: 0;\">\r\n"
					+ "						<h1\r\n"
					+ "							style=\"float: left; margin: 0; padding: 25px 0 0 0; font-size: 0; line-height: 0;\">\r\n"
					+ "							<a href=\"http://localhost:9999/\" target=\"_blank\"\r\n"
					+ "								title=\"새창 열림\" rel=\"noreferrer noopener\"><img\r\n"
					+ "								src=\"http://localhost:9999/resources/images/email/email-logo2.jpg\"\r\n"
					+ "								alt=\"hanabank\" style=\"border: 0px;\" loading=\"lazy\"></a>\r\n"
					+ "						</h1>\r\n" + "					</div>\r\n"
					+ "					<!-- //header-->\r\n" + "					<!-- content -->\r\n"
					+ "					<!-- 내용 -->\r\n" + "					<!-- content -->\r\n"
					+ "					<div\r\n"
					+ "						style=\"line-height: 1.5; color: #555555; font-size: 14px; font-family: AppleSDGothicNeo-light, 'malgun gothic', 'dotum', '돋움', sans-serif;\">\r\n"
					+ "						<!-- 비주얼영역 -->\r\n" + "						<div>\r\n"
					+ "							"
					+ "						</div>\r\n"
					+ "						<div style=\"padding: 6% 6% 7%;\">\r\n"
					+ "							<div\r\n"
					+ "								style=\"color: #0050a9; font-size: 16px; font-weight: bold; padding-bottom: 3%; border-bottom: 1px solid #dbdbdb; margin-bottom: 3%;\">2021년\r\n"
					+ "								08월 금융리포트</div>\r\n"
					+ "							<div style=\"font-size: 16px; color: #000; font-weight: bold;\">이메일\r\n"
					+ "								금융리포트 확인방법</div>\r\n"
					+ "							<div\r\n"
					+ "								style=\"padding: 4% 5%; background: #f6f6f6; margin-top: 1.5em;  margin-bottom: 30px;\">\r\n"
					+ "								<div\r\n"
					+ "									style=\"font-size: 14px; font-weight: bold; margin-bottom: 2%;\">첨부파일을\r\n"
					+ "									클릭하세요.</div>\r\n"
					+ "								<div style=\"font-size: 14px;\">이메일 금융리포트 첨부파일 확인방법 : 첨부파일을\r\n"
					+ "									클릭하신 후 금융리포트를 확인하시면 됩니다.</div>\r\n"
					+ "							</div>\r\n" + "							\r\n"
					+ "							<div style=\"font-weight: bold; color: #000; font-size: 16px;\">금융리포트를 더욱 스마트하게 사용하기</div>\r\n"
					+ "							<div\r\n"
					+ "								style=\"border: 1px solid #e1e1e1; padding: 5% 2%; margin-top: 1em; text-align: center; line-height: 1.4;\">\r\n"
											
					+ "						</div>\r\n" + "					</div>\r\n"
					+ "					<!-- //content -->\r\n" + "					<!-- //내용 -->\r\n"
					+ "							<!--//footer-->\r\n"
					+ "				</div>\r\n" + "				<br>\r\n" + "			</div>\r\n"
					+ "		</div>\r\n" + "	</div>\r\n" + "</body>\r\n" + "</html>\r\n" + "", true); // 메일 내용

			
			
			mailSender.send(message);
			
			
		} catch(Exception e) {
				e.printStackTrace();
		}
	

		
	
	}

	


	
}
