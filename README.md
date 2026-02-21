# Hall Booking Management System (Java Swing + TXT storage)

This repository now contains a coursework-ready **Object-Oriented Programming** starter implementation for the Hall Symphony booking scenario.

## Implemented modules
- **Authentication**: login + customer registration.
- **Role dashboards**:
  - Scheduler: add/view/delete halls.
  - Customer: view halls, book hall, view own bookings.
  - Administrator: view users, block user, view all bookings.
  - Manager: view total sales, view/update issues.
- **Persistence**: all data stored in plain text files under `data/` (`users.txt`, `halls.txt`, `bookings.txt`, `issues.txt`).

## OOP concepts used
- **Encapsulation** in all domain models (`User`, `Hall`, `Booking`, `Issue`).
- **Inheritance** with user subclasses (`Customer`, `Scheduler`, `Administrator`, `Manager`).
- **Polymorphism/abstraction** via `User` base type and role-specific behavior routing in UI.
- **Composition** in services/repositories and `AppContext` dependency wiring.

## Project structure
- `src/com/hallsymphony/model`: entities and enums.
- `src/com/hallsymphony/repo`: text-file repositories.
- `src/com/hallsymphony/service`: business rules.
- `src/com/hallsymphony/ui`: Swing UI frames.
- `src/com/hallsymphony/Main.java`: entry point.

## Compile and run
```bash
javac -d out $(find src -name "*.java")
java -cp out com.hallsymphony.Main
```

## Default staff accounts
- Scheduler: `scheduler` / `pass123`
- Administrator: `admin` / `pass123`
- Manager: `manager` / `pass123`

Customers can self-register from the login window.

## Notes
This version is a practical baseline to accelerate assignment completion. You can extend it with:
- richer hall availability/maintenance schedules,
- booking conflict validation,
- weekly/monthly/yearly filtered sales reports,
- full CRUD and search/filter UI for every role,
- enhanced receipt and reporting screens.
