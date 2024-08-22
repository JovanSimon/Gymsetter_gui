package gui.controller;

import gui.controller.popup.AddNotificationTypeBox;
import gui.controller.popup.ListNotificationTypesBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ListNotificationTypesAction extends AbstractAction {
    public ListNotificationTypesAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
        putValue(NAME,"List notifications type");
        putValue(SHORT_DESCRIPTION,"List notifications type");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new ListNotificationTypesBox().display();
    }
}
