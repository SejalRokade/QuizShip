import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CreateQuizGUI {

    private Homepage homepage;
    public JPanel createQuizPanel() {

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE); // Set background to white

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 102));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(800, 50));

        // Header label
        JLabel headerLabel = new JLabel("Create Quiz");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(headerLabel, BorderLayout.WEST);
        // Dashboard and Logout buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(0, 102, 102)); // Match background to the header

        JButton dashboardButton = new JButton("Dashboard");
        JButton logoutButton = new JButton("Logout");

        // Set button sizes and fonts for consistency
        dashboardButton.setPreferredSize(new Dimension(100, 30));
        logoutButton.setPreferredSize(new Dimension(100, 30));

        buttonPanel.add(dashboardButton);
        buttonPanel.add(logoutButton);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        // Add header panel to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Panel (Form)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 1, 20, 0)); // Single layout to stretch across width
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Empty border to add padding
        centerPanel.setBackground(Color.WHITE); // Set the background to white

        // Quiz Form Panel
        JPanel createQuizPanel = new JPanel();
        createQuizPanel.setBorder(BorderFactory.createTitledBorder("Add a Quiz"));
        createQuizPanel.setLayout(new GridBagLayout());
        createQuizPanel.setBackground(Color.WHITE); // Set background to white
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Increase font size for labels and text fields
        Font formFont = new Font("Arial", Font.PLAIN, 18);

        // Form fields for creating quiz
        JLabel quizNameLabel = new JLabel("Enter Quiz Name:");
        quizNameLabel.setFont(formFont); // Set larger font for labels
        JTextField quizNameField = new JTextField(20);
        quizNameField.setFont(formFont); // Set larger font for text fields
        quizNameField.setPreferredSize(new Dimension(300, 30)); // Increase size of text fields

        JLabel quizTopicLabel = new JLabel("Enter Topic:");
        quizTopicLabel.setFont(formFont); // Larger font
        // Create dropdown (JComboBox) for quiz topics using getAllTopics()
        List<String> topics = UserDA.getAllTopics(); // Assume this method returns a List<String>
        JComboBox<String> quizTopicDropdown = new JComboBox<>(topics.toArray(new String[0]));
        quizTopicDropdown.setFont(formFont); // Set larger font for the dropdown
        quizTopicDropdown.setPreferredSize(new Dimension(300, 30)); // Increase size of dropdown

        JLabel easyQuestionsLabel = new JLabel("No of Easy Questions:");
        easyQuestionsLabel.setFont(formFont); // Larger font
        JTextField easyQuestionsField = new JTextField(5);
        easyQuestionsField.setFont(formFont); // Larger font
        easyQuestionsField.setPreferredSize(new Dimension(100, 30)); // Increase size

        JLabel mediumQuestionsLabel = new JLabel("No of Medium Questions:");
        mediumQuestionsLabel.setFont(formFont); // Larger font
        JTextField mediumQuestionsField = new JTextField(5);
        mediumQuestionsField.setFont(formFont); // Larger font
        mediumQuestionsField.setPreferredSize(new Dimension(100, 30)); // Increase size

        JLabel difficultQuestionsLabel = new JLabel("No of Difficult Questions:");
        difficultQuestionsLabel.setFont(formFont); // Larger font
        JTextField difficultQuestionsField = new JTextField(5);
        difficultQuestionsField.setFont(formFont); // Larger font
        difficultQuestionsField.setPreferredSize(new Dimension(100, 30)); // Increase size

        JButton createQuizButton = new JButton("CREATE QUIZ");
        createQuizButton.setPreferredSize(new Dimension(80, 50));
        createQuizButton.setBackground(new Color(0, 102, 102)); // Set teal color for button
        createQuizButton.setForeground(Color.WHITE); // White text
        createQuizButton.setFont(formFont); // Larger font for button

        // Add fields to the quiz panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        createQuizPanel.add(quizNameLabel, gbc);
        gbc.gridx = 1;
        createQuizPanel.add(quizNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        createQuizPanel.add(quizTopicLabel, gbc);
        gbc.gridx = 1;
        createQuizPanel.add(quizTopicDropdown, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        createQuizPanel.add(easyQuestionsLabel, gbc);
        gbc.gridx = 1;
        createQuizPanel.add(easyQuestionsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        createQuizPanel.add(mediumQuestionsLabel, gbc);
        gbc.gridx = 1;
        createQuizPanel.add(mediumQuestionsField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        createQuizPanel.add(difficultQuestionsLabel, gbc);
        gbc.gridx = 1;
        createQuizPanel.add(difficultQuestionsField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        createQuizPanel.add(createQuizButton, gbc);

        // Add quiz panel to center panel
        centerPanel.add(createQuizPanel);

        // Add center panel to the main panel
        mainPanel.add(centerPanel, BorderLayout.CENTER);


        // Action Listeners for Dashboard and Logout Buttons
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
                // After successful registration, go back to Homepage
                Homepage.cardLayout.show(Homepage.cards, "HOMEPAGE");
            }
        });

        // Action listener for create quiz button
        createQuizButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String quizName = quizNameField.getText();
                String quizTopic = (String) quizTopicDropdown.getSelectedItem(); // Get the selected topic
                int numEasy = Integer.parseInt(easyQuestionsField.getText());
                int numMedium = Integer.parseInt(mediumQuestionsField.getText());
                int numHard = Integer.parseInt(difficultQuestionsField.getText());

                int totalQuestions = numEasy + numMedium + numHard;

                // Simulating getting the teacherId (this should come from your session or login system)
                int teacherId = 1; // Hardcoded for example purposes

                String query = "INSERT INTO t_Quiz (Quiz_name, Topic, Teacher_id, NumberOfQuestion) VALUES (?, ?, ?, ?)";
                try (Connection conn = DatabaseUtil.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    // insert the quiz into the database
                    stmt.setString(1, quizName);
                    stmt.setString(2, quizTopic);
                    stmt.setInt(3, teacherId);
                    stmt.setInt(4, totalQuestions); // Store total number of questions
                    //stmt.executeUpdate();

                    // Fetch random questions based on difficulty levels
                    QuestionFetcher questionFetcher = new QuestionFetcher(conn);


                    System.out.println("   ");
                    System.out.println("      Fetching random questions...");
                    System.out.println("Topic: " + quizTopic);
                    // Fetch easy, normal, and hard questions separately
                    java.util.List<Question> easyQuestions = questionFetcher.fetchRandomQuestions(quizTopic, numEasy, "Easy");
                    java.util.List<Question> normalQuestions = questionFetcher.fetchRandomQuestions(quizTopic, numMedium, "Normal");
                    java.util.List<Question> hardQuestions = questionFetcher.fetchRandomQuestions(quizTopic, numHard, "Hard");

                    // Call backend to create quiz
                    boolean success = UserDA.createQuiz(quizName, quizTopic, teacherId, numEasy, numMedium, numHard);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Quiz Created Successfully!");
                        quizNameField.setText("");
                        easyQuestionsField.setText("");
                        mediumQuestionsField.setText("");
                        difficultQuestionsField.setText("");
                        // Navigate back to the TeacherDashboard
                        Homepage.cardLayout.show(Homepage.cards, "T_DashBoard");

                    } else {
                        JOptionPane.showMessageDialog(null, "Error Creating Quiz.");
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace(); // Print stack trace for detailed error information
                    System.out.println("Error creating quiz: " + ex.getMessage());

                }

            }
        });
        return mainPanel;
    }
}
