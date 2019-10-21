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
import java.util.Properties;
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
        //String addressIp;
     /*   do{
            System.out.print("Enter a valid IP address (ex: 123.123.12.1): ");
            addressIp = scanner.next();
        }while(!checkAddress(addressIp));

        int port;
        do{
            System.out.print("Enter a valid port (entre 5000 et 5050):");
            port = scanner.nextInt();
        }while(port < 5050 && port > 5000);*/



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


        //System.out.println(directory.getP)

        //changer le repertoire
        String commandName;
        String currentDirectoryString =  System.getProperty("user.dir") ;
        // Scanner scannerAddrescurrentDirectoryString =  System.getProperty("user.dir") ;s = new Scanner(System.in);
        do {
            System.out.println("entrer votre commande");
            String clientCommand = scanner.nextLine();
            // System.out.println(clientCommand);
            String command[] = clientCommand.split(" ");// le premier string est le nom de la commande
             commandName = command[0];
        switch (commandName) {
            case "ls":
                File dir = new File(currentDirectoryString);
                System.out.println("ls:vous etes actuellement dans le chemin"+currentDirectoryString);
                String[] fileNames = dir.list();
                String response = "";
                for (String fileName : fileNames) {
                    response += fileName + ";";
                    System.out.println(fileName + "  ");
                }

                break;

            case "cd":

               String directoryName = command[1];
               String slash = "..";
              // if(directoryName == slash){//si le repertoire de deplacement est le repertoire parent
                if(directoryName.compareTo(slash) == 0){//si le repertoire de deplacement est le repertoire parent
                    File dircourant = new File(currentDirectoryString);
                    File  dirParent = dircourant.getParentFile();// le  dossier parent
                    if(dirParent.exists()){
                        currentDirectoryString = dirParent.getAbsolutePath();
                        System.out.println("vous etes actuellement dans le chemin"+currentDirectoryString);
                    }
               }else{ // si le repertoire de deplacement est le repertoire enfant

                    File dircourant = new File(currentDirectoryString);
                    //verifier si le fichier existe dans la  liste des fichiers



                    String[] FileNames = dircourant.list();
                    boolean filefound = false;
                    for (String fileName : FileNames) {
                        //System.out.println("les fichiers dans le repertoire courant sont "+fileName +" " +directoryName);
                        if(fileName .compareTo(directoryName)== 0){ // si les deux strings se correspondent
                            filefound = true;
                            break;
                        }
                    }

                        //creer un dossier a partir du fichiers choisi
                        //ouvrir le repertoire

                    if(filefound == true){ // si le fichier a ete trouve

                        //currentDirectoryString = dircourant.getAbsolutePath() + '\\' + directoryName ;
                        currentDirectoryString += '\\' + directoryName ;
                        System.out.println("vous etes actuellement dans le chemin"+currentDirectoryString);
                    }

                    //	System.out.println("les fichiers dans le repertoire courant sont "+currentDirectoryString);

                    else{
                    //	System.out.print("le nom du fichier entre ne figure pas dans  le repertoire courant");
                    System.out.println("le fichier de reference n'existe pas");
                    }

               }
                break;

            case "mkdir":
                String path = currentDirectoryString + "\\"+ command[1];
                File directory = new File(path);
                if (!directory.exists()) { // verifier que le dossier a ete cree
                    if (directory.mkdir()) {
                        System.out.println("Le fichier " + path + "a ete creer");
                    }
                }
                break;


            case "upload":
              //  if (test(command[1])) {
              //      uploadFile(command[1]);
               // }
                break;

            case "download":
                break;

            case "exit":
                break;

            default:
                break;
            }



        } while (commandName.compareTo("exit") != 0 );


    }
}
