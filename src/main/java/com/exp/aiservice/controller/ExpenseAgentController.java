package com.exp.aiservice.controller;

import java.math.BigDecimal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exp.aiservice.dto.ExpenseRequest;
import com.exp.aiservice.enums.PaymentType;
import com.exp.aiservice.tool.ExpenseTool;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/v1/aiservice")
@RestController
@Slf4j
public class ExpenseAgentController {

	private ChatClient chatClient;
	private ExpenseTool expenseTool;

	public ExpenseAgentController(@Qualifier("expenseAgentClient") ChatClient chatClient, ExpenseTool expenseTool) {
		this.chatClient = chatClient;
		this.expenseTool = expenseTool;

	}

	@GetMapping("/addexpense")
	public  String addExpense(String message) {

		log.info("it is coming here only");

		return chatClient.prompt().tools(expenseTool).user(message)
				.call()
				.content();
	}
	
	
	/**
	 *  Test data
	 */
	
	@GetMapping("/check")
	public String checkTest(String test) {
		
		ExpenseRequest  data = new ExpenseRequest("Transportation", "Taxi / Ride-sharing", new BigDecimal(100), PaymentType.UPI, "travel", "2007-12-03");
		
		return expenseTool.createExpense(data);
	}

}
