import java.util.ArrayList;
import java.util.Comparator;

public class AddressBook {
    private final ArrayList<Person> addressBook = new ArrayList<>();
    private final AddressBookDB databaseApi;

    public AddressBook(AddressBookDB databaseApi){
        this.databaseApi = databaseApi;
    }

    public void sort() {
        addressBook.sort(Comparator.comparing((Person p) -> p.getSurname().toLowerCase())
                .thenComparing((Person p) -> p.getName().toLowerCase()));
    }


    // controllo i duplicati, ritorno true se ha avuto successo
    public boolean addPerson(Person person){
        if (person == null || addressBook.contains(person))
            return false;
        boolean success = databaseApi.addPerson(person);
        if (success) {
            addressBook.add(person);
            loadPeopleFromDatabase();
        }
        return success;
    }

    public boolean editPerson(Person selectedPerson, Person newPerson){
        if (selectedPerson == null || newPerson == null){
            return false;
        }
        boolean success = databaseApi.editPerson(selectedPerson, newPerson);
        if (success){
            selectedPerson.setName(newPerson.getName());
            selectedPerson.setSurname(newPerson.getSurname());
            selectedPerson.setAddress(newPerson.getAddress());
            selectedPerson.setPhoneNumber(newPerson.getPhoneNumber());
            selectedPerson.setAge(newPerson.getAge());
            loadPeopleFromDatabase();
        }
        return success;
    }

    public boolean removePerson(Person person){
        if (person == null){
            return false;
        }
        boolean success = databaseApi.removePerson(person);
        if (success){
            addressBook.remove(person);
            loadPeopleFromDatabase();
        }
        return success;
    }

    public ArrayList<Person> getAddressBook() {
        this.sort();
        return new ArrayList<>(addressBook);
    }

    public void loadPeopleFromDatabase() {
        ArrayList<Person> people = databaseApi.getAllPeople();
        addressBook.clear();
        addressBook.addAll(people);
    }
}
