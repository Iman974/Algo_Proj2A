import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionBar extends JPanel implements ActionListener {

    private final JButton protonBtn;
    private final JButton electronBtn;
    private final JButton neutronBtn;

    public static Particle.Type selectedType = Particle.Type.PROTON;

    public SelectionBar() {
        final int BUTTON_SIZE = 50;
        setBounds(990, 800, 170, 50);

        Icon iconNeutron = new ImageIcon("../resources/neutronImage.jpg");
        neutronBtn = new JButton("N", iconNeutron);
        neutronBtn.setBounds(0, 0, BUTTON_SIZE,
                BUTTON_SIZE);
        add(neutronBtn);


        Icon iconProton = new ImageIcon("../resources/protonImage.jpg");
        protonBtn = new JButton("P", iconProton);
        protonBtn.setBounds(60, 0, BUTTON_SIZE,
                BUTTON_SIZE);
//        Graphics g = protonBtn.getGraphics();
//        g.setColor(Color.RED);
//        g.fillOval(0, 0, 40, 40);
        add(protonBtn);

        Icon iconElectron = new ImageIcon("../resources/electronImage.jpg");
        electronBtn = new JButton("E", iconElectron);
        electronBtn.setBounds(120, 0, BUTTON_SIZE,
                BUTTON_SIZE);
        add(electronBtn);

        neutronBtn.addActionListener(this);
        electronBtn.addActionListener(this);
        protonBtn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == neutronBtn) {
            selectedType = Particle.Type.NEUTRON;
        } else if (e.getSource() == protonBtn) {
            selectedType = Particle.Type.PROTON;
        } else if (e.getSource() == electronBtn) {
            selectedType = Particle.Type.ELECTRON;
        }
        System.out.println(selectedType);
    }

}
