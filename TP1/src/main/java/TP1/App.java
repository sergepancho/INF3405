package TP1;
import java.util.Scanner; 

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    public static boolean checkAddress(String addressIp) {
        String[] addressValues = addressIp.split("\\.");

        // We need to get 4 elements exactly
        if(addressValues.length != 4)
            return false;

        // values need to be between 0 and 255    
        for (String a : addressValues){
            int valueInt = Integer.parseInt(a);
            if(valueInt > 255 || valueInt < 0){
                return false;
            }
            System.out.println(valueInt);
        }
        return true;
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String addressIp;
        do{
            System.out.print("Enter a valid IP address (ex: 123.123.12.1): ");
            addressIp = scanner.next();
        }while(!checkAddress(addressIp));

        int port;
        do{
            System.out.print("Enter a valid port (entre 5000 et 5050):");
            port = scanner.nextInt();
        }while(port < 5050 && port > 5000);

        // Scanner scannerAddress = new Scanner(System.in);
        System.out.println("Hello World!");
    }
}
