import javax.swing.*;
import java.awt.*;

public class TeacherDashboardGUI {
    public static CardLayout cL; // CardLayout to switch between views
    public static JPanel cards; // The panel that uses CardLayout

    private Homepage homepage;  // Reference to Homepage for switching panels

    // Constructor accepts a reference to the Homepage instance
    public TeacherDashboardGUI(Homepage homepage) {
        this.homepage = homepage;
        // Initialize the card layout and cards panel
        cL = new CardLayout();
        cards = new JPanel(cL);

        // Create the dashboard panel and add it to the cards panel
        JPanel mainPanel = createDashboardPanel(); // Correct method call
        cards.add(mainPanel, "TEACHER_DASHBOARD");
    }

    // This method creates and returns the Teacher Dashboard panel
    JPanel createDashboardPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 102));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(800, 50));

        JLabel headerLabel = new JLabel("Teacher Dashboard");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(0, 102, 102));
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        JButton logoutButton = new JButton("Logout");
        buttonsPanel.add(logoutButton);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Panel with Buttons
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10);

        // Create Buttons
        JButton createBatchButton = new JButton("Create Batch");
        JButton deactivateBatchButton = new JButton("Deactivate Batch");
        JButton createQuizButton = new JButton("Create Quiz");
        JButton allocateQuizButton = new JButton("Allocate Quiz");
        JButton addStudentButton = new JButton("Add Student To Batch");
        JButton displayInfoButton = new JButton("Display Information");

        // Set button styles
        setButtonStyle(createBatchButton, false);
        setButtonStyle(deactivateBatchButton, false);
        setButtonStyle(createQuizButton, true);  // White background, teal text
        setButtonStyle(allocateQuizButton, true);  // White background, teal text
        setButtonStyle(addStudentButton, false);
        setButtonStyle(displayInfoButton, false);

        // Add buttons to the center panel (2 columns)
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(createBatchButton, gbc);

        gbc.gridx = 1;
        centerPanel.add(deactivateBatchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(createQuizButton, gbc);

        gbc.gridx = 1;
        centerPanel.add(allocateQuizButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        centerPanel.add(addStudentButton, gbc);

        gbc.gridx = 1;
        centerPanel.add(displayInfoButton, gbc);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Action Listeners for buttons
        createBatchButton.addActionListener(e -> {
            homepage.showPanel("CREATE_BATCH");
        });

        createQuizButton.addActionListener(e -> {
            homepage.showPanel("CREATE_QUIZ");
        });

        allocateQuizButton.addActionListener(e -> {
            homepage.showPanel("ALLOCATE_QUIZ");
        });

        deactivateBatchButton.addActionListener(e -> {
            homepage.showPanel("DEACTIVATE_BATCH");
        });

        addStudentButton.addActionListener(e -> {
            homepage.showPanel("ADD_STUDENT");
        });

        displayInfoButton.addActionListener(e -> {
            homepage.showPanel("Display_Info");
        });

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logging out...");
            homepage.showPanel("HOMEPAGE");
        });

        return mainPanel;
    }

    // Utility method to style buttons
    private void setButtonStyle(JButton button, boolean isQuizButton) {
        button.setFont(new Font("Arial", Font.BOLD, 16));  // Increased font size
        button.setPreferredSize(new Dimension(250, 70));   // Increased button size

        if (isQuizButton) {
            button.setBackground(Color.WHITE);  // White background for "Create Quiz" and "Allocate Quiz"
            button.setForeground(new Color(0, 128, 128));  // Teal text
        } else {
            button.setBackground(new Color(0, 102, 102));  // Default background for other buttons
            button.setForeground(Color.WHITE);  // White text for other buttons
        }
    }

    // Method to switch panels based on the card name
    public void showPanel(String panelName) {
        cL.show(cards, panelName);
    }
}
