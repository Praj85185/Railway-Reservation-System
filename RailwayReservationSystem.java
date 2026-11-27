import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Railway Reservation System - Final College Project Edition
 * Features:
 *  - Dual Login System: Admin Portal & User Portal (with Registration)
 *  - Coach Classes: Sleeper (SL) & Air Conditioned (AC) with separate capacities and fares
 *  - Multi-Passenger Booking under a single PNR (up to 6 passengers)
 *  - Automatic Berth Assignment (LB, MB, UB, SL, SU) & Senior/Child Concessions
 *  - PNR Status Inquiry, E-Ticket Display, and Cancellation with Refund Calculation
 *  - Automatic Data Persistence using Java Serialization
 */
public class RailwayReservationSystem {

    private static final String DATA_FILE = "railway_data.ser";
    private static final String[] BERTH_TYPES = {"Lower (LB)", "Middle (MB)", "Upper (UB)", "Side Lower (SL)", "Side Upper (SU)"};

    private static ArrayList<Train> trains = new ArrayList<>();
    private static ArrayList<Ticket> tickets = new ArrayList<>();
    private static ArrayList<User> users = new ArrayList<>();
    private static int pnrCounter = 100001;

    public static void main(String[] args) {
        loadData();
        Scanner scanner = new Scanner(System.in);

        printBanner();
        while (true) {
            System.out.println("\n=======================================================");
            System.out.println("          RAILWAY RESERVATION SYSTEM - MAIN MENU       ");
            System.out.println("=======================================================");
            System.out.println("  1. Admin Login");
            System.out.println("  2. User Login");
            System.out.println("  3. New User Registration");
            System.out.println("  4. View All Trains (Quick Inquiry)");
            System.out.println("  5. Check PNR / Ticket Status");
            System.out.println("  6. Exit");
            System.out.println("-------------------------------------------------------");
            int choice = readInt(scanner, "Enter your choice (1-6): ");

            switch (choice) {
                case 1:
                    handleAdminLogin(scanner);
                    break;
                case 2:
                    handleUserLogin(scanner);
                    break;
                case 3:
                    handleUserRegistration(scanner);
                    break;
                case 4:
                    displayAllTrains();
                    break;
                case 5:
                    displayTicketDetails(scanner);
                    break;
                case 6:
                    saveData();
                    System.out.println("\nThank you for using the Railway Reservation System. Safe travels!");
                    scanner.close();
                    return;
                default:
                    System.out.println("[!] Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private static void printBanner() {
        System.out.println("*******************************************************");
        System.out.println("*       WELCOME TO INDIAN RAILWAY RESERVATION         *");
        System.out.println("*     Default Admin: admin / admin123                 *");
        System.out.println("*     Default User : user  / user123                  *");
        System.out.println("*******************************************************");
    }

    // =========================================================================
    // AUTHENTICATION & REGISTRATION
    // =========================================================================

    private static void handleAdminLogin(Scanner scanner) {
        System.out.println("\n--- ADMIN LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        for (User u : users) {
            if (u.isAdmin() && u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                System.out.println("\n[SUCCESS] Welcome, Administrator " + u.getFullName() + "!");
                adminDashboard(scanner, u);
                return;
            }
        }
        System.out.println("[ERROR] Invalid Admin credentials.");
    }

    private static void handleUserLogin(Scanner scanner) {
        System.out.println("\n--- USER LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        for (User u : users) {
            if (!u.isAdmin() && u.getUsername().equalsIgnoreCase(username) && u.getPassword().equals(password)) {
                System.out.println("\n[SUCCESS] Welcome back, " + u.getFullName() + "!");
                userDashboard(scanner, u);
                return;
            }
        }
        System.out.println("[ERROR] Invalid User credentials. If you don't have an account, please register first.");
    }

    private static void handleUserRegistration(Scanner scanner) {
        System.out.println("\n--- NEW USER REGISTRATION ---");
        String username;
        while (true) {
            System.out.print("Choose a Username: ");
            username = scanner.nextLine().trim();
            if (username.isEmpty()) {
                System.out.println("[!] Username cannot be empty.");
                continue;
            }
            if (findUserByUsername(username) != null) {
                System.out.println("[!] Username already exists. Please choose another.");
                continue;
            }
            break;
        }

        String password = readNonEmptyString(scanner, "Choose a Password: ");
        String fullName = readNonEmptyString(scanner, "Enter Full Name: ");
        String phone = readNonEmptyString(scanner, "Enter 10-digit Mobile Number: ");

        User newUser = new User(username, password, fullName, phone, "USER");
        users.add(newUser);
        saveData();
        System.out.println("[SUCCESS] Account registered successfully! You can now log in via Option 2.");
    }

    private static User findUserByUsername(String username) {
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    // =========================================================================
    // ADMIN PORTAL
    // =========================================================================

    private static void adminDashboard(Scanner scanner, User admin) {
        while (true) {
            System.out.println("\n=======================================================");
            System.out.println("              ADMIN DASHBOARD (" + admin.getUsername() + ")");
            System.out.println("=======================================================");
            System.out.println("  1. View All Trains & Seat Occupancy");
            System.out.println("  2. Add New Train");
            System.out.println("  3. Update Train Schedule / Fares / Capacity");
            System.out.println("  4. Remove a Train");
            System.out.println("  5. View All Booked Tickets & Revenue Report");
            System.out.println("  6. View Passenger Chart by Train Number");
            System.out.println("  7. Logout");
            System.out.println("-------------------------------------------------------");
            int choice = readInt(scanner, "Select Admin Option (1-7): ");

            switch (choice) {
                case 1:
                    displayAllTrains();
                    break;
                case 2:
                    addTrain(scanner);
                    break;
                case 3:
                    updateTrain(scanner);
                    break;
                case 4:
                    removeTrain(scanner);
                    break;
                case 5:
                    viewAllTicketsAdmin();
                    break;
                case 6:
                    viewPassengerChartByTrain(scanner);
                    break;
                case 7:
                    saveData();
                    System.out.println("Logged out from Admin Portal.");
                    return;
                default:
                    System.out.println("[!] Invalid option. Please choose between 1 and 7.");
            }
        }
    }

    private static void addTrain(Scanner scanner) {
        System.out.println("\n--- ADD NEW TRAIN ---");
        int trainNo = readInt(scanner, "Enter New Train Number (e.g., 12951): ");
        if (findTrainByNumber(trainNo) != null) {
            System.out.println("[ERROR] A train with number " + trainNo + " already exists.");
            return;
        }

        String name = readNonEmptyString(scanner, "Enter Train Name (e.g., Rajdhani Express): ");
        String source = readNonEmptyString(scanner, "Enter Source Station: ");
        String destination = readNonEmptyString(scanner, "Enter Destination Station: ");
        String depTime = readNonEmptyString(scanner, "Enter Departure Time (HH:MM): ");
        String arrTime = readNonEmptyString(scanner, "Enter Arrival Time (HH:MM): ");
        int slSeats = readInt(scanner, "Enter Total Sleeper (SL) Seats: ");
        double slFare = readDouble(scanner, "Enter Sleeper (SL) Fare (Rs.): ");
        int acSeats = readInt(scanner, "Enter Total AC Seats: ");
        double acFare = readDouble(scanner, "Enter AC Fare (Rs.): ");

        Train newTrain = new Train(trainNo, name, source, destination, depTime, arrTime, slSeats, slFare, acSeats, acFare);
        trains.add(newTrain);
        saveData();
        System.out.println("[SUCCESS] Train " + trainNo + " (" + name + ") added successfully!");
    }

    private static void updateTrain(Scanner scanner) {
        System.out.println("\n--- UPDATE TRAIN DETAILS ---");
        int trainNo = readInt(scanner, "Enter Train Number to update: ");
        Train train = findTrainByNumber(trainNo);
        if (train == null) {
            System.out.println("[ERROR] Train " + trainNo + " not found.");
            return;
        }

        System.out.println("Updating Train: " + train.getTrainNumber() + " - " + train.getName() + " (" + train.getRoute() + ")");
        System.out.println("  1. Update Timings (Departure / Arrival)");
        System.out.println("  2. Update Fares (Sleeper / AC)");
        System.out.println("  3. Update Seat Capacities (Sleeper / AC)");
        System.out.println("  4. Cancel Update");
        int subChoice = readInt(scanner, "Choose field to update (1-4): ");

        switch (subChoice) {
            case 1:
                train.setDepartureTime(readNonEmptyString(scanner, "New Departure Time (current " + train.getDepartureTime() + "): "));
                train.setArrivalTime(readNonEmptyString(scanner, "New Arrival Time (current " + train.getArrivalTime() + "): "));
                System.out.println("[SUCCESS] Timings updated.");
                break;
            case 2:
                train.setSleeperFare(readDouble(scanner, "New Sleeper Fare (current Rs." + train.getSleeperFare() + "): "));
                train.setAcFare(readDouble(scanner, "New AC Fare (current Rs." + train.getAcFare() + "): "));
                System.out.println("[SUCCESS] Fares updated.");
                break;
            case 3:
                train.setSleeperTotalSeats(readInt(scanner, "New Sleeper Seat Capacity (current " + train.getSleeperTotalSeats() + "): "));
                train.setAcTotalSeats(readInt(scanner, "New AC Seat Capacity (current " + train.getAcTotalSeats() + "): "));
                System.out.println("[SUCCESS] Seat capacities updated.");
                break;
            default:
                System.out.println("Update cancelled.");
                return;
        }
        saveData();
    }

    private static void removeTrain(Scanner scanner) {
        System.out.println("\n--- REMOVE TRAIN ---");
        int trainNo = readInt(scanner, "Enter Train Number to remove: ");
        Iterator<Train> iterator = trains.iterator();
        while (iterator.hasNext()) {
            Train t = iterator.next();
            if (t.getTrainNumber() == trainNo) {
                iterator.remove();
                saveData();
                System.out.println("[SUCCESS] Train " + trainNo + " (" + t.getName() + ") has been removed.");
                return;
            }
        }
        System.out.println("[ERROR] Train " + trainNo + " not found.");
    }

    private static void viewAllTicketsAdmin() {
        System.out.println("\n=========================================================================================");
        System.out.println("                          ALL SYSTEM TICKETS & REVENUE SUMMARY                           ");
        System.out.println("=========================================================================================");
        if (tickets.isEmpty()) {
            System.out.println("No tickets have been booked in the system yet.");
            return;
        }

        double totalRevenue = 0;
        int confirmedCount = 0;
        int cancelledCount = 0;

        System.out.printf("%-8s %-10s %-8s %-20s %-12s %-6s %-6s %-10s %-10s%n",
                "PNR", "User", "TrainNo", "Route", "Date", "Class", "Pax", "Fare(Rs)", "Status");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (Ticket t : tickets) {
            System.out.printf("%-8d %-10s %-8d %-20s %-12s %-6s %-6d %-10.2f %-10s%n",
                    t.getPnrNumber(),
                    truncate(t.getBookedByUsername(), 10),
                    t.getTrainNumber(),
                    truncate(t.getRoute(), 20),
                    t.getTravelDate(),
                    t.getCoachClass(),
                    t.getPassengers().size(),
                    t.getTotalFare(),
                    t.getStatus());

            if (t.isConfirmed()) {
                totalRevenue += t.getTotalFare();
                confirmedCount++;
            } else {
                cancelledCount++;
            }
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("Confirmed Bookings: %d | Cancelled Bookings: %d | Active Revenue: Rs. %.2f%n",
                confirmedCount, cancelledCount, totalRevenue);
    }

    private static void viewPassengerChartByTrain(Scanner scanner) {
        int trainNo = readInt(scanner, "Enter Train Number for Passenger Manifest: ");
        Train train = findTrainByNumber(trainNo);
        if (train == null) {
            System.out.println("[ERROR] Train " + trainNo + " not found.");
            return;
        }

        System.out.println("\n===========================================================================");
        System.out.println(" PASSENGER CHART: " + train.getTrainNumber() + " - " + train.getName() + " (" + train.getRoute() + ")");
        System.out.println("===========================================================================");
        System.out.printf("%-8s %-12s %-6s %-8s %-20s %-5s %-6s %-15s%n",
                "PNR", "Date", "Class", "Seat", "Passenger Name", "Age", "Gender", "Berth");
        System.out.println("---------------------------------------------------------------------------");

        int totalPassengers = 0;
        for (Ticket t : tickets) {
            if (t.getTrainNumber() == trainNo && t.isConfirmed()) {
                for (Passenger p : t.getPassengers()) {
                    System.out.printf("%-8d %-12s %-6s %-8s %-20s %-5d %-6s %-15s%n",
                            t.getPnrNumber(),
                            t.getTravelDate(),
                            t.getCoachClass(),
                            p.getSeatNumber(),
                            truncate(p.getName(), 20),
                            p.getAge(),
                            p.getGender(),
                            p.getBerthType());
                    totalPassengers++;
                }
            }
        }

        if (totalPassengers == 0) {
            System.out.println("No confirmed passengers found for Train " + trainNo + ".");
        } else {
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("Total Confirmed Passengers on Train " + trainNo + ": " + totalPassengers);
        }
    }

    // =========================================================================
    // USER PORTAL
    // =========================================================================

    private static void userDashboard(Scanner scanner, User user) {
        while (true) {
            System.out.println("\n=======================================================");
            System.out.println("        PASSENGER PORTAL - Welcome, " + user.getFullName());
            System.out.println("=======================================================");
            System.out.println("  1. View All Trains");
            System.out.println("  2. Search Trains by Source & Destination");
            System.out.println("  3. Check Seat Availability & Fares");
            System.out.println("  4. Book Ticket");
            System.out.println("  5. View My Booked Tickets");
            System.out.println("  6. Check PNR Status / Print E-Ticket");
            System.out.println("  7. Cancel Ticket");
            System.out.println("  8. Logout");
            System.out.println("-------------------------------------------------------");
            int choice = readInt(scanner, "Select Option (1-8): ");

            switch (choice) {
                case 1:
                    displayAllTrains();
                    break;
                case 2:
                    searchTrainsByRoute(scanner);
                    break;
                case 3:
                    checkSeatAvailability(scanner);
                    break;
                case 4:
                    bookTicket(scanner, user);
                    break;
                case 5:
                    viewUserTickets(user);
                    break;
                case 6:
                    displayTicketDetails(scanner);
                    break;
                case 7:
                    cancelTicket(scanner, user);
                    break;
                case 8:
                    saveData();
                    System.out.println("Logged out from Passenger Portal.");
                    return;
                default:
                    System.out.println("[!] Invalid choice. Please select between 1 and 8.");
            }
        }
    }

    // =========================================================================
    // CORE RESERVATION OPERATIONS
    // =========================================================================

    private static void displayAllTrains() {
        System.out.println("\n==========================================================================================================");
        System.out.println("                                        AVAILABLE TRAIN SCHEDULE                                          ");
        System.out.println("==========================================================================================================");
        System.out.printf("%-8s %-22s %-12s %-12s %-7s %-7s %-16s %-16s%n",
                "TrainNo", "Train Name", "Source", "Destination", "Dep", "Arr", "SL (Avail/Fare)", "AC (Avail/Fare)");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        for (Train t : trains) {
            int availSL = getAvailableSeats(t.getTrainNumber(), "SL");
            int availAC = getAvailableSeats(t.getTrainNumber(), "AC");
            String slInfo = availSL + "/" + t.getSleeperTotalSeats() + " Rs." + (int) t.getSleeperFare();
            String acInfo = availAC + "/" + t.getAcTotalSeats() + " Rs." + (int) t.getAcFare();

            System.out.printf("%-8d %-22s %-12s %-12s %-7s %-7s %-16s %-16s%n",
                    t.getTrainNumber(),
                    truncate(t.getName(), 22),
                    truncate(t.getSource(), 12),
                    truncate(t.getDestination(), 12),
                    t.getDepartureTime(),
                    t.getArrivalTime(),
                    slInfo,
                    acInfo);
        }
        System.out.println("==========================================================================================================");
    }

    private static void searchTrainsByRoute(Scanner scanner) {
        System.out.println("\n--- SEARCH TRAINS BY ROUTE ---");
        String src = readNonEmptyString(scanner, "Enter Source Station (e.g., Mumbai, Delhi, Chennai): ").toLowerCase();
        String dest = readNonEmptyString(scanner, "Enter Destination Station (e.g., Delhi, Jaipur, Bengaluru): ").toLowerCase();

        boolean found = false;
        System.out.println("\nMatching Trains:");
        System.out.println("----------------------------------------------------------------------------------------------------------");
        for (Train t : trains) {
            if (t.getSource().toLowerCase().contains(src) && t.getDestination().toLowerCase().contains(dest)) {
                int availSL = getAvailableSeats(t.getTrainNumber(), "SL");
                int availAC = getAvailableSeats(t.getTrainNumber(), "AC");
                System.out.printf("Train %d | %-22s | %s -> %s (%s - %s) | SL: %d seats (Rs.%.0f) | AC: %d seats (Rs.%.0f)%n",
                        t.getTrainNumber(), t.getName(), t.getSource(), t.getDestination(),
                        t.getDepartureTime(), t.getArrivalTime(),
                        availSL, t.getSleeperFare(), availAC, t.getAcFare());
                found = true;
            }
        }
        if (!found) {
            System.out.println("[!] No direct trains found matching '" + src + "' to '" + dest + "'.");
        }
    }

    private static void checkSeatAvailability(Scanner scanner) {
        int trainNumber = readInt(scanner, "\nEnter Train Number: ");
        Train train = findTrainByNumber(trainNumber);

        if (train == null) {
            System.out.println("[ERROR] Train Number " + trainNumber + " not found.");
            return;
        }

        int availSL = getAvailableSeats(trainNumber, "SL");
        int availAC = getAvailableSeats(trainNumber, "AC");

        System.out.println("\n-------------------------------------------------------");
        System.out.println("Train: " + train.getTrainNumber() + " - " + train.getName());
        System.out.println("Route: " + train.getRoute() + " (" + train.getDepartureTime() + " to " + train.getArrivalTime() + ")");
        System.out.println("-------------------------------------------------------");
        System.out.printf("  Sleeper Class (SL) : %d / %d seats available | Fare: Rs. %.2f%n",
                availSL, train.getSleeperTotalSeats(), train.getSleeperFare());
        System.out.printf("  AC Class (3AC/CC)  : %d / %d seats available | Fare: Rs. %.2f%n",
                availAC, train.getAcTotalSeats(), train.getAcFare());
        System.out.println("-------------------------------------------------------");
    }

    private static void bookTicket(Scanner scanner, User user) {
        System.out.println("\n--- BOOK RAILWAY TICKET ---");
        displayAllTrains();

        int trainNumber = readInt(scanner, "Enter Train Number to book: ");
        Train train = findTrainByNumber(trainNumber);
        if (train == null) {
            System.out.println("[ERROR] Train not found. Please check the Train Number.");
            return;
        }

        System.out.print("Enter Travel Date (YYYY-MM-DD) [Press Enter for tomorrow]: ");
        String travelDate = scanner.nextLine().trim();
        if (travelDate.isEmpty()) {
            travelDate = LocalDate.now().plusDays(1).toString();
        }

        System.out.println("\nSelect Coach Class:");
        System.out.println("  1. Sleeper (SL) - Rs. " + train.getSleeperFare() + " per seat (Available: " + getAvailableSeats(trainNumber, "SL") + ")");
        System.out.println("  2. Air Conditioned (AC) - Rs. " + train.getAcFare() + " per seat (Available: " + getAvailableSeats(trainNumber, "AC") + ")");
        int classChoice = readInt(scanner, "Enter Class Choice (1 for SL, 2 for AC): ");

        String coachClass;
        double baseFare;
        if (classChoice == 1) {
            coachClass = "SL";
            baseFare = train.getSleeperFare();
        } else if (classChoice == 2) {
            coachClass = "AC";
            baseFare = train.getAcFare();
        } else {
            System.out.println("[ERROR] Invalid coach class selection.");
            return;
        }

        int availableSeats = getAvailableSeats(trainNumber, coachClass);
        if (availableSeats <= 0) {
            System.out.println("[SORRY] No seats available in " + coachClass + " class on Train " + trainNumber + ".");
            return;
        }

        int numPassengers = readInt(scanner, "Enter Number of Passengers (1 to 6): ");
        if (numPassengers < 1 || numPassengers > 6) {
            System.out.println("[ERROR] You can book between 1 and 6 passengers per ticket.");
            return;
        }

        if (numPassengers > availableSeats) {
            System.out.println("[SORRY] Only " + availableSeats + " seat(s) left in " + coachClass + " class.");
            return;
        }

        List<Passenger> passengerList = new ArrayList<>();
        double totalFare = 0.0;
        int currentBookedCount = countBookedSeats(trainNumber, coachClass);

        for (int i = 1; i <= numPassengers; i++) {
            System.out.println("\n--- Passenger " + i + " Details ---");
            String pName = readNonEmptyString(scanner, "Passenger Name: ");
            int pAge = readInt(scanner, "Passenger Age: ");
            String pGender = readNonEmptyString(scanner, "Gender (M/F/O): ").toUpperCase();

            System.out.println("Berth Preference: 1.Lower  2.Middle  3.Upper  4.Side Lower  5.No Preference");
            int berthPref = readInt(scanner, "Select Berth Preference (1-5): ");

            int seatIndex = currentBookedCount + i;
            String seatNo = coachClass + "-" + seatIndex;
            String assignedBerth;
            if (berthPref >= 1 && berthPref <= 4) {
                assignedBerth = BERTH_TYPES[berthPref - 1];
            } else {
                assignedBerth = BERTH_TYPES[(seatIndex - 1) % BERTH_TYPES.length];
            }

            // Moderate realistic pricing: Children under 5 travel half-fare, Senior Citizens (60+) get 25% discount
            double passengerFare = baseFare;
            if (pAge < 5) {
                passengerFare = baseFare * 0.50;
                System.out.println("  -> Child Concession (50% off) applied!");
            } else if (pAge >= 60) {
                passengerFare = baseFare * 0.75;
                System.out.println("  -> Senior Citizen Concession (25% off) applied!");
            }

            totalFare += passengerFare;
            passengerList.add(new Passenger(pName, pAge, pGender, seatNo, assignedBerth));
        }

        System.out.println("\n-------------------------------------------------------");
        System.out.printf("Total Payable Fare for %d passenger(s): Rs. %.2f%n", numPassengers, totalFare);
        System.out.println("Select Payment Mode: 1. UPI   2. Debit/Credit Card   3. NetBanking   4. Cancel");
        int payOption = readInt(scanner, "Enter Payment Option (1-4): ");
        if (payOption == 4 || payOption < 1 || payOption > 3) {
            System.out.println("[!] Booking cancelled before payment.");
            return;
        }

        String paymentMethod = (payOption == 1) ? "UPI" : (payOption == 2) ? "Card" : "NetBanking";
        int assignedPnr = pnrCounter++;

        Ticket newTicket = new Ticket(assignedPnr, user.getUsername(), train, travelDate,
                coachClass, passengerList, totalFare, paymentMethod);
        tickets.add(newTicket);
        saveData();

        System.out.println("\n[SUCCESS] Ticket Booked Successfully! Generating E-Ticket...");
        printFormattedTicket(newTicket);
    }

    private static void viewUserTickets(User user) {
        System.out.println("\n--- MY BOOKING HISTORY (" + user.getFullName() + ") ---");
        boolean found = false;
        for (Ticket t : tickets) {
            if (t.getBookedByUsername().equalsIgnoreCase(user.getUsername())) {
                printFormattedTicket(t);
                found = true;
            }
        }
        if (!found) {
            System.out.println("You have no bookings yet.");
        }
    }

    private static void displayTicketDetails(Scanner scanner) {
        int pnr = readInt(scanner, "\nEnter 6-digit PNR / Ticket Number: ");
        for (Ticket t : tickets) {
            if (t.getPnrNumber() == pnr) {
                printFormattedTicket(t);
                return;
            }
        }
        System.out.println("[ERROR] Ticket with PNR " + pnr + " not found.");
    }

    private static void cancelTicket(Scanner scanner, User user) {
        int pnr = readInt(scanner, "\nEnter PNR Number to Cancel: ");
        for (Ticket t : tickets) {
            if (t.getPnrNumber() == pnr) {
                if (!user.isAdmin() && !t.getBookedByUsername().equalsIgnoreCase(user.getUsername())) {
                    System.out.println("[ERROR] You can only cancel tickets booked under your own account.");
                    return;
                }
                if (!t.isConfirmed()) {
                    System.out.println("[!] Ticket PNR " + pnr + " is already cancelled.");
                    return;
                }

                t.setStatus("CANCELLED");
                double cancellationFee = t.getTotalFare() * 0.15; // 15% standard clerkage fee
                double refundAmount = t.getTotalFare() - cancellationFee;
                saveData();

                System.out.println("\n[SUCCESS] Ticket PNR " + pnr + " has been CANCELLED.");
                System.out.printf("  Total Fare Paid    : Rs. %.2f%n", t.getTotalFare());
                System.out.printf("  Cancellation Fee   : Rs. %.2f (15%%)%n", cancellationFee);
                System.out.printf("  Refund Initiated   : Rs. %.2f (via %s)%n", refundAmount, t.getPaymentMethod());
                return;
            }
        }
        System.out.println("[ERROR] Ticket with PNR " + pnr + " not found.");
    }

    private static void printFormattedTicket(Ticket t) {
        System.out.println("\n+-----------------------------------------------------------------------+");
        System.out.printf("| E-TICKET / PNR : %-12d                STATUS : %-15s |%n", t.getPnrNumber(), t.getStatus());
        System.out.println("+-----------------------------------------------------------------------+");
        System.out.printf("| Train : %-6d - %-26s  Date   : %-15s |%n",
                t.getTrainNumber(), truncate(t.getTrainName(), 26), t.getTravelDate());
        System.out.printf("| Route : %-35s  Timing : %-5s - %-7s |%n",
                truncate(t.getRoute(), 35), t.getDepartureTime(), t.getArrivalTime());
        System.out.printf("| Class : %-12s                         Booked By: %-13s |%n",
                t.getCoachClass().equals("SL") ? "Sleeper (SL)" : "AC Coach",
                truncate(t.getBookedByUsername(), 13));
        System.out.println("+-----------------------------------------------------------------------+");
        System.out.println("| Seat     | Passenger Name           | Age  | Gender | Berth           |");
        System.out.println("+-----------------------------------------------------------------------+");
        for (Passenger p : t.getPassengers()) {
            System.out.printf("| %-8s | %-24s | %-4d | %-6s | %-15s |%n",
                    p.getSeatNumber(),
                    truncate(p.getName(), 24),
                    p.getAge(),
                    p.getGender(),
                    p.getBerthType());
        }
        System.out.println("+-----------------------------------------------------------------------+");
        System.out.printf("| Total Fare : Rs. %-10.2f                  Payment: %-15s |%n",
                t.getTotalFare(), t.getPaymentMethod());
        System.out.println("+-----------------------------------------------------------------------+");
    }

    // =========================================================================
    // HELPER METHODS & PERSISTENCE
    // =========================================================================

    private static Train findTrainByNumber(int trainNumber) {
        for (Train t : trains) {
            if (t.getTrainNumber() == trainNumber) {
                return t;
            }
        }
        return null;
    }

    private static int countBookedSeats(int trainNumber, String coachClass) {
        int count = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getTrainNumber() == trainNumber
                    && ticket.getCoachClass().equalsIgnoreCase(coachClass)
                    && ticket.isConfirmed()) {
                count += ticket.getPassengers().size();
            }
        }
        return count;
    }

    private static int getAvailableSeats(int trainNumber, String coachClass) {
        Train train = findTrainByNumber(trainNumber);
        if (train == null) {
            return 0;
        }
        int totalCapacity = "SL".equalsIgnoreCase(coachClass)
                ? train.getSleeperTotalSeats()
                : train.getAcTotalSeats();
        return Math.max(0, totalCapacity - countBookedSeats(trainNumber, coachClass));
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("[!] Please enter a valid integer number.");
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("[!] Please enter a valid numeric amount.");
            }
        }
    }

    private static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[!] Input cannot be empty.");
        }
    }

    private static String truncate(String value, int maxLength) {
        if (value == null) return "";
        return value.length() <= maxLength ? value : value.substring(0, maxLength - 2) + "..";
    }

    private static void initializeDefaultData() {
        users.clear();
        trains.clear();
        tickets.clear();

        // Default Admin & User Accounts
        users.add(new User("admin", "admin123", "System Administrator", "9876543210", "ADMIN"));
        users.add(new User("user", "user123", "Rahul Sharma", "9123456780", "USER"));

        // Pre-loaded Trains (Elaborated from original project + realistic Indian routes)
        trains.add(new Train(1010, "Mumbai Rajdhani Exp", "Mumbai", "Delhi", "13:05", "08:30", 50, 650.0, 30, 1450.0));
        trains.add(new Train(2013, "Pink City Superfast", "Delhi", "Jaipur", "07:00", "12:15", 50, 320.0, 25, 780.0));
        trains.add(new Train(3045, "Grand Trunk Express", "Chennai", "Delhi", "10:00", "06:45", 60, 820.0, 30, 1890.0));
        trains.add(new Train(4022, "Shatabdi Express", "Bengaluru", "Chennai", "06:00", "11:00", 45, 290.0, 40, 690.0));
        trains.add(new Train(5088, "Howrah Duronto Exp", "Kolkata", "Mumbai", "18:30", "21:15", 55, 750.0, 35, 1650.0));
    }

    @SuppressWarnings("unchecked")
    private static void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            initializeDefaultData();
            saveData();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            trains = (ArrayList<Train>) ois.readObject();
            tickets = (ArrayList<Ticket>) ois.readObject();
            users = (ArrayList<User>) ois.readObject();
            pnrCounter = ois.readInt();
        } catch (Exception e) {
            initializeDefaultData();
        }
    }

    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(trains);
            oos.writeObject(tickets);
            oos.writeObject(users);
            oos.writeInt(pnrCounter);
        } catch (Exception e) {
            System.out.println("[!] Warning: Could not save data to disk: " + e.getMessage());
        }
    }
}
