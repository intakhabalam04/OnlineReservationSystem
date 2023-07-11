import java.util.*;

public class Main {
    private static boolean[] seat = new boolean[20];
    private static Scanner sc = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {
        start();
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
                    view();
                    break;
                case 2:
                    book();
                    break;
                case 3:
                    cancel();
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

    private static void view() {
        System.out.print("Current SeatMap : ");
        for (int i = 0; i < seat.length; i++) {
            if (seat[i]) {
                System.out.print("X");
            } else {
                System.out.print(i + 1);
            }
        }
        System.out.println();
    }

    private static void book() {
        List<Integer> availableSeats = new ArrayList<>();
        for (int i = 0; i < seat.length; i++) {
            if (!seat[i]) {
                availableSeats.add(i + 1);
            }
        }

        if (availableSeats.isEmpty()) {
            System.out.println("No seats available");
        } else {
            int randomIndex = random.nextInt(availableSeats.size());
            int seatNo = availableSeats.get(randomIndex);
            seat[seatNo - 1] = true;
            System.out.println("Ticket booked successfully\nSeat no " + seatNo);
        }
    }

    private static void cancel() {
        System.out.print("Enter your seat number : ");
        try {
            int seatNo = sc.nextInt();
            if (seatNo >= 1 && seatNo <= seat.length) {
                if (seat[seatNo - 1]) {
                    seat[seatNo - 1] = false;
                    System.out.println("Seat canceled successfully");
                } else {
                    System.out.println("Given seat is not booked");
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