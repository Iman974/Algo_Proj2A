import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener {

    private static Physics physics;

    private static int frameCounter;

    GameArea gameArea;
    SelectionBar particleSelector;
    scorePanel ScorePanel;

    public MainWindow(String name, int width, int height) {
        super(name);
        setSize(width, height);
        setLocation(300, 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        // Initialisation graphique
        JPanel background = new JPanel();
        background.setLayout(null);
        Insets insets = getInsets();
        background.setBounds(0, 0, getWidth() - insets.left - insets.right,
                getHeight() - insets.top - insets.bottom);
        add(background);

        this.gameArea = new GameArea(background.getWidth(), background.getHeight());

        this.particleSelector = new SelectionBar();
        background.add(this.gameArea);
        this.gameArea.add(particleSelector);

        this.ScorePanel = new scorePanel();
        background.add(this.gameArea);
        this.gameArea.add(ScorePanel);
    }

    public static int getFrame() {
        return frameCounter;
    }

    public static void main(String[] args) {
        MainWindow w = new MainWindow("Game", 1200, 900);
        physics = new Physics();

        // Initialisation du timer pour les animations
        // Toujours mettre à 17 ms d'intervalle (hors test), ce qui équivaut ~60 fps
        Timer t = new Timer(17, w);
        t.start();
//        System.out.println(System.getProperty("user.dir"));
    }

    // Appelée à chaque frame
    public void actionPerformed(ActionEvent e) {
        gameArea.updateScreen();
        physics.updateScene();
        frameCounter++;
    }
}
