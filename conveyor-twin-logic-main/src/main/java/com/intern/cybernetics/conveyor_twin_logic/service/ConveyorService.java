package com.intern.cybernetics.conveyor_twin_logic.service;

import com.intern.cybernetics.conveyor_twin_logic.model.ConveyorItem;
import com.intern.cybernetics.conveyor_twin_logic.repository.ConveyorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ConveyorService {
    private final ConveyorRepository repository;
    private final Random random = new Random();
    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public String processItem(ConveyorItem item, double speed) {
        String decision = item.isDefective()
                ? "PICK_AT " + (item.getPositionX() + (speed * 0.85))
                : "PASS";
        item.setRobotDecision(decision);
        item.setTimestamp(LocalDateTime.now().format(TS_FMT));
        item.setQualityScore(item.isDefective() ? 40 + random.nextInt(30) : 75 + random.nextInt(26));
        repository.save(item);
        return decision;
    }

    public ConveyorItem saveItem(ConveyorItem item) {
        if (item.getTimestamp() == null) {
            item.setTimestamp(LocalDateTime.now().format(TS_FMT));
        }
        return repository.save(item);
    }

    public Optional<ConveyorItem> getItem(Long id) {
        return repository.findById(id);
    }

    public List<ConveyorItem> getAllItems() {
        return repository.findAll();
    }

    public void deleteItem(Long id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public Map<String, Object> getStats() {
        List<ConveyorItem> items = repository.findAll();
        long total = items.size();
        long defectiveCount = items.stream().filter(ConveyorItem::isDefective).count();
        long healthyCount = total - defectiveCount;
        double defectRate = total > 0
                ? Math.round((defectiveCount * 100.0 / total) * 10.0) / 10.0
                : 0.0;
        long pickedCount = items.stream()
                .filter(i -> i.getRobotDecision() != null && i.getRobotDecision().startsWith("PICK_AT"))
                .count();
        double avgQuality = items.stream()
                .filter(i -> i.getQualityScore() > 0)
                .mapToInt(ConveyorItem::getQualityScore)
                .average()
                .orElse(0.0);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalItems", total);
        stats.put("healthyCount", healthyCount);
        stats.put("defectiveCount", defectiveCount);
        stats.put("defectRate", defectRate);
        stats.put("avgQualityScore", Math.round(avgQuality * 10.0) / 10.0);
        stats.put("pickedCount", pickedCount);
        return stats;
    }
}
