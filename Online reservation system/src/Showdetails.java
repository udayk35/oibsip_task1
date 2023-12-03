import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Showdetails {
    String pnr;
    Showdetails()
    {
        System.out.println("\n Enter PNR number:");
        pnr=Input.getString();
    }
    public boolean show() throws SQLException
    {
        try (Connection connection = ConnectionToDb.connect()) {
            String selectQuery = "SELECT * FROM reservation WHERE pnr_number = ?";
            try (PreparedStatement select = connection.prepareStatement(selectQuery)) {
                select.setString(1, pnr);
                try (ResultSet resultSet = select.executeQuery()) {
                    System.out.printf("%-15s %-20s %-10s %-10s %-20s %-10s\n", "PNR Number", "Passenger Name", "Age",
                            "Seat Number", "Train Name", "Date");
                    
                    while(resultSet.next())
                    {
                    System.out.printf("%-15s %-20s %-10s %-10s %-20s %-10s\n", resultSet.getString(2),
                            resultSet.getString(5), resultSet.getString(6), resultSet.getString(8),
                            resultSet.getString(4), resultSet.getDate(7).toString().substring(0, 10));
                            return true;
                    }
                        System.out.println("Enter Correct PNR number...!!!");
                }
            }
    }
    catch(Exception e)
    {
        return false;
    }
        return false;
}
}
