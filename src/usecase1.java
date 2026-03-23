/**
 * UseCase12DataPersistenceRecovery
 *
 * Demonstrates persistence of booking and inventory state using Java serialization.
 * System state is saved before shutdown and restored on startup.
 *
 * @author YourName
 * @version 12.0
 */

import java.io.*;
import java.util.*;

// ----------- Reservation Model (Serializable) ----------- //
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    public void display() {
        System.out.println("ID: " + reservationId + " | Guest: " + guestName + " | Room: " + roomType);
    }
}

// ----------- Inventory Model (Serializable) ----------- //
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory(Map<String, Integer> initialInventory) {
        inventory.putAll(initialInventory);
    }

    public boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public void releaseRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, available + 1);
    }

    public void display() {
        System.out.println("\n---- Inventory State ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// ----------- Persistence Service ----------- //
class PersistenceService {

    private static final String FILE_PATH = "booking_system_state.ser";

    public static void saveState(RoomInventory inventory, List<Reservation> reservations) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(inventory);
            oos.writeObject(reservations);
            System.out.println("\nSystem state saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static State restoreState() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("No saved state found. Starting with fresh system.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            RoomInventory inventory = (RoomInventory) ois.readObject();
            List<Reservation> reservations = (List<Reservation>) ois.readObject();
            System.out.println("\nSystem state restored successfully.");
            return new State(inventory, reservations);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error restoring system state: " + e.getMessage());
            return null;
        }
    }

    // Wrapper class to return restored state
    static class State {
        RoomInventory inventory;
        List<Reservation> reservations;

        public State(RoomInventory inventory, List<Reservation> reservations) {
            this.inventory = inventory;
            this.reservations = reservations;
        }
    }
}

// ----------- Main Application ----------- //
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App - Persistence & Recovery Simulation");
        System.out.println("Version: v12.0");

        // Attempt to restore previous state
        PersistenceService.State restored = PersistenceService.restoreState();

        RoomInventory inventory;
        List<Reservation> confirmedReservations;

        if (restored != null) {
            inventory = restored.inventory;
            confirmedReservations = restored.reservations;
        } else {
            // Fresh start
            Map<String, Integer> initialInventory = Map.of(
                    "Single Room", 3,
                    "Double Room", 2,
                    "Suite Room", 1
            );
            inventory = new RoomInventory(initialInventory);
            confirmedReservations = new ArrayList<>();
        }

        // Simulate new booking
        System.out.println("\nProcessing new booking...");

        Reservation newBooking = new Reservation("S-01", "Alice", "Single Room");
        if (inventory.allocateRoom(newBooking.getRoomType())) {
            confirmedReservations.add(newBooking);
            System.out.println("Booking confirmed: " + newBooking.getGuestName() + " -> " + newBooking.getRoomType());
        } else {
            System.out.println("Booking failed: No availability for " + newBooking.getRoomType());
        }

        // Display current system state
        System.out.println("\nCurrent Reservations:");
        for (Reservation r : confirmedReservations) {
            r.display();
        }

        inventory.display();

        // Persist system state
        PersistenceService.saveState(inventory, confirmedReservations);
    }
}