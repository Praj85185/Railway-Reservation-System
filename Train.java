import java.io.Serializable;

/**
 * Represents a train with route, schedule, coach capacities, and fares.
 */
public class Train implements Serializable {
    private static final long serialVersionUID = 1L;

    private int trainNumber;
    private String name;
    private String source;
    private String destination;
    private String departureTime;
    private String arrivalTime;
    private int sleeperTotalSeats;
    private int acTotalSeats;
    private double sleeperFare;
    private double acFare;

    public Train(int trainNumber, String name, String source, String destination,
                 String departureTime, String arrivalTime,
                 int sleeperTotalSeats, double sleeperFare,
                 int acTotalSeats, double acFare) {
        this.trainNumber = trainNumber;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.sleeperTotalSeats = sleeperTotalSeats;
        this.sleeperFare = sleeperFare;
        this.acTotalSeats = acTotalSeats;
        this.acFare = acFare;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getSleeperTotalSeats() {
        return sleeperTotalSeats;
    }

    public void setSleeperTotalSeats(int sleeperTotalSeats) {
        this.sleeperTotalSeats = sleeperTotalSeats;
    }

    public int getAcTotalSeats() {
        return acTotalSeats;
    }

    public void setAcTotalSeats(int acTotalSeats) {
        this.acTotalSeats = acTotalSeats;
    }

    public double getSleeperFare() {
        return sleeperFare;
    }

    public void setSleeperFare(double sleeperFare) {
        this.sleeperFare = sleeperFare;
    }

    public double getAcFare() {
        return acFare;
    }

    public void setAcFare(double acFare) {
        this.acFare = acFare;
    }

    public String getRoute() {
        return source + " -> " + destination;
    }
}
