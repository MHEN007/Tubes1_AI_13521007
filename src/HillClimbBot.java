import javafx.scene.control.Button;
import java.util.*;

public class HillClimbBot implements Bot {
    private int moveX, moveY, botScore = 0;
    // private Button[][] state;
    private static final int ROW = 8;
    private static final int COL = 8;

    private String symbol, enemySymbol;

    public HillClimbBot(String symbol){
        this.symbol = symbol;
        this.enemySymbol = this.symbol == "O" ? "O" : "X";
    }

    @Override
    public void updateGameBoard(int i , int j, Button[][] buttons){
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
    public void setPlayerScore(int i, int j, Button[][] btn){

        if (btn[i][j].getText().equals(this.enemySymbol)) {
            btn[i][j].setText(this.symbol);
        }

    }

    @Override
    public Button[][] copyState(Button[][] src){
        Button[][] copy = new Button[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copy[i][j] = new Button(src[i][j].getText());
            }
        }
        return copy;
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
    public int[] move(Button[][] buttons) {
        this.moveX = -1;
        this.moveY = -1;

        ArrayList<Button[][]> states = new ArrayList<>();
        ArrayList<Integer> moveXs = new ArrayList<>();
        ArrayList<Integer> moveYs = new ArrayList<>();

        Button[][] state = copyState(buttons);
    
        this.botScore = countMark(this.symbol, state) - countMark(this.enemySymbol, state);

        for(int i = 0; i < 8 ; i++){
            for(int j = 0; j < 8; j++){
                // Create a copy of the buttons array
                state = copyState(buttons);
                
                if(state[i][j].getText().equals("")){
                    /* If available, set O at i,j */
                    state[i][j].setText(this.symbol);
                    /* Update Adjacent */
                    updateGameBoard(i,j, state);
                    /* Add State to States */
                    states.add(state);
                    moveXs.add(i);
                    moveYs.add(j);
                }
            }
        }
    
        int count = 0;
        boolean found = false;
        while(count < 64){
            int idx = (int) (Math.random() * states.size());
            Button[][] eval = copyState(states.get(idx));

            /* Evaluate this State */
            int countO = countMark(this.symbol, eval) - countMark(this.enemySymbol, eval);
            if(countO > this.botScore){
                this.botScore = countO;
                this.moveX = moveXs.get(idx);
                this.moveY = moveYs.get(idx);
                found = true;
            }
            // states.remove(idx);
            // moveXs.remove(idx);
            // moveYs.remove(idx);
            count++;
        }

        /* If there isn't any better successor, just use a random state */
        if(!found){
            int idx = (int) (Math.random() * states.size());
            this.moveX = moveXs.get(idx);
            this.moveY = moveYs.get(idx);
        }
        return new int[]{this.moveX, this.moveY};
    }
}