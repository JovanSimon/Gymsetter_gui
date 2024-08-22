package gui.controller;

import gui.controller.popup.ChangeInfoGymBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ChangeInfoGymAction extends AbstractAction {

    public ChangeInfoGymAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        putValue(NAME,"Change gym");
        putValue(SHORT_DESCRIPTION,"Change gym");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new ChangeInfoGymBox().display();
    }
}
