import javafx.scene.control.Button;

public interface Bot {
    public int[] move(Button[][] buttons);
    public int countMark(String mark, Button[][] buttons);
    public void updateGameBoard(int i , int j, Button[][] buttons);
    public void setPlayerScore(int i, int j, Button[][] btn);
    public Button[][] copyState(Button[][] src);
}
