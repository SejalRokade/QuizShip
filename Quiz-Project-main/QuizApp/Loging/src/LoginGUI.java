import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI {
     public static String email;
     public static CardLayout cardLayout; // CardLayout to switch between views
     public static JPanel cards; // The panel that uses CardLayout

     public LoginGUI(){
          // Create and show TeacherDashboardGUI (pass Homepage or session-related object if needed)
          Homepage homepage = new Homepage();  // Initialize Homepage
          TeacherDashboardGUI teacherDashboardGUI = new TeacherDashboardGUI(homepage);
     }
     public static JPanel createLoginPanel(CardLayout cardLayout, JPanel cards) {
          JPanel loginPanel = new JPanel();
          loginPanel.setBackground(Color.WHITE);
          loginPanel.setLayout(new GridBagLayout());

          GridBagConstraints gbc = new GridBagConstraints();
          gbc.insets = new Insets(10, 10, 10, 10);
          gbc.fill = GridBagConstraints.HORIZONTAL;

          JLabel loginLabel = new JLabel("LOGIN");
          loginLabel.setFont(new Font("Arial", Font.BOLD, 24));
          loginLabel.setForeground(new Color(0, 102, 102));
          gbc.gridx = 0;
          gbc.gridy = 0;
          gbc.gridwidth = 2;
          gbc.anchor = GridBagConstraints.CENTER;
          loginPanel.add(loginLabel, gbc);

          JLabel emailIdLabel = new JLabel("EMAIL ID");
          emailIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
          gbc.gridy = 1;
          gbc.gridwidth = 2;
          gbc.anchor = GridBagConstraints.WEST;
          loginPanel.add(emailIdLabel, gbc);

          JTextField emailIdField = new JTextField(20);
          emailIdField.setFont(new Font("Arial", Font.PLAIN, 16));
          emailIdField.setPreferredSize(new Dimension(250, 40));
          gbc.gridy = 2;
          loginPanel.add(emailIdField, gbc);

          JLabel passwordLabel = new JLabel("PASSWORD");
          passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
          gbc.gridy = 3;
          loginPanel.add(passwordLabel, gbc);

          JPasswordField passwordField = new JPasswordField(20);
          passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
          passwordField.setPreferredSize(new Dimension(250, 40));
          gbc.gridy = 4;
          loginPanel.add(passwordField, gbc);

          JButton loginButton = new JButton("LOGIN");
          loginButton.setFont(new Font("Arial", Font.BOLD, 16));
          loginButton.setBackground(new Color(0, 102, 102));
          loginButton.setForeground(Color.WHITE);
          loginButton.setPreferredSize(new Dimension(50, 40));
          gbc.gridy = 5;
          gbc.anchor = GridBagConstraints.WEST;
          gbc.gridwidth = 2;
          loginPanel.add(loginButton, gbc);

          loginButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    email = emailIdField.getText();
                    String password = new String(passwordField.getPassword());

                    boolean isAuthenticated = UserDA.userLogin(email, password);

                    if (isAuthenticated) {
                         JOptionPane.showMessageDialog(cards, "Login Successful!");
                         emailIdField.setText("");
                         passwordField.setText("");
                         CreateBatchGUI createBatchGUI = new CreateBatchGUI(email);
                         // Switch to the TeacherDashboardGUI panel using CardLayout
                         Homepage.cardLayout.show(cards, "T_DashBoard");
                    } else {
                         JOptionPane.showMessageDialog(cards, "Invalid email or password. Please try again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                         emailIdField.setText("");
                         passwordField.setText("");
                         Homepage.cardLayout.show(Homepage.cards, "HOMEPAGE");
                    }
               }
          });

          return loginPanel;
     }
}
