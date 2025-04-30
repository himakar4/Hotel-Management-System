public class Main {
    public static void main(String[] args) {
        HotelManagementGUI frame = new HotelManagementGUI();
        frame.setTitle("Hotel Management System");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Center the window
        frame.setDefaultCloseOperation(HotelManagementGUI.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
