/**
 * UseCase10BookingCancellation
 *
 * This class demonstrates safe cancellation of bookings with rollback.
 * It ensures:
 * - Only valid reservations can be cancelled
 * - Inventory is restored immediately
 * - Room IDs are tracked using Stack (LIFO rollback)
 * - System state remains consistent
 *
 * @author YourName
 * @version 10.0
 */

import java.util.*;

// ----------- Reservation Model ----------- //
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private boolean isActive;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.isActive = true;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void cancel() {
        this.isActive = false;
    }

    public void display() {
        System.out.println("ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType +
                " | Status: " + (isActive ? "ACTIVE" : "CANCELLED"));
    }
}

// ----------- Inventory ----------- //
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 0);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room", 0);
    }

    public void increment(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void display() {
        System.out.println("\n---- Inventory State ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// ----------- Booking History ----------- //
class BookingHistory {
    private Map<String, Reservation> history = new HashMap<>();

    public void addReservation(Reservation r) {
        history.put(r.getReservationId(), r);
    }

    public Reservation getReservation(String id) {
        return history.get(id);
    }

    public void displayAll() {
        System.out.println("\n---- Booking History ----");
        for (Reservation r : history.values()) {
            r.display();
        }
    }
}

// ----------- Cancellation Service ----------- //
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory history;

    // Stack to track released room IDs (LIFO)
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {

        System.out.println("\nProcessing cancellation for ID: " + reservationId);

        Reservation reservation = history.getReservation(reservationId);

        // Validation checks
        if (reservation == null) {
            System.out.println("Cancellation FAILED: Reservation does not exist.");
            return;
        }

        if (!reservation.isActive()) {
            System.out.println("Cancellation FAILED: Already cancelled.");
            return;
        }

        // Step 1: Record room ID in rollback stack
        rollbackStack.push(reservationId);

        // Step 2: Restore inventory
        inventory.increment(reservation.getRoomType());

        // Step 3: Mark reservation as cancelled
        reservation.cancel();

        System.out.println("Cancellation SUCCESS for ID: " + reservationId);
    }

    public void displayRollbackStack() {
        System.out.println("\n---- Rollback Stack (Recent First) ----");
        for (int i = rollbackStack.size() - 1; i >= 0; i--) {
            System.out.println(rollbackStack.get(i));
        }
    }
}

// ----------- Main Application ----------- //
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: v10.0\n");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        // Simulate existing confirmed bookings
        Reservation r1 = new Reservation("SI-1", "Alice", "Single Room");
        Reservation r2 = new Reservation("DO-2", "Bob", "Double Room");

        history.addReservation(r1);
        history.addReservation(r2);

        // Initialize cancellation service
        CancellationService cancellationService =
                new CancellationService(inventory, history);

        // Perform cancellations
        cancellationService.cancelBooking("SI-1"); // valid
        cancellationService.cancelBooking("SI-1"); // already cancelled
        cancellationService.cancelBooking("XX-9"); // invalid ID

        // Display system state
        history.displayAll();
        inventory.display();
        cancellationService.displayRollbackStack();
    }
}