package TP1;
import java.util.Scanner; 

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a valid IP address (ex: 123.123.12.1): ");
        String addressIp = scanner.next();
        System.out.print("Enter a valid port (entre 5000 et 5050):");
        // Scanner scannerAddress = new Scanner(System.in);
        String port = scanner.next();
        System.out.println("Hello World!");
    }
}
