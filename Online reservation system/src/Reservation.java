import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Reservation {
    // Static variables for train details
    static int trainid;
    static int numberofseats;
    static String dateOfJourney;

    // Constructor for the Reservation class
    Reservation() throws SQLException {
        // Display available trains and take user input for booking details
        if (displayTrains()) {
            System.out.println("\n***********  Enter the booking details  ***********\n");
            System.out.print("Enter the train id: ");
            trainid = Input.getInt();
            System.out.print("\nEnter Date of journey in the format YYYY-MM-DD: ");
            dateOfJourney = Input.getString();
            System.out.print("\nEnter number of seats: ");
            numberofseats = Input.getInt();
        }
    }

    // Method to display available trains
    private static boolean displayTrains() throws SQLException {
        // JDBC URL, username, and password for MySQL server

        try (Connection connection = ConnectionToDb.connect()) {
            String selectQuery = "SELECT * FROM trains ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // Displaying train details in a tabular format
                    System.out.printf("%-15s %-20s %-15s %-20s %-20s %-20s \n",
                            "Train Number", "Train Name", "Start", "Destination", "Start Time", "Destination Time");

                    while (resultSet.next()) {
                        System.out.printf("%-15s %-20s %-15s %-20s %-20s %-20s \n",
                                resultSet.getInt("train_number"),
                                resultSet.getString("train_name"),
                                resultSet.getString("start"),
                                resultSet.getString("destination"),
                                resultSet.getTimestamp("start_time"),
                                resultSet.getTimestamp("destination_time"));
                    }

                } catch (Exception e) {
                    System.out.println("Please try again later");
                    return false;
                }
                return true;
            }
        }
    }

    public boolean bookSeats() throws SQLException {
        // Check if valid train id and number of seats are provided
        if (trainid > 0 && numberofseats > 0) {
            // Parse date of journey and get today's date
            Date date = parseDate(dateOfJourney);
            Date today = new Date();

            // Check if the date of journey is in the future
            if (today.compareTo(date) < 0) {
                // JDBC URL, username, and password for MySQL server

                // Initialize variables to store train details
                String trainname = "";
                int seatsleft = -1;

                try (Connection connection = ConnectionToDb.connect()) {
                    // Retrieve train details based on the provided train id
                    String selectQuery = "SELECT * FROM trains WHERE train_number=?";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                        preparedStatement.setInt(1, trainid);
                        try (ResultSet resultSet = preparedStatement.executeQuery()) {
                            if (resultSet.next()) {
                                trainname = resultSet.getString(2);
                                seatsleft = resultSet.getInt(7);
                            }
                        }

                        // Check if there are enough seats available
                        if (seatsleft >= numberofseats) {
                            // Calculate compartment and seat numbers
                            int cl = seatsleft / 99;
                            int scl = seatsleft - cl * 99;
                            if (scl == 0 && cl != 0) {
                                scl = 99;
                                cl--;
                            }

                            // Initialize maps to store passenger details
                            Map<String, String> seatMap = new HashMap<>();
                            Map<String, Integer> ageMap = new HashMap<>();
                            Map<String, String> pnrMap = new HashMap<>();

                            int i = scl;
                            String pnr = generatePNR();
                            while (seatMap.size() < numberofseats) {
                                // Get passenger details and generate PNR
                                System.out.print("\n " + (scl - i + 1) + ".Enter the Passenger Name: ");
                                String passengername = Input.getString();
                                System.out.print(" Age: ");
                                int age = Input.getInt();
                                // Store passenger details in maps
                                if (!seatMap.containsKey(passengername)) {
                                    String sno = "";
                                    char c = (char) ('A' + cl - 1);
                                    sno = sno + c + (i--);
                                    seatMap.put(passengername, sno);
                                    ageMap.put(passengername, age);
                                    pnrMap.put(passengername, pnr);
                                }
                            }
                            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                            if (InsertIntoReserve.insert(pnrMap, seatMap, ageMap, trainid, trainname, sqlDate)) {
                                String updateQuery = "UPDATE trains SET seats = ? WHERE train_number = ?";
                                try (PreparedStatement update = connection.prepareStatement(updateQuery)) {
                                    update.setInt(1, seatsleft - numberofseats);
                                    update.setInt(2, trainid);
                                    update.executeUpdate();
                                }
                                System.out.println("\n**** Tickets booked successfully.Please Check your Details ****\n");
                                // Display booked seat details
                                System.out.printf("%-15s %-20s %-10s %-10s %-20s %-10s\n",
                                        "PNR Number", "Passenger Name", "Age", "Seat Number", "Train Name", "Date");

                                for (Map.Entry<String, String> me : pnrMap.entrySet()) {
                                    System.out.printf("%-15s %-20s %-10d %-10s %-20s %-10s\n",
                                            me.getValue(), me.getKey(), ageMap.get(me.getKey()),
                                            seatMap.get(me.getKey()), trainname, date.toString().substring(0,11));
                                }

                            } else {
                                System.out.println("\nTickets not booked.Please try again later...?");
                            }
                        } else {
                            System.out.println("Seats not available");
                            return false;
                        }
                        return true;
                    }
                }
            } else {
                // If the date of journey is not in the future
                System.out.println("Invalid date of journey");
                return false;
            }
        }
        // If invalid train id or number of seats
        return true;
    }

    // Method to parse date from string
    private static Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }
    }

    // Method to generate a random PNR
    public static String generatePNR() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder pnrBuilder = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            pnrBuilder.append(CHARACTERS.charAt(randomIndex));
        }

        return pnrBuilder.toString();
    }
}
