# PowerShell dev run script
# Usage: Execute in the project root. Requires Docker.

param (
    [string]$dbUser = "root",
    [string]$dbPassword = "Abhi@62348",
    [int]$dbPort = 3306,
    [string]$dbName = "msp_realestate",
    [switch]$skipDocker
)

function Write-Info($m) { Write-Host "[INFO] $m" -ForegroundColor Cyan }
function Write-Warn($m) { Write-Host "[WARN] $m" -ForegroundColor Yellow }
function Write-Err($m) { Write-Host "[ERR] $m" -ForegroundColor Red }

$projectRoot = (Get-Location).Path
Write-Info "Project root: $projectRoot"

if (-not $skipDocker) {
    # Start MySQL container (if not running)
    $container = docker ps -a --filter "name=mysql" --format "{{.Names}}" 2>$null | Select-Object -First 1
    if (-not $container) {
        Write-Info "Starting MySQL Docker container with name 'mysql'..."
        docker run -d --name mysql -p ${dbPort}:3306 -e MYSQL_ROOT_PASSWORD=$dbPassword -e MYSQL_DATABASE=$dbName mysql:8.0 | Out-Null
        Start-Sleep -Seconds 6
    } else {
        $status = docker inspect -f '{{.State.Status}}' mysql 2>$null
        if ($status -eq 'running') {
            Write-Info "MySQL container already running"
        } else {
            Write-Info "Starting existing MySQL container..."
            docker start mysql | Out-Null
            Start-Sleep -Seconds 3
        }
    }
}

# Create/overwrite database.properties with MySQL config
$dbProps = @"
# Database Configuration
DB_TYPE=mysql
MYSQL_HOST=localhost
MYSQL_PORT=${dbPort}
MYSQL_DATABASE=${dbName}
MYSQL_USER=${dbUser}
MYSQL_PASSWORD=${dbPassword}

# Connection Pool Settings
MAX_POOL_SIZE=10
MIN_POOL_SIZE=5
CONNECTION_TIMEOUT=30000
IDLE_TIMEOUT=600000
"@

Write-Info "Writing database.properties with MySQL settings"
Set-Content -Path "$projectRoot\database.properties" -Value $dbProps -Force

# Build & copy dependencies (no shaded jar in dev build)
Write-Info "Building project and copying runtime dependencies (no shaded jar by default)"
mvn -DskipTests package "-Dcreate.shaded=false"
mvn org.apache.maven.plugins:maven-dependency-plugin:3.3.0:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/dependency

# Run DB health check
Write-Info "Running DBHealthCheck..."
java -cp "target\classes;target\dependency\*" com.msp.realestate.DBHealthCheck

Write-Info "To run the GUI app now, run: java -cp \"target\\classes;target\\dependency\\*\" com.msp.realestate.MSPRealEstateSystem"
