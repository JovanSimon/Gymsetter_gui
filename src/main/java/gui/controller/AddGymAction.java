package gui.controller;

import gui.controller.popup.AddGymBox;
import gui.controller.popup.ChangeInfoBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class AddGymAction extends AbstractAction {

    public AddGymAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        putValue(NAME,"Add gym");
        putValue(SHORT_DESCRIPTION,"Add gym");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new AddGymBox().display();
    }
}
