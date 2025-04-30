import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomerManagementGUI extends JFrame {
    private JPanel panel;
    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton removeButton;
    private JButton checkInButton;
    private JButton checkOutButton;
    private JButton updateButton;
    private JTextField searchField;
    private ArrayList<Customer> customers;
    private ArrayList<Room> rooms;

    public CustomerManagementGUI(ArrayList<Customer> customers, ArrayList<Room> rooms) {
        this.customers = customers;
        this.rooms = rooms;

        setTitle("Customer Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Create main panel with gradient background
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(0, 102, 204);
                Color color2 = new Color(0, 153, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new BorderLayout());

        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Customer Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Create table model
        String[] columnNames = {"Customer ID", "Name", "Email", "Phone", "Room ID", 
                              "Check-in Date", "Check-out Date", "Total Amount", 
                              "Checked In", "Payment Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        customerTable = new JTable(tableModel);
        customerTable.setFont(new Font("Arial", Font.PLAIN, 14));
        customerTable.setRowHeight(30);
        customerTable.setSelectionBackground(new Color(0, 153, 255));
        customerTable.setSelectionForeground(Color.WHITE);
        
        // Style table header
        JTableHeader header = customerTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(0, 102, 204));
        header.setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(customerTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        addButton = createStyledButton("Add Customer");
        removeButton = createStyledButton("Remove Customer");
        checkInButton = createStyledButton("Check In");
        checkOutButton = createStyledButton("Check Out");
        updateButton = createStyledButton("Update Customer");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(updateButton);

        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JButton searchButton = createStyledButton("Search");
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Create bottom panel to hold both search and button panels
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(searchPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addCustomer());
        removeButton.addActionListener(e -> removeCustomer());
        checkInButton.addActionListener(e -> checkIn());
        checkOutButton.addActionListener(e -> checkOut());
        updateButton.addActionListener(e -> updateCustomer());
        searchButton.addActionListener(e -> searchCustomers());

        add(panel);
        refreshTable();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 102, 204));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 153, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));
            }
        });
        
        return button;
    }

    private void refreshTable() {
        // Reload customers from database
        customers = CustomerDataStore.getInstance().getAllCustomers();
        
        tableModel.setRowCount(0);
        for (Customer customer : customers) {
            Object[] rowData = {
                customer.getCustomerId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getRoomId(),
                customer.getCheckInDate(),
                customer.getCheckOutDate(),
                customer.getTotalAmount(),
                customer.isCheckedIn() ? "Yes" : "No",
                customer.getPaymentStatus()
            };
            tableModel.addRow(rowData);
        }
    }

    private void addCustomer() {
        JPanel inputPanel = new JPanel(new GridLayout(8, 2));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField checkInField = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        JTextField checkOutField = new JTextField(LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE));

        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Email:"));
        inputPanel.add(emailField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        inputPanel.add(checkInField);
        inputPanel.add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        inputPanel.add(checkOutField);

        // Get available rooms
        ArrayList<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isAvailable() && !room.isUnderMaintenance()) {
                availableRooms.add(room);
            }
        }

        if (availableRooms.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No available rooms!");
            return;
        }

        JComboBox<Room> roomComboBox = new JComboBox<>(availableRooms.toArray(new Room[0]));
        inputPanel.add(new JLabel("Select Room:"));
        inputPanel.add(roomComboBox);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add New Customer",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int customerId = customers.size() + 1;
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                LocalDate checkInDate = LocalDate.parse(checkInField.getText());
                LocalDate checkOutDate = LocalDate.parse(checkOutField.getText());
                Room selectedRoom = (Room) roomComboBox.getSelectedItem();
                double totalAmount = selectedRoom.getPrice() * 
                    (checkOutDate.toEpochDay() - checkInDate.toEpochDay());

                Customer newCustomer = new Customer(customerId, name, email, phone, 
                    selectedRoom.getRoomId(), checkInDate, checkOutDate, totalAmount);
                CustomerDataStore.getInstance().addCustomer(newCustomer);
                selectedRoom.setAvailable(false);
                RoomDataStore.getInstance().updateRoom(selectedRoom);
                refreshTable();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check your entries.");
            }
        }
    }

    private void removeCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            Customer customer = CustomerDataStore.getInstance().getCustomerById(customerId);
            if (customer != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to remove this customer?",
                        "Confirm Removal",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Free up the room
                    Room room = RoomDataStore.getInstance().getRoomById(customer.getRoomId());
                    if (room != null) {
                        room.setAvailable(true);
                        RoomDataStore.getInstance().updateRoom(room);
                    }
                    CustomerDataStore.getInstance().deleteCustomer(customerId);
                    refreshTable();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to remove.");
        }
    }

    private void checkIn() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            Customer customer = CustomerDataStore.getInstance().getCustomerById(customerId);
            if (customer != null) {
                customer.setCheckedIn(true);
                CustomerDataStore.getInstance().updateCustomer(customer);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to check in.");
        }
    }

    private void checkOut() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            Customer customer = CustomerDataStore.getInstance().getCustomerById(customerId);
            if (customer != null) {
                customer.setCheckedIn(false);
                // Free up the room
                Room room = RoomDataStore.getInstance().getRoomById(customer.getRoomId());
                if (room != null) {
                    room.setAvailable(true);
                    room.setNeedsCleaning(true);
                    RoomDataStore.getInstance().updateRoom(room);
                }
                CustomerDataStore.getInstance().updateCustomer(customer);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to check out.");
        }
    }

    private void updateCustomer() {
        int selectedRow = customerTable.getSelectedRow();
        if (selectedRow != -1) {
            int customerId = (int) tableModel.getValueAt(selectedRow, 0);
            Customer customer = CustomerDataStore.getInstance().getCustomerById(customerId);
            if (customer != null) {
                JPanel inputPanel = new JPanel(new GridLayout(8, 2));
                JTextField nameField = new JTextField(customer.getName());
                JTextField emailField = new JTextField(customer.getEmail());
                JTextField phoneField = new JTextField(customer.getPhone());
                JTextField checkInField = new JTextField(customer.getCheckInDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                JTextField checkOutField = new JTextField(customer.getCheckOutDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                JTextField paymentStatusField = new JTextField(customer.getPaymentStatus());

                inputPanel.add(new JLabel("Name:"));
                inputPanel.add(nameField);
                inputPanel.add(new JLabel("Email:"));
                inputPanel.add(emailField);
                inputPanel.add(new JLabel("Phone:"));
                inputPanel.add(phoneField);
                inputPanel.add(new JLabel("Check-in Date (YYYY-MM-DD):"));
                inputPanel.add(checkInField);
                inputPanel.add(new JLabel("Check-out Date (YYYY-MM-DD):"));
                inputPanel.add(checkOutField);
                inputPanel.add(new JLabel("Payment Status:"));
                inputPanel.add(paymentStatusField);

                int result = JOptionPane.showConfirmDialog(this, inputPanel, "Update Customer",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        customer.setName(nameField.getText());
                        customer.setEmail(emailField.getText());
                        customer.setPhone(phoneField.getText());
                        customer.setCheckInDate(LocalDate.parse(checkInField.getText()));
                        customer.setCheckOutDate(LocalDate.parse(checkOutField.getText()));
                        customer.setPaymentStatus(paymentStatusField.getText());
                        CustomerDataStore.getInstance().updateCustomer(customer);
                        refreshTable();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Invalid input. Please check your entries.");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a customer to update.");
        }
    }

    private void searchCustomers() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        for (Customer customer : customers) {
            if (String.valueOf(customer.getCustomerId()).contains(searchText) ||
                customer.getName().toLowerCase().contains(searchText) ||
                customer.getEmail().toLowerCase().contains(searchText) ||
                customer.getPhone().contains(searchText)) {
                Object[] rowData = {
                    customer.getCustomerId(),
                    customer.getName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getRoomId(),
                    customer.getCheckInDate(),
                    customer.getCheckOutDate(),
                    customer.getTotalAmount(),
                    customer.isCheckedIn() ? "Yes" : "No",
                    customer.getPaymentStatus()
                };
                tableModel.addRow(rowData);
            }
        }
    }
} 