package com.exp.aiservice.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/aiservice")
@RestController
public class ChatBotController {

	private ChatClient chatClient;

	public ChatBotController(@Qualifier("chatBotClient") ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@GetMapping("/general")
	public String chatBotResponse(String message) {
             
           

		return chatClient.prompt().user(message).call().content();

	}

}
