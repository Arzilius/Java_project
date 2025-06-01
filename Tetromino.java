import java.util.Random;

public class Tetromino {
    public int[][] shape;
    public int x, y;

    private static final int[][][] SHAPES = {
        // Square
        {{1, 1},
         {1, 1}},

        // Line
        {{1, 1, 1, 1}},

        // L-shape
        {{1, 0},
         {1, 0},
         {1, 1}},

        // T-shape
        {{0, 1, 0},
         {1, 1, 1}},

        // Z-shape
        {{1, 1, 0},
         {0, 1, 1}}
    };

    public Tetromino() {
        Random rand = new Random();
        shape = SHAPES[rand.nextInt(SHAPES.length)];
        x = 5; // start near the middle
        y = 0;
    }

    public int[][] getRotatedShape() {
        int rows = shape.length;
        int cols = shape[0].length;
        int[][] rotated = new int[cols][rows];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                rotated[col][rows - 1 - row] = shape[row][col];
            }
        }
        return rotated;
    }
}
