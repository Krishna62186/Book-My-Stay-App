/**
 * UseCase2RoomInitialization
 *
 * This class demonstrates basic object-oriented modeling using abstraction,
 * inheritance, and polymorphism for a Hotel Booking System.
 *
 * It initializes different room types and displays their availability.
 *
 * @author YourName
 * @version 2.1
 */

// Abstract class representing a generic Room
abstract class Room {
    protected String roomType;
    protected int numberOfBeds;
    protected double price;

    // Constructor
    public Room(String roomType, int numberOfBeds, double price) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.price = price;
    }

    // Method to display room details
    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Price per night: ₹" + price);
    }
}

// Single Room class
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000.0);
    }
}

// Double Room class
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500.0);
    }
}

// Suite Room class
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000.0);
    }
}

// Main application class
public class UseCase2RoomInitialization {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: v2.1\n");

        // Creating room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailability = 5;
        int doubleAvailability = 3;
        int suiteAvailability = 2;

        // Display room details and availability
        System.out.println("---- Room Details ----\n");

        single.displayDetails();
        System.out.println("Available: " + singleAvailability);
        System.out.println();

        doubleRoom.displayDetails();
        System.out.println("Available: " + doubleAvailability);
        System.out.println();

        suite.displayDetails();
        System.out.println("Available: " + suiteAvailability);
    }
}