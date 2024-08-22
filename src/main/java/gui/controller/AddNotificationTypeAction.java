package gui.controller;

import gui.controller.popup.AddNotificationTypeBox;
import gui.controller.popup.ChangePasswordBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class AddNotificationTypeAction extends AbstractAction {
    public AddNotificationTypeAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        putValue(NAME,"Add notification");
        putValue(SHORT_DESCRIPTION,"Add notification");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new AddNotificationTypeBox().display();
    }
}
