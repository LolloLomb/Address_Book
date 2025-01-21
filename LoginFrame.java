import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class LoginFrame extends JFrame{
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginFrame(LoginCallback callback) {

        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new GridLayout(4, 1));

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        usernamePanel.add(usernameField);
        add(usernamePanel);

        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        passwordPanel.add(passwordField);
        add(passwordPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        buttonPanel.add(loginButton);
        add(buttonPanel);

        JPanel errorPanel = new JPanel(new GridLayout(1, 2));
        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        errorPanel.add(new JLabel());
        errorPanel.add(errorLabel);
        add(errorPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                boolean loginSuccess = false;
                loginSuccess = LoginManager.validate(username, password);

                if (loginSuccess) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    errorLabel.setText("");
                    dispose();
                    callback.onLoginResult(true);

                } else {
                    errorLabel.setText("Login failed");
                    callback.onLoginResult(false);
                }
            }
        });
    }
}
