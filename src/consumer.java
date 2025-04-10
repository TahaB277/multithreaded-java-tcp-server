import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class consumer {
    private static int port = 20000;

    public static void printInstructions(){
        System.out.println("-----------------------------------------------------------------");
        System.out.println("    LIST: Retrieve a list of all cities and their temperatures.");
        System.out.println("    AVG: Get the average temperature of all recorded cities.");
        System.out.println("    City: Get the temperature of the given city.");
        System.out.println("    .: Exit");
        System.out.println("-----------------------------------------------------------------\n");
    }

    public static void main(String[] args) {

        String input;
        Scanner key = new Scanner(System.in);
        System.out.println("Give the IP addres to connect to the server:");

        try {
            Socket socket = new Socket(key.nextLine(), port);
            System.out.println("Connected successfully\n");
            PrintStream outSocket = new PrintStream(socket.getOutputStream());
            Scanner inSocket = new Scanner(socket.getInputStream());
            outSocket.println("consumer");

            while(true){
                printInstructions();
                input = key.nextLine();
                if(input.equals(".")){
                    outSocket.println(input);
                    break;
                }
                // Send request to server
                outSocket.println(input);

                // If LIST, output to screen line by line (Check how the server handles "LIST"), else output only one value
                if(input.equals("LIST"))
                {
                    String line;
                    while (!(line = inSocket.nextLine()).equals("END")) {
                        System.out.println(line);
                    }
                }
                else
                    System.out.println(inSocket.nextLine());
            }

            socket.close();
            inSocket.close();
            key.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
