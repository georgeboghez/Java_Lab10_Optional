package ro.uaic.info.georgeboghez;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * An instance of this class will read commands from the keyboard and it will send them to the server. The client stops when it reads from the keyboard the string "exit".
 */
public class GameClient {
    /**
     * The server's IP address
     */
    String serverAddress = "127.0.0.1";
    /**
     * The server's port
     */
    int PORT = 8100;

    /**
     * The constructor in which the communication will take place
     *
     * @throws IOException
     */
    private GameClient() throws IOException {
        try (
                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {
            // Send a request to the server
            Scanner scanner = new Scanner(System.in);

            System.out.print("Please enter your name: ");
            String request = scanner.nextLine();
            out.println(request);
            // Wait the response from the server ("Hello World!")
            String response = in.readLine();
            System.out.println(response);

            boolean loop = true;
            boolean isFirstMove = false;
            boolean isPlayerFirst = false;
            while (loop) {
                System.out.print(" > ");
                request = scanner.nextLine();
                if (request.equals("new game")) {
                    isFirstMove = true;
                    isPlayerFirst = true;
                }
                out.println(request);
                response = in.readLine();
                System.out.println(response);

                if (response.equals("Connection stopped")) {
                    loop = false;
                } else if (response.equals("Insert game number: ")) {
                    request = scanner.nextLine();
                    out.println(request);
                    response = in.readLine();
                    if (response.equals("Connected to the game successfully")) {
                        isFirstMove = true;
                    }
                    System.out.println(response);
                } else if (response.equals("The Board:")) {
                    for (int i = 0; i < 16; i++) {
                        response = in.readLine();
                        System.out.println(response);
                    }
                } else if (response.equals("Insert the row: ")) {
                    if (isFirstMove && isPlayerFirst) {
                        int threeMoves = 3;
                        while (threeMoves > 0) {
                            request = scanner.nextLine();
                            out.println(request);
                            response = in.readLine();
                            System.out.println(response);
                            request = scanner.nextLine();
                            out.println(request);
                            response = in.readLine();
                            System.out.println(response);
                            if (response.equals("The move has been made successfully. The board:")) {
                                for (int i = 0; i < 16; i++) {
                                    response = in.readLine();
                                    System.out.println(response);
                                }
                                --threeMoves;
                            }
                            if (threeMoves > 0) {
                                response = in.readLine();
                                System.out.println(response);
                            }
                        }
                        isFirstMove = false;
                    } else {
                        request = scanner.nextLine();
                        out.println(request);
                        response = in.readLine();
                        System.out.println(response);
                        request = scanner.nextLine();
                        out.println(request);
                        response = in.readLine();
                        System.out.println(response);
                        if (response.equals("The move has been made successfully. The board:")) {
                            for (int i = 0; i < 16; i++) {
                                response = in.readLine();
                                System.out.println(response);
                            }
                        }
                    }
                    response = in.readLine();
                    System.out.println(response);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("No server listening... " + e);
        }
    }

    public static void main(String[] args) {
        try {
            GameClient client = new GameClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
