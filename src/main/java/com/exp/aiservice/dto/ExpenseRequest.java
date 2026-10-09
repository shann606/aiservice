package com.exp.aiservice.dto;

import java.math.BigDecimal;

import com.exp.aiservice.enums.PaymentType;

public record ExpenseRequest(String category, String subcategory, BigDecimal amount, PaymentType payment, String comments,
		String spenton) {

}
