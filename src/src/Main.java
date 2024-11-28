import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Pricing Strategy Interface for Bridge Pattern
interface PricingStrategy {
    double getPrice(double basePrice);
}

// Standard Pricing Implementation
class StandardPricing implements PricingStrategy {
    @Override
    public double getPrice(double basePrice) {
        return basePrice;
    }
}

// Discount Pricing Implementation
class DiscountPricing implements PricingStrategy {
    @Override
    public double getPrice(double basePrice) {
        return basePrice * 0.9; // 10% discount
    }
}

// Abstract Room for Bridge and State Patterns
abstract class Room {
    protected PricingStrategy pricingStrategy;
    private RoomState state = new AvailableState(); // Default state

    public Room(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public abstract String getDescription();

    public double getPrice(double basePrice) {
        return pricingStrategy.getPrice(basePrice);
    }

    public void setState(RoomState state) {
        this.state = state;
    }

    public void bookRoom() {
        state.bookRoom(this);
    }

    public void cancelRoom() {
        state.cancelRoom(this);
    }

    public void checkIn() {
        state.checkIn(this);
    }

    public void checkOut() {
        state.checkOut(this);
    }
}

// Single Room Implementation
class SingleRoom extends Room {
    public SingleRoom(PricingStrategy pricingStrategy) {
        super(pricingStrategy);
    }

    @Override
    public String getDescription() {
        return "Single Room";
    }
}

// Double Room Implementation
class DoubleRoom extends Room {
    public DoubleRoom(PricingStrategy pricingStrategy) {
        super(pricingStrategy);
    }

    @Override
    public String getDescription() {
        return "Double Room";
    }
}

// Decorator for Adding Features
abstract class RoomDecorator extends Room {
    protected Room room;

    public RoomDecorator(Room room) {
        super(room.pricingStrategy);
        this.room = room;
    }

    @Override
    public String getDescription() {
        return room.getDescription();
    }
}

// WiFi Decorator
class WiFiDecorator extends RoomDecorator {
    public WiFiDecorator(Room room) {
        super(room);
    }

    @Override
    public String getDescription() {
        return room.getDescription() + ", Wi-Fi";
    }

    @Override
    public double getPrice(double basePrice) {
        return room.getPrice(basePrice) + 200; // Additional WiFi cost
    }
}

// Breakfast Decorator
class BreakfastDecorator extends RoomDecorator {
    public BreakfastDecorator(Room room) {
        super(room);
    }

    @Override
    public String getDescription() {
        return room.getDescription() + ", Breakfast";
    }

    @Override
    public double getPrice(double basePrice) {
        return room.getPrice(basePrice) + 300; // Additional Breakfast cost
    }
}

// Holder for Room Management
class RoomHolder {
    Room[] luxuryDoubleRooms = new Room[10];
    Room[] deluxeDoubleRooms = new Room[20];
    Room[] luxurySingleRooms = new Room[10];
    Room[] deluxeSingleRooms = new Room[20];
}

// Factory Class for Room Creation
class RoomFactory {
    public static Room createRoom(String roomType, PricingStrategy pricingStrategy) {
        return switch (roomType.toLowerCase()) {
            case "luxury single" -> new SingleRoom(pricingStrategy);
            case "deluxe single" -> new SingleRoom(pricingStrategy);
            case "luxury double" -> new DoubleRoom(pricingStrategy);
            case "deluxe double" -> new DoubleRoom(pricingStrategy);
            default -> throw new IllegalArgumentException("Invalid room type: " + roomType);
        };
    }
}

// Observer Interface
interface Observer {
    void update(String message);
}

// Customer (Observer Implementation)
class Customer implements Observer {
    private String name;

    public Customer(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println("Notification for " + name + ": " + message);
    }
}

// Room States for State Pattern
interface RoomState {
    void bookRoom(Room room);
    void cancelRoom(Room room);
    void checkIn(Room room);
    void checkOut(Room room);
}

class AvailableState implements RoomState {
    @Override
    public void bookRoom(Room room) {
        System.out.println("Room booked successfully!");
        room.setState(new ReservedState());
    }

    @Override
    public void cancelRoom(Room room) {
        System.out.println("Room is already available.");
    }

    @Override
    public void checkIn(Room room) {
        System.out.println("Cannot check in. Room is not reserved.");
    }

    @Override
    public void checkOut(Room room) {
        System.out.println("Room is already available.");
    }
}

class ReservedState implements RoomState {
    @Override
    public void bookRoom(Room room) {
        System.out.println("Room is already reserved.");
    }

    @Override
    public void cancelRoom(Room room) {
        System.out.println("Reservation cancelled.");
        room.setState(new AvailableState());
    }

    @Override
    public void checkIn(Room room) {
        System.out.println("Checked in successfully.");
        room.setState(new OccupiedState());
    }

    @Override
    public void checkOut(Room room) {
        System.out.println("Cannot check out. Room is not occupied.");
    }
}

class OccupiedState implements RoomState {
    @Override
    public void bookRoom(Room room) {
        System.out.println("Room is occupied. Cannot book.");
    }

    @Override
    public void cancelRoom(Room room) {
        System.out.println("Cannot cancel. Room is occupied.");
    }

    @Override
    public void checkIn(Room room) {
        System.out.println("Room is already occupied.");
    }

    @Override
    public void checkOut(Room room) {
        System.out.println("Checked out successfully.");
        room.setState(new AvailableState());
    }
}

// Hotel Management with Observer Integration
class Hotel {
    private static Hotel instance;
    private List<Observer> customers = new ArrayList<>();
    static RoomHolder roomHolder = new RoomHolder();
    static Scanner sc = new Scanner(System.in);

    private Hotel() {}

    public static Hotel getInstance() {
        if (instance == null) {
            instance = new Hotel();
        }
        return instance;
    }

    public void addObserver(Observer observer) {
        customers.add(observer);
    }

    public void removeObserver(Observer observer) {
        customers.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer customer : customers) {
            customer.update(message);
        }
    }

    public void roomAvailabilityChanged(String roomType) {
        notifyObservers("Room type " + roomType + " availability has changed.");
    }

    static void displayFeatures(int roomType) {
        String[] features = {
            "Luxury Double Room: 1 double bed, AC, Free breakfast, Rs.4000 per night.",
            "Deluxe Double Room: 1 double bed, AC, Rs.3000 per night.",
            "Luxury Single Room: 1 single bed, AC, Free breakfast, Rs.2200 per night.",
            "Deluxe Single Room: 1 single bed, Rs.1200 per night."
        };
        if (roomType >= 1 && roomType <= 4) {
            System.out.println(features[roomType - 1]);
        } else {
            System.out.println("Invalid room type.");
        }
    }

    static void checkAvailability(int roomType) {
        Room[] rooms = switch (roomType) {
            case 1 -> roomHolder.luxuryDoubleRooms;
            case 2 -> roomHolder.deluxeDoubleRooms;
            case 3 -> roomHolder.luxurySingleRooms;
            case 4 -> roomHolder.deluxeSingleRooms;
            default -> null;
        };

        if (rooms == null) {
            System.out.println("Invalid room type.");
            return;
        }

        long count = java.util.Arrays.stream(rooms).filter(room -> room == null).count();
        System.out.println("Available rooms: " + count);
    }

    static void bookRoom(int roomType) {
        System.out.println("Enter customer name: ");
        String name = sc.next();
        System.out.println("Enter contact number: ");
        String contact = sc.next();
        System.out.println("Enter gender: ");
        String gender = sc.next();

        PricingStrategy pricingStrategy = new StandardPricing();
        String roomTypeName = switch (roomType) {
            case 1 -> "luxury double";
            case 2 -> "deluxe double";
            case 3 -> "luxury single";
            case 4 -> "deluxe single";
            default -> {
                System.out.println("Invalid room type.");
                yield null;
            }
        };

        if (roomTypeName == null) return;

        Room[] rooms = switch (roomType) {
            case 1 -> roomHolder.luxuryDoubleRooms;
            case 2 -> roomHolder.deluxeDoubleRooms;
            case 3 -> roomHolder.luxurySingleRooms;
            case 4 -> roomHolder.deluxeSingleRooms;
            default -> null;
        };

        if (rooms == null) {
            System.out.println("Invalid room type.");
            return;
        }

        for (int i = 0; i < rooms.length; i++) {
            if (rooms[i] == null) {
                rooms[i] = RoomFactory.createRoom(roomTypeName, pricingStrategy);
                Hotel.getInstance().roomAvailabilityChanged(roomTypeName);
                System.out.println("Room booked successfully.");
                return;
            }
        }
        System.out.println("No rooms available.");
    }
}

// Main Class
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Hotel hotel = Hotel.getInstance();

        System.out.println("Enter your name to subscribe to notifications:");
        String customerName = sc.nextLine();
        hotel.addObserver(new Customer(customerName));

        while (true) {
            System.out.println("\nEnter your choice:");
            System.out.println("1. Display room details");
            System.out.println("2. Check room availability");
            System.out.println("3. Book room");
            System.out.println("4. Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.println("Enter room type (1-4):");
                    int roomType = sc.nextInt();
                    Hotel.displayFeatures(roomType);
                }
                case 2 -> {
                    System.out.println("Enter room type (1-4):");
                    int roomType = sc.nextInt();
                    Hotel.checkAvailability(roomType);
                }
                case 3 -> {
                    System.out.println("Enter room type (1-4):");
                    int roomType = sc.nextInt();
                    Hotel.bookRoom(roomType);
                }
                case 4 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}
