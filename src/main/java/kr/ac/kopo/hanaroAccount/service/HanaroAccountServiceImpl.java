package kr.ac.kopo.hanaroAccount.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.ac.kopo.account.dao.AccountDAO;
import kr.ac.kopo.account.vo.AccountVO;
import kr.ac.kopo.hanaroAccount.dao.HanaroAccountDAO;
import kr.ac.kopo.hanaroAccount.vo.HanaroVO;
import kr.ac.kopo.hanaroAccount.vo.SplitHistoryVO;
import kr.ac.kopo.hanaroAccount.vo.SplitKindInfoVO;

@Service
public class HanaroAccountServiceImpl implements HanaroAccountService {
	
	@Autowired
	private HanaroAccountDAO hanaroAccountDAO;
	
	@Autowired
	private AccountDAO accountDAO;
	
	public HanaroVO selectHanaroAcc(int userCode) {
		
		HanaroVO hanaro = hanaroAccountDAO.selectHanaroAcc(userCode);
		System.out.println(hanaro);
		return hanaro;
	}

	public AccountVO selectHanaroInfo(int userCode) {
		AccountVO account = hanaroAccountDAO.selectHanaroInfo(userCode);
		
		return account;
	}
	
	
	/*
	 * public AccountVO selectHanaroInfoById(String id) {
	 * 
	 * AccountVO account = hanaroAccountDAO.selectHanaroInfoById(id); return
	 * account; }
	 */
	
	@Transactional
	public void changeToHanaro(String accountNo) {
		
		//1. accountType변경
			hanaroAccountDAO.updateType(accountNo);
			AccountVO account = accountDAO.selectByAccNo(accountNo);
			
			System.out.println(account);
			
		//2. hanaro통장 table에 insert
			hanaroAccountDAO.insert(account);
		
	}
	
	/*
	 * //잔액이동
	 * 
	 * @Transactional public void splitMoney(SplitInfoVO splitInfo, SplitHistoryVO
	 * splitHistory) { System.out.println("잔액이동으로 넘어가는 splitVO :" + splitInfo);
	 * //잔액이동 hanaroAccountDAO.splitMoney(splitInfo); //잔액이동내역 생성
	 * hanaroAccountDAO.insertSplitHistory(splitHistory); }
	 */
	//잔액이동
	@Transactional
	public void splitMoney(SplitHistoryVO splitHistory) {
		System.out.println("잔액이동으로 넘어가는 splitVO :" + splitHistory);
		//잔액이동
		/* hanaroAccountDAO.splitMoney(splitInfo); */
		hanaroAccountDAO.splitMoney(splitHistory);
		//잔액이동내역 생성
		hanaroAccountDAO.insertSplitHistory(splitHistory);
	}

	// 거래내역+잔액이동 내역 조회
	public List<SplitHistoryVO> historyByKind(SplitKindInfoVO splitKindInfo) {
		System.out.println("내역조회 service!!" + splitKindInfo);
		List<SplitHistoryVO> historyList = hanaroAccountDAO.historyByKind(splitKindInfo);
//		List<SplitHistoryVO> basicHistory = hanaroAccountDAO.basicHistory(accountNo);
		String splitFrom = "";
		String splitTo = "";
		String splitKind = splitKindInfo.getSplitKind();
		for(int i =0; i <historyList.size(); i ++) {
			 splitFrom = historyList.get(i).getSplitFrom();
			 splitTo = historyList.get(i).getSplitTo();
			System.out.println("splitFrom : " + splitFrom);
			System.out.println("splitTo : " + splitTo);
			
			  if(splitFrom.equals("basic_balance") && !splitKind.equals("basic_balance")) {
				  historyList.get(i).setSplitFrom("기본금"); 			   
			  } else if(splitFrom.equals("consumption_balance") && !splitKind.equals("consumption_balance")) {
				  historyList.get(i).setSplitFrom("생활금"); 
			  } else if(splitFrom.equals("saving_balance") && !splitKind.equals("saving_balance")) {
				  historyList.get(i).setSplitFrom("비상금"); 
			  } else if (splitTo.equals("basic_balance")) {
				  historyList.get(i).setSplitTo("기본금");
			  } else if(splitTo.equals("consumption_balance")) {
				  historyList.get(i).setSplitTo("생활금");
			  }else if(splitTo.equals("saving_balance")) {
				  historyList.get(i).setSplitTo("비상금");
			  }
	
		}
		System.out.println(historyList);
		return historyList;
	}
	
	//급여액, 급여일 설정
	public void setSalary(HanaroVO hanaro) {
		hanaroAccountDAO.setSalary(hanaro);
	}	
	
}
