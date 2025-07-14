import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateBatchGUI {
    private Homepage homepage;
    private String email;

    public CreateBatchGUI(String email){
        this.email = email;
    }
    public JPanel createBatchPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 102));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(800, 50)); // Set a thinner height for the header

        // Header Label
        JLabel headerLabel = new JLabel("Create Batch");
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

        // Label and TextField for Entering Batch Name
        JLabel batchNameLabel = new JLabel("Enter name of batch:");
        batchNameLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased
        JTextField batchNameField = new JTextField(20);
        batchNameField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased
        batchNameField.setPreferredSize(new Dimension(300, 40)); // Size increased for TextField
/*
        // Label and TextField for Entering Email
        JLabel emailLabel = new JLabel("Enter Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased
        JTextField emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased
        emailField.setPreferredSize(new Dimension(300, 40)); // Size increased for TextField
*/
        // Create Button
        JButton createBatchButton = new JButton("CREATE");
        createBatchButton.setFont(new Font("Arial", Font.BOLD, 18)); // Large font for the button
        createBatchButton.setPreferredSize(new Dimension(150, 40)); // Button size increased
        createBatchButton.setBackground(new Color(0, 102, 102));
        createBatchButton.setForeground(Color.WHITE);


        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(batchNameLabel, gbc);
        gbc.gridy = 1;
        centerPanel.add(batchNameField, gbc);
        gbc.gridy = 2;
  /*      centerPanel.add(emailLabel, gbc);
        gbc.gridy = 3;
        centerPanel.add(emailField, gbc);
   */     gbc.gridy = 4;
        centerPanel.add(createBatchButton, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Action Listeners for Dashboard and Logout Buttons
        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // JOptionPane.showMessageDialog(null, "Navigating to Dashboard...");
                Homepage.cardLayout.show(Homepage.cards, "T_DashBoard");

            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Logging out...");
                // After successful registration, go back to Homepage
                Homepage.cardLayout.show(Homepage.cards, "HOMEPAGE");

            }
        });

        // Action Listener for Create Batch Button
        createBatchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String batchName = batchNameField.getText().trim();
                String email = LoginGUI.email; // Get email input

                if (batchName.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter both batch name and email.");
                } else {
                    // Your code to handle batch creation and email can go here
                    boolean success = UserDA.Batch(email, batchName);

                    if (success) {
                        JOptionPane.showMessageDialog(null, "Batch created successfully.");
                        batchNameField.setText("");
                        // Navigate back to the TeacherDashboard
                        Homepage.cardLayout.show(Homepage.cards, "T_DashBoard");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to create batch. It may already exist.");
                        batchNameField.setText("");
                    }
                }
            }
        });

        return mainPanel;
    }
}
