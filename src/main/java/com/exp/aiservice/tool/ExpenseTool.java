package com.exp.aiservice.tool;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.exp.aiservice.dto.ExpenseRequest;
import com.exp.aiservice.dto.UserContext;
import com.exp.aiservice.service.CreateExpenseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExpenseTool {

	private final CreateExpenseService createExpenseService;
	private final UserContext userContext;

	@Tool(name = "createExpense", description = "create the expense based on the Expense data as input", returnDirect = true)
	public String createExpense(@ToolParam(description = "Expense data") ExpenseRequest request) {
		log.info("getting the data or not :: " + request.toString());
		
		   if(!checkValues(request))
			   return "Required fileds are missing please check example -> 'I had food with my family on 09-04-2024 and paid 600 by UPI' ";

		return createExpenseService.processExpense(request, UUID.fromString(userContext.getUserId())).collectList()
				.map(results -> String.join("\n", results)).block();

	}

	private boolean checkValues(ExpenseRequest request) {

		if (StringUtils.hasText(request.category()) && StringUtils.hasText(request.subcategory())
				&& StringUtils.hasText(request.comments()) && StringUtils.hasText(request.spenton())
				&& request.payment() != null  && (request.amount().compareTo(BigDecimal.ZERO) != 0  && request.amount() != null )) {

			return true;

		} else {
			return false;
		}
	}

}
