/**
 * UseCase8BookingHistoryReport
 *
 * This class demonstrates how confirmed bookings are stored and used
 * for reporting purposes. It maintains a booking history and generates reports.
 *
 * It reinforces persistence mindset using in-memory storage.
 *
 * @author YourName
 * @version 8.0
 */

import java.util.*;

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

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId +
                " | Guest: " + guestName +
                " | Room: " + roomType);
    }
}

// ----------- Booking History ----------- //
class BookingHistory {

    private List<Reservation> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed reservation
    public void addReservation(Reservation reservation) {
        history.add(reservation);
    }

    // Retrieve all reservations (read-only usage)
    public List<Reservation> getAllReservations() {
        return history;
    }
}

// ----------- Reporting Service ----------- //
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {
        System.out.println("---- Booking History ----\n");

        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation r : reservations) {
            r.display();
        }
    }

    // Generate summary report
    public void generateSummaryReport(List<Reservation> reservations) {

        System.out.println("\n---- Booking Summary Report ----");

        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : reservations) {
            summary.put(
                    r.getRoomType(),
                    summary.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        for (Map.Entry<String, Integer> entry : summary.entrySet()) {
            System.out.println(entry.getKey() + " Bookings: " + entry.getValue());
        }
    }
}

// ----------- Main Application ----------- //
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: v8.0\n");

        // Initialize booking history
        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("SI-1", "Alice", "Single Room"));
        history.addReservation(new Reservation("SI-2", "Bob", "Single Room"));
        history.addReservation(new Reservation("DO-3", "Charlie", "Double Room"));
        history.addReservation(new Reservation("SU-4", "David", "Suite Room"));

        // Initialize report service
        BookingReportService reportService = new BookingReportService();

        // Display all bookings
        reportService.displayAllBookings(history.getAllReservations());

        // Generate summary report
        reportService.generateSummaryReport(history.getAllReservations());
    }
}

