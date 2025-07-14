import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AllocateQuizGUI {

    private Homepage homepage;

    public JPanel allocateQuizPanel() {
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE); // Set background color to white

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 102));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(800, 50)); // Set a thinner height for the header

        // Header Label
        JLabel headerLabel = new JLabel("Allocate Quiz");
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

        // Label and Dropdown for Selecting Batch
        JLabel selectBatchLabel = new JLabel("Select Batch:");
        selectBatchLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased

        JComboBox<String> batchDropdown = new JComboBox<>();
        batchDropdown.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased

        // Label and Dropdown for Selecting Quiz
        JLabel selectQuizLabel = new JLabel("Select Quiz:");
        selectQuizLabel.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased

        JComboBox<String> quizDropdown = new JComboBox<>();
        quizDropdown.setFont(new Font("Arial", Font.PLAIN, 18)); // Font size increased

        // Allocate Button
        JButton allocateButton = new JButton("ALLOCATE");
        allocateButton.setFont(new Font("Arial", Font.BOLD, 18)); // Large font for the button
        allocateButton.setPreferredSize(new Dimension(150, 40)); // Button size increased
        allocateButton.setBackground(new Color(0, 102, 102));
        allocateButton.setForeground(Color.WHITE);

        // Add components to the panel using GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(selectBatchLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(batchDropdown, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(selectQuizLabel, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        centerPanel.add(quizDropdown, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        centerPanel.add(allocateButton, gbc);

        // Add the center panel to the main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Populate dropdowns dynamically when the panel is shown
        mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent evt) {
                // Fetch and populate the batch dropdown
                batchDropdown.removeAllItems();
                String[] batches = getBatchNamesFromDatabase();
                for (String batch : batches) {
                    batchDropdown.addItem(batch);
                }

                // Fetch and populate the quiz dropdown
                quizDropdown.removeAllItems();
                String[] quizzes = getQuizNamesFromDatabase();
                for (String quiz : quizzes) {
                    quizDropdown.addItem(quiz);
                }
            }
        });

        // Action Listeners for Dashboard and Logout Buttons
        dashboardButton.addActionListener(e -> {
            //JOptionPane.showMessageDialog(null, "Navigating to Dashboard...");
            Homepage.cardLayout.show(Homepage.cards, "T_DashBoard");
        });

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logging out...");
            Homepage.cardLayout.show(Homepage.cards, "HOMEPAGE");
        });

        // Action Listener for Allocate Button
        allocateButton.addActionListener(e -> {
            String selectedBatch = (String) batchDropdown.getSelectedItem();
            String selectedQuiz = (String) quizDropdown.getSelectedItem();

            // Validate selection
            if (selectedBatch == null || selectedQuiz == null) {
                JOptionPane.showMessageDialog(null, "Please select both a batch and a quiz.");
            } else {
                try (Connection conn = DatabaseUtil.getConnection()) {
                    // Get Quiz and Batch IDs
                    int quizId = getIdFromDatabase(conn, "SELECT quiz_id FROM t_quiz WHERE quiz_name = ?", selectedQuiz);
                    int batchId = getIdFromDatabase(conn, "SELECT batch_id FROM t_batch WHERE batch_name = ?", selectedBatch);

                    // Call assignQuizToBatch method to handle the allocation
                    boolean success = UserDA.assignQuizToBatch(quizId, batchId);

                    if (success) {
                        JOptionPane.showMessageDialog(null, "Quiz allocated successfully!");
                        Homepage.cardLayout.show(Homepage.cards, "T_DashBoard");
                    } else {
                        JOptionPane.showMessageDialog(null, "Error allocating quiz. Please try again.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error allocating quiz. Please try again.");
                }
            }
        });

        return mainPanel;
    }

    // Method to fetch batch names from the database
    private static String[] getBatchNamesFromDatabase() {
        ArrayList<String> batchList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT Batch_name FROM t_batch WHERE Record_status = 'Active'")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                batchList.add(rs.getString("Batch_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return batchList.toArray(new String[0]);
    }

    // Method to fetch quiz names from the database
    private static String[] getQuizNamesFromDatabase() {
        ArrayList<String> quizList = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT Quiz_name FROM t_quiz")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                quizList.add(rs.getString("Quiz_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizList.toArray(new String[0]);
    }

    // Helper method to get quiz/batch ID based on name
    private static int getIdFromDatabase(Connection conn, String query, String name) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("No record found for: " + name);
            }
        }
    }
}
