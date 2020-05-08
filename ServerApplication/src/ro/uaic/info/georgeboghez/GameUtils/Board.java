package ro.uaic.info.georgeboghez.GameUtils;

public class Board {
    private static Board board;
    private int[][] positions;

    public Board() {
        positions = new int[15][15];
    }

    public int[][] getPositions() {
        return positions;
    }

    public void setPositions(int[][] positions) {
        this.positions = positions;
    }

    public boolean isPositionFree(int xPos, int yPos) {
        return positions[xPos][yPos] == 0;
    }

    public void setPosition(boolean isFirst, boolean isThePlayersPiece, int xPos, int yPos) {
        if(isFirst) {
            if(isThePlayersPiece)
                positions[xPos][yPos] = 1;
            else
                positions[xPos][yPos] = 2;
        }
        else {
            if(isThePlayersPiece)
                positions[xPos][yPos] = 2;
            else
                positions[xPos][yPos] = 1;
        }
    }
}
