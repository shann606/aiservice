package com.exp.aiservice.dto;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import lombok.Data;

@Component
@RequestScope
@Data
public class UserContext {
	
	String userId;
	

}
