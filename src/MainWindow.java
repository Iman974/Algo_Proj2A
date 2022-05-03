import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener {

    private static Physics physics;
    private static int currentFrame;

    GameArea gameArea;
    SelectionBar particleSelector;
    ScorePanel scorePanel;

    private long previousFrameTime;
    public static double deltaTime;
    public static int FPS = 60;

    public MainWindow(String name, int width, int height) {
        super(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Dimension de la fenêtre
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets insets = getInsets();
        setSize(width, screenSize.height - insets.top - insets.bottom);
        setLocation(screenSize.width / 2 - width / 2, 0);

        // Initialisation graphique
        JPanel background = new JPanel();
        background.setLayout(null);
        background.setBounds(0, 0, getWidth() - insets.left - insets.right,
                getHeight() - insets.top - insets.bottom);
        add(background);

        this.gameArea = new GameArea(background.getWidth(), background.getHeight());

        background.add(this.gameArea);
        this.scorePanel = new ScorePanel();
        this.particleSelector = new SelectionBar();
        this.gameArea.add(particleSelector);
        this.gameArea.add(scorePanel);
    }

    public static int getFrame() {
        return currentFrame;
    }

    public static void main(String[] args) {
        MainWindow w = new MainWindow("Game", 1200, 900);
        physics = new Physics();
        // Initialisation du timer pour les mises à jour graphiques
        // Toujours mettre à 17 ms d'intervalle (hors test), ce qui équivaut ~60 fps
        Timer frameTimer = new Timer((int)(1.0 / FPS * 1000), w);
        frameTimer.start();
    }

    public void actionPerformed(ActionEvent e) {
        deltaTime = (System.currentTimeMillis() - previousFrameTime) / 1000.0;
        previousFrameTime = System.currentTimeMillis();
        gameArea.updateScreen();
        currentFrame++;
    }
}
