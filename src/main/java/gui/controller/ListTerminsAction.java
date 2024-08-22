package gui.controller;

import gui.controller.popup.ListNotificationTypesBox;
import gui.controller.popup.ListTerminsBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ListTerminsAction extends AbstractAction {
    public ListTerminsAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        putValue(NAME,"List trainings");
        putValue(SHORT_DESCRIPTION,"List trainings");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new ListTerminsBox().display();
    }
}
