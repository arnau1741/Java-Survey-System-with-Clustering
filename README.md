# Survey Management System 📊

A Java application for designing, conducting, and analyzing surveys.

## 🚀 Key Features
- **Survey Design:** Create surveys with multiple question types (Single choice, Multiple choice, Free text).
- **Data Import:** Validated import of massive datasets (compatible with CIS format).
- **Cluster Analysis:** Integrated **K-Means, K-Means++ and K-Medoids algorithms** to group respondents by similarity.
- **User Management:** Secure login and role management (Admin, Moderator, etc.).

## 🛠️ Tech Stack
- **Language:** Java (JDK 11+)
- **Architecture:** 3-Layer (Presentation, Domain, Persistence)
- **GUI:** Java Swing / AWT
- **Data:** JSON & Raw Text processing

## 📂 Project Structure
- `src/domini`: Core logic and algorithms (K-Means, Data parsing).
- `src/persistencia`: File handling (JSON/TXT readers).
- `src/presentacio`: Views and UI Controllers.
