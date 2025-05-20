# Semester assignment - Insurance system

## Project description
This project implements a simplified insurance system for small insurance companies. The system supports three types of insurance policies:
- **SingleVehicleContract** - compulsory insurance contract (CIC) for one vehicle.
- **MasterVehicleContract** - master insurance for a fleet of vehicles.
- **TravelContract** - travel insurance for persons.

The system allows for contract management, payments, arrears updates and claims processing.

## Main functions
1. **Contract creation and management**:
    - Creation of new contracts for vehicles and persons.
    - Move individual contracts to master contracts.
    - Set up payment details and payment frequency.

2. **Payments and arrears**:
    - Making payments via `PaymentHandler`.
    - Updating arrears on contracts.
    - Support for master contracts and distribution of payments between subsidiary contracts.

3. **Claim Processing**:
    - Payment of insurance claims for vehicles and persons.
    - Automatic deactivation of contracts in case of total loss.

4. **Data validation**:
    - Verification of birth numbers and ID numbers.
    - Checking the validity of the registration number and the price of the vehicle.

## Project structure
The project is divided into the following packages:
- **company** - Contains the `InsuranceCompany` class for managing policies and claims.
- **contracts** - Implementation of insurance contracts (`AbstractContract`, `SingleVehicleContract`, `MasterVehicleContract`, `TravelContract`).
- **objects** - Classes for insured objects (`Person`, `Vehicle`) and enum `LegalForm`.
- **payment** - Classes for managing payments (`ContractPaymentData`, `PaymentHandler`, `PaymentInstance`, `PaymentFrequency`).

## Requirements
- Java 8 or later.
- The code must conform to the specification given in the specification, including the UML diagram and functionalities.

## Installation and startup
1. Clone the repository or download the ZIP archive.
2. Import the project into your development environment (e.g. IntelliJ IDEA, Eclipse).
3. Run tests in the `RequiredTests` class to verify functionality.

## Evaluation
The project will be evaluated based on:
- Fulfillment of the UML diagram and specification.
- Functionality of the tests (`RequiredTests` and private tests).
- Cleanliness and readability of the code.
- Oral defence of the solution.

## Author
Second-year bachelor's degree student in Robotics and Cybernetics   
Heorhii Chukhir