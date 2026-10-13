package com.exp.aiservice;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;



@Configuration
public class WebClientConfig {
	
	
	@Value("${custom.catalog.endpoint}")
	private String catalogEndpointUrl;
	@Value("${custom.expense.endpoint}")
	private String expenseEndpointUrl;
	
	

	@Bean("catalogClient")
	WebClient catalogClient() {

		return WebClient.builder().baseUrl(catalogEndpointUrl).clientConnector(new ReactorClientHttpConnector(getHttpClient())).defaultHeader("from", "agent")
				.build();

	}

	@Bean("expenseClient")
	WebClient expenseClient() {

		return WebClient.builder().baseUrl(expenseEndpointUrl).clientConnector(new ReactorClientHttpConnector(getHttpClient())).defaultHeader("from", "agent")
				.build();

	}
	
	
	HttpClient  getHttpClient() {
		
	return HttpClient.create()
			.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100000)
			
			;
				
	}

}
