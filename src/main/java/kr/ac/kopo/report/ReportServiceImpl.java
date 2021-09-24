package kr.ac.kopo.report;



import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	
	public void monthlyReportExecute() {
		
		String setfrom = "dpfls0106@naver.com";
		String tomail = "bubi0106@naver.com";
		String title = "제목입니다";
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
			
			
			mailSender.send(message);
			
			
		} catch(Exception e) {
				e.printStackTrace();
		}
	

		
	}

}
