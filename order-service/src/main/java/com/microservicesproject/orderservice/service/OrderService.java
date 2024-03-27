package com.microservicesproject.orderservice.service;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.microservicesproject.orderservice.dto.InventoryResponse;
import com.microservicesproject.orderservice.dto.OrderLineitemsDto;
import com.microservicesproject.orderservice.dto.OrderRequest;
import com.microservicesproject.orderservice.model.Order;
import com.microservicesproject.orderservice.model.OrderLineItem;
import com.microservicesproject.orderservice.repository.OrderRepository;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public Future<InventoryResponse[]> placeOrderWrapper(List<String> skuCodes, SecurityContext originalContext) {
        ExecutorService executors = Executors.newFixedThreadPool(3);

        Future<InventoryResponse[]> futureResult = executors.submit(() -> {
            try {
                // Restore security context in the new thread
                SecurityContextHolder.setContext(originalContext);

                // Call the method to get inventory response
                return getInventoryResponse(skuCodes);
            } finally {
                // Clear the security context after the task is done (important for thread
                // safety)
                SecurityContextHolder.clearContext();
            }
        });

        executors.shutdown();
        return futureResult;
    }

    public InventoryResponse[] getInventoryResponse(List<String> skuCodes) {
        return webClientBuilder.build().get()
                .uri("http://inventory-service:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
    }

    public String placeOrder(OrderRequest orderRequest) throws InterruptedException, ExecutionException {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(orderLineItemsDto -> mapToDto(orderLineItemsDto))
                .toList();

        order.setOrderListItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderListItemsList().stream().map(OrderLineItem::getSkuCode).toList();

        ExecutorService executor = Executors.newCachedThreadPool();
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .cancelRunningFuture(true)
                .timeoutDuration(Duration.ofMillis(3000))
                .build();

        TimeLimiter timeLimiter = TimeLimiter.of("inventory", config);
        SecurityContext originalContext = SecurityContextHolder.getContext();

        Callable<String> taskDecorateWithTimeout = () -> {
            try {
                SecurityContextHolder.setContext(originalContext);
                InventoryResponse[] inventoryResponses = timeLimiter
                        .decorateFutureSupplier(() -> placeOrderWrapper(skuCodes, originalContext)).call();
                // Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
                if (Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock)) {
                    orderRepository.save(order);
                    return "Order placed sucessfully";
                } else {
                    throw new IllegalArgumentException("Product is not in stock, please try again later");
                }
            } catch (TimeoutException ex) {
                // manually call the fallback method in case of TimeoutException
                // fallFn(ex);
                LOGGER.info(ex.getMessage());
                throw ex;
            }
        };
        return executor.submit(taskDecorateWithTimeout).get();
    }

    private OrderLineItem mapToDto(OrderLineitemsDto orderLineItemsDto) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemsDto.getPrice());
        orderLineItem.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItem.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItem;
    }
}
