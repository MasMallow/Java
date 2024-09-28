package ContactManagementSystem;
import java.util.Scanner;

class InnerContact {
    String name;
    String phone;
    String email;

    public InnerContact(String name, String phone, String email){
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
    public void displayContact(){
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phone);
        System.out.println("Email: " + email);
    }
}

public class ContactManagement {
    public static void main(String[]args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter name:");
        String name = scanner.nextLine();
        System.out.println("Phone Number:");
        String phone = scanner.nextLine();
        System.out.println("Email:");
        String email = scanner.nextLine();

        InnerContact contact = new InnerContact(name, phone, email);

        contact.displayContact();
        scanner.close();
    }
}
