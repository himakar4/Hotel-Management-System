public class Room {
    private int roomId;
    private String roomType;
    private boolean isAvailable;
    private double price;
    private int capacity;
    private String amenities;
    private boolean needsCleaning;
    private boolean underMaintenance;

    public Room(int roomId, String roomType, boolean isAvailable, double price, 
                int capacity, String amenities, boolean needsCleaning, boolean underMaintenance) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.isAvailable = isAvailable;
        this.price = price;
        this.capacity = capacity;
        this.amenities = amenities;
        this.needsCleaning = needsCleaning;
        this.underMaintenance = underMaintenance;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public boolean needsCleaning() {
        return needsCleaning;
    }

    public void setNeedsCleaning(boolean needsCleaning) {
        this.needsCleaning = needsCleaning;
    }

    public boolean isUnderMaintenance() {
        return underMaintenance;
    }

    public void setUnderMaintenance(boolean underMaintenance) {
        this.underMaintenance = underMaintenance;
    }

    @Override
    public String toString() {
        return String.format("Room %d - %s (Capacity: %d, Price: ₹%.2f, Available: %s)",
                roomId, roomType, capacity, price, isAvailable() ? "Yes" : "No");
    }
}
