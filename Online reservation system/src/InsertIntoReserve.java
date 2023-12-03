import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class InsertIntoReserve {
    public static boolean insert(Map<String,String> pnrMap,Map<String,String> seatMap,Map<String,Integer> ageMap,int trainnumber,String trainname,Date dateOfJourney)
    {
      

        try (Connection connection = ConnectionToDb.connect()) {
            // Example: Reading data with a specific login_id
            String insertQuery = "INSERT INTO reservation (pnr_number, train_number, train_name, passenger, age, date, seat_no)\r\n" + //
                    "VALUES (?,?,?,?,?,?,?);";
                for(Map.Entry<String,String> me:pnrMap.entrySet())
                {
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, me.getValue());
                preparedStatement.setInt(2, trainnumber);
                preparedStatement.setString(3, trainname);
                preparedStatement.setString(4, me.getKey());
                preparedStatement.setInt(5, ageMap.get(me.getKey()));
                preparedStatement.setDate(6,  dateOfJourney);
                preparedStatement.setString(7, seatMap.get(me.getKey()));
                preparedStatement.executeUpdate();
               

            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    } 
}
