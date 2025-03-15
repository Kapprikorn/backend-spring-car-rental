[//]: # (Install Mermaid plugin to view solution)
```mermaid
  classDiagram
    class User {
        <<abstract>>
        +Long id
        +String name
        +String email
        +String password
        +String role
    }

    class Customer {
        +String driverLicense
    }

    class Employee {
        +String employeeId
        +String position
    }

    class Admin {
        +String adminId
    }

    class Vehicle {
        +Long id
        +String licensePlate
        +String make
        +String model
        +String type
        +Status status
        +Double pricePerDay
    }

    class Reservation {
        +Long id
        +Date startDate
        +Date endDate
        +Double totalPrice
    }

    class ParkingSpace {
        +Long id
        +String location
        +String size
        +boolean occupied
    }

    class ParkingLot {
        +Long id
        +String name
    }

    class Location {
        +Long id
        +String name
        +String address
        +String phoneNumber
    }

    class Status {
        <<enumeration>>
        AVAILABLE
        RENTED_OUT
        MAINTENANCE
    }

%% Relationships
    User <|-- Customer
    User <|-- Employee
    Employee <|-- Admin

    Vehicle "1" -- "1" Status : has
    Customer "1" -- "0..*" Reservation : makes
    Vehicle "1" -- "0..*" Reservation : is reserved in
    Vehicle "1" -- "1" ParkingSpace : is parked in
    Employee "1" -- "0..*" Vehicle : can update status of
    Admin "1" -- "0..*" Vehicle : can add/delete
    ParkingLot "1" -- "0..*" ParkingSpace : contains
    Location "1" -- "0..*" ParkingLot : has
    Location "1" -- "0..*" Employee : employs
```
