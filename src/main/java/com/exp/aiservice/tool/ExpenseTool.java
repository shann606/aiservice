package com.exp.aiservice.tool;

import java.util.UUID;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.exp.aiservice.dto.ExpenseRequest;
import com.exp.aiservice.service.CreateExpenseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ExpenseTool {

	private final CreateExpenseService createExpenseService;

	@Tool(name = "createExpense", description = "create the expense based on the Expense data as input", returnDirect = true)
	public String createExpense(@ToolParam(description = "Expense data") ExpenseRequest request) {
		log.info("getting the data or not :: " + request.toString());
		
	return	createExpenseService.processExpense(request, UUID.fromString("e6089aaa-faa0-4f32-9284-229ccd44da53")).collectList().map(results -> String.join("\n", results))
            .block();
		
		 
		
	}

}
