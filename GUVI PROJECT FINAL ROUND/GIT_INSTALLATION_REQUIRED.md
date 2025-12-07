# Git Installation Required

To push your project to GitHub, you need Git installed on your system.

## ⚠️ Git is Not Installed

The deployment script requires Git. Follow the steps below to install it.

---

## Windows Installation

### Step 1: Download Git
1. Visit: **https://git-scm.com/download/win**
2. Download the latest **64-bit** installer (currently Git-2.x.x-64-bit.exe)

### Step 2: Install Git
1. Run the downloaded installer
2. Follow the setup wizard with these settings:
   - **Choose Install Location:** Default (`C:\Program Files\Git`) is fine
   - **Select Components:** Keep defaults (Git Bash, Git GUI, Shell Integration)
   - **Choose Default Editor:** Visual Studio Code (or your preference)
   - **Configure Git:** Use "Git from command line and also from 3rd-party software"
   - **Configure Line Endings:** Checkout Windows-style, commit Unix-style (recommended)
   - **Configure Terminal:** Use Windows' default console window
   - **Enable Git Credential Manager:** Yes (recommended)

### Step 3: Verify Installation
After installation, **close and reopen Command Prompt** (cmd.exe), then run:
```batch
git --version
```

Expected output:
```
git version 2.x.x.windows.x
```

---

## macOS Installation

### Option 1: Homebrew (Recommended)
```bash
brew install git
```

### Option 2: Official Installer
1. Visit: **https://git-scm.com/download/mac**
2. Download and run the installer

### Verify Installation
```bash
git --version
```

---

## Linux Installation

### Ubuntu/Debian
```bash
sudo apt-get update
sudo apt-get install git
```

### Fedora/RedHat
```bash
sudo dnf install git
```

### Verify Installation
```bash
git --version
```

---

## ✅ After Installation

1. **Close this terminal**
2. **Open a new Command Prompt** (cmd.exe)
3. **Run the deployment script:**

**Windows:**
```batch
cd "C:\Users\Abhi Nandan Pandey\OneDrive\Desktop\GUVI PROJECT"
setup-github.bat
```

**Linux/macOS:**
```bash
cd ~/Desktop/GUVI\ PROJECT
chmod +x setup-github.sh
./setup-github.sh
```

---

## Need Help?

If you encounter issues during Git installation:

1. **Download Failed?** → Try a different mirror: https://git-scm.com/download/
2. **Installation Fails?** → Download Visual Studio Build Tools first, then Git
3. **Path Issues?** → Ensure "Add Git to PATH during installation" is selected
4. **Permission Denied?** → Run as Administrator

---

## Alternative: Manual Git Setup (Advanced Users)

If you prefer manual git commands instead of the automated script, see:
**GITHUB_DEPLOYMENT_GUIDE.md** (Step 3: Configure Git Locally)

---

## What to Do Now

### ✅ Priority Actions:
1. **Install Git** (follow instructions above for your OS)
2. **Restart your terminal** after installation
3. **Verify Git installation** with `git --version`
4. **Run deployment script** (setup-github.bat or setup-github.sh)

### ⏱️ Time Required:
- Git Download: 2-5 minutes
- Git Installation: 2-5 minutes
- Deployment: 5-10 minutes
- **Total:** 10-20 minutes to GitHub

---

**Status:** ⏳ **AWAITING GIT INSTALLATION**

Once Git is installed, your project will deploy to GitHub automatically! 🚀
