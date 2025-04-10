import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Scanner;

public class consumerProducer extends Thread{
    Socket socket;
    HashMap<String ,Double> map;
    String text, filePath, city;
    Double temp;

    public consumerProducer(Socket socket, String filePath, HashMap<String ,Double> map) {
        this.socket = socket;
        this.map = map;
        this.filePath = filePath;
    }
    
    @Override
    public void run()
    {
        try {
            Scanner inSocket = new Scanner(socket.getInputStream());
            PrintStream outSocket=new PrintStream(socket.getOutputStream());
            if(inSocket.nextLine().equals("consumer"))
                handleConsumer(inSocket, outSocket, map);
            else
                handleProducer(inSocket, outSocket, map);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleConsumer(Scanner inSocket, PrintStream outSocket, HashMap<String ,Double> map){
        
        write("Consumer " + socket.getRemoteSocketAddress() + " is connected");
        while(true){
            text = inSocket.nextLine();
            if(text.equals("."))
                break;

            switch (text) {
                case "LIST":
                    listCities(outSocket);
                    write("Consumer " + socket.getRemoteSocketAddress() + " requested LIST");
                    break;

                case "AVG":
                    sendAvg(outSocket);
                    write("Consumer " + socket.getRemoteSocketAddress() + " requested AVG");
                    break;

                default:
                    // Getting the temp corresponding to the city 
                    memoryLock.getInstance().lock();
                    Double temp = map.get(text);
                    memoryLock.getInstance().unlock();

                    // Sending the temp
                    if (!map.containsKey(text)) {
                        outSocket.println("City not found.");
                        break;
                    }
                    outSocket.println("Temperature for " + text + ": " + temp);
                    write("Consumer " + socket.getRemoteSocketAddress() + " requested " + text + " temperature");
                    break;
            }
        }
        write("Consumer " + socket.getRemoteSocketAddress() + " is DISCONNECTED");
        inSocket.close();
        return;
    }



    public void handleProducer(Scanner inSocket, PrintStream outSocket, HashMap<String ,Double> map){
            
        write("Producer " + socket.getRemoteSocketAddress() + " is connected");
        while(true){
            // Reading the values
            text = inSocket.nextLine();
            if(text.equals("."))
                break;

            city = text.split(" ")[0];
            temp = Double.parseDouble(text.split(" ")[1]);

            // Checking conditions
            if(temp == null){
                outSocket.println("This city does not exist in the hashMap, Please enter a valid city!");
                write("Producer " + socket.getRemoteSocketAddress() + " sent an invalid city");
                continue;
            }
            if (temp < -50 || temp > 60) {
                outSocket.println("Temperature out of range (-50 to 60°C).");
                write("Producer " + socket.getRemoteSocketAddress() + " sent out-of-range temperature for " + city);
                continue;
            }

            // Updating the value
            memoryLock.getInstance().lock();
            map.put(city, temp);
            memoryLock.getInstance().unlock();
            outSocket.println("Value updated successfully.");
            write("Producer " + socket.getRemoteSocketAddress() + " updated " + city + " to " + temp + "°C.");
            }
        write("Producer " + socket.getRemoteSocketAddress() + " is DISCONNECTED");
        inSocket.close();
    }


    public void listCities(PrintStream outSocket) {
        for (String city : map.keySet()) {
            outSocket.println(city + ": " + map.get(city));
        }
        outSocket.println("END");  // special marker to signal end
    }
    public void sendAvg(PrintStream outSocket){
        // Calculating the average temperature
        double sum = 0;
        for (double temperature : map.values()) {
            sum += temperature;
        }
        double avgTemp = sum / map.size();
        outSocket.println("Average temperature: " + avgTemp);
    }
    public void write(String text){
        logFileLock.getInstance().lock();
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(LocalDateTime.now().toString() + "\t" + text + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logFileLock.getInstance().unlock();
    }
}
