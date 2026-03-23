/**
 * UseCase3InventorySetup
 *
 * This class demonstrates centralized inventory management using HashMap.
 * It replaces scattered availability variables with a single data structure.
 *
 * @author YourName
 * @version 3.1
 */

import java.util.HashMap;
import java.util.Map;

// Inventory class responsible for managing room availability
class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability for a specific room type
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (increase/decrease)
    public void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + change);
    }

    // Display entire inventory
    public void displayInventory() {
        System.out.println("---- Current Room Inventory ----");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main application class
public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: v3.1\n");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();
        System.out.println();

        // Simulate booking (decrease availability)
        System.out.println("Booking 1 Single Room...");
        inventory.updateAvailability("Single Room", -1);

        // Simulate cancellation (increase availability)
        System.out.println("Cancelling 1 Suite Room...");
        inventory.updateAvailability("Suite Room", +1);

        System.out.println();

        // Display updated inventory
        inventory.displayInventory();
    }
}