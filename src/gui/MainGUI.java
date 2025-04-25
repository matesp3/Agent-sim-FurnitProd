package gui;

import javax.swing.*;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

public class MainGUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FurnitureProdForm::new);
    }
}
