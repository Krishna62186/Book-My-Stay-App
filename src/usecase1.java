/**
 * UseCase7AddOnServiceSelection
 *
 * This class demonstrates how add-on services can be attached to an existing
 * reservation without modifying core booking or inventory logic.
 *
 * It uses a Map<String, List<Service>> to model a one-to-many relationship.
 *
 * @author YourName
 * @version 7.0
 */

import java.util.*;

// ----------- Add-On Service Model ----------- //
class Service {
    private String serviceName;
    private double price;

    public Service(String serviceName, double price) {
        this.serviceName = serviceName;
        this.price = price;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getPrice() {
        return price;
    }
}

// ----------- Add-On Service Manager ----------- //
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<Service>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, Service service) {
        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service: " + service.getServiceName()
                + " to Reservation ID: " + reservationId);
    }

    // Display services for a reservation
    public void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation ID: " + reservationId);

        List<Service> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (Service s : services) {
            System.out.println("- " + s.getServiceName() + " (₹" + s.getPrice() + ")");
        }
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {
        List<Service> services = serviceMap.get(reservationId);

        if (services == null) return 0;

        double total = 0;
        for (Service s : services) {
            total += s.getPrice();
        }
        return total;
    }
}

// ----------- Main Application ----------- //
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: v7.0\n");

        // Assume reservation IDs from previous use case
        String reservationId1 = "SI-1";
        String reservationId2 = "DO-3";

        // Initialize Add-On Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Create services
        Service breakfast = new Service("Breakfast", 500);
        Service wifi = new Service("WiFi", 200);
        Service spa = new Service("Spa Access", 1500);

        // Guest selects services
        serviceManager.addService(reservationId1, breakfast);
        serviceManager.addService(reservationId1, wifi);

        serviceManager.addService(reservationId2, spa);

        // Display services
        serviceManager.displayServices(reservationId1);
        System.out.println("Total Add-On Cost: ₹"
                + serviceManager.calculateTotalCost(reservationId1));

        serviceManager.displayServices(reservationId2);
        System.out.println("Total Add-On Cost: ₹"
                + serviceManager.calculateTotalCost(reservationId2));
    }
}