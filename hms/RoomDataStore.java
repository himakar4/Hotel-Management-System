import java.sql.*;
import java.util.ArrayList;

public class RoomDataStore {
    private static RoomDataStore instance;
    private ArrayList<Room> roomList;

    private RoomDataStore() {
        roomList = new ArrayList<>();
        initializeDatabase();
        loadRooms();
    }

    public static RoomDataStore getInstance() {
        if (instance == null) {
            instance = new RoomDataStore();
        }
        return instance;
    }

    private void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String createTableSQL = "CREATE TABLE IF NOT EXISTS rooms (" +
                    "room_id INT PRIMARY KEY, " +
                    "room_type VARCHAR(50), " +
                    "is_available BOOLEAN, " +
                    "price DOUBLE, " +
                    "capacity INT, " +
                    "amenities VARCHAR(255), " +
                    "needs_cleaning BOOLEAN, " +
                    "under_maintenance BOOLEAN" +
                    ")";
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    private void loadRooms() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rooms")) {
            
            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_type"),
                    rs.getBoolean("is_available"),
                    rs.getDouble("price"),
                    rs.getInt("capacity"),
                    rs.getString("amenities"),
                    rs.getBoolean("needs_cleaning"),
                    rs.getBoolean("under_maintenance")
                );
                roomList.add(room);
            }
        } catch (SQLException e) {
            System.out.println("Error loading rooms: " + e.getMessage());
        }
    }

    public void addRoom(Room room) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO rooms (room_id, room_type, is_available, price, capacity, amenities, needs_cleaning, under_maintenance) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            
            pstmt.setInt(1, room.getRoomId());
            pstmt.setString(2, room.getRoomType());
            pstmt.setBoolean(3, room.isAvailable());
            pstmt.setDouble(4, room.getPrice());
            pstmt.setInt(5, room.getCapacity());
            pstmt.setString(6, room.getAmenities());
            pstmt.setBoolean(7, room.needsCleaning());
            pstmt.setBoolean(8, room.isUnderMaintenance());
            
            pstmt.executeUpdate();
            roomList.add(room);
        } catch (SQLException e) {
            System.out.println("Error adding room: " + e.getMessage());
        }
    }

    public Room getRoomById(int roomId) {
        for (Room room : roomList) {
            if (room.getRoomId() == roomId) {
                return room;
            }
        }
        return null;
    }

    public void updateRoom(Room room) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE rooms SET room_type = ?, is_available = ?, price = ?, capacity = ?, " +
                 "amenities = ?, needs_cleaning = ?, under_maintenance = ? WHERE room_id = ?")) {
            
            pstmt.setString(1, room.getRoomType());
            pstmt.setBoolean(2, room.isAvailable());
            pstmt.setDouble(3, room.getPrice());
            pstmt.setInt(4, room.getCapacity());
            pstmt.setString(5, room.getAmenities());
            pstmt.setBoolean(6, room.needsCleaning());
            pstmt.setBoolean(7, room.isUnderMaintenance());
            pstmt.setInt(8, room.getRoomId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating room: " + e.getMessage());
        }
    }

    public void deleteRoom(int roomId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM rooms WHERE room_id = ?")) {
            
            pstmt.setInt(1, roomId);
            pstmt.executeUpdate();
            roomList.removeIf(room -> room.getRoomId() == roomId);
        } catch (SQLException e) {
            System.out.println("Error deleting room: " + e.getMessage());
        }
    }

    public ArrayList<Room> getAllRooms() {
        return new ArrayList<>(roomList);
    }
}
