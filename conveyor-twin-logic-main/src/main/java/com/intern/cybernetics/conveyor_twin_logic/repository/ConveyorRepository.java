package com.intern.cybernetics.conveyor_twin_logic.repository;

import com.intern.cybernetics.conveyor_twin_logic.model.ConveyorItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ConveyorRepository {
    private static final String DATA_DIR = "conveyor_data";
    private static final String DATA_FILE = "conveyor_data/items.json";
    private static final java.util.logging.Logger LOGGER =
            java.util.logging.Logger.getLogger(ConveyorRepository.class.getName());
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final AtomicLong idCounter = new AtomicLong(0);

    public ConveyorRepository() {
        initializeDataDirectory();
        loadIdCounter();
    }

    private void initializeDataDirectory() {
        try {
            Path path = Paths.get(DATA_DIR);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
            Path dataFile = Paths.get(DATA_FILE);
            if (!Files.exists(dataFile)) {
                Files.write(dataFile, "[]".getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize data directory", e);
        }
    }

    private void loadIdCounter() {
        List<ConveyorItem> items = findAll();
        if (items.isEmpty()) {
            seedMockData();
            items = findAll();
        }
        if (!items.isEmpty()) {
            long maxId = items.stream().mapToLong(ConveyorItem::getId).max().orElse(0);
            idCounter.set(maxId);
        }
    }

    private void seedMockData() {
        List<ConveyorItem> mockItems = List.of(
            ConveyorItem.builder().id(1L).name("Item001").positionX(50.5).defective(false).robotDecision("PASS").build(),
            ConveyorItem.builder().id(2L).name("Item002").positionX(150.3).defective(true).robotDecision("PICK_AT 180.255").build(),
            ConveyorItem.builder().id(3L).name("Item003").positionX(250.8).defective(false).robotDecision("PASS").build(),
            ConveyorItem.builder().id(4L).name("Item004").positionX(350.2).defective(true).robotDecision("PICK_AT 372.67").build(),
            ConveyorItem.builder().id(5L).name("Item005").positionX(450.5).defective(false).robotDecision("PASS").build(),
            ConveyorItem.builder().id(6L).name("Item006").positionX(550.0).defective(true).robotDecision("PICK_AT 576.75").build(),
            ConveyorItem.builder().id(7L).name("Item007").positionX(650.4).defective(false).robotDecision("PASS").build(),
            ConveyorItem.builder().id(8L).name("Item008").positionX(750.9).defective(false).robotDecision("PASS").build()
        );
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(mockItems);
            Files.write(Paths.get(DATA_FILE), json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to seed mock data", e);
        }
    }

    public synchronized ConveyorItem save(ConveyorItem item) {
        try {
            List<ConveyorItem> items = findAll();
            
            if (item.getId() == null) {
                item.setId(idCounter.incrementAndGet());
                items.add(item);
            } else {
                items.removeIf(i -> i.getId().equals(item.getId()));
                items.add(item);
            }
            
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(items);
            Files.write(Paths.get(DATA_FILE), json.getBytes());
            return item;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save item", e);
        }
    }

    public Optional<ConveyorItem> findById(Long id) {
        try {
            return findAll().stream()
                    .filter(item -> item.getId().equals(id))
                    .findFirst();
        } catch (Exception e) {
            throw new RuntimeException("Failed to find item", e);
        }
    }

    public List<ConveyorItem> findAll() {
        try {
            String json = Files.readString(Paths.get(DATA_FILE));
            CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, ConveyorItem.class);
            return objectMapper.readValue(json, listType);
        } catch (IOException e) {
            LOGGER.severe("Failed to read items from " + DATA_FILE + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public synchronized void deleteById(Long id) {
        try {
            List<ConveyorItem> items = findAll();
            items.removeIf(item -> item.getId().equals(id));
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(items);
            Files.write(Paths.get(DATA_FILE), json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete item", e);
        }
    }

    public void deleteAll() {
        try {
            Files.write(Paths.get(DATA_FILE), "[]".getBytes());
            idCounter.set(0);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete all items", e);
        }
    }
}
