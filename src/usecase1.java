/**
 * UseCase4RoomSearch
 *
 * This class demonstrates read-only search functionality for available rooms.
 * It ensures that inventory is not modified during search operations.
 *
 * @author YourName
 * @version 4.0
 */

import java.util.HashMap;
import java.util.Map;

// ----------- Room Domain Model ----------- //
abstract class Room {
    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Price: ₹" + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 2000.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 3500.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 6000.0);
    }
}

// ----------- Inventory (State Holder) ----------- //
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // intentionally unavailable
    }

    // Read-only access method
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Expose inventory safely (read-only usage)
    public Map<String, Integer> getAllAvailability() {
        return inventory;
    }
}

// ----------- Search Service ----------- //
class RoomSearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;

        // Room catalog (domain data)
        roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom());
        roomCatalog.put("Double Room", new DoubleRoom());
        roomCatalog.put("Suite Room", new SuiteRoom());
    }

    // Search available rooms (READ-ONLY)
    public void searchAvailableRooms() {
        System.out.println("---- Available Rooms ----\n");

        for (String roomType : roomCatalog.keySet()) {

            int available = inventory.getAvailability(roomType);

            // Defensive check: only show available rooms
            if (available > 0) {
                Room room = roomCatalog.get(roomType);

                room.displayDetails();
                System.out.println("Available: " + available);
                System.out.println();
            }
        }
    }
}

// ----------- Main Application ----------- //
public class UseCase4RoomSearch {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: v4.0\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Perform search (read-only)
        searchService.searchAvailableRooms();
    }
}