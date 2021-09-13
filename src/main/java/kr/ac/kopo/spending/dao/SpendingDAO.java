package kr.ac.kopo.spending.dao;

import java.util.List;

import kr.ac.kopo.spending.vo.SpendingInfoVO;

public interface SpendingDAO {
	
//	List<TransactionHistoryVO> spendingByCategory(String accountNo);
	List<SpendingInfoVO> spendingByCategory(SpendingInfoVO spendingInfo);
	
	List<String> topSpending(SpendingInfoVO spendingInfo);
}