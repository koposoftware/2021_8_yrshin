package kr.ac.kopo;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.json.simple.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import kr.ac.kopo.dashBoard.dao.DashBoardDAO;
import kr.ac.kopo.fixedExpense.service.FixedExpenseService;
import kr.ac.kopo.report.dao.ReportDAO;
import kr.ac.kopo.report.vo.ReportVO;
import kr.ac.kopo.report.vo.SmsVO;
import kr.ac.kopo.spending.dao.SpendingDAO;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@EnableScheduling 
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/spring-mvc.xml"})
public class test {
	
	@Autowired
	private FixedExpenseService fixedExpenseService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private ReportDAO reportDAO;
	
	@Autowired
	private DashBoardDAO dashBoardDAO;
	
	@Autowired
	private SpendingDAO spendingDAO;
	
	@Ignore	
	@Test  
	public void Service생성테스트() throws Exception {
		//System.out.println("dataSource : " + dataSource);
		assertNotNull(fixedExpenseService);    	//dataSource가 가지고 있는 값이 null이 아닌지만 확인
	}
	//@Ignore
	@Test
	public void smsCreate2() {

		
		
	
				
				
				String text = "[하나은행]\n";
				text += "강하나님 [쇼핑 줄이기] 초과소비 패널티 발생!\n";
				text += "이동 : 생활금 → 비상금\n";
				//text += "[" + penaltyMoney +"원]";
				text += "[25020원]";
				
				System.out.println("text : \n"+text);
				
				
				
				String api_key = "NCS9XWBKWK5YKNTD"; 
				String api_secret = "IO9LVLNUVWWJ5ZRDFEEMLR9TRZHQWLEF"; 
				Message coolsms = new Message(api_key,api_secret); 
				HashMap<String, String> params = new HashMap<String, String>();
				
				params.put("from", "01047520453");
				params.put("to", "01047520453"); 
				params.put("type","SMS"); 
				params.put("text", text); 
				params.put("app_version", "test app 1.2");
				
				try { JSONObject obj = (JSONObject) coolsms.send(params);
				System.out.println(obj.toString());
				
				} catch (CoolsmsException e) { 
					System.out.println(e.getMessage());
					System.out.println(e.getCode()); 
				}
				
				
				
				
				
		
		
	}
	@Ignore
	@Test
	public void smsCreate() {
		List<SmsVO> smsList = reportDAO.smsList();
		System.out.println(smsList);

		
		
		if(smsList != null) {
		
			int i=0;
				
		while(i < smsList.size()) {
			String name = smsList.get(i).getName();
			String tel = smsList.get(i).getTel();
			//String accountNo = smsList.get(i).getAccountNo();
			String category = smsList.get(i).getCategory();
			int penaltyMoney = smsList.get(i).getPenaltyMoney();
			//int logCode = smsList.get(i).getLogCode();
			
			
			
			String text = "[하나은행]\n";
			text += name +" 님 [" +category+ "줄이기] 초과소비 패널티 발생!\n";
			text += "이동 : 생활금 → 비상금\n";
			text += "[" + penaltyMoney +"원]";
			
			
			System.out.println("text : \n"+text);
			
			
			
			 String api_key = ""; 
			 String api_secret = ""; 
			 Message coolsms = new Message(api_key,api_secret); 
			 HashMap<String, String> params = new HashMap<String, String>();
			 
			 params.put("from", "01047520453");
			 params.put("to", tel); 
			 params.put("type","SMS"); 
			 params.put("text", text); 
			 params.put("app_version", "test app 1.2");
			 
			 try { JSONObject obj = (JSONObject) coolsms.send(params);
			 System.out.println(obj.toString());
			 
			 } catch (CoolsmsException e) { 
				 System.out.println(e.getMessage());
				 System.out.println(e.getCode()); 
			 }
			 
			
			
				
			
		  i++;
		}
		
		reportDAO.changeFlag(smsList);
	}
		
		
	}
	@Ignore
	@Test
	public void sendSms() {
		
		

        String api_key = "NCS9XWBKWK5YKNTD";
        String api_secret = "IO9LVLNUVWWJ5ZRDFEEMLR9TRZHQWLEF";
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", "01047520453");
        params.put("from", "01047520453");
        params.put("type", "SMS");
        params.put("text", "sms 테스트!");
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
	}
	
	@Ignore
	@Test
	public void monthlyReportCreate() throws IOException {
		
		
		List<ReportVO> reportMemberList = reportDAO.reportMemberList();
		System.out.println(reportMemberList);
		

		
		if(reportMemberList.size() > 0) {
			int i = 0;
			
			while( i < reportMemberList.size()) {
				FileOutputStream fos = null;
				BufferedOutputStream bos = null;
				
			try {
				String str = "hi";
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
	
	//@Ignore
	@Test
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
