# Java Survey System with K-Means Clustering 📊

Developed as part of the Software Engineering course at UPC.

## 📖 Overview
A robust software solution developed using a **Three-Tier Architecture** (Presentation, Domain, Persistence) to ensure modularity and scalability. The system manages the complete lifecycle of sociological surveys: from creation and data collection to statistical analysis.

The core strength of the application lies in its ability to process massive real-world datasets (such as CIS Barometers) and identification of patterns in public opinion.
## ✨ Key Features

* **Role-Based Access Control (RBAC):** Hierarchical permission system managing different levels of access for Admins, Moderators, and Surveyors.
* **Big Data Import:** Optimized parsing logic capable of handling large text datasets (50k+ lines), including robust error handling for corrupt data and formatting inconsistencies.
* **Algorithmic Analysis:** Custom implementation of the **K-Means Clustering** algorithm to segment respondents into distinct profiles based on their answers.
* **Data Persistence:** JSON-based storage system for users and surveys, ensuring data integrity between sessions without requiring an external SQL database.
* **Interactive GUI:** A complete desktop interface developed with Java Swing/AWT.

## 🏗️ Technical Architecture

The project follows a strict separation of concerns:

1.  **Presentation Layer (`src/presentacio`):** Handles all Views and UI Controllers.
2.  **Domain Layer (`src/domini`):** Contains business logic, the K-Means algorithm, and data parsing rules.
3.  **Persistence Layer (`src/persistencia`):** Manages file I/O (JSON/TXT readers and writers).

## 🚀 Getting Started

### Prerequisites
* Java Development Kit (JDK) 11 or higher.
* Any Java IDE (IntelliJ IDEA, Eclipse, NetBeans).

### Installation
1.  Clone the repository:
    ```bash
    git clone [https://github.com/tu-usuario/tu-repo.git](https://github.com/tu-usuario/tu-repo.git)
    ```
2.  Open the project in your IDE.
3.  Ensure the `DATA` folder is in the root directory (contains `respuestasCis.txt`, etc.).
4.  Run the Main class to start the application.

### Makefile alternative
1.  Execute "make".
2.  Execute "make run" and a window will pop up.

## 📂 Project Structure

```text
src/
├── domini/          # Core logic (User, Survey, Question classes)
│   ├── algorithms/  # K-Means implementation
│   └── controllers/ # Domain controllers
├── persistencia/    # Data Access Objects (DAO) & File Managers
└── presentacio/     # Java Swing Views & UI Controllers
data/                # Storage for JSON and TXT files
