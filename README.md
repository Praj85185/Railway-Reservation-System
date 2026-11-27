# Railway Reservation System (Java Console Project)

A moderate-level, Object-Oriented **Railway Reservation System** built purely in Java. It features a dual-login portal (**Admin** & **User**), multi-passenger PNR ticket generation, Sleeper (`SL`) and Air-Conditioned (`AC`) coach classes, berth allocation, fare concessions, ticket cancellation with refund calculation, and automatic file persistence.

---

## Key Features

### 1. Authentication & Role-Based Access
- **Admin Login**: Manage train schedules, fares, seat capacities, revenue reports, and train-wise passenger manifests.
- **User Login & Registration**: Passengers can register a new account or log in to search trains, check availability, book multi-passenger tickets, view booking history, and cancel tickets.
- **Quick Public Inquiry**: View train schedules and check PNR status directly from the main menu.

### 2. Admin Module
- View all trains with real-time Sleeper (`SL`) and AC (`AC`) seat occupancy.
- Add new trains with source, destination, timings, coach capacities, and fares.
- Update train timings, fares, or seat capacities.
- Remove a train from the schedule.
- View system-wide booked tickets and active revenue summary.
- Generate a train-wise confirmed **Passenger Chart / Manifest**.

### 3. Passenger (User) Module
- View all available trains or search trains by **Source & Destination**.
- Check class-wise seat availability (`SL` and `AC`) and fares.
- **Book Ticket**:
  - Choose Travel Date and Coach Class (`SL` or `AC`).
  - Add **1 to 6 passengers** under a single 6-digit **PNR Number**.
  - Automatic seat numbering (`SL-1`, `AC-1`, etc.) and berth assignment (`Lower`, `Middle`, `Upper`, `Side Lower`, `Side Upper`).
  - Automatic **Child Concession (50% off for under 5 yrs)** and **Senior Citizen Concession (25% off for 60+ yrs)**.
  - Simulated payment modes (`UPI`, `Card`, `NetBanking`) and formatted **E-Ticket** receipt.
- **Cancel Ticket**: Restores booked seats back to the train's available pool and calculates the refund (after a standard 15% cancellation fee).

---

## Project Structure

- `RailwayReservationSystem.java` — Main entry point, menus, Admin/User workflows, and file persistence (`railway_data.ser`).
- `User.java` — Model class representing Admin and Passenger accounts.
- `Train.java` — Model class representing train routes, timings, capacities, and fares.
- `Passenger.java` — Model class storing individual passenger details, seat numbers, and berth types.
- `Ticket.java` — Model class representing a PNR booking with passenger list, fare, and status.

---

## How to Compile & Run

```bash
javac *.java
java RailwayReservationSystem
```

### Default Login Credentials
| Role | Username | Password |
| :--- | :--- | :--- |
| **Admin** | `admin` | `admin123` |
| **User** | `user` | `user123` |
