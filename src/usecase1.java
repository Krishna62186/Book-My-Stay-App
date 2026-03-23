/**
 * UseCase11ConcurrentBookingSimulation
 *
 * Demonstrates thread-safe booking under concurrent guest requests.
 * Synchronization ensures no double-booking occurs and inventory remains consistent.
 *
 * @author YourName
 * @version 11.0
 */

import java.util.*;
import java.util.concurrent.*;

// ----------- Reservation Model ----------- //
class Reservation {
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

// ----------- Thread-Safe Inventory ----------- //
class RoomInventory {
    private final Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory(Map<String, Integer> initialInventory) {
        inventory.putAll(initialInventory);
    }

    // Thread-safe inventory check and decrement
    public synchronized boolean allocateRoom(String roomType) {
        int available = inventory.getOrDefault(roomType, 0);
        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }
        return false;
    }

    public synchronized void display() {
        System.out.println("\n---- Inventory State ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// ----------- Booking Processor (Thread) ----------- //
class BookingTask implements Runnable {
    private final String guestName;
    private final String roomType;
    private final RoomInventory inventory;
    private final List<Reservation> confirmedReservations;
    private static final Object lock = new Object();

    public BookingTask(String guestName, String roomType, RoomInventory inventory, List<Reservation> confirmedReservations) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.inventory = inventory;
        this.confirmedReservations = confirmedReservations;
    }

    @Override
    public void run() {
        // Critical section: allocate room safely
        synchronized (lock) {
            if (inventory.allocateRoom(roomType)) {
                String reservationId = roomType.substring(0,2).toUpperCase() + "-" + (confirmedReservations.size()+1);
                Reservation reservation = new Reservation(reservationId, guestName, roomType);
                confirmedReservations.add(reservation);
                System.out.println("Booking SUCCESS: " + guestName + " -> " + roomType + " [" + reservationId + "]");
            } else {
                System.out.println("Booking FAILED: " + guestName + " -> " + roomType + " (No availability)");
            }
        }
    }
}

// ----------- Main Application ----------- //
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to Book My Stay App - Concurrent Booking Simulation");
        System.out.println("Version: v11.0\n");

        // Initialize inventory
        Map<String, Integer> initialInventory = Map.of(
                "Single Room", 2,
                "Double Room", 2,
                "Suite Room", 1
        );
        RoomInventory inventory = new RoomInventory(initialInventory);

        // Shared list of confirmed reservations
        List<Reservation> confirmedReservations = Collections.synchronizedList(new ArrayList<>());

        // Simulate multiple guest booking requests
        Runnable[] tasks = new Runnable[] {
                new BookingTask("Alice", "Single Room", inventory, confirmedReservations),
                new BookingTask("Bob", "Single Room", inventory, confirmedReservations),
                new BookingTask("Charlie", "Double Room", inventory, confirmedReservations),
                new BookingTask("Diana", "Suite Room", inventory, confirmedReservations),
                new BookingTask("Eve", "Single Room", inventory, confirmedReservations),
                new BookingTask("Frank", "Double Room", inventory, confirmedReservations)
        };

        // Launch threads
        Thread[] threads = new Thread[tasks.length];
        for (int i=0; i<tasks.length; i++) {
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (Thread t : threads) {
            t.join();
        }

        // Display final booking state
        System.out.println("\n---- Confirmed Reservations ----");
        for (Reservation r : confirmedReservations) {
            r.display();
        }

        inventory.display();
    }
}