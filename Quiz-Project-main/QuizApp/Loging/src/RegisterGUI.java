import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterGUI {
    // Create the registration panel
    public static JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel();
        registerPanel.setLayout(new BorderLayout());

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 102, 102));
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(800, 50));

        // Header label
        JLabel headerLabel = new JLabel("Register");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 30));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setHorizontalAlignment(SwingConstants.LEFT);
        headerPanel.add(headerLabel, BorderLayout.WEST);

        // Add header panel to the main panel
        registerPanel.add(headerPanel, BorderLayout.NORTH);

        // Center Panel (Form)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centerPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Add padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form fields for registration
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JTextField nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(400, 40));

        JLabel emailLabel = new JLabel("Email ID:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JTextField emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(400, 40));

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(400, 40));

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"Teacher"});
        roleComboBox.setPreferredSize(new Dimension(400, 40));
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 22));  // Set font size to 20

        JButton registerButton = new JButton("REGISTER");
        registerButton.setFont(new Font("Arial", Font.BOLD, 20));
        registerButton.setPreferredSize(new Dimension(150, 50));
        registerButton.setBackground(new Color(0, 102, 102));
        registerButton.setForeground(Color.WHITE);

        // Add fields to center panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        centerPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(emailLabel, gbc);
        gbc.gridx = 1;
        centerPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        centerPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        centerPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        centerPanel.add(roleLabel, gbc);
        gbc.gridx = 1;
        centerPanel.add(roleComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        centerPanel.add(registerButton, gbc);

        // Add center panel to register panel
        registerPanel.add(centerPanel, BorderLayout.CENTER);

        // Action Listener for Registration Button
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values
                String name = nameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();

                // Call backend method to register user
                boolean result = UserDA.userRegister(name, email, password, role); // Assuming this method exists in UserDA
                JOptionPane.showMessageDialog(Homepage.frame, "Registration in process");


                if (result) {
                    JOptionPane.showMessageDialog(Homepage.frame, "Registration Successful!");
                    // After successful registration, go back to Homepage
                    Homepage.cardLayout.show(Homepage.cards, "HOMEPAGE");
                } else {
                    JOptionPane.showMessageDialog(Homepage.frame, "Email already exists. Please try another Email ID", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    Homepage.cardLayout.show(Homepage.cards, "HOMEPAGE");

                }
            }
        });

        return registerPanel;
    }
}
