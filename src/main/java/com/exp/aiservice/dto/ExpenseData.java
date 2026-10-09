package com.exp.aiservice.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.exp.aiservice.enums.PaymentType;

public record ExpenseData(UUID userid, UUID categoryid, UUID subcatgegoryid, BigDecimal amount, PaymentType payment, String comments,
		String spenton) {

}
