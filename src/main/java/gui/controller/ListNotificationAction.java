package gui.controller;

import gui.controller.popup.ListNotificationTypesBox;
import gui.controller.popup.ListNotificationsBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ListNotificationAction extends AbstractAction {
    public ListNotificationAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        putValue(NAME,"List notifications");
        putValue(SHORT_DESCRIPTION,"List notifications");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new ListNotificationsBox().display();
    }
}
