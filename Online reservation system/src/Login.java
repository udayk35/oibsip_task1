import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {
    int login_id;
    String userPassword;

    Login() {
        System.out.println("\nEnter login id: ");
        login_id = Input.getInt();
         System.out.println("\nEnter Pass: ");
        userPassword = Input.getString();

    }

    public boolean login() throws SQLException {

        try (Connection connection = ConnectionToDb.connect()) {
            String selectQuery = "SELECT * FROM user WHERE login_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, login_id);
                try (ResultSet resultset = preparedStatement.executeQuery()) {
                    if (resultset.next()) {
                        String password = resultset.getString(5);
                        return userPassword.equals(password);
                    }
                }
            }
        }

        return false;
    }
}
