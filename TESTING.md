# NewRMS Testing Guide - CURRENTLY DISABLED

⚠️ **NOTICE: All tests are currently disabled in this project.**

This document provides comprehensive information about testing in the NewRMS (Restaurant Management System) project, but all testing functionality has been temporarily disabled.

## Test Structure

The project includes comprehensive test coverage with the following test types:

### 1. Unit Tests
- **Model Tests**: Validation and business logic testing for entities
- **Service Tests**: Business logic testing with mocked dependencies
- **Repository Tests**: Data access layer testing with in-memory H2 database
- **Controller Tests**: Web layer testing with MockMvc
- **Security Tests**: Authentication and authorization testing

### 2. Integration Tests
- **API Integration Tests**: End-to-end testing of REST endpoints
- **Database Integration Tests**: Testing with real database interactions

## Test Technologies

- **JUnit 5**: Primary testing framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Spring-specific testing utilities
- **Spring Security Test**: Security testing support
- **H2 Database**: In-memory database for testing
- **TestContainers**: Container-based integration testing
- **JaCoCo**: Code coverage analysis

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Unit Tests Only
```bash
mvn test -Dtest="**/*Test"
```

### Run Integration Tests Only
```bash
mvn failsafe:integration-test
```

### Run Tests with Coverage Report
```bash
mvn clean test jacoco:report
```

### Run Specific Test Class
```bash
mvn test -Dtest=StaffServiceTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=StaffServiceTest#shouldCreateStaffWithEncodedPassword
```

## Test Profiles

### Test Profile Configuration
The project uses a dedicated `test` profile with the following configuration:
- In-memory H2 database
- Disabled Flyway migrations
- Debug logging for Spring Security and Web
- Faster password encoding for tests

### Test Properties
Located in `src/test/resources/application-test.properties`

## Test Coverage

### Coverage Goals
- **Minimum Line Coverage**: 70%
- **Package Coverage**: Enforced via JaCoCo plugin
- **Critical Components**: Aim for 90%+ coverage

### Viewing Coverage Reports
After running tests with coverage:
```bash
open target/site/jacoco/index.html
```

## Test Categories

### Model Tests (`src/test/java/.../model/`)
- **StaffTest**: Staff entity validation and business rules
- **CustomerTest**: Customer entity validation
- **OrderTest**: Order entity relationships and validation

### Repository Tests (`src/test/java/.../repository/`)
- **StaffRepositoryTest**: Staff data access operations
- **CustomerRepositoryTest**: Customer data access operations
- Uses `@DataJpaTest` for focused repository testing

### Service Tests (`src/test/java/.../service/`)
- **StaffServiceTest**: Staff business logic with mocked dependencies
- **CustomerServiceTest**: Customer business logic
- Uses `@ExtendWith(MockitoExtension.class)` for mocking

### Controller Tests (`src/test/java/.../controller/`)
- **StaffControllerTest**: Staff REST API endpoints
- **AuthControllerTest**: Authentication endpoints
- Uses `@WebMvcTest` for web layer testing

### Security Tests (`src/test/java/.../security/`)
- **JpaUserDetailsServiceTest**: User authentication service
- Tests authentication and authorization logic

### Integration Tests (`src/test/java/.../integration/`)
- **AuthControllerIntegrationTest**: End-to-end authentication testing
- Uses full Spring context with real database

## Test Data Management

### Test Configuration
- **TestConfig**: Custom configuration for test environment
- Faster password encoding for improved test performance
- Mock beans where appropriate

### Test Data Setup
- Each test class has `@BeforeEach` setup methods
- Test data builders for consistent object creation
- Cleanup handled automatically by test framework

## Best Practices

### Writing Tests
1. **Follow AAA Pattern**: Arrange, Act, Assert
2. **Descriptive Test Names**: Use `@DisplayName` for clear descriptions
3. **One Assertion Per Test**: Focus on single behavior
4. **Mock External Dependencies**: Use Mockito for isolation
5. **Test Edge Cases**: Include null, empty, and boundary conditions

### Test Organization
1. **Package Structure**: Mirror main package structure
2. **Test Naming**: End with `Test` for unit tests, `IntegrationTest` for integration
3. **Test Categories**: Group related tests in same class
4. **Setup Methods**: Use `@BeforeEach` for common setup

### Performance
1. **Fast Tests**: Unit tests should run quickly
2. **Minimal Context**: Use `@WebMvcTest`, `@DataJpaTest` for focused testing
3. **Parallel Execution**: Tests are designed to run in parallel
4. **Resource Cleanup**: Automatic cleanup via Spring Test framework

## Continuous Integration

### Maven Lifecycle
- **test**: Runs unit tests
- **integration-test**: Runs integration tests
- **verify**: Runs all tests and checks coverage

### Build Pipeline
1. Compile code
2. Run unit tests
3. Generate coverage report
4. Run integration tests
5. Verify coverage thresholds
6. Generate final reports

## Troubleshooting

### Common Issues

#### Test Database Connection
If tests fail with database connection issues:
```bash
# Check H2 dependency in pom.xml
# Verify test profile configuration
# Check application-test.properties
```

#### Security Test Failures
For authentication-related test failures:
```bash
# Verify @WithMockUser annotations
# Check security configuration
# Ensure proper test context setup
```

#### Coverage Threshold Failures
If coverage checks fail:
```bash
# Run: mvn jacoco:report
# Check target/site/jacoco/index.html
# Add tests for uncovered code
# Adjust thresholds if necessary
```

## Test Reports

### JUnit Reports
- Location: `target/surefire-reports/`
- Format: XML and TXT

### Coverage Reports
- Location: `target/site/jacoco/`
- Format: HTML, XML, CSV

### Integration Test Reports
- Location: `target/failsafe-reports/`
- Format: XML and TXT

## Contributing

When adding new features:
1. Write tests first (TDD approach)
2. Ensure all tests pass
3. Maintain or improve coverage
4. Follow existing test patterns
5. Update this documentation if needed

## Test Commands Reference

```bash
# Run all tests
mvn clean test

# Run with coverage
mvn clean test jacoco:report

# Run integration tests
mvn clean integration-test

# Run specific test
mvn test -Dtest=ClassName

# Run tests in specific package
mvn test -Dtest="com.DevanshNewRMS.NewRMS.service.*"

# Skip tests
mvn clean install -DskipTests

# Run tests with specific profile
mvn test -Dspring.profiles.active=test

# Generate coverage report only
mvn jacoco:report

# Check coverage thresholds
mvn jacoco:check
```

For more information, refer to the individual test classes and their documentation.
## 🚫 
Tests Currently Disabled

All JUnit and Mockito tests have been disabled for this project:

### What was disabled:
- ✅ All test dependencies commented out in `pom.xml`
- ✅ Maven Surefire plugin configured to skip tests (`skipTests=true`)
- ✅ Maven Failsafe plugin configured to skip integration tests (`skipITs=true`)
- ✅ JaCoCo coverage plugin disabled
- ✅ Test directory moved to `src/test-disabled`
- ✅ Global Maven properties set to skip all tests

### To re-enable tests:
1. Uncomment test dependencies in `pom.xml`
2. Remove `<skipTests>true</skipTests>` from Maven plugins
3. Move `src/test-disabled` back to `src/test`
4. Remove test skip properties from `<properties>` section
5. Run `mvn clean test`

### Current build behavior:
- `mvn clean install` - Will skip all tests
- `mvn test` - Will skip all tests  
- `mvn package` - Will skip all tests
- No test reports will be generated
- No coverage analysis will be performed

The test framework infrastructure remains in place and can be quickly re-enabled when needed.