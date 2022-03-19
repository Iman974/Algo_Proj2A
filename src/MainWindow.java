import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame implements ActionListener {

    public MainWindow(String nom, int width, int height) {
        super(nom);
        setSize(width,height);
        setLocation(300,200);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        Timer t = new Timer(1 / 60, this);
        t.start();
    }

    public static void main(String[] args) {
        MainWindow w = new MainWindow("Game", 600, 600);
    }

    public void paint(Graphics g) {

    }

    public void actionPerformed(ActionEvent e) {

    }
}