/**
 * UseCase9ErrorHandlingValidation
 *
 * This class demonstrates validation and error handling in booking processing.
 * It ensures invalid inputs are detected early and handled gracefully.
 *
 * Key Features:
 * - Input validation
 * - Custom exceptions
 * - Fail-fast design
 * - System stability after errors
 *
 * @author YourName
 * @version 9.0
 */

import java.util.*;

// ----------- Custom Exception ----------- //
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// ----------- Reservation Model ----------- //
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// ----------- Inventory Service ----------- //
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) throws InvalidBookingException {
        int current = getAvailability(roomType);

        if (current <= 0) {
            throw new InvalidBookingException("No availability for room type: " + roomType);
        }

        inventory.put(roomType, current - 1);
    }

    public void displayInventory() {
        System.out.println("\n---- Current Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// ----------- Validator ----------- //
class BookingValidator {

    public static void validate(Reservation reservation, RoomInventory inventory)
            throws InvalidBookingException {

        // Validate guest name
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!inventory.isValidRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        // Validate availability
        if (inventory.getAvailability(reservation.getRoomType()) <= 0) {
            throw new InvalidBookingException("Room not available: " + reservation.getRoomType());
        }
    }
}

// ----------- Booking Service ----------- //
class BookingService {

    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void processBooking(Reservation reservation) {

        try {
            // Fail-fast validation
            BookingValidator.validate(reservation, inventory);

            // Proceed only if valid
            inventory.decrementAvailability(reservation.getRoomType());

            System.out.println("Booking SUCCESS for " + reservation.getGuestName()
                    + " | Room: " + reservation.getRoomType());

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking FAILED for " + reservation.getGuestName()
                    + " | Reason: " + e.getMessage());
        }
    }
}

// ----------- Main Application ----------- //
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: v9.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Test cases (valid + invalid scenarios)
        Reservation r1 = new Reservation("Alice", "Single Room");   // valid
        Reservation r2 = new Reservation("", "Double Room");        // invalid name
        Reservation r3 = new Reservation("Bob", "Suite Room");      // invalid type
        Reservation r4 = new Reservation("Charlie", "Double Room"); // valid
        Reservation r5 = new Reservation("David", "Double Room");   // no availability

        // Process bookings
        bookingService.processBooking(r1);
        bookingService.processBooking(r2);
        bookingService.processBooking(r3);
        bookingService.processBooking(r4);
        bookingService.processBooking(r5);

        // Display final inventory (system still stable)
        inventory.displayInventory();
    }
}