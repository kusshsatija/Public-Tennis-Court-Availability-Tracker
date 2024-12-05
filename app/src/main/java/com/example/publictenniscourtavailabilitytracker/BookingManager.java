package com.example.publictenniscourtavailabilitytracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingManager {
    // Nested HashMap structure to store bookings
    // parkName -> courtName -> date -> List of booked time ranges
    private static final HashMap<String, HashMap<String, HashMap<String, List<TimeRange>>>> bookings = new HashMap<>();

    // Class to encapsulate a time range
    public static class TimeRange {
        int startTime; // in minutes since midnight
        int endTime;

        public TimeRange(int startTime, int endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        // Check if this time range overlaps with another
        public boolean overlaps(TimeRange other) {
            return this.startTime < other.endTime && other.startTime < this.endTime;
        }
    }

    // Add a booking
    public static void addBooking(String parkName, String courtName, String date, int startTime, int endTime) {
        // Initialize nested maps if necessary
        bookings.putIfAbsent(parkName, new HashMap<>());
        HashMap<String, HashMap<String, List<TimeRange>>> parkBookings = bookings.get(parkName);

        parkBookings.putIfAbsent(courtName, new HashMap<>());
        HashMap<String, List<TimeRange>> courtBookings = parkBookings.get(courtName);

        courtBookings.putIfAbsent(date, new ArrayList<>());
        List<TimeRange> dateBookings = courtBookings.get(date);

        // Add the new time range
        dateBookings.add(new TimeRange(startTime, endTime));
    }

    // Check if a court is booked for the given time range
    public static boolean isCourtBooked(String parkName, String courtName, String date, int startTime, int endTime) {
        HashMap<String, HashMap<String, List<TimeRange>>> parkBookings = bookings.get(parkName);
        if (parkBookings == null) return false;

        HashMap<String, List<TimeRange>> courtBookings = parkBookings.get(courtName);
        if (courtBookings == null) return false;

        List<TimeRange> dateBookings = courtBookings.get(date);
        if (dateBookings == null) return false;

        // Check for overlap with any existing booking
        TimeRange requestedRange = new TimeRange(startTime, endTime);
        for (TimeRange existingRange : dateBookings) {
            if (requestedRange.overlaps(existingRange)) {
                return true; // Overlap found
            }
        }
        return false; // No overlap
    }
    public static void deleteBooking(String parkName, String courtName, String date, int startTime, int endTime) {
        HashMap<String, HashMap<String, List<TimeRange>>> parkBookings = bookings.get(parkName);
        if (parkBookings != null) {
            HashMap<String, List<TimeRange>> courtBookings = parkBookings.get(courtName);
            if (courtBookings != null) {
                List<TimeRange> dateBookings = courtBookings.get(date);
                if (dateBookings != null) {
                    // Iterate and remove the booking
                    dateBookings.removeIf(range -> range.startTime == startTime && range.endTime == endTime);
                }
            }
        }
    }

}
