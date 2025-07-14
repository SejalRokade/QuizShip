import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class DeactivateBatchGUI {

    private Homepage homepage;
    private static UserDA userDA = new UserDA();

    public JPanel deactivateBatchPanel() {

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE); // Set background color to white

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 102));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(800, 60)); // Set a thinner height for the header

        // Header Label
        JLabel headerLabel = new JLabel("Deactivate Batch");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Slightly reduced font size
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.LEFT); // Left align the header
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Buttons Panel (Dashboard and Logout)
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(0, 102, 102)); // Matching background color
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right

        JButton dashboardButton = new JButton("Dashboard");
        JButton logoutButton = new JButton("Logout");

        // Set button styles
        dashboardButton.setFont(new Font("Arial", Font.PLAIN, 12));
        dashboardButton.setPreferredSize(new Dimension(100, 30));

        logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutButton.setPreferredSize(new Dimension(100, 30));

        // Add buttons to the panel
        buttonsPanel.add(dashboardButton);
        buttonsPanel.add(logoutButton);

        // Add buttons panel to the header
        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        // Add header to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE); // Ensure the center panel is white
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 10, 20, 10); // Padding between components

        // Label and Dropdown for Selecting Batch to Deactivate
        JLabel selectBatchLabel = new JLabel("Select batch to be deleted:");
        selectBatchLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased

        // Create a blank JComboBox (without fetching batch names yet)
        JComboBox<String> batchDropdown = new JComboBox<>();
        batchDropdown.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased

        // Deactivate Button
        JButton deactivateBatchButton = new JButton("DEACTIVATE");
        deactivateBatchButton.setFont(new Font("Arial", Font.BOLD, 18)); // Large font for the button
        deactivateBatchButton.setPreferredSize(new Dimension(150, 40)); // Button size increased
        deactivateBatchButton.setBackground(new Color(0, 102, 102));
        deactivateBatchButton.setForeground(Color.WHITE);

        // Add components to the panel using GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(selectBatchLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(batchDropdown, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(deactivateBatchButton, gbc);

        // Add the center panel to the main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Action Listeners for Dashboard and Logout Buttons
        dashboardButton.addActionListener(e -> {
            //JOptionPane.showMessageDialog(null, "Navigating to Dashboard...");
            Homepage.cardLayout.show(Homepage.cards, "T_DashBoard");
        });

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logging out...");
            Homepage.cardLayout.show(Homepage.cards, "HOMEPAGE");
        });

        // Action Listener for Deactivate Button
        deactivateBatchButton.addActionListener(e -> {
            String selectedBatch = (String) batchDropdown.getSelectedItem();
            boolean success = UserDA.deactivateBatch(selectedBatch); // Call the backend function
            if (success) {
                JOptionPane.showMessageDialog(null, "Batch " + selectedBatch + " has been deactivated.");
                Homepage.cardLayout.show(Homepage.cards, "T_DashBoard");

            } else {
                JOptionPane.showMessageDialog(null, "Error deactivating batch " + selectedBatch + ".");
            }
        });

        // Add a component listener to populate the dropdown when the panel is shown
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Fetch batch names when the panel is shown
                List<String> batchNamesList = UserDA.getBatchNames();
                batchDropdown.removeAllItems(); // Clear existing items
                for (String batchName : batchNamesList) {
                    batchDropdown.addItem(batchName); // Add batch names dynamically
                }
            }
        });

        return mainPanel;
    }
}
