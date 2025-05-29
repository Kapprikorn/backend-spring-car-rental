[//]: # (Install Mermaid plugin to view solution)
```mermaid
sequenceDiagram
    participant Client
    participant VehicleController
    participant VehicleService
    participant VehicleRepository

%% Get All Vehicles Flow
    Client->>VehicleController: GET /vehicles
    VehicleController->>VehicleService: getVehicles()
    VehicleService->>VehicleRepository: findAll()
    VehicleRepository-->>VehicleService: List<Vehicle>
    VehicleService-->>VehicleController: List<Vehicle>
    VehicleController->>VehicleController: mapToDto(vehicles)
    VehicleController-->>Client: List<VehicleDto>

%% Create Vehicle Flow
    Client->>VehicleController: POST /vehicles
    VehicleController->>VehicleController: mapToEntity(vehicleDto)
    VehicleController->>VehicleService: createVehicle(vehicle)
    VehicleService->>VehicleRepository: save(vehicle)
    VehicleRepository-->>VehicleService: Vehicle
    VehicleService-->>VehicleController: Vehicle
    VehicleController->>VehicleController: mapToDto(vehicle)
    VehicleController-->>Client: VehicleDto
```
