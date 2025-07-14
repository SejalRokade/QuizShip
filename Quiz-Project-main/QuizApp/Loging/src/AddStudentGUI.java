import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

public class AddStudentGUI {

    private Homepage homepage;
    private static UserDA userDA = new UserDA();

    public JPanel addStudentPanel() {

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE); // Set background color to white

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 102));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(600, 50)); // Header height

        // Header Label
        JLabel headerLabel = new JLabel("Add Student To Batch");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(0, 102, 102));
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton dashboardButton = new JButton("Dashboard");
        JButton logoutButton = new JButton("Logout");

        // Set button styles
        dashboardButton.setFont(new Font("Arial", Font.PLAIN, 12));
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));

        buttonsPanel.add(dashboardButton);
        buttonsPanel.add(logoutButton);

        headerPanel.add(buttonsPanel, BorderLayout.EAST);

        // Center Panel with Titled Border
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 10, 15, 10); // Padding between components

        // Student Name, Email, Batch Dropdown
        JLabel studentNameLabel = new JLabel("Student Name:");
        JLabel studentEmailLabel = new JLabel("Student Email:");
        JLabel selectBatchLabel = new JLabel("Select Batch:");

        studentNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        studentEmailLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        selectBatchLabel.setFont(new Font("Arial", Font.PLAIN, 20));

        JTextField studentNameField = new JTextField();
        JTextField studentEmailField = new JTextField();
        studentNameField.setFont(new Font("Arial", Font.PLAIN, 20));
        studentEmailField.setFont(new Font("Arial", Font.PLAIN, 20));

        // Set preferred size for text fields
        studentNameField.setPreferredSize(new Dimension(300, 30)); // Width: 300, Height: 30
        studentEmailField.setPreferredSize(new Dimension(300, 30)); // Width: 300, Height: 30
        selectBatchLabel.setPreferredSize(new Dimension(300, 30));


        // Batch Dropdown - Initially empty
        JComboBox<String> batchDropdown = new JComboBox<>();
        batchDropdown.setFont(new Font("Arial", Font.PLAIN, 20));

        JButton addButton = new JButton("ADD");
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        addButton.setBackground(new Color(0, 102, 102));
        addButton.setForeground(Color.WHITE);

        // Add components using GridBagLayout
        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(studentNameLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(studentNameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(studentEmailLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(studentEmailField, gbc);
        gbc.gridx = 0; gbc.gridy = 4;
        centerPanel.add(selectBatchLabel, gbc);
        gbc.gridx = 0; gbc.gridy = 5;
        centerPanel.add(batchDropdown, gbc);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        centerPanel.add(addButton, gbc);

        // Add panels to frame
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Action Listeners for Buttons
        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(null, "Navigating to Dashboard...");
                Homepage.cardLayout.show(Homepage.cards, "T_DashBoard");
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Logging out...");
                Homepage.cardLayout.show(Homepage.cards, "HOMEPAGE");
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Capture inputs from the form
                String studentName = studentNameField.getText();
                String studentEmail = studentEmailField.getText();
                String selectedBatch = (String) batchDropdown.getSelectedItem();
                UserDA userDA1 = new UserDA();

                // Call the backend method to add the student to the batch
                if (Login.addStudentToBatch(studentName, studentEmail, selectedBatch, userDA1)) {
                    JOptionPane.showMessageDialog(null, "Student " + studentName + " successfully added to batch: " + selectedBatch);
                    studentNameField.setText("");
                    studentEmailField.setText("");

                    Homepage.cardLayout.show(Homepage.cards, "T_DashBoard");
                } else {
                    JOptionPane.showMessageDialog(null, "Error in adding student to batch.");
                }
            }
        });

        // Add a component listener to populate the dropdown when the panel is shown
        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                // Fetch batch names when the panel is shown
                List<String> availableBatches = userDA.getAvailableBatches();
                batchDropdown.removeAllItems(); // Clear existing items
                for (String batch : availableBatches) {
                    batchDropdown.addItem(batch); // Add batch names dynamically
                }
            }
        });

        return mainPanel;
    }
}
