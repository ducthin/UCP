# UCP Calculator (Use Case Point Calculator)

## Overview

The UCP Calculator is a web-based application designed to help software development teams estimate project effort based on the Use Case Points method. This modernized version features an improved user interface, English language support, updated UCP calculation formulas, and customizable productivity factors.

## Key Features

- **Project Management**: Create, view, update, and delete software development projects
- **Actor Management**: Define and classify system actors based on complexity
- **Use Case Management**: Create and categorize use cases with appropriate weights
- **Modern UCP Calculation**: Uses the formula UCP = (UUCW + UAW) × VAF
- **Customizable Productivity Factor**: Adjust effort estimates based on team capability and project complexity
- **Actual Effort Tracking**: Record and compare actual effort with estimates
- **User-Friendly Interface**: Clean, intuitive design focusing on content and usability

## UCP Calculation Method

### Core Components

1. **UAW (Unadjusted Actor Weight)**
   - Simple Actor: 1 point
   - Average Actor: 2 points
   - Complex Actor: 3 points

2. **UUCW (Unadjusted Use Case Weight)**
   - Simple Use Case: 5 points
   - Average Use Case: 10 points
   - Complex Use Case: 15 points

3. **Technical Factors**
   - 13 technical factors rated from 0-5
   - Each factor has a predetermined weight
   - TDI (Total Degree of Influence) = Sum of (Factor Rating × Factor Weight)

4. **VAF (Value Adjustment Factor)**
   - VAF = 0.65 + (0.01 × TDI)

5. **UCP (Use Case Points)**
   - UCP = (UUCW + UAW) × VAF

6. **Effort Estimation**
   - Estimated Effort = UCP × PF
   - PF (Productivity Factor) is customizable (default: 20 hours/UCP)

### Productivity Factor Guidelines

- **Low complexity projects/experienced teams**: 15-18 hours/UCP
- **Average complexity projects**: 18-25 hours/UCP
- **High complexity projects/new technology**: 25-30+ hours/UCP

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Web browser (Chrome, Firefox, Edge recommended)

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/your-username/UserCasePoint.git
   ```

2. Navigate to the project directory:
   ```
   cd UserCasePoint
   ```

3. Build the project:
   ```
   mvn clean install
   ```

4. Run the application:
   ```
   mvn spring-boot:run
   ```

5. Access the application at:
   ```
   http://localhost:8080
   ```

## Usage Guide

### Creating a New Project

1. Navigate to the Projects section
2. Click "New Project"
3. Fill in the project details and save

### Managing Actors

1. Select a project
2. Navigate to Actors
3. Add actors and classify them as Simple, Average, or Complex

### Managing Use Cases

1. Select a project
2. Navigate to Use Cases
3. Add use cases with descriptions and classify their complexity

### Creating a UCP Calculation

1. Select a project
2. Click "New Calculation"
3. Enter a name for the calculation
4. Review the actors and use cases
5. Evaluate the 13 technical factors on a scale of 0-5
6. Set your preferred productivity factor (default: 20 hours/UCP)
7. Complete the calculation and review results

### Recording Actual Effort

1. Find a completed calculation
2. Click "Set Actual Effort"
3. Enter the actual hours spent
4. Save to compare with the estimate

## Technical Stack

- **Backend**: Java with Spring Boot
- **Frontend**: Thymeleaf, Bootstrap
- **Database**: H2 Database (embedded)
- **Build Tool**: Maven

## Recent Improvements

- Modernized UI with simplified, content-focused design
- Converted user interface from Vietnamese to English
- Updated formula from UCP = UUCP × TCF × ECF to UCP = (UUCW + UAW) × VAF
- Added customizable productivity factor feature
- Improved result visualization and dashboard

## Contributors

- Group 2 - Software Engineering Department

## License

This project is licensed under the MIT License
