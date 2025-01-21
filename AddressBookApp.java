import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class AddressBookApp {
    private JFrame frame;
    private JTable table;
    private AddressBook addressBook;
    private JPanel buttonPanel;
    private PersonTableModel tableModel;

    public AddressBookApp(AddressBook addressBook){

        this.addressBook = addressBook;

        frame = new JFrame("AddressBook");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);

        tableModel = new PersonTableModel(addressBook.getAddressBook());
        table = new JTable(tableModel);
        table.setRowHeight(50);

        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("Add Person");
        JButton modifyButton = new JButton("Modify Person");
        JButton removeButton = new JButton("Remove Person");

        addButton.addActionListener(e -> addPersonMode());
        modifyButton.addActionListener(e -> editPersonMode());
        removeButton.addActionListener(e-> removePersonMode());

        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(removeButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public void addPersonMode(){
        JTextField nameField = new JTextField(10);
        JTextField surnameField = new JTextField(10);
        JTextField phoneField = new JTextField(10);
        JTextField addressField = new JTextField(10);
        JTextField ageField = new JTextField(10);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Surname:"), gbc);
        gbc.gridx = 1;
        panel.add(surnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        panel.add(ageField, gbc);

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;  // l'ultimo msg prende due colonne
        panel.add(errorLabel, gbc);

        while (true) {
            int result = JOptionPane.showConfirmDialog(
                    frame,
                    panel,
                    "Enter details",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                break;
            }

            String name = nameField.getText();
            String surname = surnameField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String ageText = ageField.getText();

            // validazione campi
            if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || address.isEmpty() || ageText.isEmpty()) {
                errorLabel.setText("All fields are required. Please fill them in.");
                continue;
            }

            // try-catch sul parsing dell'età
            try {
                int age = Integer.parseInt(ageText);

                if (age <= 0) {
                    errorLabel.setText("Please enter a valid age greater than 0.");
                    continue;
                }

                // se va bene aggiungila
                Person newPerson = new Person(name, surname, address, phone, age);
                if (!addressBook.addPerson(newPerson)){
                    // allora c'è stato un errore nell'inserimento
                    errorLabel.setText("This record already exists");
                    continue;
                }

                tableModel = new PersonTableModel(addressBook.getAddressBook());
                table.setModel(tableModel);
                break;

            } catch (NumberFormatException ex) {
                errorLabel.setText("Please enter a valid number for age.");
            }
        }
    }

    public void editPersonMode(){
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a person to edit.");
            return;
        }

        Person selectedPerson = addressBook.getAddressBook().get(selectedRow);

        JTextField nameField = new JTextField(selectedPerson.getName(), 10);
        JTextField surnameField = new JTextField(selectedPerson.getSurname(), 10);
        JTextField phoneField = new JTextField(selectedPerson.getPhoneNumber(), 10);
        JTextField addressField = new JTextField(selectedPerson.getAddress(), 10);
        JTextField ageField = new JTextField(String.valueOf(selectedPerson.getAge()), 10);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Surname:"), gbc);
        gbc.gridx = 1;
        panel.add(surnameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        panel.add(phoneField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Age:"), gbc);
        gbc.gridx = 1;
        panel.add(ageField, gbc);

        JLabel errorLabel = new JLabel("");
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;  // l'ultimo msg prende due colonne
        panel.add(errorLabel, gbc);

        while(true) {
            int result = JOptionPane.showConfirmDialog(
                    frame,
                    panel,
                    "Enter details",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );
            if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                break;
            }
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String surname = surnameField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();
                String ageText = ageField.getText();

                if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || address.isEmpty() || ageText.isEmpty()) {
                    errorLabel.setText("All fields are required. Please fill them in.");
                    continue;
                }

                try {
                    int age = Integer.parseInt(ageText);

                    if (age <= 0) {
                        errorLabel.setText("Please enter a valid age greater than 0.");
                        continue;
                    }

                    Person newPerson = new Person(name, surname, address, phone, age);
                    if (!addressBook.editPerson(selectedPerson, newPerson)){
                        errorLabel.setText("Failed to edit this record");
                        continue;
                    }

                    tableModel = new PersonTableModel(addressBook.getAddressBook());
                    table.setModel(tableModel);
                    break;

                } catch (NumberFormatException ex) {
                    errorLabel.setText("Please enter a valid number for age.");
                }
            }
        }
    }

    public void removePersonMode(){
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a person to remove.");
            return;
        }

        Person selectedPerson = addressBook.getAddressBook().get(selectedRow);

        int option = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete " + selectedPerson.getName() + " " + selectedPerson.getSurname() + "?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            if (addressBook.removePerson(selectedPerson)){
                JOptionPane.showMessageDialog(frame, "Person removed successfully.", "Successo", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(frame, "Couldn't remove person from AccessBook", "Errore", JOptionPane.ERROR_MESSAGE);
            }
            tableModel = new PersonTableModel(addressBook.getAddressBook());
            table.setModel(tableModel);
        }
    }

    private static class PersonTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Name", "Surname", "Phone Number"};
        private final ArrayList<Person> people;

        public PersonTableModel(ArrayList<Person> people) {
            this.people = people;
        }

        public int getRowCount() {
            return people.size();
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public String getColumnName(int column) {
            return columnNames[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Person person = people.get(rowIndex);
            return switch (columnIndex) {
                case 0 -> person.getName();
                case 1 -> person.getSurname();
                case 2 -> person.getPhoneNumber();
                default -> null;
            };
        }

        public void addPerson(Person person){
            people.add(person);
            fireTableRowsInserted(people.size() - 1, people.size() - 1);
        }
    }

    public static void main(String[] args) {

        // lettura del percorso
        String propertiesFilePath;
        try {
            propertiesFilePath = args[0];
        }
        catch(ArrayIndexOutOfBoundsException e){
            propertiesFilePath = "credenziali_database.properties";
        }
        PropertiesReader propertiesReader = null;

        // try-catch dell'istanza del reader
        try {
            propertiesReader = new PropertiesReader(propertiesFilePath);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
            return;
        }

        // tento la connessione e la setto se possibile
        Connection connection = null;
        try {
            connection = LoginManager.createConnection(propertiesReader);
            LoginManager.setConnection(connection);
        } catch (SQLException | IOException e) {
            System.err.println("Error creating database connection: " + e.getMessage());
            return;
        }

        // hook per la chiusura della connessione una volta chiuso l'applicativo
        Runtime.getRuntime().addShutdownHook(new Thread(LoginManager::closeConnection));

        Connection finalConnection = connection;
        SwingUtilities.invokeLater(() -> {
            new LoginFrame(success -> {
                if (success) {
                    try {
                            AddressBookDB databaseAPI = new AddressBookDB(finalConnection);
                            AddressBook addressBook = new AddressBook(databaseAPI);
                            addressBook.loadPeopleFromDatabase();
                            SwingUtilities.invokeLater(() -> new AddressBookApp(addressBook));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).setVisible(true);
        });
    }
}
