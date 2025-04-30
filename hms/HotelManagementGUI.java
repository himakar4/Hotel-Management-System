import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class HotelManagementGUI extends JFrame {
    private ArrayList<Room> rooms;
    private ArrayList<Customer> customers;
    private JButton roomManagementButton;
    private JButton customerManagementButton;

    public HotelManagementGUI() {
        rooms = RoomDataStore.getInstance().getAllRooms();
        customers = CustomerDataStore.getInstance().getAllCustomers();

        setTitle("Hotel Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                Color color1 = new Color(0, 102, 204); // Dark blue
                Color color2 = new Color(0, 153, 255); // Light blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Create title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(40, 0, 40, 0));
        JLabel titleLabel = new JLabel("Hotel Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Create button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 200, 100, 200));

        roomManagementButton = createStyledButton("Room Management");
        customerManagementButton = createStyledButton("Customer Management");

        buttonPanel.add(roomManagementButton);
        buttonPanel.add(customerManagementButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Add action listeners
        roomManagementButton.addActionListener(e -> {
            RoomManagementGUI roomGUI = new RoomManagementGUI(rooms);
            roomGUI.setVisible(true);
        });

        customerManagementButton.addActionListener(e -> {
            CustomerManagementGUI customerGUI = new CustomerManagementGUI(customers, rooms);
            customerGUI.setVisible(true);
        });

        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(0, 102, 204));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(0, 153, 255));
                } else {
                    g2d.setColor(new Color(0, 102, 204));
                }
                
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 80));
        
        return button;
    }
}
