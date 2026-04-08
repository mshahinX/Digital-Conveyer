package com.intern.cybernetics.conveyor_twin_logic.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConveyorItem {

    private Long id;

    @NotBlank(message = "name must not be blank")
    private String name;

    @Min(value = 0, message = "positionX must be >= 0")
    private double positionX;

    private boolean defective = false;
    private String robotDecision;
    private String timestamp;
    private int qualityScore;

}
