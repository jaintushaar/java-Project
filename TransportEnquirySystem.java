import java.io.*;
import java.util.*;

public class TransportEnquirySystem {
private static final Scanner scanner = new Scanner(System.in);
private static final String ADMIN_USERNAME = "admin";
private static final String ADMIN_PASSWORD = "password";
public static void main(String[] args) {
    try {
        Admin.loadTransportsFromFile();
    } catch (IOException e) {
        System.err.println("Failed to load transports from file.");
    }

    while (true) {
        System.out.println("1. Admin login");
        System.out.println("2. Customer login");
        System.out.println("3. Exit");
        int choice = getChoice(1, 3);
        switch (choice) {
            case 1:
                adminLogin();
                break;
            case 2:
                Customer.login();
                break;
            case 3:
                Admin.saveTransportsToFile();
                System.out.println("Exiting...");
                return;
        }
    }
}

private static void adminLogin() {
    System.out.print("Enter username: ");
    String username = scanner.nextLine();
    System.out.print("Enter password: ");
    String password = scanner.nextLine();
    if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
        Admin.login();
    } else {
        System.out.println("Invalid username or password.");
    }
}

private static int getChoice(int min, int max) {
    int choice;
    do {
        System.out.print("Enter your choice: ");
        choice = getIntInput();
    } while (choice < min || choice > max);
    return choice;
}

private static int getIntInput() {
    int input;
    while (true) {
        try {
            input = Integer.parseInt(scanner.nextLine());
            break;
        } catch (NumberFormatException e) {
            System.out.print("Invalid input. Please enter a number: ");
        }
    }
    return input;
}
}


interface Transport {
String getTransportType();
String getTransportName();
int getCost();
int getCapacity();
int getAvailability();
boolean book(int seats);
}
class TransportImpl implements Transport {
    private String transportType;
    private String transportName;
    private int cost;
    private int capacity;
    private int availability;
    
    public TransportImpl(String transportType, String transportName ,int cost, int capacity, int availability) {
        this.transportType = transportType;
        this.transportName = transportName;
        this.cost = cost;
        this.capacity = capacity;
        this.availability = availability;
    }
    
    @Override
    public String getTransportType() {
        return transportType;
    }
    @Override
    public String getTransportName() {
        return transportName;
    }
    
    @Override
    public int getCost() {
        return cost;
    }
    
    @Override
    public int getCapacity() {
        return capacity;
    }
    
    @Override
    public int getAvailability() {
        return availability;
    }
    
    @Override
    public boolean book(int seats) {
    if (availability >= seats) {
        availability -= seats;
        return true;
    } else {
        return false;
    }
    }
    
    @Override
    public String toString() {
        return (transportType + " - " + transportName + " - " + cost + " - " + capacity + " - " + availability);
    }
}



class Admin {
private static final Scanner scanner = new Scanner(System.in);
protected static List<Transport> transports = new ArrayList<>();
private static final String FILENAME = "admin.txt";
    
    public static void login() {
    while (true) {
        System.out.println("1. Add transport");
        System.out.println("2. View all transports");
        System.out.println("3. View transport by Type");
        System.out.println("4. Remove transport");
        System.out.println("5. Logout");
        int choice = getChoice(1, 5);
        switch (choice) {
            case 1:
                addTransport();
                break;
            case 2:
                viewTransports();
                break;
            case 3:
                viewTransportByType();
                break;
            case 4:
                removeTransport();
                break;
            case 5:
                return;
             }
         }
}
private static void addTransport() {
    System.out.print("Enter transport type: ");
    String transportType = scanner.nextLine();
    System.out.print("Enter transport Name: ");
    String transportName = scanner.nextLine();
    System.out.print("Enter cost: ");
    int cost = getIntInput();
    System.out.print("Enter capacity: ");
    int capacity = getIntInput();
    System.out.print("Enter availability: ");
    int availability = getIntInput();
    Transport transport = new TransportImpl(transportType, transportName ,cost, capacity, availability);
    try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File("Transport.txt"), true))) {
        writer.println(transportType + "," + transportName + "," + cost + "," + capacity + "," + availability);
        System.out.println("Transport added successfully.");
    } catch (IOException e) {
        System.out.println("Failed to write to file: " + e.getMessage());
    }
}


private static void viewTransports() {
    if (transports.isEmpty()) {
        System.out.println("No transports available.");
    } else {
        System.out.println("Transport Type - Name - Cost - Capacity - Availability");
        for (Transport transport : transports) {
            System.out.println(transport.toString());
        }
    }
}

private static void viewTransportByType() {
    System.out.print("Enter transport Type: ");
    String name = scanner.nextLine();
    for (Transport transport : transports) {
        while(transport.getTransportType().equalsIgnoreCase(name)) {
            System.out.println(transport.toString());
        }
    }
    System.out.println("Transport not found.");
}

private static void removeTransport() {
    System.out.print("Enter transport name: ");
    String name = scanner.nextLine();
    for (int i = 0; i < transports.size(); i++) {
        Transport transport = transports.get(i);
        if (transport.getTransportName().equalsIgnoreCase(name)) {
            transports.remove(i);
            System.out.println("Transport removed successfully.");
            return;
        }
    }
    System.out.println("Transport not found.");
}

public static void loadTransportsFromFile() throws IOException {

    try (BufferedReader reader = new BufferedReader(new FileReader("Transport.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String transportType = parts[0];
                String transportName = parts[1];
                int cost = Integer.parseInt(parts[2]);
                int capacity = Integer.parseInt(parts[3]);
                int availability = Integer.parseInt(parts[4]);
                Transport transport = new TransportImpl(transportType, transportName, cost, capacity, availability);
                transports.add(transport);
            }
        } catch (IOException e) {
            System.out.println("Failed to load transports: " + e.getMessage());
        }
    
}

public static void saveTransportsToFile() {
    
    try (PrintWriter pw = new PrintWriter(new FileWriter("Transport.txt"))) {
        for (Transport transport : transports) {
            pw.println("," + transport.getTransportType() + "," + transport.getTransportType() + "," + transport.getTransportName() + "," + transport.getCost() + "," + transport.getCapacity() + "," + transport.getAvailability());
        }
    } catch (IOException e) {
        System.err.println("Failed to save transports to file.");
    }
}

private static int getChoice(int min, int max) {
    int choice;
    do {
        System.out.print("Enter your choice: ");
        choice = getIntInput();
    } while (choice < min || choice > max);
    return choice;
}

private static int getIntInput() {
    int input;
    while (true) {
        try {
            input = Integer.parseInt(scanner.nextLine());
            break;
        } catch (NumberFormatException e) {
            System.out.print("Invalid input. Please enter a number: ");
        }
    }
    return input;
}
}


class Customer {
private static final Scanner scanner = new Scanner(System.in);
public static void login() {
    System.out.println("Welcome to Transport Enquiry System!");
    while (true) {
        System.out.println("1. View available transports");
        System.out.println("2. Book a transport");
        System.out.println("3. Exit");
        int choice = getChoice(1, 3);
        switch (choice) {
            case 1:
                viewAvailableTransports();
                break;
            case 2:
                bookSeat();
                break;
            case 3:
                System.out.println("Exiting...");
                return;
        }
    }
}

private static void viewAvailableTransports() {
    System.out.println("List of available transports:");
    System.out.println("Transport Type -- name - Cost - Capacity - Availability");
    for (Transport transport : Admin.transports) {
        if (transport.getAvailability() > 0) {
            System.out.println(transport);
        }
    }
}

private static void bookSeat() {
    System.out.print("Enter transport name: ");
    String name = scanner.nextLine();
    for (Transport transport : Admin.transports) {
        if (transport.getTransportName().equalsIgnoreCase(name)) {
            System.out.print("Enter number of seats: ");
            int seats = getIntInput();
            if (transport.book(seats)) {
                System.out.println("Booking successful.");
                return;
            } else {
                System.out.println("Booking unsuccessful.");
                return;
            }
        }
    }
    System.out.println("Transport not found.");
}
   private static int getChoice(int min, int max) {
    int choice;
    do {
        System.out.print("Enter your choice: ");
        choice = getIntInput();
    } while (choice < min || choice > max);
    return choice;
}

private static int getIntInput() {
    int input;
    while (true) {
        try {
            input = Integer.parseInt(scanner.nextLine());
            break;
        } catch (NumberFormatException e) {
            System.out.print("Invalid input. Please enter a number: ");
        }
    }
    return input;
}
}