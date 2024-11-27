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

// Abstract Room for Bridge Pattern
abstract class Room {
    protected PricingStrategy pricingStrategy;

    public Room(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public abstract String getDescription();

    public double getPrice(double basePrice) {
        return pricingStrategy.getPrice(basePrice);
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

//Added Factory Class
class RoomFactory {
    public static Room createRoom(String roomType, PricingStrategy pricingStrategy) {
        switch (roomType.toLowerCase()) {
            case "luxury single":
                return new SingleRoom(pricingStrategy);
            case "deluxe single":
                return new SingleRoom(pricingStrategy);
            case "luxury double":
                return new DoubleRoom(pricingStrategy);
            case "deluxe double":
                return new DoubleRoom(pricingStrategy);
            default:
                throw new IllegalArgumentException("Invalid room type: " + roomType);
        }
    }
}

// Hotel Management Class
class Hotel {
    //Added Singlton Pattern
    private static Hotel instance;

    private Hotel() {
    }

    public static Hotel getInstance() {
        if (instance == null) {
            instance = new Hotel();
        }
        return instance;
    }

    static RoomHolder roomHolder = new RoomHolder();
    static Scanner sc = new Scanner(System.in);

    // Display Room Features
    static void displayFeatures(int roomType) {
        switch (roomType) {
            case 1:
                System.out.println("Luxury Double Room: 1 double bed, AC, Free breakfast, Rs.4000 per night.");
                break;
            case 2:
                System.out.println("Deluxe Double Room: 1 double bed, AC, Rs.3000 per night.");
                break;
            case 3:
                System.out.println("Luxury Single Room: 1 single bed, AC, Free breakfast, Rs.2200 per night.");
                break;
            case 4:
                System.out.println("Deluxe Single Room: 1 single bed, Rs.1200 per night.");
                break;
            default:
                System.out.println("Invalid room type.");
        }
    }

    // Check Room Availability
    static void checkAvailability(int roomType) {
        int count = 0;
        switch (roomType) {
            case 1:
                for (Room room : roomHolder.luxuryDoubleRooms) {
                    if (room == null) count++;
                }
                break;
            case 2:
                for (Room room : roomHolder.deluxeDoubleRooms) {
                    if (room == null) count++;
                }
                break;
            case 3:
                for (Room room : roomHolder.luxurySingleRooms) {
                    if (room == null) count++;
                }
                break;
            case 4:
                for (Room room : roomHolder.deluxeSingleRooms) {
                    if (room == null) count++;
                }
                break;
            default:
                System.out.println("Invalid room type.");
        }
        System.out.println("Available rooms: " + count);
    }

    // Book Room
    static void bookRoom(int roomType) {
        System.out.println("Enter customer name: ");
        String name = sc.next();
        System.out.println("Enter contact number: ");
        String contact = sc.next();
        System.out.println("Enter gender: ");
        String gender = sc.next();
    
        PricingStrategy pricingStrategy = new StandardPricing();
        Room room;
        String roomTypeName;
    
        if (roomType == 1) {
            roomTypeName = "luxury double";
        } else if (roomType == 2) {
            roomTypeName = "deluxe double";
        } else if (roomType == 3) {
            roomTypeName = "luxury single";
        } else if (roomType == 4) {
            roomTypeName = "deluxe single";
        } else {
            System.out.println("Invalid room type.");
            return;
        }
    
        room = RoomFactory.createRoom(roomTypeName, pricingStrategy);
    
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
                rooms[i] = room;
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
        while (true) {
            System.out.println("\nEnter your choice:");
            System.out.println("1. Display room details");
            System.out.println("2. Check room availability");
            System.out.println("3. Book room");
            System.out.println("4. Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("Enter room type:");
                    System.out.println("1. Luxury Double Room");
                    System.out.println("2. Deluxe Double Room");
                    System.out.println("3. Luxury Single Room");
                    System.out.println("4. Deluxe Single Room");
                    int roomType = sc.nextInt();
                    Hotel.displayFeatures(roomType);
                    break;
                case 2:
                    System.out.println("Enter room type:");
                    roomType = sc.nextInt();
                    Hotel.checkAvailability(roomType);
                    break;
                case 3:
                    System.out.println("Enter room type:");
                    roomType = sc.nextInt();
                    Hotel.bookRoom(roomType);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}