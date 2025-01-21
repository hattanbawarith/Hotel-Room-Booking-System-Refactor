# Hotel Room Booking System Refactor

A Java-based hotel room booking system that implements multiple **Software Design Patterns** to enhance modularity, scalability, and maintainability. The system supports room management, pricing strategies, customer notifications, and state management.

---

## **Key Features**
- **Room Management:**  
  Book, cancel, and manage the state of rooms (Available, Reserved, Occupied).
- **Dynamic Pricing:**  
  Integrates pricing strategies for standard and discounted rates using the **Bridge Pattern**.
- **Customizable Services:**  
  Allows adding features like Wi-Fi and Breakfast dynamically using the **Decorator Pattern**.
- **Customer Notifications:**  
  Notifies customers about room availability using the **Observer Pattern**.
- **Room States:**  
  Manages room lifecycle with states such as Available, Reserved, and Occupied using the **State Pattern**.

---

## **Software Design Patterns Implemented**
### **Creational Patterns**
- **Singleton:** Ensures a single instance of the hotel management system.
- **Factory:** Dynamically creates various room types (e.g., Luxury Double, Deluxe Single).

### **Structural Patterns**
- **Bridge:** Separates room types from pricing strategies for flexibility.
- **Decorator:** Adds dynamic features like Wi-Fi and Breakfast to rooms.

### **Behavioral Patterns**
- **Observer:** Notifies customers of room availability changes.
- **State:** Manages the lifecycle of room states (Available, Reserved, Occupied).

---

## **How to Run**
1. Clone the repository:
   ```bash
   git clone https://github.com/hattanbawarith/Hotel-Room-Booking-System-Refactor.git
2. Navigate to the project directory:
   cd Hotel-Room-Booking-System-Refactor
3.	Compile the Java files:
    javac *.java
4.	Run the main program:
    java Main