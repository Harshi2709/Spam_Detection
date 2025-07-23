# 📧 Spam Detection System (Java + MySQL)

A desktop application for detecting, managing, and analyzing spam emails using Java Swing and MySQL database integration. This project allows user registration, login, spam message categorization, and CRUD operations on email records.

---

## 🚀 Features

- 🧾 **User Login & Registration**
- 📥 **Inbox View** with spam tagging
- 🗑️ **Delete Emails** by ID
- ✏️ **Update Spam Type, Message, and Subject**
- 📊 **Analyze Most Frequent Spam Type**
- 📋 **Display Tables**: Inbox, Blocked Users, User Profile
- 🎨 **User-Friendly GUI** with Java Swing

---

## 🛠️ Tech Stack

| Technology        | Description                         |
|------------------|-------------------------------------|
| Java             | Frontend using Java Swing           |
| MySQL            | Backend relational database         |
| JDBC             | Java Database Connectivity (JDBC)   |
| Maven            | Build automation and dependency management |
| GitHub           | Version Control                     |

---

## 💾 Database Structure

The database name is `spam_detection` and includes the following tables:

- `inbox (I_ID, From_Mail, To_Mail, Day)`
- `message (ID, Content, Subject)`
- `spam (I_ID, IS_SPAM, Type)`
- `user_profile (Email, Name, DOB, Phone)`
- `login (Email, Password)`
- `blocked (Email, Name)`

You can import the schema using the provided `spam_detection.sql` file.

---

## 🔧 How to Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/Spam_Detection.git
   cd Spam_Detection
