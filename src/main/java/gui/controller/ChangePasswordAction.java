package gui.controller;

import gui.controller.popup.ChangeInfoBox;
import gui.controller.popup.ChangePasswordBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ChangePasswordAction extends AbstractAction {

    public ChangePasswordAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_MASK));
        putValue(NAME,"Change password");
        putValue(SHORT_DESCRIPTION,"Change password");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new ChangePasswordBox().display();
    }
}
