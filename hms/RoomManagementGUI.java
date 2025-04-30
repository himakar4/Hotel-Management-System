import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Toolkit;

public class RoomManagementGUI extends JFrame {
    private JPanel panel;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JButton removeButton;
    private JButton updateButton;
    private JButton toggleMaintenanceButton;
    private JButton toggleCleaningButton;
    private JTextField searchField;
    private ArrayList<Room> rooms;
    private BufferedImage backgroundImage;
    private Timer fadeTimer;
    private float alpha = 0.0f;

    public RoomManagementGUI(ArrayList<Room> rooms) {
        this.rooms = rooms;

        // Load background image
        try {
            backgroundImage = ImageIO.read(new File("resources/hotel_background.jpg"));
        } catch (IOException e) {
            backgroundImage = null;
        }

        setTitle("Room Management");
        setSize(1200, 800);
        setLocationRelativeTo(null);

        // Create main panel with animated background
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background image with fade effect
                if (backgroundImage != null) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    // Fallback gradient background
                    Color color1 = new Color(0, 102, 204);
                    Color color2 = new Color(0, 153, 255);
                    GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panel.setLayout(new BorderLayout());

        // Create title panel with animation
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Room Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Create table model with hover effects
        String[] columnNames = {"Room ID", "Type", "Price", "Capacity", "Amenities", 
                              "Available", "Needs Cleaning", "Under Maintenance"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(0, 153, 255, 150));
                } else if (row % 2 == 0) {
                    c.setBackground(new Color(255, 255, 255, 50));
                } else {
                    c.setBackground(new Color(255, 255, 255, 30));
                }
                return c;
            }
        };
        roomTable.setFont(new Font("Arial", Font.PLAIN, 14));
        roomTable.setRowHeight(40);
        roomTable.setSelectionBackground(new Color(0, 153, 255, 150));
        roomTable.setSelectionForeground(Color.WHITE);
        roomTable.setGridColor(new Color(200, 200, 200, 100));
        roomTable.setShowGrid(true);
        roomTable.setOpaque(false);
        
        // Style table header with animation
        JTableHeader header = roomTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(0, 102, 204, 200));
        header.setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create buttons panel with hover animations
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        addButton = createAnimatedButton("Add Room", "resources/add_icon.png");
        removeButton = createAnimatedButton("Remove Room", "resources/remove_icon.png");
        updateButton = createAnimatedButton("Update Room", "resources/update_icon.png");
        toggleMaintenanceButton = createAnimatedButton("Toggle Maintenance", "resources/maintenance_icon.png");
        toggleCleaningButton = createAnimatedButton("Toggle Cleaning", "resources/cleaning_icon.png");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(toggleMaintenanceButton);
        buttonPanel.add(toggleCleaningButton);

        // Create search panel with animation
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        searchField = new JTextField(30) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 50));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        searchField.setOpaque(false);
        searchField.setForeground(Color.WHITE);
        
        JButton searchButton = createAnimatedButton("Search", "resources/search_icon.png");
        
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
        addButton.addActionListener(e -> addRoom());
        removeButton.addActionListener(e -> removeRoom());
        updateButton.addActionListener(e -> updateRoom());
        toggleMaintenanceButton.addActionListener(e -> toggleMaintenance());
        toggleCleaningButton.addActionListener(e -> toggleCleaning());
        searchButton.addActionListener(e -> searchRooms());

        // Start fade-in animation
        fadeTimer = new Timer(20, e -> {
            alpha += 0.05f;
            if (alpha >= 1.0f) {
                alpha = 1.0f;
                fadeTimer.stop();
            }
            panel.repaint();
        });
        fadeTimer.start();

        add(panel);
        refreshTable();
    }

    private JButton createAnimatedButton(String text, String iconPath) {
        JButton button = new JButton(text) {
            private float hoverAlpha = 0.0f;
            private Timer hoverTimer;
            private BufferedImage icon;

            {
                try {
                    icon = ImageIO.read(new File(iconPath));
                } catch (IOException e) {
                    icon = null;
                }

                hoverTimer = new Timer(20, e -> {
                    if (getModel().isRollover()) {
                        hoverAlpha = Math.min(1.0f, hoverAlpha + 0.1f);
                    } else {
                        hoverAlpha = Math.max(0.0f, hoverAlpha - 0.1f);
                    }
                    if (hoverAlpha == 0.0f || hoverAlpha == 1.0f) {
                        hoverTimer.stop();
                    }
                    repaint();
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Draw button background
                g2d.setColor(new Color(0, 102, 204, (int)(200 * (1 - hoverAlpha))));
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Draw hover effect
                g2d.setColor(new Color(0, 153, 255, (int)(200 * hoverAlpha)));
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                
                // Draw icon if available
                if (icon != null) {
                    int iconSize = 20;
                    int iconX = 10;
                    int iconY = (getHeight() - iconSize) / 2;
                    g2d.drawImage(icon, iconX, iconY, iconSize, iconSize, null);
                }
                
                // Draw text
                g2d.setColor(Color.WHITE);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = icon != null ? 40 : (getWidth() - fm.stringWidth(getText())) / 2;
                int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2d.drawString(getText(), textX, textY);
            }

            @Override
            public void addNotify() {
                super.addNotify();
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        hoverTimer.start();
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        hoverTimer.start();
                    }
                });
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 40));
        
        return button;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Room room : rooms) {
            Object[] rowData = {
                room.getRoomId(),
                room.getRoomType(),
                room.getPrice(),
                room.getCapacity(),
                room.getAmenities(),
                room.isAvailable() ? "Yes" : "No",
                room.needsCleaning() ? "Yes" : "No",
                room.isUnderMaintenance() ? "Yes" : "No"
            };
            tableModel.addRow(rowData);
        }
    }

    private void addRoom() {
        JPanel inputPanel = new JPanel(new GridLayout(7, 2));
        JTextField roomIdField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField capacityField = new JTextField();
        JTextField amenitiesField = new JTextField();

        inputPanel.add(new JLabel("Room ID:"));
        inputPanel.add(roomIdField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Capacity:"));
        inputPanel.add(capacityField);
        inputPanel.add(new JLabel("Amenities:"));
        inputPanel.add(amenitiesField);

        int result = JOptionPane.showConfirmDialog(this, inputPanel, "Add New Room",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int roomId = Integer.parseInt(roomIdField.getText());
                String type = typeField.getText();
                double price = Double.parseDouble(priceField.getText());
                int capacity = Integer.parseInt(capacityField.getText());
                String amenities = amenitiesField.getText();

                Room newRoom = new Room(roomId, type, true, price, capacity, amenities, false, false);
                RoomDataStore.getInstance().addRoom(newRoom);
                rooms.add(newRoom);
                refreshTable();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please check your entries.");
            }
        }
    }

    private void removeRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) {
            int roomId = (int) tableModel.getValueAt(selectedRow, 0);
            Room room = RoomDataStore.getInstance().getRoomById(roomId);
            if (room != null) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to remove this room?",
                        "Confirm Removal",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    RoomDataStore.getInstance().deleteRoom(roomId);
                    rooms.remove(room);
                    refreshTable();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room to remove.");
        }
    }

    private void updateRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) {
            int roomId = (int) tableModel.getValueAt(selectedRow, 0);
            Room room = RoomDataStore.getInstance().getRoomById(roomId);
            if (room != null) {
                JPanel inputPanel = new JPanel(new GridLayout(7, 2));
                JTextField typeField = new JTextField(room.getRoomType());
                JTextField priceField = new JTextField(String.valueOf(room.getPrice()));
                JTextField capacityField = new JTextField(String.valueOf(room.getCapacity()));
                JTextField amenitiesField = new JTextField(room.getAmenities());

                inputPanel.add(new JLabel("Type:"));
                inputPanel.add(typeField);
                inputPanel.add(new JLabel("Price:"));
                inputPanel.add(priceField);
                inputPanel.add(new JLabel("Capacity:"));
                inputPanel.add(capacityField);
                inputPanel.add(new JLabel("Amenities:"));
                inputPanel.add(amenitiesField);

                int result = JOptionPane.showConfirmDialog(this, inputPanel, "Update Room",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        room.setRoomType(typeField.getText());
                        room.setPrice(Double.parseDouble(priceField.getText()));
                        room.setCapacity(Integer.parseInt(capacityField.getText()));
                        room.setAmenities(amenitiesField.getText());
                        RoomDataStore.getInstance().updateRoom(room);
                        refreshTable();
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Invalid input. Please check your entries.");
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room to update.");
        }
    }

    private void toggleMaintenance() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) {
            int roomId = (int) tableModel.getValueAt(selectedRow, 0);
            Room room = RoomDataStore.getInstance().getRoomById(roomId);
            if (room != null) {
                room.setUnderMaintenance(!room.isUnderMaintenance());
                RoomDataStore.getInstance().updateRoom(room);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room to toggle maintenance.");
        }
    }

    private void toggleCleaning() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow != -1) {
            int roomId = (int) tableModel.getValueAt(selectedRow, 0);
            Room room = RoomDataStore.getInstance().getRoomById(roomId);
            if (room != null) {
                room.setNeedsCleaning(!room.needsCleaning());
                RoomDataStore.getInstance().updateRoom(room);
                refreshTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a room to toggle cleaning status.");
        }
    }

    private void searchRooms() {
        String searchText = searchField.getText().toLowerCase();
        tableModel.setRowCount(0);
        for (Room room : rooms) {
            if (String.valueOf(room.getRoomId()).contains(searchText) ||
                room.getRoomType().toLowerCase().contains(searchText) ||
                room.getAmenities().toLowerCase().contains(searchText)) {
                Object[] rowData = {
                    room.getRoomId(),
                    room.getRoomType(),
                    room.getPrice(),
                    room.getCapacity(),
                    room.getAmenities(),
                    room.isAvailable() ? "Yes" : "No",
                    room.needsCleaning() ? "Yes" : "No",
                    room.isUnderMaintenance() ? "Yes" : "No"
                };
                tableModel.addRow(rowData);
            }
        }
    }
}
