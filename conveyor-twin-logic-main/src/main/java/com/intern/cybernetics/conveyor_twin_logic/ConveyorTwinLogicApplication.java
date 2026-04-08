package com.intern.cybernetics.conveyor_twin_logic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ConveyorTwinLogicApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConveyorTwinLogicApplication.class, args);
    }

}
