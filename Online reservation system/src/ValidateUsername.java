import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidateUsername {
    public static boolean validate(String s) throws SQLException
    {
        try (Connection connection = ConnectionToDb.connect()) {
            String selectQuery = "SELECT username FROM user Where username=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, s);
                try (ResultSet resultset = preparedStatement.executeQuery()) {
                    while(resultset.next()) {
                       return false;
                    }
                }
            }
        }
        return true;
    }
    
}
