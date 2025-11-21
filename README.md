# Restful-Booker API Testing using Rest Assured

This project contains automated API tests for the **Restful-Booker** API using **Java**, **Rest Assured**, and **TestNG**.

## 🧪 Project Purpose
The goal of this project is to practice and demonstrate:
- REST API automation using Rest Assured
- Handling GET, POST, PUT, PATCH, DELETE requests
- TestNG assertions and reporting
- Request/response specifications
- Handling dynamic data (path parameters, query parameters, JSON bodies)

The public API used for testing:
👉 https://restful-booker.herokuapp.com

---

## 📦 Tech Stack
- **Java 21**
- **Rest Assured**
- **TestNG**
- **Maven**
- **Allure**
- **Log4j**

---

## 🚀 How to Run the Tests
### Using Maven:
Run the following command in terminal:

```bash
mvn clean test -DsuiteXmlFile=testNg.xml 
```
## 📊 Test Reports
After running the tests, you can generate Allure reports with:

```bash
allure generate --clean --single-file
allure open
```
