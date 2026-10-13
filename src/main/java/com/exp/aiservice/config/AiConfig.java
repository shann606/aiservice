package com.exp.aiservice.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class AiConfig {

	@Value("classpath:prompttemplate/aboutcompanydetails.pt")
	Resource chatBotPrompt;
	@Value("classpath:prompttemplate/addexpense.pt")
	Resource agentPrompt;

	@Bean(name = "chatBotClient")
	ChatClient chatBotClient(ChatClient.Builder chatClient) {
		return chatClient.defaultSystem(chatBotPrompt)
				.defaultAdvisors(new SimpleLoggerAdvisor()).build();

	}
	
	

	@Bean(name = "expenseAgentClient")
	ChatClient expenseAgentClient(ChatClient.Builder chatClient) {
		return chatClient.defaultSystem(agentPrompt)
				.defaultAdvisors(new SimpleLoggerAdvisor())
				.build();

	}
	

}
