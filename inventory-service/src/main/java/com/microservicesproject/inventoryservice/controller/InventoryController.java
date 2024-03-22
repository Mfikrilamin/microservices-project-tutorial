package com.microservicesproject.inventoryservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservicesproject.inventoryservice.dto.InventoryResponse;
import com.microservicesproject.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        LOGGER.info("Received inventory check request for skuCode: {}", skuCode);
        return inventoryService.isInStock(skuCode);
    }
}
