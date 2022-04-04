import javax.swing.*;
import java.awt.*;

public class SelectionBar extends JPanel {

    JButton protonBtn;
    JButton electronBtn;
    JButton neutronBtn;

    public SelectionBar() {
        final int BTN_SIZE = 100;
//        setBounds(GameArea.width - BTN_SIZE - 10, GameArea.height - BTN_SIZE - 10, BTN_SIZE,
//                BTN_SIZE);
        setBounds(0, 0, 100, 100);

        neutronBtn = new JButton("Neutron");
        neutronBtn.setBounds(0, 0, BTN_SIZE,
                BTN_SIZE);
        add(neutronBtn);
    }
}
