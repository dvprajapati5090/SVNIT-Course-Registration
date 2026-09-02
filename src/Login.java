import java.util.Scanner;

public class Login {

    Scanner sc = new Scanner(System.in);

    boolean loginUser;
    int choiceUser;

    public Login() {

        loginUser = true;

        while(loginUser) {

            System.out.println("\n\n---- User Login Menu ----\n");
            System.out.println("1. Student");
            System.out.println("2. Professor");
            System.out.println("3. Administrator");
            System.out.println("4. Back to Main Menu");

            System.out.print("\nEnter your choice : ");
            choiceUser = sc.nextInt();

            switch(choiceUser) {

                case 1 :
                    new StudentAuthentication().start();
                    break;

                case 2 :
                    new ProfessorAuthentication().start();
                    break;

                case 3 :
                    new AdminAuthentication().start();
                    break;

                case 4 :
                    loginUser = false;
                    break;

                default :
                    break;

            }
        }
    }

}
