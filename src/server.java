import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


/* This is a server done with singleton lock to handle the hashmap
A more reliable way to manage the shared memory is to use ConcurentHashMap instead */

public class server{
    private static int port = 20000;

    public static HashMap<String, Double> initMap() {
        HashMap<String, Double> map = new HashMap<String, Double>();
    
        map.put("New York", 12.6);
        map.put("London", 11.1);
        map.put("Tokyo", 15.8);
        map.put("Paris", 12.3);
        map.put("Sydney", 18.2);
        map.put("Moscow", 5.8);
        map.put("Cairo", 22.0);
        map.put("Beirut", 20.4);
        map.put("Toronto", 7.9);
        map.put("Rio de Janeiro", 23.9);
    
        return map;
    }
    public static String makeFile(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"); // To make a safe file name
        String formattedTime = now.format(formatter);
        new File("Logs").mkdirs(); // To make sure that the directory is created
        String filePath = "Logs/" + formattedTime + ".txt";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Server launched at: " + now.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }


    public static void main(String[] args){
        
        // Initializing map with values
        HashMap<String, Double> map = initMap();

        // Making log file
        String filePath = makeFile();

        try {
            System.out.println(InetAddress.getLocalHost());
            System.out.println("Server started. Waiting for connection...");
            try (ServerSocket server = new ServerSocket(port)) {
                while(true){
                    Socket socket = server.accept();
                    System.out.println("Connection established with: " + socket.getRemoteSocketAddress());
                    // Making a thread for each client
                    Thread thread = new Thread(new consumerProducer(socket, filePath, map));
                    thread.start();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}