package gui.controller;

import gui.controller.popup.AddGymBox;
import gui.controller.popup.ChangeInfoBox;
import gui.view.MainFrame;
import gui.view.typesOfViews.LoginView;
import gui.view.typesOfViews.WorkView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class LogOutAction extends AbstractAction {

    public LogOutAction(){
        putValue(ACCELERATOR_KEY,  KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.CTRL_MASK));
        putValue(NAME,"LogOut");
        putValue(SHORT_DESCRIPTION,"LogOut");
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        WorkView.getInstance().setToken("");
        MainFrame.getInstance().changePanel(LoginView.getInstance());
    }
}
