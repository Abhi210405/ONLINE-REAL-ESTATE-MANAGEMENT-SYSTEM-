# Building and Packaging MSP Real Estate System

This guide covers building the application as a single JAR, Windows installer (MSI), and creating portable packages.

---

## Prerequisites

- **Java 21 LTS** installed and `JAVA_HOME` set
- **Maven 3.8.0+** installed
- **Windows only** (for MSI): WiX Toolset 3.11+ for jpackage MSI creation

---

## Build Artifacts

### 1. Standard JAR Build (Default)

```bash
mvn clean package
```

**Output:**
- `target/msp-realestate-1.0-SNAPSHOT.jar` (executable fat JAR, ~13 MB)
- `target/original-msp-realestate-1.0-SNAPSHOT.jar` (non-shaded JAR)

**Run:**
```bash
java -jar target/msp-realestate-1.0-SNAPSHOT.jar
```

---

### 2. Windows MSI Installer (jpackage)

#### Prerequisites
1. Java 21 with jpackage support (included in Temurin JDK 21)
2. **Windows only**: Install WiX Toolset v3.11+
   - Download: https://wixtoolset.org/releases/
   - Add WiX bin directory to PATH (e.g., `C:\Program Files (x86)\WiX Toolset v3.11\bin`)

#### Build Steps

**Step 1:** Ensure JDK 21 is active
```bat
java -version
```
Expected: `openjdk version "21"` or `java version "21"`

**Step 2:** Build the project (creates fat JAR)
```bat
cd "C:\Users\Abhi Nandan Pandey\OneDrive\Desktop\GUVI PROJECT"
mvn clean package
```

**Step 3:** Run jpackage manually (if Maven profile doesn't work)
```bat
cd target
jpackage ^
  --input . ^
  --name MSPRealEstate ^
  --main-jar msp-realestate-1.0-SNAPSHOT.jar ^
  --main-class com.msp.realestate.MSPRealEstateSystem ^
  --type msi ^
  --dest installer ^
  --vendor "MSP Real Estate" ^
  --app-version 1.0 ^
  --win-menu ^
  --win-shortcut ^
  --description "MSP Real Estate System - Java 21 Desktop Application"
```

**Step 4:** Locate the MSI installer
- **Output path**: `target/installer/MSPRealEstate-1.0.msi`
- **Size**: ~50-60 MB (includes bundled JDK 21 runtime)

#### Installation

1. Double-click `MSPRealEstate-1.0.msi`
2. Follow the installer wizard
3. Application will be installed to `C:\Program Files\MSP Real Estate\` (or custom path)
4. Start menu shortcut and desktop shortcut will be created
5. Run from Start menu or desktop icon

**Uninstall:** Control Panel → Programs → Uninstall MSP Real Estate

---

### 3. Portable EXE (Alternative to MSI)

If you prefer a portable executable without an installer:

```bat
cd target
jpackage ^
  --input . ^
  --name MSPRealEstate ^
  --main-jar msp-realestate-1.0-SNAPSHOT.jar ^
  --main-class com.msp.realestate.MSPRealEstateSystem ^
  --type exe ^
  --dest installer ^
  --vendor "MSP Real Estate" ^
  --app-version 1.0 ^
  --win-shortcut ^
  --description "MSP Real Estate System - Java 21 Desktop Application"
```

**Output:** `target/installer/MSPRealEstate-1.0.exe` (~50 MB, portable executable)

---

### 4. Custom Installer Branding (Optional)

To customize the installer with your own icon and splash screen:

```bat
jpackage ^
  --input target ^
  --name MSPRealEstate ^
  --main-jar msp-realestate-1.0-SNAPSHOT.jar ^
  --main-class com.msp.realestate.MSPRealEstateSystem ^
  --type msi ^
  --dest target/installer ^
  --vendor "MSP Real Estate" ^
  --app-version 1.0 ^
  --win-menu ^
  --win-shortcut ^
  --description "MSP Real Estate System" ^
  --icon path/to/icon.ico
```

**Icon Requirements:**
- Format: `.ico` (Windows icon)
- Recommended size: 256x256 pixels minimum
- Path: relative or absolute

---

## Distribution Methods

### Method 1: Direct JAR Distribution (End Users)
- Distribute: `msp-realestate-1.0-SNAPSHOT.jar`
- Prerequisites: Java 21 must be installed
- Run: `java -jar msp-realestate-1.0-SNAPSHOT.jar`
- Pros: Single file, small size (~13 MB)
- Cons: Requires Java pre-installed

### Method 2: MSI Installer (Enterprise)
- Distribute: `MSPRealEstate-1.0.msi` (~50 MB)
- Prerequisites: Windows 10/11
- Installation: Double-click to install, creates start menu entry
- Includes: Bundled JDK 21 runtime (no Java pre-installation needed)
- Pros: Professional installer, handles dependencies
- Cons: Larger file size, Windows only

### Method 3: Portable EXE (Portable Deployment)
- Distribute: `MSPRealEstate-1.0.exe` (~50 MB)
- Run: Execute directly from USB, network drive, or any folder
- No installation required
- Includes: Bundled JDK 21 runtime
- Pros: True portability, single executable
- Cons: Requires write permissions in app directory for database

---

## Troubleshooting

### Issue: jpackage command not found

**Solution:**
1. Verify JDK 21 is installed: `java -version`
2. Verify jpackage is available: `jpackage --version`
3. If not found, reinstall JDK 21 ensuring tools are included
4. Add JDK 21 `bin` to PATH:
   ```bat
   set PATH=C:\Program Files\Java\jdk-21\bin;%PATH%
   ```

### Issue: WiX Toolset error when building MSI

**Solution:**
1. Install WiX Toolset v3.11+ from https://wixtoolset.org/
2. Add WiX bin directory to PATH:
   ```bat
   setx PATH "%PATH%;C:\Program Files (x86)\WiX Toolset v3.11\bin"
   ```
3. Restart command prompt and retry

### Issue: Database file not created after installation

**Solution:**
1. Ensure the installation directory is writable
2. Run application as Administrator if needed
3. Check for write permission errors in the application log

---

## Installer Customization

### Add License File to Installer

1. Create `LICENSE.txt` in project root
2. Update jpackage command to include:
   ```bat
   --license-file LICENSE.txt
   ```

### Add Release Notes

1. Create `RELEASE-NOTES.txt` in project root
2. Include in documentation or installer

### Add System Requirements

Ensure installer notes the following requirements:
- **OS**: Windows 10/11 (64-bit)
- **RAM**: Minimum 512 MB
- **Disk Space**: Minimum 100 MB
- **No additional Java installation needed** (JDK 21 bundled)

---

## GitHub Actions Automation (Future)

To automate MSI creation on push/release:

1. Add Windows runner to GitHub Actions workflow:
   ```yaml
   build-installer:
     runs-on: windows-latest
     steps:
       - uses: actions/checkout@v4
       - uses: actions/setup-java@v4
         with:
           distribution: 'temurin'
           java-version: '21'
       - run: mvn clean package
       - run: jpackage ... (MSI build command)
       - uses: actions/upload-artifact@v3
         with:
           name: msi-installer
           path: target/installer/*.msi
   ```

2. This creates automatic MSI builds on every push/release

---

## Summary

| Method | Size | Setup Time | Target Users |
|--------|------|-----------|--------------|
| JAR | ~13 MB | 1 min | Developers, tech-savvy users |
| MSI | ~50 MB | 2-3 min | Enterprise, end-users |
| EXE | ~50 MB | <1 min | USB/portable deployment |

**Recommended:** MSI for professional distribution, JAR for development.

---

For more information, see:
- [Java jpackage documentation](https://docs.oracle.com/en/java/javase/21/docs/specs/man/jpackage.html)
- [WiX Toolset](https://wixtoolset.org/)
- Main [README.md](README.md)
