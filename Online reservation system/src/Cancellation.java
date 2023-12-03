import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Cancellation {
    String pnr;

    Cancellation() {
        System.out.println("Enter pnr number: ");
        pnr = Input.getString();
    }

    public boolean cancel() throws SQLException {
        try (Connection connection = ConnectionToDb.connect()) {
            String deleteQuery = "DELETE FROM reservation WHERE pnr_number = ?";
            String selectQuery = "SELECT * FROM reservation WHERE pnr_number = ?";
            try (PreparedStatement select = connection.prepareStatement(selectQuery)) {
                select.setString(1, pnr);
                try (ResultSet resultSet = select.executeQuery()) {
                    boolean check=false;
                    System.out.printf("%-15s %-20s %-10s %-10s %-20s %-10s\n", "PNR Number", "Passenger Name", "Age",
                            "Seat Number", "Train Name", "Date");
                    while(resultSet.next())
                    {
                        check=true;
                    System.out.printf("%-15s %-20s %-10s %-10s %-20s %-10s\n", resultSet.getString(2),
                            resultSet.getString(5), resultSet.getString(6), resultSet.getString(8),
                            resultSet.getString(4), resultSet.getDate(7));
                    }
                    if(!check)
                    {
                        System.out.println("Enter Correct PNR number...!!!");
                    }
                }
            }
            System.out.println("Do you confirm to cancel ?(yes for cancel/anything for No)");
            boolean procced = ("yes".equals(Input.getString().toLowerCase()));

            if (procced) {
                try (PreparedStatement delete = connection.prepareStatement(deleteQuery)) {
                    delete.setString(1, pnr);
                    delete.executeUpdate();
                    return true;
                } catch (Exception e) {
                    System.out.println("\nCancellation failed.Please try again....!!!");
                }
            } else {
                return false;
            }
        }
        return false;
    }
}
