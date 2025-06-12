import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TetrisPanel extends JPanel implements ActionListener, KeyListener {
    private final int ROWS = 20;
    private final int COLS = 13;
    private final int BLOCK_SIZE = 30;

    private int[][] board;
    private Tetromino currentPiece;
    private Timer timer;

    private int score = 0;
    private JLabel scoreLabel;

    public TetrisPanel() {
        setFocusable(true);
        addKeyListener(this);

        board = new int[ROWS][COLS];
        spawnNewPiece();

        for (int col = 0; col < COLS; col++) {
            board[ROWS - 1][col] = 1;
        }

        timer = new Timer(500, this);
        timer.start();

        setLayout(null);
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setBounds(10, 10, 100, 20);
        add(scoreLabel);
    }

    private boolean canMove(int newX, int newY, int[][] shape) {
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[0].length; col++) {
                if (shape[row][col] != 0) {
                    int x = newX + col;
                    int y = newY + row;
                    if (x < 0 || x >= COLS || y < 0 || y >= ROWS || board[y][x] != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean canMove(int newX, int newY) {
        return canMove(newX, newY, currentPiece.shape);
    }

    private void movePieceDown() {
        if (canMove(currentPiece.x, currentPiece.y + 1)) {
            currentPiece.y++;
        } else {
            for (int row = 0; row < currentPiece.shape.length; row++) {
                for (int col = 0; col < currentPiece.shape[0].length; col++) {
                    if (currentPiece.shape[row][col] != 0) {
                        board[currentPiece.y + row][currentPiece.x + col] = 1;
                    }
                }
            }
            clearFullRows();
            spawnNewPiece();
        }
    }

    private void clearFullRows() {
        int cleared = 0;

        for (int row = ROWS - 2; row >= 0; row--) {
            boolean full = true;
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == 0) {
                    full = false;
                    break;
                }
            }

            if (full) {
                for (int r = row; r > 0; r--) {
                    System.arraycopy(board[r - 1], 0, board[r], 0, COLS);
                }
                for (int col = 0; col < COLS; col++) {
                    board[0][col] = 0;
                }
                row++;
                cleared++;
            }
        }

        if (cleared > 0) {
            score += cleared * 100;
            scoreLabel.setText("Score: " + score);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] != 0) {
                    g.setColor(row == ROWS - 1 ? Color.GRAY : Color.LIGHT_GRAY);
                    g.fillRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(col * BLOCK_SIZE, row * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                }
            }
        }

        g.setColor(Color.RED);
        for (int row = 0; row < currentPiece.shape.length; row++) {
            for (int col = 0; col < currentPiece.shape[0].length; col++) {
                if (currentPiece.shape[row][col] != 0) {
                    int x = (currentPiece.x + col) * BLOCK_SIZE;
                    int y = (currentPiece.y + row) * BLOCK_SIZE;
                    g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
                    g.setColor(Color.RED);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movePieceDown();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (canMove(currentPiece.x - 1, currentPiece.y)) {
                currentPiece.x--;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (canMove(currentPiece.x + 1, currentPiece.y)) {
                currentPiece.x++;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            movePieceDown();
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            int[][] rotated = currentPiece.getRotatedShape();
            if (canMove(currentPiece.x, currentPiece.y, rotated)) {
                currentPiece.shape = rotated;
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void spawnNewPiece() {
        currentPiece = new Tetromino();
        if (!canMove(currentPiece.x, currentPiece.y, currentPiece.shape)) {
            gameOver();
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + score);
        System.exit(0);
    }
}
