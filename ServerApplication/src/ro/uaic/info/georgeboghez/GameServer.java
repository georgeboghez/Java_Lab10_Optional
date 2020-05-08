package ro.uaic.info.georgeboghez;

import ro.uaic.info.georgeboghez.GameUtils.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * An instance of this class will create a ServerSocket running at a specified port. The server will receive requests (commands) from clients and it will execute them.
 */
public class GameServer {
    /**
     * the port on which the server is listening
     */
    public static final int PORT = 8100;

    private static ArrayList<Game> games = new ArrayList<>();

    /**
     * the constructor in which threads are created for handling the communication with the clients
     * @throws IOException
     */
    public GameServer() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            while (true) {
                System.out.println("Waiting for a client ...");
                Socket socket = serverSocket.accept();
                // Execute the client's request in a new thread
                new ClientThread(socket).start();
            }
        } catch (IOException e) {
            System.err.println("Ooops... " + e);
        } finally {
            serverSocket.close();
        }
    }

    public static int createGame(String name) {
        Game game = new Game();
        game.addPlayer(name);
        games.add(game);
        return games.indexOf(game);
    }

    public static ArrayList<Integer> getAvailableGames() {
        ArrayList<Integer> availableGames = new ArrayList<Integer>();
        for (Game game : games) {
            if (!game.didTheGameStart())
                availableGames.add(games.indexOf(game));
        }
        return availableGames;
    }

    public static boolean joinGame(int gameNumber, String name) {
        return games.get(gameNumber).addPlayer(name);
    }

    public static boolean submitMove(int gameNumber, boolean isFirstPlayer, boolean isThePlayersPiece, int xPos, int yPos) {
        return games.get(gameNumber).addPiece(isFirstPlayer, isThePlayersPiece, xPos - 1, yPos - 1);
    }

    public static int[][] getStateOfTheGame(int gameNumber) {
        return games.get(gameNumber).getPositions();
    }

    public static boolean isFirstPlayersTurn(int gameNumber) {
        return games.get(gameNumber).isFirstPlayersTurn();
    }

    public static void main(String[] args) throws IOException {
        GameServer server = new GameServer();
    }
}
