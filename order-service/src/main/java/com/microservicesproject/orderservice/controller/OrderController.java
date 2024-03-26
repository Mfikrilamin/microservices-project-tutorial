package com.microservicesproject.orderservice.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesproject.orderservice.dto.OrderRequest;
import com.microservicesproject.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    // @TimeLimiter(name = "inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest) throws Exception {
        LOGGER.info("Placing Order");

        return orderService.placeOrder(orderRequest);
    }

    // In order to have a fallback method, it must has the same return type as the
    // caller/parent function
    // TODO : To implement a more appropriate response status code instead of 201
    // which follow the caller/parent function
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest,
            RuntimeException runtimeException) {
        LOGGER.info("Cannot Place Order Executing Fallback logic");
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time");
        // return "Oops! Something went wrong, please order after some time";
    }

    @ExceptionHandler({ TimeoutException.class })
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public String handleTimeoutException() {
        return "Timeout occurs, please try again next time";
    }
}
