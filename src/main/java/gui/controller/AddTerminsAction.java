package gui.controller;

import gui.controller.popup.AddGymBox;
import gui.controller.popup.AddTerminsBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddTerminsAction extends AbstractAction {

    public AddTerminsAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        putValue(NAME,"Add Termin");
        putValue(SHORT_DESCRIPTION,"Add Termin");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new AddTerminsBox().display();
    }
}
