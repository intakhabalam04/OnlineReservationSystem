import java.sql.*;
import java.util.*;

public class Main {
    private static Connection connection;
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        initializeDatabase();

        start();

        closeDatabaseConnection();
    }

    private static void initializeDatabase() {
        String jdbcUrl = "";
        String username = "";
//        String password = "your_password";
//        String password="";
        /*
        Replace your MySQL password, username , jdbcUrl with you original database ;
         */

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeDatabaseConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        while (true) {
            System.out.println("Enter your choice ");
            System.out.println("1. View Seat Availability");
            System.out.println("2. Book Ticket");
            System.out.println("3. Cancel Ticket");
            System.out.println("4. Exit");

            int choice = 0;
            try {
                choice = sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Enter only integers");
                sc.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    try {
                        view();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        book();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        cancel();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Enter a valid number");
                    break;
            }
        }
    }

    private static void view() throws SQLException {
        String selectQuery = "SELECT seat_number, is_reserved FROM reserved_seats";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);

        System.out.print("Current SeatMap : ");
        while (resultSet.next()) {
            int seatNumber = resultSet.getInt("seat_number");
            boolean isReserved = resultSet.getBoolean("is_reserved");

            if (isReserved) {
                System.out.print("X ");
            } else {
                System.out.print(seatNumber + " ");
            }
        }
        System.out.println();
    }

    private static void book() throws SQLException {
        String selectQuery = "SELECT seat_number FROM reserved_seats WHERE is_reserved = false";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);

        List<Integer> availableSeats = new ArrayList<>();
        while (resultSet.next()) {
            int seatNumber = resultSet.getInt("seat_number");
            availableSeats.add(seatNumber);
        }

        if (availableSeats.isEmpty()) {
            System.out.println("No seats available");
        } else {
            int randomIndex = new Random().nextInt(availableSeats.size());
            int seatNo = availableSeats.get(randomIndex);

            String updateQuery = "UPDATE reserved_seats SET is_reserved = true WHERE seat_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, seatNo);
            preparedStatement.executeUpdate();

            System.out.println("Ticket booked successfully\nSeat no " + seatNo);
        }
    }


    private static void cancel() throws SQLException {
        System.out.print("Enter your seat number : ");
        try {
            int seatNo = sc.nextInt();
            if (seatNo >= 1 && seatNo <= 20) {
                String selectQuery = "SELECT is_reserved FROM reserved_seats WHERE seat_number = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
                preparedStatement.setInt(1, seatNo);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    boolean isReserved = resultSet.getBoolean("is_reserved");
                    if (isReserved) {
                        String updateQuery = "UPDATE reserved_seats SET is_reserved = false WHERE seat_number = ?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setInt(1, seatNo);
                        updateStatement.executeUpdate();
                        System.out.println("Seat canceled successfully");
                    } else {
                        System.out.println("Given seat is not booked");
                    }
                }
            } else {
                System.out.println("Enter a valid seat number");
            }
        } catch (InputMismatchException e) {
            System.out.println("Enter only integers");
            sc.nextLine();
        }
    }
}
