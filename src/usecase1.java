/**
 * UseCase6RoomAllocationService
 *
 * This class demonstrates booking confirmation and room allocation.
 * It ensures:
 * - FIFO processing of requests
 * - Unique room assignment
 * - Immediate inventory updates
 * - Prevention of double-booking
 *
 * @author YourName
 * @version 6.0
 */

import java.util.*;

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
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) {
        inventory.put(roomType, getAvailability(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\n---- Updated Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// ----------- Booking Queue ----------- //
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // FIFO
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// ----------- Booking Service ----------- //
class BookingService {

    private RoomInventory inventory;

    // Map: RoomType -> Set of allocated Room IDs
    private Map<String, Set<String>> allocatedRooms;

    // Global set to ensure uniqueness
    private Set<String> allRoomIds;

    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRooms = new HashMap<>();
        this.allRoomIds = new HashSet<>();
    }

    // Process booking requests
    public void processBookings(BookingRequestQueue queue) {

        System.out.println("---- Processing Booking Requests ----\n");

        while (!queue.isEmpty()) {

            Reservation request = queue.getNextRequest();
            String roomType = request.getRoomType();

            System.out.println("Processing request for " + request.getGuestName()
                    + " (" + roomType + ")");

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness (defensive check)
                if (!allRoomIds.contains(roomId)) {

                    // Add to global set
                    allRoomIds.add(roomId);

                    // Add to room-type specific set
                    allocatedRooms
                            .computeIfAbsent(roomType, k -> new HashSet<>())
                            .add(roomId);

                    // Decrement inventory immediately
                    inventory.decrementAvailability(roomType);

                    // Confirm booking
                    System.out.println("Booking CONFIRMED for "
                            + request.getGuestName()
                            + " | Room ID: " + roomId);

                } else {
                    System.out.println("Error: Duplicate Room ID detected!");
                }

            } else {
                System.out.println("Booking FAILED for "
                        + request.getGuestName()
                        + " | No availability");
            }

            System.out.println();
        }
    }

    // Generate unique Room ID
    private String generateRoomId(String roomType) {
        return roomType.replace(" ", "").substring(0, 2).toUpperCase()
                + "-" + (idCounter++);
    }

    // Display allocated rooms
    public void displayAllocations() {
        System.out.println("\n---- Room Allocations ----");
        for (Map.Entry<String, Set<String>> entry : allocatedRooms.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// ----------- Main Application ----------- //
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: v6.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Single Room"));
        queue.addRequest(new Reservation("Bob", "Single Room"));
        queue.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        queue.addRequest(new Reservation("David", "Double Room"));
        queue.addRequest(new Reservation("Eve", "Suite Room"));

        // Initialize booking service
        BookingService bookingService = new BookingService(inventory);

        // Process bookings
        bookingService.processBookings(queue);

        // Display results
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}