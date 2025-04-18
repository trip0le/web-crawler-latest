# Web Crawler

## Problem Statement

### Goal:
Implement a mini-web app following the architecture described below. The app will have a React frontend with two pages:

1. **Summarize Websites Page**: This page will allow the user to input two website URLs. Upon submitting, the app will summarize the content of both websites. The functionality will be similar to asking ChatGPT to “Summarize http://example.com”.
   
2. **History Page**: This page will display the history of previous summarization requests. Each entry in the history will be deletable.

### Architecture:
This project is built using the following architecture:

![Architecture Diagram](https://github.com/user-attachments/assets/9d053705-1c69-41a3-bfef-90f6a5252a27)

### Working Demo:
[Click here for the working demo](https://drive.google.com/drive/u/1/folders/16pq_kv69E5QTsnHtIvNexBjASmYWIUwO)

---

## Approach

This project is built with a multi-service architecture. Each service is responsible for a specific task, and they communicate with each other to provide the final output.

### 0. DB Setup
After a summarize request is hit from the frontend, the request details is inserted in the db and later fetched in request history.

### 1. **Backend - Java Spring Boot**
   - **Build Tool**: Gradle
   - **Purpose**: A simple Spring Boot API server that will handle API requests from the frontend.
   - **Responsibilities**:
     - It will handle all incoming API requests, such as URL submissions for summarization.
     - It will call the Scala library to perform the summarization and return the result to the frontend.
   
### 2. **Scala Library**
   - **Build Tool**: Gradle
   - **Purpose**: This Scala service will be used as a library, which will be called from the Java Spring Boot server.
   - **Responsibilities**:
     - Perform the logic to summarize the content of the URLs.
     - Connect to a Postgres database to log all request histories (such as URL summaries).
   
### 3. **Python FastAPI**
   - **Purpose**: This service will call an OpenAI or any other LLM (Large Language Model) endpoint to summarize the content of the websites.
   - **Responsibilities**:
     - It will handle the request to summarize the content of the URLs.
     - It will query the OpenAI or LLM API and return the summarized text back to the backend.
   
### 4. **React Frontend**
   - **Purpose**: This is the user-facing part of the application.
   - **Responsibilities**:
     - **Summarize Websites Page**: This page will have a simple form where users can input URLs (e.g., LinkedIn URLs or any website).
     - **History Page**: This page will display the history of previous summarization requests. Users can view and delete past entries.

### Workflow:
1. The user submits a LinkedIn URL or any website URL from the React frontend.
2. The backend (Java Spring Boot) receives the URL and forwards the request to the Scala library for summarization.
3. The Scala library processes the request, interacts with the Postgres database to log the history, and fetches the summary by calling the Python FastAPI service.
4. The Python service calls the OpenAI (or any other LLM) endpoint to summarize the content of the website and returns the result.
5. The backend sends the summary back to the React frontend.
6. The frontend displays the summarized content to the user, and the history page shows the previously processed summaries.

---

## How to Run the Project

This project consists of multiple services: Python FastAPI backend, Scala library for summarization, Spring Boot service, and React frontend. Below are the steps to run each part of the project.


### 1. **DB Setup**

Before running any services, you need to set up PostgreSQL and create the necessary table to store request history.

#### 1.1 **Install PostgreSQL**

If you haven’t already installed PostgreSQL, you can follow the official installation guide for your operating system:
- [PostgreSQL Installation Guide](https://www.postgresql.org/download/)

#### 1.2 **Create the `summarize_logs` Table**

Once PostgreSQL is installed, follow these steps to create the required table:

1. Log in to the PostgreSQL database with the following command:
    ```bash
    psql -U postgres
    ```

2. Create a new database `summarizer_db`:
    ```sql
    CREATE DATABASE summarizer_db;
    ```

3. Connect to the `summarizer_db` database:
    ```sql
    \c summarizer_db
    ```

4. Create the `summarize_logs` table with the following SQL:
    ```sql
    CREATE TABLE summarize_logs (
        id SERIAL PRIMARY KEY,
        url VARCHAR(255) NOT NULL,
        summary TEXT NOT NULL,
        created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
    );
    ```
Example of configuration in a `application.conf` file of the scala library:

```application.conf
db {
  url = "jdbc:postgresql://localhost:5432/summarizer_db"
  user = "postgres"
  password = "123456"
}
```

(Make sure the password for the 'postgres' user you created is same as the one you enter in the application.conf.)

### 1. **Run Python FastAPI**

To run the FastAPI backend:

1. Navigate to the `/python-fast-api` directory.
2. Create a `.env` file and add your Hugging Face API key in the following format:
    ```env
    HUGGINGFACE_API_KEY=your_api_key
    ```
3. Run the FastAPI server with the following command:
    ```bash
    uvicorn main:app --reload
    ```

### 2. **Build Scala Summarizer Library**

To build the Scala summarizer library:

1. Navigate to the `/scala-summarizer-lib` directory.
2. Run the following command to build the Scala library:
    ```bash
    gradle clean build fatjar
    ```
3. After the build is complete, copy the `/libs` folder created inside `/build` to the `/spring-summarizer-app` directory.

### 3. **Run Spring Boot Service**

To run the Spring Boot service:

1. Navigate to the `/spring-summarizer-app` directory.
2. Run the Spring Boot application with the following command:
    ```bash
    gradle bootRun
    ```

### 4. **Run React Frontend Server**

To run the React frontend:

1. Navigate to the `/summarizer-frontend` directory.
2. Install the required dependencies by running:
    ```bash
    npm install
    ```
3. Once the dependencies are installed, start the React development server with:
    ```bash
    npm start
    ```

---

## Troubleshooting

If you encounter any issues, ensure that:

- All services are running on their respective ports.
- The `.env` file is correctly configured with the Hugging Face API key.
- The Scala library is successfully built and copied to the Spring Boot application.

For additional help, please refer to the documentation or open an issue in the repository.

---
