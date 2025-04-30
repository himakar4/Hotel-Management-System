import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class CustomerDataStore {
    private static CustomerDataStore instance;
    private ArrayList<Customer> customerList;

    private CustomerDataStore() {
        customerList = new ArrayList<>();
        initializeDatabase();
        loadCustomers();
    }

    public static CustomerDataStore getInstance() {
        if (instance == null) {
            instance = new CustomerDataStore();
        }
        return instance;
    }

    private void initializeDatabase() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            String createTableSQL = "CREATE TABLE IF NOT EXISTS customers (" +
                    "customer_id INT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "email VARCHAR(100), " +
                    "phone VARCHAR(20), " +
                    "room_id INT, " +
                    "check_in_date DATE, " +
                    "check_out_date DATE, " +
                    "total_amount DOUBLE, " +
                    "checked_in BOOLEAN, " +
                    "payment_status VARCHAR(20), " +
                    "FOREIGN KEY (room_id) REFERENCES rooms(room_id)" +
                    ")";
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    private void loadCustomers() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers")) {
            
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getInt("room_id"),
                    rs.getDate("check_in_date").toLocalDate(),
                    rs.getDate("check_out_date").toLocalDate(),
                    rs.getDouble("total_amount")
                );
                customer.setCheckedIn(rs.getBoolean("checked_in"));
                customer.setPaymentStatus(rs.getString("payment_status"));
                customerList.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }

    public void addCustomer(Customer customer) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO customers (customer_id, name, email, phone, room_id, " +
                 "check_in_date, check_out_date, total_amount, checked_in, payment_status) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            
            pstmt.setInt(1, customer.getCustomerId());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPhone());
            pstmt.setInt(5, customer.getRoomId());
            pstmt.setDate(6, Date.valueOf(customer.getCheckInDate()));
            pstmt.setDate(7, Date.valueOf(customer.getCheckOutDate()));
            pstmt.setDouble(8, customer.getTotalAmount());
            pstmt.setBoolean(9, customer.isCheckedIn());
            pstmt.setString(10, customer.getPaymentStatus());
            
            pstmt.executeUpdate();
            customerList.add(customer);
        } catch (SQLException e) {
            System.out.println("Error adding customer: " + e.getMessage());
        }
    }

    public Customer getCustomerById(int customerId) {
        for (Customer customer : customerList) {
            if (customer.getCustomerId() == customerId) {
                return customer;
            }
        }
        return null;
    }

    public ArrayList<Customer> getCustomersByRoomId(int roomId) {
        ArrayList<Customer> result = new ArrayList<>();
        for (Customer customer : customerList) {
            if (customer.getRoomId() == roomId) {
                result.add(customer);
            }
        }
        return result;
    }

    public ArrayList<Customer> getCustomersByDate(LocalDate date) {
        ArrayList<Customer> result = new ArrayList<>();
        for (Customer customer : customerList) {
            if (!date.isBefore(customer.getCheckInDate()) && !date.isAfter(customer.getCheckOutDate())) {
                result.add(customer);
            }
        }
        return result;
    }

    public void updateCustomer(Customer customer) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE customers SET name = ?, email = ?, phone = ?, room_id = ?, " +
                 "check_in_date = ?, check_out_date = ?, total_amount = ?, " +
                 "checked_in = ?, payment_status = ? WHERE customer_id = ?")) {
            
            pstmt.setString(1, customer.getName());
            pstmt.setString(2, customer.getEmail());
            pstmt.setString(3, customer.getPhone());
            pstmt.setInt(4, customer.getRoomId());
            pstmt.setDate(5, Date.valueOf(customer.getCheckInDate()));
            pstmt.setDate(6, Date.valueOf(customer.getCheckOutDate()));
            pstmt.setDouble(7, customer.getTotalAmount());
            pstmt.setBoolean(8, customer.isCheckedIn());
            pstmt.setString(9, customer.getPaymentStatus());
            pstmt.setInt(10, customer.getCustomerId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

    public void deleteCustomer(int customerId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM customers WHERE customer_id = ?")) {
            
            pstmt.setInt(1, customerId);
            pstmt.executeUpdate();
            customerList.removeIf(customer -> customer.getCustomerId() == customerId);
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }

    public ArrayList<Customer> getAllCustomers() {
        return new ArrayList<>(customerList);
    }
} 