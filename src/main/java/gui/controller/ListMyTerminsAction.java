package gui.controller;

import gui.controller.popup.ListClientsBox;
import gui.controller.popup.ListMyTerminsBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ListMyTerminsAction extends AbstractAction {
    public ListMyTerminsAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
        putValue(NAME,"List my trainings");
        putValue(SHORT_DESCRIPTION,"List my trainings");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        new ListMyTerminsBox().display();
    }
}
