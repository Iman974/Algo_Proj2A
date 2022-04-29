import java.awt.Color;

import javax.swing.*;

public class scorePanel extends JPanel{
    private final JLabel score;

    public scorePanel() {
        setBackground(Color.YELLOW);
        setBounds(1115, 20, 50, 50);

        score = new JLabel();
        score.setSize(50, 50);
        score.setLocation(0, 0);
        score.setText("score : " + GameArea.nbPoint);
        add(score);
    }


}