import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;


public class DisplayInfoGUI {

    private final UserDA userDA;
    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JTextArea outputArea;
    private JTextField emailField;

    //private UserDA userDA; // Assume this is your data access object for student/batch info

    public DisplayInfoGUI(UserDA userDA) {
        this.userDA = new UserDA();
        displayInfoPanel();
    }

    public JPanel displayInfoPanel() {

        // Main panel with BorderLayout
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

        // Card Layout Panel for switching between views
        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // Adding cards
       // cardPanel.add(createStudentDetailsPanel(), "StudentDetails");
        //cardPanel.add(createBatchNamesPanel(), "BatchNames");
        cardPanel.add(createQuizzesPanel(), "Quizzes");

        // Control Panel with buttons to switch between cards
        JPanel controlPanel = new JPanel();
        JButton studentButton = new JButton("Student Details");
        JButton batchButton = new JButton("Batch Names");
        JButton quizButton = new JButton("Quizzes");

        //controlPanel.add(studentButton);
        //controlPanel.add(batchButton);
        //controlPanel.add(quizButton);

        //Action listeners for switching cards
        //studentButton.addActionListener(e -> cardLayout.show(cardPanel, "StudentDetails"));
       //batchButton.addActionListener(e -> cardLayout.show(cardPanel, "BatchNames"));
        quizButton.addActionListener(e -> cardLayout.show(cardPanel, "Quizzes"));

        // Action Listeners for Dashboard and Logout Buttons
        dashboardButton.addActionListener(e -> {
            //JOptionPane.showMessageDialog(null, "Navigating to Dashboard...");
            Homepage.cardLayout.show(Homepage.cards, "T_DashBoard");
        });

        logoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "Logging out...");
            Homepage.cardLayout.show(Homepage.cards, "HOMEPAGE");
        });

        // Adding panels to the frame
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);


        return mainPanel;
    }
/*
    // Create the student details panel
    private JPanel createStudentDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        JLabel emailLabel = new JLabel("Enter Student Email: ");
        emailField = new JTextField(20);
        JButton fetchButton = new JButton("Fetch Details");

        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(fetchButton);

        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add action listener to the button
        fetchButton.addActionListener(e -> fetchStudentDetails());

        return panel;
    }
    */

    // Create quizzes panel
    private JPanel createQuizzesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JButton fetchQuizButton = new JButton("Fetch Quizzes Assigned to Batches");
        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        panel.add(fetchQuizButton, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Action listener to fetch quizzes
        fetchQuizButton.addActionListener(e -> displayAllQuizzesAssignedToBatches());

        return panel;
    }

    // Method to fetch student details
    private void fetchStudentDetails() {
        String email = emailField.getText().trim();
        if (!email.isEmpty()) {
            String details = userDA.getStudentDetails(email);
            outputArea.setText(details);
        } else {
            outputArea.setText("Please enter a valid email.");
        }
    }

    // Method to display all batch names
    private void displayAllBatchNames() {
        List<String> batchNames = (List<String>) userDA.getAllBatchNames();
        if (batchNames.isEmpty()) {
            outputArea.setText("No batches found.");
        } else {
            StringBuilder sb = new StringBuilder("Batch Names:\n");
            for (String batchName : batchNames) {
                sb.append("- ").append(batchName).append("\n");
            }
            outputArea.setText(sb.toString());
        }
    }

    // Method to display quizzes assigned to batches
    private void displayAllQuizzesAssignedToBatches() {
        Map<String, List<String>> batchQuizMap = userDA.getQuizzesAssignedToBatches();
        if (batchQuizMap.isEmpty()) {
            outputArea.setText("No quizzes assigned to any batch.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, List<String>> entry : batchQuizMap.entrySet()) {
                sb.append("Batch: ").append(entry.getKey()).append("\n");
                sb.append("Quizzes: ").append(String.join(", ", entry.getValue())).append("\n");
                sb.append("----------------------------\n");
            }
            outputArea.setText(sb.toString());
        }
    }

    public static void main(String[] args) {
        // Assume UserDA is already instantiated and passed here
        UserDA userDA = new UserDA();
        new DisplayInfoGUI(userDA);
    }
}

