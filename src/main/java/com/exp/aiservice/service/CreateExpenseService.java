package com.exp.aiservice.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.exp.aiservice.dto.Category;
import com.exp.aiservice.dto.ExpenseData;
import com.exp.aiservice.dto.ExpenseRequest;
import com.exp.aiservice.dto.SubCategory;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CreateExpenseService {

	private WebClient catalogClient;
	private WebClient expenseClient;

	public CreateExpenseService(@Qualifier("catalogClient") WebClient catalogClient,
			@Qualifier("expenseClient") WebClient expenseClient) {

		this.catalogClient = catalogClient;
		this.expenseClient = expenseClient;

	}

	public  Flux<String> processExpense(ExpenseRequest request, UUID userId) {

		log.info("it is hitting the service method");

		return catalogClient.get().uri("/api/v1/categories").retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, response -> {
					log.info("Client error: occured while calling categories {}", response.statusCode());

					return response.bodyToMono(String.class).defaultIfEmpty("No error details available")
							.map(errorBody -> {
								log.error("Error response body: {}", errorBody);
								return new RuntimeException(errorBody);
							});

				}).onStatus(HttpStatusCode::is5xxServerError, response -> {
					log.info("Client error: occured while calling categories {}", response.statusCode());

					return response.bodyToMono(String.class).defaultIfEmpty("No error details available")
							.map(errorBody -> {
								log.error("Error response body: {}", errorBody);
								return new RuntimeException(errorBody);
							});

				}).bodyToFlux(Category.class)
				
				.doOnNext(category -> log.info("Category received: name={}, id={}", category.toString()))
				// Find category
				.filter(category -> category.name().equalsIgnoreCase(request.category()))
				.doOnNext(category -> log.info("Category found: name={}, id={}", category.name(), category.id())).next()

				// Get category ID
				.map(Category::id) 

				// Use category ID to get subcategory
				.flatMap(categoryId -> catalogClient.get().uri("/api/v1/categories/" + categoryId + "/sub-categories")
						.retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> {
							log.info("Client error: occured while calling sub-categories {}", response.statusCode());

							return response.bodyToMono(String.class).defaultIfEmpty("No error details available")
									.map(errorBody -> {
										log.error("Error response body: {}", errorBody);
										return new RuntimeException(errorBody);
									});

						}).onStatus(HttpStatusCode::is5xxServerError, response -> {
							log.info("Client error: occured while calling sub-categories {}", response.statusCode());

							return response.bodyToMono(String.class).defaultIfEmpty("No error details available")
									.map(errorBody -> {
										log.error("Error response body: {}", errorBody);
										return new RuntimeException(errorBody);
									});

						}).bodyToFlux(SubCategory.class)

						// Find subcategory
						.filter(subCategory -> subCategory.name().equalsIgnoreCase(request.subcategory())).next()

						// Get subcategory ID
						.map(SubCategory::id).defaultIfEmpty(UUID.randomUUID())

						// Build ExpenseData
						.map(subCategoryId -> new ExpenseData(userId, categoryId, subCategoryId, request.amount(),
								request.payment(), request.comments(), request.spenton())))
				.doOnNext(expense -> log.info("do we build the expense data {} ::" + expense.toString()))

				// Call expense service
				.flatMap(expense -> expenseClient.post().uri("/api/v1/expenses").header("X-Username", "AI-Agent")
						.bodyValue(expense).retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> {
							log.info("Client error: occured while calling expense creation service {}",
									response.statusCode());

							return response.bodyToMono(String.class).defaultIfEmpty("No error details available")
									.map(errorBody -> {
										log.error("Error response body: {}", errorBody);
										return new RuntimeException(errorBody);
									});

						}).onStatus(HttpStatusCode::is5xxServerError, response -> {
							log.info("Client error: occured while calling expense creation service {}",
									response.statusCode());

							return response.bodyToMono(String.class).defaultIfEmpty("No error details available")
									.map(errorBody -> {
										log.error("Error response body: {}", errorBody);
										return new RuntimeException(errorBody);
									});

						}).bodyToMono(Void.class))
				.thenReturn("Expense created successfully please check DashBoard -> Search Expense")
				.doOnNext(result -> log.info("Expense processing completed successfully: {}", result))
				.onErrorResume(error -> {
					log.error("Expense processing failed: {}", error.getMessage(), error);
					return Mono.just("Failed , please try again after some time..");
				})
				.flux();
	}

}
