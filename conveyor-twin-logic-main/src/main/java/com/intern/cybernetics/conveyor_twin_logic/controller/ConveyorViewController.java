package com.intern.cybernetics.conveyor_twin_logic.controller;

import com.intern.cybernetics.conveyor_twin_logic.model.ConveyorItem;
import com.intern.cybernetics.conveyor_twin_logic.repository.ConveyorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ConveyorViewController {

    private final ConveyorRepository conveyorRepository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        List<ConveyorItem> allItems = conveyorRepository.findAll();

        long totalItems = allItems.size();
        long defectiveCount = allItems.stream().filter(ConveyorItem::isDefective).count();
        long healthyCount = totalItems - defectiveCount;
        double defectRate = totalItems > 0 ? Math.round((defectiveCount * 100.0 / totalItems) * 10.0) / 10.0 : 0;
        long pickedCount = allItems.stream().filter(i -> i.getRobotDecision() != null && i.getRobotDecision().startsWith("PICK_AT")).count();
        double avgPosition = allItems.stream().mapToDouble(ConveyorItem::getPositionX).average().orElse(0);
        double maxPosition = allItems.stream().mapToDouble(ConveyorItem::getPositionX).max().orElse(0);

        double avgQuality = allItems.stream()
                .filter(i -> i.getQualityScore() > 0)
                .mapToInt(ConveyorItem::getQualityScore)
                .average()
                .orElse(0.0);

        model.addAttribute("items", allItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("defectiveCount", defectiveCount);
        model.addAttribute("healthyCount", healthyCount);
        model.addAttribute("defectRate", defectRate);
        model.addAttribute("pickedCount", pickedCount);
        model.addAttribute("avgPosition", Math.round(avgPosition * 10.0) / 10.0);
        model.addAttribute("maxPosition", maxPosition);
        model.addAttribute("avgQualityScore", Math.round(avgQuality * 10.0) / 10.0);
        return "dashboard";
    }

}
