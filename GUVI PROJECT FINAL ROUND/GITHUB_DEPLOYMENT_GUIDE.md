# GitHub Deployment Guide

## Prerequisites

Before pushing to GitHub, you need:
1. **Git** installed on your system
2. **GitHub account** with repository created
3. **Git credentials** configured (SSH keys or personal access token)

---

## Step 1: Install Git

### Windows
Download Git from: https://git-scm.com/download/win

1. Run the installer (Git-2.x.x-64-bit.exe)
2. Follow the installation wizard with default settings
3. Restart your terminal/command prompt

### Verify Installation
After installation, verify Git is available:
```bash
git --version
```

Expected output:
```
git version 2.x.x
```

---

## Step 2: Create GitHub Repository

1. Go to https://github.com/new
2. **Repository name:** `msp-realestate-system`
3. **Description:** "MSP Real Estate Management System - Java 21 LTS with Swing GUI and SQLite Database"
4. **Visibility:** Public (for CI/CD, can make private later)
5. **Initialize with:** Do NOT initialize with README (we have one)
6. Click **Create repository**

You'll get a repository URL like:
```
https://github.com/YOUR_USERNAME/msp-realestate-system.git
```
or
```
git@github.com:YOUR_USERNAME/msp-realestate-system.git
```

---

## Step 3: Configure Git Locally

Navigate to project root and run:

### Set Your Identity
```bash
cd "C:\Users\Abhi Nandan Pandey\OneDrive\Desktop\GUVI PROJECT"

git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

### Initialize Repository
```bash
git init
```

### Add Remote Origin
Replace `YOUR_USERNAME` with your GitHub username:
```bash
git remote add origin https://github.com/YOUR_USERNAME/msp-realestate-system.git
```

Or if using SSH keys:
```bash
git remote add origin git@github.com:YOUR_USERNAME/msp-realestate-system.git
```

### Verify Remote
```bash
git remote -v
```

---

## Step 4: Create .gitignore

Create a `.gitignore` file to exclude build artifacts:

```bash
# In project root, create .gitignore with this content:
```

```
# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.backup
release.properties
dependency-reduced-pom.xml
.flattened-pom.xml

# Database
*.db
*.sqlite

# IDE
.vscode/
.idea/
*.iml
*.iws
*.ipr
.classpath
.project
.settings/
.DS_Store

# OS
Thumbs.db
.DS_Store
*.swp
*.swo
*~

# Backup
backup/
*.backup
*.bak

# Java
*.class
*.jar (except pom artifacts)
*.log
```

---

## Step 5: Stage All Changes

```bash
# Add all files to staging area
git add .

# Verify staged files
git status
```

Expected output shows:
- Modified: pom.xml, README.md
- Untracked: .gitignore, CHANGELOG.md, INSTALLER.md, QA_SIGN_OFF.md, build-installer.bat, build-installer.sh, .github/workflows/maven.yml, src/test/java/...

---

## Step 6: Commit Changes

```bash
git commit -m "Upgrade to Java 21 LTS with JUnit 5, fat JAR packaging, installers, and CI/CD"
```

Or more detailed:
```bash
git commit -m "Upgrade MSP Real Estate System to Java 21 LTS

Features:
- Java 21 LTS compilation with Maven compiler plugin (release=21)
- JUnit 5 testing framework with 6 unit tests for User model
- Maven Shade Plugin for fat JAR creation (13 MB with bundled dependencies)
- Cross-platform installer support (Windows MSI/EXE, Linux DEB, macOS DMG)
- GitHub Actions CI/CD pipeline for automated build and test
- Comprehensive documentation (README, CHANGELOG, INSTALLER guide)
- QA sign-off and production readiness verification

Files changed:
- pom.xml: Updated Java version, added test and packaging plugins
- README.md: Enhanced with build instructions and troubleshooting
- New files: CHANGELOG.md, INSTALLER.md, QA_SIGN_OFF.md
- New scripts: build-installer.bat, build-installer.sh
- New CI/CD: .github/workflows/maven.yml
- New tests: src/test/java/com/msp/realestate/UserTest.java (6 tests)
- New config: .gitignore

All tests passing (6/6), application verified running, database operational."
```

---

## Step 7: Push to GitHub

### First Push (Create Remote Branch)
```bash
git branch -M main
git push -u origin main
```

The `-u` flag sets the upstream tracking, so future pushes only need `git push`.

### Subsequent Pushes
```bash
git push
```

---

## Step 8: Verify Push on GitHub

1. Go to https://github.com/YOUR_USERNAME/msp-realestate-system
2. Verify all files appear in the repository
3. Check that GitHub Actions workflow is detected (under "Actions" tab)
4. GitHub Actions should automatically trigger a build

---

## Step 9: Monitor GitHub Actions

After push:

1. Click on **Actions** tab on your GitHub repository
2. Look for workflow run named "Java CI with Maven"
3. Workflow will execute:
   - Checkout code on Ubuntu runner
   - Setup JDK 21 (Temurin)
   - Run `mvn clean package` (build)
   - Run `mvn test` (6 unit tests)
   - Publish test results
   - Upload JAR artifacts

Expected result: ✅ **All green checks** (build success, tests passed)

---

## Alternative: GitHub Web Upload (No Git Required)

If you don't want to install Git, you can manually upload files via GitHub web interface:

1. Create repository on GitHub (same as Step 2)
2. Click "Add file" → "Upload files"
3. Drag and drop project files
4. Create initial commit

**Limitations:** This method doesn't preserve full Git history and is slower for large projects.

---

## Complete Git Commands Summary

```bash
# Navigate to project
cd "C:\Users\Abhi Nandan Pandey\OneDrive\Desktop\GUVI PROJECT"

# Configure Git (first time only)
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Initialize repository
git init

# Add remote origin (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/msp-realestate-system.git

# Create .gitignore (see Step 4)
# ... create file with content above ...

# Stage all changes
git add .

# Commit
git commit -m "Upgrade to Java 21 LTS with JUnit 5, fat JAR packaging, installers, and CI/CD"

# Create main branch and push
git branch -M main
git push -u origin main

# Verify on GitHub
# Go to https://github.com/YOUR_USERNAME/msp-realestate-system
```

---

## Troubleshooting

### "fatal: not a git repository"
**Solution:** Run `git init` first

### "Permission denied" or "Could not resolve host"
**Solution:** Check GitHub authentication:
- For HTTPS: Verify your GitHub token/password
- For SSH: Generate SSH keys: `ssh-keygen -t ed25519 -C "your.email@example.com"`

### Large files rejected
**Solution:** Make sure .gitignore excludes `target/`, `*.db` files

### Workflow not triggering
**Solution:** 
1. Verify `.github/workflows/maven.yml` is in repository
2. Go to Actions → Enable workflows if disabled
3. Check for YAML syntax errors

---

## Next Steps After Push

1. **Monitor CI/CD:** Watch GitHub Actions workflow complete (5-10 minutes)
2. **Verify Build:** Check build badge appears in README
3. **Release Builds:** Create release with fat JAR as artifact
4. **Documentation:** Update project description on GitHub
5. **Share:** Repository is ready for team collaboration

---

## GitHub Repository Structure

After successful push, your repository will have:

```
msp-realestate-system/
├── .github/
│   └── workflows/
│       └── maven.yml              # CI/CD pipeline
├── src/
│   ├── main/java/com/msp/realestate/
│   │   ├── DashboardFrame.java
│   │   ├── DashboardPanel.java
│   │   ├── DatabaseManager.java
│   │   ├── DocumentsPanel.java
│   │   ├── LoginFrame.java
│   │   ├── MSPRealEstateSystem.java
│   │   ├── PropertiesPanel.java
│   │   ├── SignupFrame.java
│   │   ├── User.java
│   │   └── UsersPanel.java
│   └── test/java/com/msp/realestate/
│       └── UserTest.java           # 6 unit tests
├── .gitignore
├── pom.xml                         # Java 21, plugins, dependencies
├── README.md                       # Comprehensive guide
├── CHANGELOG.md                    # Release notes
├── INSTALLER.md                    # Packaging guide
├── QA_SIGN_OFF.md                  # QA verification report
├── build-installer.bat             # Windows MSI/EXE builder
└── build-installer.sh              # Linux/macOS DEB/DMG builder
```

---

## Final Verification Checklist

After push to GitHub:

- [ ] Repository appears on GitHub
- [ ] All source files visible
- [ ] README.md displays correctly
- [ ] GitHub Actions workflow detected
- [ ] CI/CD build triggered automatically
- [ ] Build completed successfully (✅ green check)
- [ ] 6 unit tests passed in CI/CD
- [ ] Test results published
- [ ] JAR artifact uploaded to Actions
- [ ] Build badge in README is live

---

## Support Resources

- **Git Documentation:** https://git-scm.com/book/en/v2
- **GitHub Help:** https://docs.github.com/en
- **GitHub Actions Docs:** https://docs.github.com/en/actions
- **Maven Documentation:** https://maven.apache.org/guides/
- **JUnit 5 Documentation:** https://junit.org/junit5/docs/

---

## Quick Reference: Push in 5 Steps

```bash
# 1. Install Git from https://git-scm.com/download/win

# 2. Navigate to project
cd "C:\Users\Abhi Nandan Pandey\OneDrive\Desktop\GUVI PROJECT"

# 3. Initialize and configure
git init
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
git remote add origin https://github.com/YOUR_USERNAME/msp-realestate-system.git

# 4. Commit changes
git add .
git commit -m "Upgrade to Java 21 LTS with JUnit 5, fat JAR, installers, and CI/CD"

# 5. Push to GitHub
git branch -M main
git push -u origin main
```

---

**Status:** Ready to deploy. Follow steps above to push to GitHub and trigger CI/CD pipeline.

**Estimated Time:** 15-30 minutes (including Git installation)

**Expected Result:** ✅ Automated build + tests on GitHub Actions within 5-10 minutes of push
