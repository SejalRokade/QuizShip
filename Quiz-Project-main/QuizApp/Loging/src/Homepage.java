import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Homepage {

    public static JFrame frame;
    public static JPanel cards; // The panel that uses CardLayout
    public static CardLayout cardLayout; // CardLayout to switch between views
    public static UserDA userDA;

    public Homepage() {
        // Create the main frame
        frame = new JFrame("QuizShip");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Default size
        frame.setLayout(new BorderLayout());

        // Create the card layout panel
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Create the homepage panel
        JPanel homepagePanel = createHomepagePanel();
        cards.add(homepagePanel, "HOMEPAGE");

        // Create the registration panel and add it to cards
        JPanel registerPanel = RegisterGUI.createRegisterPanel();
        cards.add(registerPanel, "REGISTER");

        JPanel loginPanel = LoginGUI.createLoginPanel(cardLayout, cards);
        cards.add(loginPanel, "LOGIN");

        // Add TeacherDashboardGUI to the card layout (initially hidden)
        TeacherDashboardGUI teacherDashboardGUI = new TeacherDashboardGUI(this);
        JPanel teacherDashboardPanel = teacherDashboardGUI.createDashboardPanel();
        cards.add(teacherDashboardPanel, "T_DashBoard");

        String email = null;
        CreateBatchGUI createBatchGUI = new CreateBatchGUI(email);
        JPanel createBatchPanel = createBatchGUI.createBatchPanel();
        cards.add(createBatchPanel, "CREATE_BATCH");

        CreateQuizGUI createQuizGUI = new CreateQuizGUI();
        JPanel createQuizPanel = createQuizGUI.createQuizPanel();
        cards.add(createQuizPanel, "CREATE_QUIZ");

        AllocateQuizGUI allocateQuizGUI = new AllocateQuizGUI();
        JPanel allocateQuizPanel = allocateQuizGUI.allocateQuizPanel();
        cards.add(allocateQuizPanel, "ALLOCATE_QUIZ");

        AddStudentGUI addStudentGUI = new AddStudentGUI();
        JPanel addStudentPanel = addStudentGUI.addStudentPanel();
        cards.add(addStudentPanel, "ADD_STUDENT");

        DeactivateBatchGUI deactivateBatchGUI = new DeactivateBatchGUI();
        JPanel deactivateBatchPanel = deactivateBatchGUI.deactivateBatchPanel();
        cards.add(deactivateBatchPanel, "DEACTIVATE_BATCH");

        DisplayInfoGUI displayInfoGUI = new DisplayInfoGUI(userDA);
        JPanel displayInfoPanel = displayInfoGUI.displayInfoPanel();
        cards.add(displayInfoPanel, "Display_Info");

        // Add the card layout panel to the frame
        frame.add(cards, BorderLayout.CENTER);

        // Make the frame visible
        frame.setVisible(true);
    }

    // Create the homepage panel with buttons
    private JPanel createHomepagePanel() {
        JPanel homepagePanel = new JPanel(new BorderLayout());

        // Create the left panel for "QuizShip" text
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(0, 102, 102)); // Teal color
        leftPanel.setLayout(new GridBagLayout()); // Center alignment
        JLabel smpLabel = new JLabel("QuizShip");
        smpLabel.setFont(new Font("Arial", Font.BOLD, 28));
        smpLabel.setForeground(Color.WHITE);
        leftPanel.add(smpLabel);

        // Create the right panel for buttons
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between components

        // Add "HOMEPAGE" label
        JLabel homepageLabel = new JLabel("HOMEPAGE");
        homepageLabel.setFont(new Font("Arial", Font.BOLD, 30));
        homepageLabel.setForeground(new Color(0, 102, 102)); // Teal color
        gbc.gridx = 0;
        gbc.gridy = 0;
        rightPanel.add(homepageLabel, gbc);

        // Define button size and add "TEACHER LOGIN" button
        JButton teacherLoginButton = new JButton("TEACHER LOGIN");
        teacherLoginButton.setFont(new Font("Arial", Font.BOLD, 20));
        teacherLoginButton.setBackground(new Color(0, 102, 102));
        teacherLoginButton.setForeground(Color.WHITE);
        teacherLoginButton.setPreferredSize(new Dimension(300, 70)); // Set default size
        gbc.gridy = 1;
        rightPanel.add(teacherLoginButton, gbc);

        // Add "REGISTER" button
        JButton registerButton = new JButton("REGISTER");
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
        registerButton.setBackground(new Color(0, 102, 102));
        registerButton.setForeground(Color.WHITE);
        registerButton.setPreferredSize(new Dimension(300, 70)); // Set default size
        gbc.gridy = 2;
        rightPanel.add(registerButton, gbc);

        // Action listener to switch to the registration panel
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to registration card
                cardLayout.show(cards, "REGISTER");
            }
        });

        // Action listener to switch to the TeacherLogin panel
        teacherLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Switch to Teacher Dashboard card
                cardLayout.show(cards, "LOGIN");
            }
        });

        // Add panels to homepage panel
        homepagePanel.add(leftPanel, BorderLayout.WEST);
        homepagePanel.add(rightPanel, BorderLayout.CENTER);

        return homepagePanel;
    }

    // Method to switch panels based on the card name
    public void showPanel(String panelName) {
        cardLayout.show(cards, panelName);
    }

    public static void main(String[] args) {
        new Homepage();
    }
}
