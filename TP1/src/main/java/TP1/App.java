package TP1;
import java.util.Scanner; 


//
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
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
    /*    Scanner scanner = new Scanner(System.in);
        String addressIp;
     /*   do{
            System.out.print("Enter a valid IP address (ex: 123.123.12.1): ");
            addressIp = scanner.next();
        }while(!checkAddress(addressIp));

        int port;
        do{
            System.out.print("Enter a valid port (entre 5000 et 5050):");
            port = scanner.nextInt();
        }while(port < 5050 && port > 5000);*/
        System.out.println("Working Directory = " +
        System.getProperty("user.dir"));

        
      /*  File file = new File("C:\\Directory1");
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }*/

       /* File theDir = new File("new foldero");

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;
        
            try{
                System.out.println( theDir.mkdir());
                result = true;
            } 
            catch(SecurityException se){
                //handle it
            }        
            if(result) {    
                System.out.println("DIR created");  
            }
        }
*/
        String name ="d";
        File directory = new File(name);
        if(!directory.exists()){
            if(directory.mkdir()){
                System.out.println("DIR created");
            }
        }

        //changer le repertoire 

        // Scanner scannerAddress = new Scanner(System.in);
        System.out.println("Working Directory = " +
        System.getProperty("user.dir"));


        Scanner scanner = new Scanner(System.in);
        String papa = scanner.next();

        System.setProperty("userdir",papa);

        System.out.println("le nouveau Working Directory = " +
        System.getProperty("user.dir"));

        System.out.println("Hello World!");
    }
}
