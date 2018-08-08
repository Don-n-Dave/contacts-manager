import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.io.BufferedReader;
//import java.util.*;

public class ContactApp {

    public static HashMap<String, String> contacts = new HashMap<String, String>();


    public static void main(String[] args) {
        while (true) {
           addDirectory();
           options();
        }
    }


        public static void options() {

            Scanner input = new Scanner(System.in);
            System.out.println("1. View  Contacts. \n" +
                               "2. Add a new contact.\n" +
                               "3. Search a contact by name.\n" +
                               "4. Delete an existing contact.\n" +
                               "5. Exit.\n" +
                               "Enter an option (1, 2, 3, 4, or 5):");

            switch(input.nextInt()) {
            case 1:
                viewContacts();
                break;
            case 2:
                addContacts();
                break;
            case 3:
                searchContacts();
                break;
            case 4:
                deleteContacts();
                break;
            case 5:
                System.out.println("Have a nice day");
                System.exit(0);
                break;
        }
    }

    public static void addDirectory(){
        String directory = "data";
        String filePath = "data/contacts.txt";

        Path dataDirectory = Paths.get(directory);
        Path dataFile = Paths.get(filePath);


        if (!Files.isDirectory(dataDirectory)) {
            try {
                Files.createDirectories(dataDirectory);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!Files.exists(dataFile)) {
            try {
                Files.createFile(dataFile);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        createContacts();
    }

    public static void createContacts() {
        String filePath = "data/contacts.txt";

        try {
            FileReader io = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(io);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length >= 2)
                {
                    String key = parts[0].toLowerCase();
                    String value = parts[1];
                    contacts.put(key, value);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void viewContacts() {

        for (String key : contacts.keySet()) {
            System.out.println(key.substring(0,1).toUpperCase() + key.substring(1) + " | " + contacts.get(key));


        }

    }

    public static void searchContacts() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the name you would like to search for.");
        String name = input.nextLine().trim().toLowerCase();
        System.out.println(contacts.get(name));
    }

    public static void deleteContacts() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter contact you wish to delete.");
        List<String> contacts = null;
        try {
            contacts = Files.readAllLines(Paths.get("data", "contacts.txt"));
            }
            catch (IOException e) {
            e.printStackTrace();
            }

            List<String> newList = new ArrayList<>();
        for (String line : contacts) {
            if (line.equals(input.next().trim())) {
                newList.add(" ");
                continue;
            }
            newList.add(line);

        }

        try {
            Files.write(Paths.get("data", "contacts.txt"), newList);

        }

        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("hi there!");
    }

    public static void addContacts() {
        Scanner input = new Scanner(System.in);

        String name = "";
        String number = "";

        System.out.println("Please enter new name.");
        name = input.nextLine();

        System.out.println("Please enter new contact number.");
        number = input.next();

        String contact = name + ":" + number;

        try {
            Files.write(
                    Paths.get("data", "contacts.txt"),
                    Arrays.asList(contact),
                    StandardOpenOption.APPEND
            );
        }
        catch (IOException e) {
            e.printStackTrace();
        }



    }

}
