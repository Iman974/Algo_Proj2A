import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SelectionBar extends JPanel implements ActionListener {

    JButton protonBtn;
    JButton electronBtn;
    JButton neutronBtn;
    Particle.Type selectedType;

    public SelectionBar() {
        final int BTN_SIZE = 100;
        //setBounds(GameArea.width - BTN_SIZE - 10, GameArea.height - BTN_SIZE - 10, BTN_SIZE,
        //        BTN_SIZE);
        setBounds(0, 0, 400, 100);
        
        Icon iconNeutron = new ImageIcon("../resources/neutronImage.jpg");
        neutronBtn = new JButton("N", iconNeutron);
        neutronBtn.setBounds(0, 0, BTN_SIZE,
                BTN_SIZE);
        add(neutronBtn);


        Icon iconProton = new ImageIcon("../resources/protonImage.jpg");
        protonBtn = new JButton("P", iconProton);
        protonBtn.setBounds(150, 0, BTN_SIZE,
                BTN_SIZE);
        add(protonBtn);

        Icon iconElectron = new ImageIcon("../resources/electronImage.jpg");
        electronBtn = new JButton("E", iconElectron);
        electronBtn.setBounds(300, 0, BTN_SIZE,
                BTN_SIZE);
        add(electronBtn);

        neutronBtn.addActionListener (this);
        electronBtn.addActionListener (this);
        protonBtn.addActionListener (this);

    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource() == neutronBtn){
                selectedType = Particle.Type.NEUTRON;
        } else if (e.getSource() == protonBtn){
                selectedType = Particle.Type.PROTON;
        } else if (e.getSource() == electronBtn){
                selectedType = Particle.Type.ELECTRON;
        }
        System.out.println(selectedType);
    }

}
