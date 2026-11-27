import java.io.Serializable;

/**
 * Represents an individual passenger on a booked ticket.
 */
public class Passenger implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private String gender;
    private String seatNumber;
    private String berthType; // Lower, Middle, Upper, Side Lower

    public Passenger(String name, int age, String gender, String seatNumber, String berthType) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.seatNumber = seatNumber;
        this.berthType = berthType;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getBerthType() {
        return berthType;
    }
}
