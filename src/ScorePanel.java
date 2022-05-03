import java.awt.*;

import javax.swing.*;

public class ScorePanel extends JPanel {

    public JLabel score;

    public ScorePanel() {
        setBackground(Color.YELLOW);
        final int SCORE_SIZE = 50;
        setBounds(GameArea.width - SCORE_SIZE - 30, 20, SCORE_SIZE, SCORE_SIZE);
        setLayout(null);

        score = new JLabel();
        score.setSize(50, 50);
        score.setHorizontalAlignment(SwingConstants.CENTER);
        score.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        score.setLocation(0, 0);
        score.setText(String.valueOf(Main.getScore()));
        add(score);
    }
}