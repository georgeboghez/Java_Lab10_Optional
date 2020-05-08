package ro.uaic.info.georgeboghez;

import ro.uaic.info.georgeboghez.GameUtils.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * An instance of this class (class which extends Thread) will be responsible with communicating with a client Socket. If the server receives the command stop it will stop and will return to the client the respons "Server stopped", otherwise it return: "Command ... received".
 */
public class ClientThread extends Thread {
    /**
     * a Socket object representing an endpoint for communication between two machines.
     */
    private Socket socket;
    private String clientName;
    private int gameNumber;
    private boolean firstPlayer;
    private boolean isClientConnectedToAGame;
    private boolean isFirstMove;

    /**
     * Constructor which sets the socket correspondingly
     *
     * @param socket a Socket object
     */
    public ClientThread(Socket socket) {
        this.socket = socket;
    }

    /**
     * Get the request from the input stream: client → server
     * Send the response to the oputput stream: server → client
     */
    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String request = in.readLine();

            PrintWriter out = new PrintWriter(socket.getOutputStream());

            clientName = request;

            System.out.println("Client named " + clientName + " connected.");
            String raspuns = "Hello, " + clientName + "!";
            out.println(raspuns);
            out.flush();

            boolean loop = true;
            while (loop) {
                request = in.readLine();
                System.out.println(clientName + ": command '" + request + "' received.");
                if (request.equals("exit")) {
                    loop = false;
                    raspuns = "Connection stopped";
                } else if (request.equals("new game")) {
                    gameNumber = GameServer.createGame(clientName);
                    firstPlayer = true;
                    isFirstMove = true;
                    raspuns = "Game number " + gameNumber + " initiated. Waiting for an opponent.";
                } else if (request.equals("list games")) {
                    ArrayList<Integer> availableGames = GameServer.getAvailableGames();
                    if (availableGames.isEmpty()) {
                        raspuns = "There are no games available. Consider creating one yourself by using the command 'new game'.";
                    } else {
                        raspuns = "Available games: " + availableGames.stream().map(Object::toString)
                                .collect(Collectors.joining(", "));
                    }
                } else if (request.equals("join game")) {
                    raspuns = "Insert game number: ";
                    isFirstMove = true;
                    out.println(raspuns);
                    out.flush();
                    request = in.readLine();
                    gameNumber = Integer.parseInt(request);
                    if (GameServer.joinGame(gameNumber, clientName)) {
                        raspuns = "Connected to the game successfully";
                        isClientConnectedToAGame = true;
                    } else {
                        raspuns = "The game corresponding to the number selected is not available. Please consider listing the available games.";
                    }
                } else if (request.equals("submit move")) {
                    if ((GameServer.isFirstPlayersTurn(gameNumber) && firstPlayer) || (!GameServer.isFirstPlayersTurn(gameNumber) && !firstPlayer)) {
                        if (isFirstMove && firstPlayer) {
                            int threeMoves = 3;
                            while (threeMoves > 0) {
                                raspuns = "Insert the row: ";
                                out.println(raspuns);
                                out.flush();
                                int xPos = Integer.parseInt(in.readLine());
                                raspuns = "Insert the column: ";
                                out.println(raspuns);
                                out.flush();
                                int yPos = Integer.parseInt(in.readLine());
                                if (threeMoves == 1) {
                                    if (GameServer.submitMove(gameNumber, firstPlayer, false, xPos, yPos)) {
                                        System.out.println("ceva");
                                        raspuns = "The move has been made successfully. The board:\n";
                                        int[][] positions = GameServer.getStateOfTheGame(gameNumber);
                                        StringBuilder raspunsBuilder = new StringBuilder(raspuns);
                                        for (int i = 0; i < positions.length; i++) {
                                            for (int j = 0; j < positions[i].length; j++) {
                                                raspunsBuilder.append(positions[i][j]).append(' ');
                                            }
                                            raspunsBuilder.append('\n');
                                        }
                                        raspuns = raspunsBuilder.toString();
                                        out.println(raspuns);
                                        out.flush();
                                        --threeMoves;
                                    } else {
                                        raspuns = "A piece is already placed on the selected position. Submit another move.";
                                        out.println(raspuns);
                                        out.flush();
                                    }
                                } else {
                                    if (GameServer.submitMove(gameNumber, firstPlayer, true, xPos, yPos)) {
                                        raspuns = "The move has been made successfully. The board:\n";
                                        int[][] positions = GameServer.getStateOfTheGame(gameNumber);
                                        StringBuilder raspunsBuilder = new StringBuilder(raspuns);
                                        for (int i = 0; i < positions.length; i++) {
                                            for (int j = 0; j < positions[i].length; j++) {
                                                raspunsBuilder.append(positions[i][j]).append(' ');
                                            }
                                            raspunsBuilder.append('\n');
                                        }
                                        raspuns = raspunsBuilder.toString();
                                        out.println(raspuns);
                                        out.flush();
                                        --threeMoves;
                                    } else {
                                        raspuns = "A piece is already placed on the selected position. Submit another move.";
                                        out.println(raspuns);
                                        out.flush();
                                    }
                                }
                                isFirstMove = false;
                            }
                        } else {
                            boolean done = false;
                            while (!done) {
                                raspuns = "Insert the row: ";
                                out.println(raspuns);
                                out.flush();
                                int xPos = Integer.parseInt(in.readLine());
                                raspuns = "Insert the column: ";
                                out.println(raspuns);
                                out.flush();
                                int yPos = Integer.parseInt(in.readLine());
                                if (GameServer.submitMove(gameNumber, firstPlayer, true, xPos, yPos)) {
                                    raspuns = "The move has been made successfully. The board:\n";
                                    int[][] positions = GameServer.getStateOfTheGame(gameNumber);
                                    StringBuilder raspunsBuilder = new StringBuilder(raspuns);
                                    for (int i = 0; i < positions.length; i++) {
                                        for (int j = 0; j < positions[i].length; j++) {
                                            raspunsBuilder.append(positions[i][j]).append(' ');
                                        }
                                        raspunsBuilder.append('\n');
                                    }
                                    raspuns = raspunsBuilder.toString();
                                    out.println(raspuns);
                                    out.flush();
                                    done = true;
                                } else {
                                    raspuns = "A piece is already placed on the selected position. Submit another move.";
                                    out.println(raspuns);
                                    out.flush();
                                }
                            }
                        }
                        raspuns = "After a few seconds, check if the opponent has made his move by trying to make your move.";
                    } else {
                        raspuns = "The other player hasn't made his move yet. Please wait a second and then try again.";
                    }
                } else if (request.equals("show board")) {
                    raspuns = "The Board:\n";
                    int[][] positions = GameServer.getStateOfTheGame(gameNumber);
                    StringBuilder raspunsBuilder = new StringBuilder(raspuns);
                    for (int i = 0; i < positions.length; i++) {
                        for (int j = 0; j < positions[i].length; j++) {
                            raspunsBuilder.append(positions[i][j]).append(' ');
                        }
                        raspunsBuilder.append('\n');
                    }
                    raspuns = raspunsBuilder.toString();

                } else {
                    raspuns = "Response: command '" + request + "' received.";
                }
                out.println(raspuns);
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Communication error... " + e);
        } finally {
            try {
                socket.close(); // or use try-with-resources
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

}
