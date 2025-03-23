package javaeight;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class AddressBookSystem {
    public static void main(String[] args) {

        Systems system = new Systems();


        AddressBook addressBook1 = new AddressBook("Personal");
        AddressBook addressBook2 = new AddressBook("Work");

        String path = "D:\\Java+DSA\\Regex\\src\\javaeight\\KanikaTheGreat.txt";

        // Adding contacts
        Contact contact1 = new Contact("Kanika", "Agrawal", "Nagpur", "Maharashtra", "8668321235", "kanika.agrawal@gmail.com","101");
        Contact contact2 = new Contact("Arnav", "Saharan", "Pune", "Maharashtra", "8668451235", "arnav@gmail.com","101");
        Contact contact3 = new Contact("Kan", "Agra", "Nagp", "Maha", "8668321675", "kanika@gmail.com", "101");


        addressBook1.addContact(contact1);
        addressBook1.addContact(contact2);

        addressBook1.writeToFile(path);
        addressBook2.addContact(contact3);

        system.addAddressBook(addressBook1);
        system.addAddressBook(addressBook2);

        System.out.println("Searching for contacts in 'Nagpur':");
        List<Contact> resultByCity = system.searchByCity("Nagpur");
        resultByCity.forEach(System.out::println);


        System.out.println(" Searching for contacts in 'Maharashtra':");
        List<Contact> resultByState = system.searchByState("Maharashtra");
        resultByState.forEach(System.out::println);
    }
}

class Systems {
    HashMap<String, AddressBook> addressBookSystems = new HashMap<>();
    HashMap<String, ArrayList<Contact>> cityToContact = new HashMap<>();
    HashMap<String, ArrayList<Contact>> stateToContact = new HashMap<>();

    public void cityToContactMapping() {
        addressBookSystems.values().forEach(addressBook -> addressBook.getContactList().forEach(contact -> cityToContact.computeIfAbsent(contact.getCity(), k -> new ArrayList<>()).add(contact)));

    }

    public void stateToContactMapping() {
        addressBookSystems.values().forEach(addressBook -> addressBook.getContactList().forEach(contact -> stateToContact.computeIfAbsent(contact.getState(), k -> new ArrayList<>()).add(contact)));
    }

    public Map<String, Long> getCountByCity() {
        return addressBookSystems.values().stream()
                .flatMap(book -> book.getContactList().stream())
                .collect(Collectors.groupingBy(Contact::getCity, Collectors.counting()));
    }

    public Map<String, Long> getCountByState() {
        return addressBookSystems.values().stream()
                .flatMap(book -> book.getContactList().stream())
                .collect(Collectors.groupingBy(Contact::getState, Collectors.counting()));
    }

    public Systems() {
        this.addressBookSystems = new HashMap<>();
    }

    public void addAddressBook(AddressBook addressBook) {
        addressBookSystems.put(addressBook.getName(), addressBook);
    }


    public List<Contact> searchByCity(String city) {
        return addressBookSystems.values().stream()
                .flatMap(book -> book.getContactList().stream())
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList()); // Collect as list
    }

    public List<Contact> searchByState(String state) {
        return addressBookSystems.values().stream()
                .flatMap(book -> book.getContactList().stream())
                .filter(person -> person.getState().equalsIgnoreCase(state))
                .collect(Collectors.toList());
    }

}

class AddressBook {
    private String name;
    private List<Contact> contactList;

    public AddressBook(String name) {
        this.name = name;
        this.contactList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Contact> getContactList() {
        return contactList;
    }



//    public void writeToFile(String fileName) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
//            for (Contact contact : contactList) {
//                writer.write(contact.getFirstName() + "," + contact.getLastName() + "," +
//                        contact.getState() + "," + contact.getCity() + "," + contact.getState() + "," +
//                        contact.getZip() + "," + contact.getNumber() + "," + contact.getEmail());
//
//                writer.newLine();
//            }
//            System.out.println("Contacts successfully written to file: " + fileName);
//        } catch (IOException e) {
//            System.out.println("Error writing to file: " + e.getMessage());
//        }
//    }
//    public void readFromFile(String fileName){
//        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
//            String line;
//            contactList.clear();
//            while((line = reader.readLine()) != null){
//                String[] parts = line.split(",");
//                if (parts.length == 7) {
//                    Contact contact = new Contact(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
//                    contactList.add(contact);
//                }
//            }
//            System.out.println("Contacts successfully read from file: " + fileName);
//        }catch (IOException e){
//            System.out.println("Error reading from file: " + e.getMessage());
//        }
//}
//    public void readToFile(String fileName){
//        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
//            for(Contact contact:contactList){
//
//            }
//
//        }
//        catch(IOException e){
//            System.out.println("Error reading file");
//        }
//    }
public void writeToFile(String fileName) {
    try (CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName))) {

        String[] header = { "First Name", "Last Name", "Address", "City", "State", "Zip", "Phone Number", "Email" };
        csvWriter.writeNext(header);


        for (Contact contact : contactList) {
            String[] contactData = {
                    contact.getFirstName(),
                    contact.getLastName(),
                    contact.getCity(),
                    contact.getState(),
                    contact.getZip(),
                    contact.getNumber(),
                    contact.getEmail()
            };
            csvWriter.writeNext(contactData);
        }
        System.out.println("Contacts successfully written to file: " + fileName);
    } catch (IOException e) {
        System.out.println("Error writing to file: " + e.getMessage());
    }
}

    public void readFromFile(String fileName) {
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            String[] line;
            contactList.clear();
            while ((line = csvReader.readNext()) != null) {
                if (line.length == 7) {
                    Contact contact = new Contact(line[0], line[1], line[2], line[3], line[4], line[5], line[6]);
                    contactList.add(contact);
                }
            }
            System.out.println("Contacts successfully read from file: " + fileName);
        } catch (IOException | CsvValidationException e) {
            System.out.println("Error reading from file: " + e.getMessage());
        }
    }

    public void addContact(Contact contact) {
        if (contactList.contains(contact)) {
            System.out.println(" Duplicate contact! A person with the same name already exists.");
            return;
        }
        contactList.add(contact);
        System.out.println("Contact added successfully: " + contact);
    }

    public void editContact(String firstName, String newNumber) {
        for (Contact contact : contactList) {
            if (contact.getFirstName().equalsIgnoreCase(firstName)) {
                contact.setNumber(newNumber);
                System.out.println("✏ Contact updated: " + contact);
                return;
            }
        }
        System.out.println(" Contact not found: " + firstName);
    }

    public void deleteContact(String firstName) {
        Iterator<Contact> iterator = contactList.iterator();
        while (iterator.hasNext()) {
            Contact contact = iterator.next();
            if (contact.getFirstName().equalsIgnoreCase(firstName)) {
                iterator.remove();
                System.out.println("🗑 Contact deleted: " + contact);
                return;
            }
        }
        System.out.println(" Contact not found: " + firstName);
    }
}

class Contact {
    private String firstName;
    private String lastName;
    private String city;
    private String state;
    private String number;
    private String email;
    private String zip;

    public Contact(String firstName, String lastName, String city, String state, String number, String email, String zip) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.state = state;
        this.number = number;
        this.email = email;
        this.zip = zip;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return firstName.equalsIgnoreCase(contact.firstName) && lastName.equalsIgnoreCase(contact.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName.toLowerCase(), lastName.toLowerCase());
    }

    @Override
    public String toString() {
        return "📇 Contact{" +
                "Name='" + firstName + " " + lastName + '\'' +
                ", City='" + city + '\'' +
                ", State='" + state + '\'' +
                ", Phone='" + number + '\'' +
                ", Email='" + email + '\'' +
                '}';
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getNumber() {
        return number;
    }

    public String getZip() {
        return zip;
    }
}
