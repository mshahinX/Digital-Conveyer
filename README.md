# Digital Conveyer (Conveyor Twin Logic)

A small Spring Boot application that implements conveyor-line "digital twin" logic and a minimal dashboard. The service stores conveyor items in a JSON file (conveyor_data/items.json) and exposes REST endpoints plus a Thymeleaf dashboard.

## Quick summary

- Language: Java 17 (Spring Boot)
- Build: Maven and Gradle are provided
- Runtime: No database required — uses a local JSON file at `conveyor_data/items.json`
- Entry point: `conveyor-twin-logic-main/src/main/java/com/intern/cybernetics/conveyor_twin_logic/ConveyorTwinLogicApplication.java`

## Features

- Add and persist ConveyorItem records to a JSON file
- Process items to decide whether the robot should PICK or PASS (returns `PICK_AT <position>` for defective items)
- Dashboard (Thymeleaf) at `/dashboard` with basic stats
- OpenAPI/Swagger UI available via springdoc dependency (when running)

## Project layout (important files)

- `conveyor-twin-logic-main/` - main Spring Boot project
  - `src/main/java/.../controller/` - REST controllers and view controller
  - `src/main/java/.../service/` - business logic
  - `src/main/java/.../repository/` - file-backed repository (`conveyor_data/items.json`)
  - `conveyor_data/items.json` - data file (created automatically if missing)
  - `pom.xml` and `build.gradle` - Maven and Gradle build files

## Requirements

- Java 17
- Either Maven or Gradle installed (or use included wrapper scripts `mvnw`, `gradlew`)

## Running (Maven)

1. From repository root, run:

   ./conveyor-twin-logic-main/mvnw spring-boot:run

2. Build a jar and run:

   ./conveyor-twin-logic-main/mvnw -DskipTests package
   java -jar conveyor-twin-logic-main/target/conveyor-twin-logic-0.0.1-SNAPSHOT.jar

## Running (Gradle)

1. Run with the included wrapper:

   ./conveyor-twin-logic-main/gradlew bootRun

2. Build a jar and run:

   ./conveyor-twin-logic-main/gradlew bootJar
   java -jar conveyor-twin-logic-main/build/libs/conveyor-twin-logic-0.0.1-SNAPSHOT.jar

## API (examples)

Base path: /api/v1/conveyor

- POST /api/v1/conveyor
  - Create or update a ConveyorItem (JSON body). Example body:

    {
      "name": "Item009",
      "positionX": 120.5,
      "defective": false
    }

  - Response: saved ConveyorItem (with id and timestamp)

- POST /api/v1/conveyor/process?speed=2.5
  - Process an item and return robot decision. Body: ConveyorItem JSON. Example:

    curl -X POST "http://localhost:8080/api/v1/conveyor/process?speed=2.5" \
      -H "Content-Type: application/json" \
      -d '{"name":"Item010","positionX":200.1,"defective":true}'

- GET /api/v1/conveyor
  - Returns list of all items

- GET /api/v1/conveyor/{id}
  - Get single item by id

- GET /api/v1/conveyor/stats
  - Returns summary statistics (totalItems, defectiveCount, defectRate, avgQualityScore, pickedCount)

- DELETE /api/v1/conveyor/{id}
  - Delete an item

- DELETE /api/v1/conveyor
  - Delete all items (resets data file and id counter)

## Dashboard

Visit http://localhost:8080/dashboard to view a simple Thymeleaf dashboard showing items and computed stats.

## Data storage

The application persists items to `conveyor_data/items.json` (created automatically). The repository seed includes mock data if the file is empty.

## Notes & Implementation details

- The application excludes Spring Boot auto-configuration for a DataSource (no DB required).
- Repository is file-backed and synchronized for thread safety.
- Quality score and timestamps are set by the service when processing/saving items.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Open a PR with a clear description

## License

Add a LICENSE file if you want to specify terms. For now, this repository has no license.

## Contact

Repository owner: @mshahinX

---

(Generated and added by repository assistant.)