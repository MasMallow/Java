package BasicCalculator;
import java.util.Scanner;
public class calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input Numder = ");
        double num1 = scanner.nextDouble();

        System.out.println("Input Numder = ");
        double num2 = scanner.nextDouble();

        System.out.println("Choose (+, -, *, /): ");
        char operator = scanner.next().charAt(0);

        scanner.close();
        double result = 0;
        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
            if (num2 != 0) {
                result = num1 / num2;
            }
            else{
                System.out.println("Then divide by zero to get");
                return;
            }
                break;
            default:
                System.out.println("Invalid operator");
                return; 
        }
        System.out.println("result = " + result);
    }
}
