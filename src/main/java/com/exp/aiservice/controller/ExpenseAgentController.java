package com.exp.aiservice.controller;

import java.math.BigDecimal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exp.aiservice.dto.ExpenseRequest;
import com.exp.aiservice.dto.UserContext;
import com.exp.aiservice.enums.PaymentType;
import com.exp.aiservice.tool.ExpenseTool;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/api/v1/aiservice")
@RestController
@Slf4j
public class ExpenseAgentController {

	private ChatClient chatClient;
	private ExpenseTool expenseTool;
	private UserContext userContext;

	public ExpenseAgentController(@Qualifier("expenseAgentClient") ChatClient chatClient, ExpenseTool expenseTool , UserContext userContext) {
		this.chatClient = chatClient;
		this.expenseTool = expenseTool;
		this.userContext=userContext;

	}

	@GetMapping("/addexpense")
	public  String addExpense(@RequestParam String message ,@RequestHeader(name = "X-UserId") String userId) {

		 log.info("it is coming here only");
		 userContext.setUserId(userId);

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
