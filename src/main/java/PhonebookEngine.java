/**
 * Created by raiden on 3/14/17.
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;

public class PhonebookEngine {

    private static Scanner userInput = new Scanner(System.in);
    ArrayList<String> menuOptions = new ArrayList<String>();
    ConnectionManager connectionManager;

    private String option1 = " 1 - Add a contact";
    private String option2 = " 2 - Remove a contact";
    private String option3 = " 3 - Search for a contact";
    private String option4 = " 4 - List all contacts";
    private String option5 = " 5 - Dial a contact";
    private String option6 = " 6 - Export contact list";
    private String option0 = " 0 - Exit";

    public PhonebookEngine() {
        System.out.println("Welcome to your phonebook");
        menuOptions.add(option1);
        menuOptions.add(option2);
        menuOptions.add(option3);
        menuOptions.add(option4);
        menuOptions.add(option5);
        menuOptions.add(option6);
        menuOptions.add(option0);
        mainMenu();
    }

    public void mainMenu() {
        for (String option : menuOptions) {
            System.out.println(option);
        }
            System.out.println();
            System.out.print("Select an option by number: ");

            try {
                int option = userInput.nextInt();
                switch (option) {
                    //case 1: addContact(); break;
                    //case 2: removeContact(); break;
                    case 3: findContact(); break;
                    //case 4: listAll(); break;
                    //case 5: dialContact(); break;
                    //case 6: exportList(); break;
                    case 0: System.exit(0); break;
                    default: break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Please use numbers to select an option");
                userInput.nextInt();
            }
            mainMenu();
    }

    public void findContact() {
        System.out.print("Search for: ");
        String toFind = userInput.nextLine();

        try {
            ResultSet resultSet = connectionManager.getResults(toFind);

            System.out.println("Contact found: " + resultSet);

            resultSet.close();
        } catch (SQLException connExc) {
            connExc.printStackTrace();
        }
    }
} // End of class
