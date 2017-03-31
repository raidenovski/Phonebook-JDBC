import jdk.internal.util.xml.impl.Input;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Created by raiden on 3/15/17.
 */
public class Phonebook {

    private static Scanner userInput = new Scanner(System.in);
    ArrayList<String> menuOptions = new ArrayList<String>();

    private String option1 = " 1 - Add a contact";
    private String option2 = " 2 - Delete a contact";
    private String option3 = " 3 - Search by name";
    private String option4 = " 4 - Search by number";
    private String option5 = " 5 - List all contacts";
    private String option6 = " 6 - Update a contact";
    private String option7 = " 7 - Export contact list";
    private String option0 = " 0 - Exit";

    public Phonebook() {
        System.out.println("Welcome to your phonebook");
        menuOptions.add(option1);
        menuOptions.add(option2);
        menuOptions.add(option3);
        menuOptions.add(option4);
        menuOptions.add(option5);
        menuOptions.add(option6);
        menuOptions.add(option7);
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
            userInput.nextLine();
            switch (option) {
                case 1: addContact(); break;
                case 2: deleteContact(); break;
                case 3: findByName(); break;
                case 4: findByNumber(); break;
                case 5: listAll(); break;
                case 6: editContact(); break;
                //case 7: exportList(); break;
                case 0: System.exit(0); break;
                default: break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Please use numbers to select an option");
            userInput.nextInt();
        }
        System.out.println("----------- Menu ------------");
        mainMenu();
    }

    public void findByName() {
        System.out.print("Search for: ");
        Contact contact = searchContacts();

        if (contact == null) {
            System.out.println("Sorry, no contacts found by that name");
        } else {
            System.out.println(contact.toString());
        }
    }

    public void findByNumber() {
        System.out.print("Enter phone number:");
        int tel = validateNumber();
        Contact contact = getbyNumber(tel);

        if (contact == null) {
            System.out.println("No contacts found by that number");
        } else {
            System.out.println("Contact found: " + contact.toString());
        }
    }

    public void addContact() {
        System.out.println("Creating a new contact");
        String name = addName();
        int tel = addNumber();

        try {
            PhonebookEngine.insertContact(name, tel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteContact() {
        System.out.print("Enter contact name to delete: ");
        Contact toDelete = searchContacts();
        boolean isDeleted = false;

        if (toDelete == null) {
            System.out.println("No contacts found by that name");
        } else {
            try {
                isDeleted = PhonebookEngine.removeContact(toDelete.getName(), toDelete.getNumber());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (isDeleted) {
                System.out.println(toDelete.toString() + " has been deleted");
            }
        }
    }

    public void listAll() {
        List<Contact> contactList = new ArrayList<Contact>();

        try {
            contactList = PhonebookEngine.getAllContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (Contact contact : contactList) {
            System.out.println(contact.toString());
        }
        System.out.println("Total contacts in phonebook: " + contactList.size());
    }

    public void editContact() {
        System.out.print("Enter a contact name to edit: ");
        Contact contactToEdit = searchContacts();
        System.out.println("Enter new name for contact " + contactToEdit.getName());
        String newName = addName();
        boolean contactUpdated = false;

        try {
            contactUpdated = PhonebookEngine.updateContact(contactToEdit.getName(), newName);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (contactUpdated) {
            System.out.println("Contact edited");
        } else {
            System.out.println("Something went wrong, contact has not been edited");
        }

    }

    // private methods

    // adds a name to a contact
    private String addName() {
        String name;

        while (true) {
            System.out.print("Name: ");
            name = userInput.nextLine();

            if (!(name.matches("(([A-Za-z][ ]?){2,30})"))) {
                System.out.println("Invalid name, cannot contain special characters or numbers");
                continue;
            }
            break;
        }
        return name;
    }

    // adds phone number to a contact
    private int addNumber() {
        System.out.print("Enter contact phone number: ");
        int tel;

        while(true) {
            tel = validateNumber();
            Contact contact = getbyNumber(tel);
            if (contact == null) {
                System.out.println("Adding new number...");
            } else if (contact.getNumber() == tel) {
                System.out.println("That number already exists for contact " + contact.getName() + ". Try a different one");
                continue;
            }
            break;
        }
        return tel;
    }

    private Contact searchContacts() {
        String toSearch = userInput.nextLine().toLowerCase();
        List<Contact> contactList = new ArrayList<Contact>();
        Contact foundContact = null;

        try {
            contactList = PhonebookEngine.getContacts(toSearch);
            if (contactList.size() == 1) {
                System.out.println("One contact found");
                foundContact = contactList.get(0);
            } else if (contactList.size() > 1) {
                System.out.println("More contacts found with name " + toSearch);
                foundContact = moreThanOneContactFound(contactList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foundContact;
    }

    private Contact moreThanOneContactFound(List<Contact> contactList) {
        List<Integer> phoneNumbers = new ArrayList<Integer>();
        Contact foundContact = null;

        for (Contact contact : contactList) {
            System.out.println(contact.toString());
            phoneNumbers.add(contact.getNumber());
        }

        int selectByNumber;
        while (true) {
            System.out.print("Please choose an above contact by phone number: ");
            try {
                selectByNumber = userInput.nextInt();

                if (!phoneNumbers.contains(selectByNumber)) {
                    System.out.println("Number not found in the above list");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid phone number entered");
                userInput.next();
            }
        }
        for (Contact contact : contactList) {
            if (contact.getNumber() == selectByNumber) {
                foundContact = contact;
            }
        }
        return foundContact;
    }

    // validates correct phone number format
    private int validateNumber() {
        int tel;

        while (true) {
            try {
                tel = userInput.nextInt();
                int telLength = String.valueOf(tel).length();

                if ((telLength < 6) || (telLength > 15)) {
                    System.out.println("Incorrect phone number length. Try again");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid phone number format. Please do not use spaces or letters");
                userInput.next();
            }
        }
        return tel;
    }

    private Contact getbyNumber (int tel) {
        Contact contact = null;

        try {
            contact = PhonebookEngine.getContactByNumber(tel);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contact;
    }


} // End of class
