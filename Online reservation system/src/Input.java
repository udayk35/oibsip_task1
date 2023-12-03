import java.util.Scanner;

public class Input {
    public static Scanner scanner = new Scanner(System.in);

    public static int getInt() {
        return scanner.nextInt();
    }

    public static String getString() {
        return scanner.next();
    }
}
