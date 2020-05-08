package ro.uaic.info.georgeboghez.GameUtils;

public class Game {
    private Board board;
    private Player player1;
    private Player player2;
    private boolean isFirst;

    public Game() {
        board = new Board();
        isFirst = true;
    }

    public boolean addPlayer(String name) {
        if (player1 == null) {
            player1 = new Player(name, true);
            return true;
        } else if (player2 == null) {
            player2 = new Player(name, false);
            return true;
        }
        return false;
    }

    public boolean addPiece(boolean firstPlayer, boolean isThePlayersPiece, int xPos, int yPos) {
        if(!board.isPositionFree(xPos,yPos)) {
            return false;
        }

        board.setPosition(firstPlayer, isThePlayersPiece, xPos, yPos);
        isFirst = !isFirst;
        return true;
    }

    public int[][] getPositions() {
        return board.getPositions();
    }

    public boolean isFirstPlayersTurn() {
        return isFirst;
    }

    public boolean didTheGameStart() {
        return player1 != null && player2 != null;
    }
}
