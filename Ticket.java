import java.io.Serializable;
import java.util.List;

/**
 * Represents a booked railway ticket (PNR) containing one or more passengers.
 */
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;

    private int pnrNumber;
    private String bookedByUsername;
    private int trainNumber;
    private String trainName;
    private String route;
    private String departureTime;
    private String arrivalTime;
    private String travelDate;
    private String coachClass; // "SL" (Sleeper) or "AC" (Air Conditioned)
    private List<Passenger> passengers;
    private double totalFare;
    private String status; // "CONFIRMED" or "CANCELLED"
    private String paymentMethod;

    public Ticket(int pnrNumber, String bookedByUsername, Train train, String travelDate,
                  String coachClass, List<Passenger> passengers, double totalFare, String paymentMethod) {
        this.pnrNumber = pnrNumber;
        this.bookedByUsername = bookedByUsername;
        this.trainNumber = train.getTrainNumber();
        this.trainName = train.getName();
        this.route = train.getRoute();
        this.departureTime = train.getDepartureTime();
        this.arrivalTime = train.getArrivalTime();
        this.travelDate = travelDate;
        this.coachClass = coachClass;
        this.passengers = passengers;
        this.totalFare = totalFare;
        this.status = "CONFIRMED";
        this.paymentMethod = paymentMethod;
    }

    public int getPnrNumber() {
        return pnrNumber;
    }

    public String getBookedByUsername() {
        return bookedByUsername;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public String getTrainName() {
        return trainName;
    }

    public String getRoute() {
        return route;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getTravelDate() {
        return travelDate;
    }

    public String getCoachClass() {
        return coachClass;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public boolean isConfirmed() {
        return "CONFIRMED".equalsIgnoreCase(status);
    }
}
