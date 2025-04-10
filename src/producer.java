import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class producer {
    private static int port = 20000;

    public static void printInstructions(){
        System.out.println("-----------------------------------------------------------------");
        System.out.println("    Enter city name followed by temperature.");
        System.out.println("    Type '.' as city name to exit.");
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
            outSocket.println("producer");

            while(true){
                printInstructions();
                input = key.nextLine();
                if(input.equals(".")){
                    outSocket.println(input);
                    break;
                }
                // Send request to server
                outSocket.println(input);

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
