#!/bin/bash
# Build executable packages for MSP Real Estate System (Linux/macOS)
# Prerequisites: Java 21, Maven 3.8.0+
#
# Usage: ./build-installer.sh [app-image|deb|dmg]
#   app-image - Create self-contained app image (default)
#   deb       - Create Debian package (Linux only)
#   dmg       - Create macOS DMG installer (macOS only)

set -e

# Ensure Java 21 is used
export JAVA_HOME="$(/usr/libexec/java_home -v 21 2>/dev/null || echo /usr/lib/jvm/temurin-21-jdk-amd64)"

echo "==================================================================="
echo "MSP Real Estate System - Package Build (Linux/macOS)"
echo "==================================================================="
echo ""

# Verify Java 21
java -version
if [ $? -ne 0 ]; then
    echo "ERROR: Java 21 not found. Please install JDK 21."
    exit 1
fi

# Verify jpackage
jpackage --version
if [ $? -ne 0 ]; then
    echo "ERROR: jpackage not found. This is included in JDK 21."
    exit 1
fi

# Determine package type
PACKAGE_TYPE="app-image"
if [ -n "$1" ]; then
    PACKAGE_TYPE="$1"
fi

echo ""
echo "Building project..."
mvn -DskipTests clean package
if [ $? -ne 0 ]; then
    echo "ERROR: Maven build failed."
    exit 1
fi

echo ""
echo "Packaging as $PACKAGE_TYPE..."
cd target

if [ "$PACKAGE_TYPE" = "app-image" ]; then
    echo "Creating self-contained application image..."
    mkdir -p installer
    
    jpackage \
      --input . \
      --name MSPRealEstate \
      --main-jar msp-realestate-1.0-SNAPSHOT.jar \
      --main-class com.msp.realestate.MSPRealEstateSystem \
      --type app-image \
      --dest installer \
      --vendor "MSP Real Estate" \
      --app-version 1.0 \
      --description "MSP Real Estate System - Java 21 Desktop Application"
    
    if [ $? -ne 0 ]; then
        echo "ERROR: jpackage build failed."
        exit 1
    fi
    
    echo ""
    echo "SUCCESS! App image created:"
    echo "Location: $PWD/installer/MSPRealEstate"
    echo "Run: ./installer/MSPRealEstate/bin/MSPRealEstate"
    
elif [ "$PACKAGE_TYPE" = "deb" ]; then
    echo "Creating Debian package (.deb)..."
    mkdir -p installer
    
    jpackage \
      --input . \
      --name msp-realestate \
      --main-jar msp-realestate-1.0-SNAPSHOT.jar \
      --main-class com.msp.realestate.MSPRealEstateSystem \
      --type deb \
      --dest installer \
      --vendor "MSP Real Estate" \
      --app-version 1.0 \
      --description "MSP Real Estate System - Java 21 Desktop Application" \
      --license-file ../LICENSE.txt 2>/dev/null || true
    
    if [ $? -ne 0 ]; then
        echo "ERROR: jpackage DEB build failed."
        exit 1
    fi
    
    echo ""
    echo "SUCCESS! Debian package created:"
    echo "Location: $PWD/installer/msp-realestate_1.0_amd64.deb"
    echo "Install: sudo dpkg -i installer/msp-realestate_1.0_amd64.deb"
    
elif [ "$PACKAGE_TYPE" = "dmg" ]; then
    echo "Creating macOS DMG installer..."
    mkdir -p installer
    
    jpackage \
      --input . \
      --name MSPRealEstate \
      --main-jar msp-realestate-1.0-SNAPSHOT.jar \
      --main-class com.msp.realestate.MSPRealEstateSystem \
      --type dmg \
      --dest installer \
      --vendor "MSP Real Estate" \
      --app-version 1.0 \
      --description "MSP Real Estate System - Java 21 Desktop Application"
    
    if [ $? -ne 0 ]; then
        echo "ERROR: jpackage DMG build failed."
        exit 1
    fi
    
    echo ""
    echo "SUCCESS! macOS DMG installer created:"
    echo "Location: $PWD/installer/MSPRealEstate-1.0.dmg"
    
else
    echo "ERROR: Invalid package type '$PACKAGE_TYPE'"
    echo "Valid options: app-image, deb, dmg"
    exit 1
fi

echo ""
echo "==================================================================="
echo "To distribute the package:"
echo "  1. Locate the package file"
echo "  2. Share with end users"
echo "  3. End users follow installation instructions"
echo "==================================================================="
