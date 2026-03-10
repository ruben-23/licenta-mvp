# JobsMVP Application

This guide provides instructions on how to set up and run the JobsMVP application, including populating the Knowledge Graph and starting the Spring Boot backend.

## Prerequisites

*   **Java 17** or higher
*   **Python 3.14** or higher
*   **Memgraph** (or Neo4j) running locally on port `7687`
*   **Maven** (wrapper included)

## Configuration

### 1. Database
Ensure your Memgraph/Neo4j instance is running. The application expects the database to be accessible at `bolt://localhost:7687` with no authentication by default.

If you have a username and password, update the following files:
*   `KG_Ingestion/main.py`: Update `USER` and `PASSWORD` variables.
*   `src/main/resources/application.properties`: Update `spring.neo4j.authentication.username` and `spring.neo4j.authentication.password`.

### 2. API Keys
The Spring Boot application requires a Gemini API key. You can set this as an environment variable:

*   **Linux/macOS**: `export GEMINI_API_KEY=your_api_key_here`
*   **Windows (PowerShell)**: `$env:GEMINI_API_KEY="your_api_key_here"`
*   **Windows (CMD)**: `set GEMINI_API_KEY=your_api_key_here`

Alternatively, you can directly edit `src/main/resources/application.properties` and replace `${GEMINI_API_KEY}` with your actual key (not recommended for committed code).

## Step 1: Populate the Knowledge Graph

Before running the backend, you need to ingest the initial data into the graph database.

1.  Navigate to the `KG_Ingestion` directory:
    ```bash
    cd KG_Ingestion
    ```

2.  Install the required Python dependencies. It is recommended to use a virtual environment.
    
    Using `pip`:
    ```bash
    pip install neo4j
    ```
    
    Or if you are using `uv`:
    ```bash
    uv sync
    ```

3.  Run the ingestion script:
    ```bash
    python main.py
    ```
    
    This script reads `job.json` and `student.json` and populates the database. You should see success messages for inserted jobs and students.

## Step 2: Start the Spring Boot Application

Once the database is populated, you can start the backend service.

1.  Return to the project root directory:
    ```bash
    cd ..
    ```

2.  Run the application using the Maven wrapper:

    **Linux/macOS**:
    ```bash
    ./mvnw spring-boot:run
    ```

    **Windows**:
    ```bash
    ./mvnw.cmd spring-boot:run
    ```

    *Note: Ensure the `GEMINI_API_KEY` environment variable is set before running this command.*

3.  The application will start on port `8080` (default). You can access the frontend at:
    [http://localhost:8080](http://localhost:8080)
