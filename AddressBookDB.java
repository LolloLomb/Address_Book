import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddressBookDB {
    private final Connection connection;

    public AddressBookDB(Connection connection){
        this.connection = connection;
    }

    public boolean addPerson(Person person) {
        String query = "INSERT INTO person (name, surname, address, phone_number, age) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, person.getName());
            stmt.setString(2, person.getSurname());
            stmt.setString(3, person.getAddress());
            stmt.setString(4, person.getPhoneNumber());
            stmt.setInt(5, person.getAge());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean editPerson(Person selectedPerson, Person newPerson) {
        String query = "UPDATE person SET name = ?, surname = ?, address = ?, phone_number = ?, age = ? " +
                "WHERE name = ? AND surname = ? AND address = ? AND phone_number = ? AND age = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, newPerson.getName());
            stmt.setString(2, newPerson.getSurname());
            stmt.setString(3, newPerson.getAddress());
            stmt.setString(4, newPerson.getPhoneNumber());
            stmt.setInt(5, newPerson.getAge());

            stmt.setString(6, selectedPerson.getName());
            stmt.setString(7, selectedPerson.getSurname());
            stmt.setString(8, selectedPerson.getAddress());
            stmt.setString(9, selectedPerson.getPhoneNumber());
            stmt.setInt(10, selectedPerson.getAge());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removePerson(Person person){
        String query = "DELETE FROM person WHERE name = ? AND surname = ? AND address = ? AND phone_number = ? AND age = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, person.getName());
            stmt.setString(2, person.getSurname());
            stmt.setString(3, person.getAddress());
            stmt.setString(4, person.getPhoneNumber());
            stmt.setInt(5, person.getAge());

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ArrayList<Person> getAllPeople() {
        ArrayList<Person> people = new ArrayList<>();
        String query = "SELECT * FROM person";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String address = rs.getString("address");
                String phone_number = rs.getString("phone_number");
                int age = rs.getInt("age");
                people.add(new Person(name, surname, address, phone_number, age));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }
}
