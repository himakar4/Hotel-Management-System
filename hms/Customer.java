import java.time.LocalDate;

public class Customer {
    private int customerId;
    private String name;
    private String email;
    private String phone;
    private int roomId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalAmount;
    private boolean checkedIn;
    private String paymentStatus;

    public Customer(int customerId, String name, String email, String phone, int roomId, 
                   LocalDate checkInDate, LocalDate checkOutDate, double totalAmount) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalAmount = totalAmount;
        this.checkedIn = false;
        this.paymentStatus = "Pending";
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return String.format("Customer %d: %s (Room: %d, Check-in: %s, Check-out: %s)",
                customerId, name, roomId, checkInDate, checkOutDate);
    }
}
