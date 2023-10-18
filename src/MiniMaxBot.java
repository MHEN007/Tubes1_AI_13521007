import javafx.scene.control.Button;

public class MiniMaxBot implements Bot {
    private static final int ROW = 8;
    private static final int COL = 8;
    private int moveX, moveY, botScore = Integer.MIN_VALUE;
    private int depth;
    private boolean playersTurn;

    private String symbol, enemySymbol;

    public MiniMaxBot(String symbol){
        this.symbol = symbol;
        if(this.symbol.equals("O")){
            this.enemySymbol = "X";
        }else{
            this.enemySymbol = "O";
        }
        this.playersTurn = true;
    }

    public void setDepth(int depth){
        this.depth = depth;
    }

    @Override
    public int[] move(Button[][] buttons) {
        return move(buttons, this.depth, this.playersTurn);
    }

    public int[] move(Button[][] buttons, int depth, boolean playersTurn) {
        this.moveX = -1;
        this.moveY = -1;
        this.botScore = Integer.MIN_VALUE;

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                System.out.println(i + " " + j);
                if (buttons[i][j].getText().equals("")) {
                    Button[][] copyBoard = copyState(buttons);

                    copyBoard[i][j].setText(this.symbol);

                    updateGameBoard(i, j, copyBoard);

                    int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;

                    int score = minimax(copyBoard, depth + 1, alpha, beta, !playersTurn);

                    if (score > this.botScore) {
                        this.botScore = score;
                        this.moveX = i;
                        this.moveY = j;
                    }
                }
            }
        }
        return new int[]{this.moveX, this.moveY};
    }

    @Override
    public void updateGameBoard(int i, int j, Button[][] buttons) {
        // Value of indices to control the lower/upper bound of rows and columns
        // in order to change surrounding/adjacent X's and O's only on the game board.
        // Four boundaries:  First & last row and first & last column.

        int startRow, endRow, startColumn, endColumn;

        if (i - 1 < 0)     // If clicked button in first row, no preceding row exists.
            startRow = i;
        else               // Otherwise, the preceding row exists for adjacency.
            startRow = i - 1;

        if (i + 1 >= ROW)  // If clicked button in last row, no subsequent/further row exists.
            endRow = i;
        else               // Otherwise, the subsequent row exists for adjacency.
            endRow = i + 1;

        if (j - 1 < 0)     // If clicked on first column, lower bound of the column has been reached.
            startColumn = j;
        else
            startColumn = j - 1;

        if (j + 1 >= COL)  // If clicked on last column, upper bound of the column has been reached.
            endColumn = j;
        else
            endColumn = j + 1;


        // Search for adjacency for X's and O's or vice versa, and replace them.
        // Update scores for X's and O's accordingly.
        for (int x = startRow; x <= endRow; x++) {
            this.setPlayerScore(x, j, buttons);
        }

        for (int y = startColumn; y <= endColumn; y++) {
            this.setPlayerScore(i, y, buttons);
        }
    }

    @Override
    public void setPlayerScore(int i, int j, Button[][] buttons){
        if (this.playersTurn) {
            if (buttons[i][j].getText().equals(this.enemySymbol)) {
                buttons[i][j].setText(this.symbol);
            }
        } else {
            if (buttons[i][j].getText().equals(this.symbol)) {
                buttons[i][j].setText(this.enemySymbol);
            }
        }
    }

    private int minimax(Button[][] buttons, int depth, int alpha, int beta, boolean playersTurn) {
        if (depth == 3 || isGameOver(buttons)) {
            return heuristic(buttons);
        }

        int maxEval = Integer.MIN_VALUE;
        int minEval = Integer.MAX_VALUE;

        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (buttons[i][j].getText().equals("")) {
                    Button[][] copyBoard = copyState(buttons);
                    copyBoard[i][j].setText(playersTurn ? this.symbol : this.enemySymbol);
                    this.playersTurn = playersTurn;
                    updateGameBoard(i, j, copyBoard);

                    int eval = minimax(copyBoard, depth + 1, alpha, beta, !playersTurn);

                    if (playersTurn) {
                        maxEval = Math.max(eval, maxEval);
                        alpha = Math.max(alpha, eval);
                    } else {
                        minEval = Math.min(eval, minEval);
                        beta = Math.min(beta, eval);
                    }

                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }

        return playersTurn ? maxEval : minEval;
    }

    private boolean isGameOver(Button[][] buttons) {
        return (countMark("O", buttons) + countMark("X", buttons) == 64);
    }

    private int heuristic(Button[][] buttons) {
        return countMark(this.symbol, buttons) - countMark(this.enemySymbol, buttons);
    }

    @Override
    public int countMark(String mark, Button[][] buttons) {
        int count = 0;
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (buttons[i][j].getText().equals(mark)) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public Button[][] copyState(Button[][] original) {
        Button[][] copy = new Button[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                copy[i][j] = new Button(original[i][j].getText());
            }
        }
        return copy;
    }
}
