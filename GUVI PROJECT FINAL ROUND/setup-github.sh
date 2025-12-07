#!/bin/bash
# Quick Git Setup and Push Script for Linux/macOS
# This script automates the Git setup and push to GitHub

echo "========================================"
echo "MSP Real Estate System - GitHub Setup"
echo "========================================"
echo ""

# Check if Git is installed
if ! command -v git &> /dev/null; then
    echo "[ERROR] Git is not installed"
    echo ""
    echo "Install Git:"
    echo "  macOS: brew install git"
    echo "  Linux: sudo apt-get install git"
    echo ""
    exit 1
fi

echo "[OK] Git found: $(git --version)"
echo ""

# Navigate to project directory
cd "$(dirname "$0")"

echo ""
echo "========================================"
echo "Step 1: Configure Git Identity"
echo "========================================"
echo ""
read -p "Enter your full name (e.g., John Doe): " GIT_NAME
read -p "Enter your email (e.g., john@example.com): " GIT_EMAIL

git config --global user.name "$GIT_NAME"
git config --global user.email "$GIT_EMAIL"

echo "[OK] Git identity configured"
echo ""

# Check if already a git repository
if [ -d .git ]; then
    echo "[OK] Git repository already initialized"
else
    echo "[INIT] Initializing Git repository..."
    git init
    echo "[OK] Repository initialized"
fi

echo ""
echo "========================================"
echo "Step 2: Configure GitHub Remote"
echo "========================================"
echo ""
read -p "Enter your GitHub username: " GITHUB_USERNAME
read -p "Enter repository name (default: msp-realestate-system): " GITHUB_REPO

if [ -z "$GITHUB_REPO" ]; then
    GITHUB_REPO="msp-realestate-system"
fi

GITHUB_URL="https://github.com/$GITHUB_USERNAME/$GITHUB_REPO.git"

# Remove existing remote if it exists
git remote remove origin 2>/dev/null

# Add new remote
git remote add origin "$GITHUB_URL"

echo "[OK] Remote configured: $GITHUB_URL"
echo ""

# Verify remote
echo "[VERIFY] Remote configuration:"
git remote -v
echo ""

echo "========================================"
echo "Step 3: Stage Files"
echo "========================================"
echo ""
git add .

echo "[OK] Files staged"
echo ""
echo "[STATUS] Current git status:"
git status
echo ""

echo "========================================"
echo "Step 4: Create Commit"
echo "========================================"
echo ""
git commit -m "Upgrade MSP Real Estate System to Java 21 LTS

Features:
- Java 21 LTS compilation with Maven compiler plugin (release=21)
- JUnit 5 testing framework with 6 unit tests for User model
- Maven Shade Plugin for fat JAR creation (13 MB with bundled dependencies)
- Cross-platform installer support (Windows MSI/EXE, Linux DEB, macOS DMG)
- GitHub Actions CI/CD pipeline for automated build and test
- Comprehensive documentation (README, CHANGELOG, INSTALLER guide)
- QA sign-off and production readiness verification

Build Verified:
- All 6 unit tests passing
- Application GUI launches successfully
- SQLite database operational
- Fat JAR executable (13 MB)
- GitHub Actions workflow configured"

if [ $? -ne 0 ]; then
    echo "[ERROR] Commit failed"
    exit 1
fi

echo "[OK] Changes committed"
echo ""

echo "========================================"
echo "Step 5: Push to GitHub"
echo "========================================"
echo ""
echo "[NOTE] Setting up main branch..."
git branch -M main

echo "[PUSH] Pushing to GitHub..."
echo "[NOTE] You may be prompted for authentication"
echo ""

git push -u origin main

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Push failed. Possible reasons:"
    echo "- Invalid GitHub credentials"
    echo "- Repository doesn't exist on GitHub"
    echo "- Network connectivity issue"
    echo ""
    echo "[HELP] To create the repository:"
    echo "1. Go to https://github.com/new"
    echo "2. Enter repository name: $GITHUB_REPO"
    echo "3. Create repository (DO NOT initialize with README)"
    echo "4. Run this script again"
    echo ""
    exit 1
fi

echo ""
echo "========================================"
echo "[SUCCESS] Push Complete!"
echo "========================================"
echo ""
echo "Repository: $GITHUB_URL"
echo "Branch: main"
echo ""
echo "[NEXT STEPS]"
echo "1. Go to: https://github.com/$GITHUB_USERNAME/$GITHUB_REPO"
echo "2. Check the \"Actions\" tab to monitor CI/CD build"
echo "3. Wait for the Maven workflow to complete (5-10 minutes)"
echo "4. Verify all tests passed (6/6)"
echo ""
echo "[INFO] Your changes are now on GitHub and CI/CD will:"
echo "- Build with Java 21"
echo "- Run all unit tests"
echo "- Upload JAR artifacts"
echo "- Publish test results"
echo ""
