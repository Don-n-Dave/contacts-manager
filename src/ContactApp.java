import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.io.BufferedReader;

public class ContactApp {

    private static Map<String, String> contacts = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    private static Map<String, String> firstName = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
    private static Map<String, String> lastName = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);


    public static void main(String[] args) {
        while (true) {
           addDirectory();
           options();
        }
    }

        public static void options() {
            System.out.println("1. View  Contacts. \n" +
                               "2. Add a new contact.\n" +
                               "3. Search a contact by name.\n" +
                               "4. Delete an existing contact.\n" +
                               "5. Exit.\n\n" +
                               "Enter an option (1, 2, 3, 4, or 5):");

            switch(getInt()) {
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
        contacts.clear();
        try {
            FileReader io = new FileReader(filePath);
            BufferedReader reader = new BufferedReader(io);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length >= 2)
                {
                    String key = parts[0];
                    String value = parts[1];
                    contacts.put(key, value);
                    for(String name : parts){
                        String[] names = name.split(" ",2);
                        if(names.length >= 2){
                            String first = names[0];
                            String last = names[1];
                            firstName.put(first,value);
                            lastName.put(last,value);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void viewContacts() {
        createContacts();
        for (String key : contacts.keySet()) {
            System.out.println(key.substring(0,1).toUpperCase() + key.substring(1) + " | " + contacts.get(key));
        }
        for (String last : lastName.keySet()){
            System.out.println(last + " | " + lastName.get(last));
        }
    }

    public static void searchContacts() {
        Scanner input = new Scanner(System.in);
        System.out.println("Please enter the name you would like to search for.");
        String name = input.nextLine().trim();
        if(contacts.containsKey(name))
        {
            System.out.println(name + " :   " + contacts.get(name));
            System.out.println("If this is not the person you are looking for try searching their first or last name!");
        }
        else if(firstName.containsKey(name)){
            System.out.println(name + " :   " + firstName.get(name));
            System.out.println("If this is not the person you are looking for please try searching their last name or whole name!");
        }
        else if(lastName.containsKey(name)){
            System.out.println(name + " :   " + lastName.get(name));
            System.out.println("If this is not the person you are looking for please try searching their first name or whole name!");
        }
        else {
            System.out.println("Sorry, there is no record for " + name +"!\n" +
                    "Try looking at the list of names before searching or search using their whole name!");
        }
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
        String delete = input.nextLine().trim();
        for (String line : contacts) {
            if (line.startsWith(delete)) {
                newList.add("");
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
        createContacts();
    }

    public static void addContacts() {
        Scanner input = new Scanner(System.in);

        System.out.println("Please enter new name.");
        String name = input.nextLine().trim();
        yesNo(name);
        String number;

        while(true){
            System.out.println("Please enter new contact number without any dashes ( - ).");
            number = input.next().trim();
            if(!checkNumber(number)){
                System.out.println("Please enter only numbers!");
            }
            else{
                break;
            }
        }

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

    private static int getInt(){

        Scanner sc = new Scanner(System.in);
        while(true){
            String input = sc.nextLine();

            try{
                Integer.valueOf(input);
                return Integer.valueOf(input);
            }
            catch (NumberFormatException e){
                System.out.println("Sorry but that is not a valid integer!\n" +
                        "Please try again!");
            }
        }
    }

    private static boolean checkNumber(String number){
        boolean test = number.matches("[0-9]+");
        return test;
    }

    public static void yesNo(String name) {

        Scanner input = new Scanner(System.in);
      if (contacts.containsKey(name)) {
          System.out.println("There is already a person in our system by that name. Would you like to overwrite? y/n ");

          String userInput = input.nextLine();
// Do this instead
          if(userInput.equalsIgnoreCase("y")) {
              System.out.println("What is the new number?");
              String number = input.next();
              List<String> contacts = null;

              try {
                  contacts = Files.readAllLines(Paths.get("data", "contacts.txt"));
              }
              catch (IOException e) {
                  e.printStackTrace();
              }

              List<String> newList = new ArrayList<>();

              for (String line : contacts) {
                  if (line.startsWith(name)) {
                      newList.add(name + ":" + number);
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
              createContacts();
              options();
          }

        } else {
          options();
      }
    }


}
