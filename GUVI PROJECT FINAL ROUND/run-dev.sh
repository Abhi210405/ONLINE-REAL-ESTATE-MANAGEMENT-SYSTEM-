#!/usr/bin/env bash
# Unix shell dev run script (macOS / Linux) to start Docker MySQL and run DBHealthCheck
DB_USER=${1:-root}
DB_PASSWORD=${2:-Abhi@62348}
DB_PORT=${3:-3306}
DB_NAME=${4:-msp_realestate}

PROJECT_ROOT="$(pwd)"

echo "[INFO] Project root: $PROJECT_ROOT"

# Start MySQL container if not running
if [ -z "$(docker ps -a --filter "name=mysql" --format "{{.Names}}")" ]; then
  echo "[INFO] Starting MySQL Docker container..."
  docker run -d --name mysql -p ${DB_PORT}:3306 -e MYSQL_ROOT_PASSWORD=${DB_PASSWORD} -e MYSQL_DATABASE=${DB_NAME} mysql:8.0
  sleep 5
else
  echo "[INFO] MySQL container exists, starting if needed..."
  docker start mysql 2>/dev/null || true
  sleep 3
fi

# Generate database.properties
cat > database.properties << EOF
# Database Configuration
DB_TYPE=mysql
MYSQL_HOST=localhost
MYSQL_PORT=${DB_PORT}
MYSQL_DATABASE=${DB_NAME}
MYSQL_USER=${DB_USER}
MYSQL_PASSWORD=${DB_PASSWORD}

# Connection Pool Settings
MAX_POOL_SIZE=10
MIN_POOL_SIZE=5
CONNECTION_TIMEOUT=30000
IDLE_TIMEOUT=600000
EOF

# Build & copy dependencies
mvn -DskipTests package
mvn org.apache.maven.plugins:maven-dependency-plugin:3.3.0:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target/dependency

# Run the DB health check
java -cp "target/classes:target/dependency/*" com.msp.realestate.DBHealthCheck

echo "[INFO] To run the GUI app: java -cp 'target/classes:target/dependency/*' com.msp.realestate.MSPRealEstateSystem"
