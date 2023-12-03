
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("\n\n*************** Online Reservation System ***************\n");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Booking");
        System.out.println("4. Cancel");
        System.out.println("5. Show Details");
        System.out.println("0. Exit");
        System.out.println("Select an option:");
        boolean loginCheck = false;
        int choice;
        while (true) {
            choice = Input.getInt();
            if (choice != 0) {
                switch (choice) {
                    case 1:
                        System.out.println("You selected Register");
                        Registration register = new Registration();
                        register.register();
                        break;
                    case 2:
                        System.out.println("You selected Login");
                        Login login = new Login();
                        if (login.login()) {
                            System.out.println("Login Successfull");
                            loginCheck = true;
                        } else
                            System.out.println("Incorrect login id or password");
                        break;
                    case 3:
                        System.out.println("You selected Booking");
                        if (loginCheck) {
                            Reservation book = new Reservation();
                            if (book.bookSeats()) {
                                System.out.println("\n****** Happy Jounrey ******");
                            } else {
                                System.out.println("Seats not booked.Please try again later....!!");
                            }
                        } else {
                            System.out.println("Please login for Booking..!!!");
                        }
                        break;
                    case 4:
                        System.out.println("You selected Cancel");
                       
                        if (loginCheck) {
                             Cancellation cancellation = new Cancellation();
                            if (cancellation.cancel()) {
                                System.out.println("Tickets Cancelled");
                            } else {
                                System.out.println("Please Try again....!!!");
                            }
                        } else {
                            System.out.println("Please login for Booking..!!!");
                        }
                        break;
                    case 5:
                        System.out.println("You selected Show Details");
                        if (loginCheck) {
                            Showdetails showdetails=new Showdetails();
                            if(showdetails.show())
                            System.out.println("\nThese are your ticket details.\n****** Thankyou for Booking. Wishyou a very HappyJourney ******");
                            else
                            System.out.println("********** Technical issue.Try again later **********");
                        }
                        else {
                            System.out.println("Please login for Booking..!!!");
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } else {
                System.out.println("Thankyou");
                break;
            }
            System.out.println("Do you Want to continue. Select an option?: ");
        }
    }
}
