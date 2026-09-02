import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        boolean enterApplication = true;

        System.out.println("------ Welcome to the SVNIT Course Registration System ------");

        while(enterApplication) {

            System.out.println("\n\n------- MAIN MENU -------\n");
            System.out.println("1. Enter the Application");
            System.out.println("2. Exit");

            System.out.print("\nEnter your choice : ");
            int choiceApplication = sc.nextInt();

            switch(choiceApplication) {
                case 1 :
                    new Login();
                    break;

                case 2 :
                    enterApplication = false;
                    System.out.println("\nBye, Visit Again !");
                    break;

                default:
                    break;

            }
        }

    }
}