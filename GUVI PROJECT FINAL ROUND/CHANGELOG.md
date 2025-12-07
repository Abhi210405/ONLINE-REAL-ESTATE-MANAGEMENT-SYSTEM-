# Changelog

All notable changes to the MSP Real Estate System are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0] — 2025-11-24

### ✨ Added

- **Java 21 LTS Upgrade**: Migrated project from Java 17 to Java 21 LTS for improved performance, security, and modern language features.
  - Updated `maven.compiler.source` and `maven.compiler.target` to 21
  - Configured `maven-compiler-plugin` with `release: 21` for explicit Java 21 compilation

- **Unit Testing Framework**: Integrated JUnit 5 (Jupiter) for comprehensive testing.
  - Added `UserTest.java` with 6 test cases covering User model functionality
  - Configured `maven-surefire-plugin` for test execution
  - All tests passing: 6/6 ✓

- **Executable Fat JAR (Uber JAR)**: Created single, self-contained executable JAR with all dependencies bundled.
  - Added `maven-shade-plugin` v3.5.0 to the build pipeline
  - Produces `msp-realestate-1.0-SNAPSHOT.jar` (~13 MB) with SQLite JDBC and all runtime dependencies
  - Main class (`com.msp.realestate.MSPRealEstateSystem`) configured in JAR manifest
  - End users can run: `java -jar msp-realestate-1.0-SNAPSHOT.jar`

- **CI/CD Pipeline (GitHub Actions)**: Enhanced automated build and test workflow.
  - Workflow: `.github/workflows/maven.yml`
  - Runs on: `push` to `main`/`master`, and `pull_request`
  - Steps:
    - Checkout code
    - Setup JDK 21 (Temurin distribution)
    - Maven build: `mvn clean package`
    - Test execution: `mvn test`
    - Test result publishing (JUnit XML reports)
    - Artifact upload (JAR files)
  - Maven dependency caching for faster builds

- **Documentation Enhancements**:
  - Updated `README.md` with comprehensive setup and run instructions
  - Added build status badge (GitHub Actions workflow)
  - Java 21 and Maven version badges
  - Three easy run methods:
    1. Maven exec plugin
    2. IDE run from source
    3. Fat JAR (recommended for end users)
  - Added troubleshooting section with common issues and solutions
  - IDE setup guides for IntelliJ IDEA, Eclipse, and NetBeans

### 🔧 Changed

- **Build Configuration**:
  - `pom.xml` now targets Java 21 explicitly
  - Added Maven Shade plugin for fat JAR generation
  - Maven Surefire plugin (v3.1.2) for test execution

- **Project Structure**:
  - Added `src/test/java/com/msp/realestate/` directory for test classes
  - Added `.github/workflows/maven.yml` for CI/CD automation

### 🧪 Testing

- **6 Unit Tests** in `UserTest.java`:
  - User creation and initialization
  - Field modification and getters
  - Multiple user instances and field independence
  - Edge cases (empty fields, role changes)

### 🎯 Compatibility

- **Java Version**: 21 LTS (Long-Term Support until September 2028)
- **Maven Version**: 3.8.0+
- **Build Tool Chain**: Temurin JDK 21 (Adoptium)
- **Test Framework**: JUnit 5.10.0
- **Dependencies**:
  - SQLite JDBC: 3.42.0.0 (bundled in fat JAR)

### 📦 Distribution

- **Development Build**: `mvn clean package` produces:
  - `msp-realestate-1.0-SNAPSHOT.jar` (fat JAR, all dependencies)
  - `original-msp-realestate-1.0-SNAPSHOT.jar` (original non-shaded JAR)

- **Runtime**: Single command:
  ```bash
  java -jar msp-realestate-1.0-SNAPSHOT.jar
  ```

### 🔐 Security

- Updated all dependencies to latest stable versions
- Java 21 includes security patches and improvements over Java 17
- No known CVEs in current dependency set

### 📋 Known Issues

- None reported

### 🚀 Future Enhancements

- Native image compilation using GraalVM (jlink / jpackager)
- Platform-specific installers (Windows MSI, macOS DMG, Linux RPM)
- Enhanced error logging and reporting
- Database migration utilities
- Additional integration tests

### 🙏 Contributors

- MSP Real Estate Development Team

---

## [0.9] — Previous Release

- Original single-file implementation (backup available in `backup/MSPRealEstateSystem-ORIGINAL.java`)
- Refactored into modular Java classes
- SQLite database integration

---

## Migration Notes (Java 17 → Java 21)

### What Changed

1. **Language Level**: All source files now compile and run under Java 21 release constraints
2. **Build Output**: Maven automatically compiles with `--release 21` flag
3. **Runtime Requirements**: End users must have Java 21 or later installed

### Verification Steps

```bash
# Verify Java 21 is installed
java -version

# Verify Maven uses Java 21
mvn -version

# Build the project
mvn clean package

# Run tests
mvn test

# Execute the application
java -jar target/msp-realestate-1.0-SNAPSHOT.jar
```

### Compatibility

- ✅ Clean compile with no errors or warnings
- ✅ All unit tests passing
- ✅ Application launches and runs successfully
- ✅ SQLite database operations functional
- ✅ Swing GUI operational

---

**For questions or contributions, please refer to the project README and GitHub Actions workflow results.**
