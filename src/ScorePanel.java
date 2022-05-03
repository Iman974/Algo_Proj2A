import java.awt.Color;

import javax.swing.*;

public class ScorePanel extends JPanel{
    private final JLabel score;

    public ScorePanel() {
        setBackground(Color.YELLOW);
        final int SCORE_SIZE = 50;
        setBounds(GameArea.width - SCORE_SIZE - 30, 20, SCORE_SIZE, SCORE_SIZE);
        setLayout(null);

        score = new JLabel();
        score.setSize(50, 50);
        score.setLocation(0, 0);
        score.setText("score : " + GameArea.nbPoint);
        add(score);
    }
}