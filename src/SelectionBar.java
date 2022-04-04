import javax.swing.*;
import java.awt.*;

public class SelectionBar extends JPanel {

    JButton protonBtn;
    JButton electronBtn;
    JButton neutronBtn;

    public SelectionBar() {
        final int BTN_SIZE = 100;
        //setBounds(GameArea.width - BTN_SIZE - 10, GameArea.height - BTN_SIZE - 10, BTN_SIZE,
        //        BTN_SIZE);
        setBounds(0, 0, 400, 100);
        
        Icon iconNeutron = new ImageIcon("../resources/neutronImage.jpg");
        neutronBtn = new JButton(iconNeutron);
        neutronBtn.setBounds(0, 0, BTN_SIZE,
                BTN_SIZE);
        add(neutronBtn);

        Icon iconElectron = new ImageIcon("../resources/electronImage.jpg");
        electronBtn = new JButton(iconElectron);
        electronBtn.setBounds(150, 0, BTN_SIZE,
                BTN_SIZE);
        add(electronBtn);

        Icon iconProton = new ImageIcon("../resources/protonImage.jpg");
        protonBtn = new JButton(iconProton);
        protonBtn.setBounds(300, 0, BTN_SIZE,
                BTN_SIZE);
        add(protonBtn);

        


    }
}
