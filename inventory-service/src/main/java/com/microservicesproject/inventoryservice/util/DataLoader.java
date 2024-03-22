package com.microservicesproject.inventoryservice.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservicesproject.inventoryservice.model.Inventory;
import com.microservicesproject.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args) throws Exception {
        Inventory inventory = new Inventory();
        inventory.setSkuCode("iphone_13");
        inventory.setQuantity(100);

        Inventory inventory1 = new Inventory();
        inventory1.setSkuCode("iphone_13_red");
        inventory1.setQuantity(2);

        inventoryRepository.save(inventory);
        inventoryRepository.save(inventory1);
    }
}
