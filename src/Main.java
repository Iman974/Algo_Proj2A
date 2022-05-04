import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame implements ActionListener {

    private static Physics physics;
    private static int currentFrame;

    private static GameArea gameArea;
    private static SelectionBar particleSelector;
    private static JLabel scoreText;

    public static int FPS = 60;

    private static int scoreCount;

    public Main(String name, int width) {
        super(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Dimensionnement de la fenêtre
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets insets = getInsets();
        setSize(width, screenSize.height - insets.top - insets.bottom);
        setLocation(screenSize.width / 2 - width / 2, 0);

        // Initialisation composants graphiques
        JPanel background = new JPanel();
        background.setLayout(null);
        background.setBounds(0, 0, getWidth() - insets.left - insets.right,
                getHeight() - insets.top - insets.bottom);
        add(background);

        gameArea = new GameArea(background.getWidth(), background.getHeight());

        JPanel scorePanel = new JPanel();
        scorePanel.setBackground(new Color(255, 225, 0));
        final int SCORE_SIZE = 50;
        scorePanel.setBounds(GameArea.width - SCORE_SIZE - 30, 20, SCORE_SIZE, SCORE_SIZE);
        scoreText = new JLabel();
        scoreText.setSize(50, 50);
        scoreText.setHorizontalAlignment(SwingConstants.CENTER);
        scoreText.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        scoreText.setLocation(0, 0);
        scoreText.setText("0");

        background.add(gameArea);
        particleSelector = new SelectionBar();
        gameArea.add(particleSelector);
        gameArea.add(scorePanel);
        scorePanel.add(scoreText);
    }

    public static int getFrame() {
        return currentFrame;
    }

    public static void main(String[] args) {
        Main game = new Main("•~Neutrino~•", 1200);
        physics = new Physics();

        // Initialisation du timer pour les mises à jour graphiques
        Timer frameTimer = new Timer((int)(1.0 / FPS * 1000), game);
        frameTimer.start();
    }

    public static void addScore(int points) {
        scoreCount += points;
        scoreText.setText(String.valueOf(scoreCount));
    }

    public static int getScore() {
        return scoreCount;
    }

    public void actionPerformed(ActionEvent e) {
        gameArea.updateScreen();
        currentFrame++;
    }
}
