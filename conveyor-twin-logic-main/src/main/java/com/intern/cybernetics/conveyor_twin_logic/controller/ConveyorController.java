package com.intern.cybernetics.conveyor_twin_logic.controller;

import com.intern.cybernetics.conveyor_twin_logic.model.ConveyorItem;
import com.intern.cybernetics.conveyor_twin_logic.service.ConveyorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/conveyor")
@RequiredArgsConstructor
public class ConveyorController {
    private final ConveyorService service;

    @PostMapping("/process")
    public String handleItem(@Valid @RequestBody ConveyorItem item, @RequestParam double speed) {
        return service.processItem(item, speed);
    }

    @PostMapping
    public ResponseEntity<ConveyorItem> createItem(@Valid @RequestBody ConveyorItem item) {
        return ResponseEntity.ok(service.saveItem(item));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConveyorItem> getItem(@PathVariable Long id) {
        return service.getItem(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ConveyorItem>> getAllItems() {
        return ResponseEntity.ok(service.getAllItems());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(service.getStats());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        service.deleteItem(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllItems() {
        service.deleteAll();
        return ResponseEntity.ok().build();
    }
}
