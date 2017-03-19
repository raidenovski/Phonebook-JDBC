/**
 * Created by raiden on 3/14/17.
 */
public class Contact {

    private String name;
    private int number;

    public Contact() {}

    public Contact(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    protected void printSearchResults(Contact contact) {
        System.out.println("Contact found:");
        System.out.println("Name: " + contact.getName());
        System.out.println("Telephone: " + contact.getNumber());
    }
}
