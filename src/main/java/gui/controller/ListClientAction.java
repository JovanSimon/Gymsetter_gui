package gui.controller;

import gui.controller.popup.ListClientsBox;
import gui.controller.popup.ListNotificationsBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ListClientAction extends AbstractAction {
    public ListClientAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        putValue(NAME,"List clients");
        putValue(SHORT_DESCRIPTION,"List clients");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new ListClientsBox().display();
    }
}
