package gui.controller;

import gui.controller.popup.ChangeInfoBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class ChangeInfoAction extends AbstractAction {

    public ChangeInfoAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.CTRL_MASK));
        putValue(NAME,"Change info");
        putValue(SHORT_DESCRIPTION,"Change info");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new ChangeInfoBox().display();
    }
}
