package gui.view.typesOfViews;

import javax.swing.*;

public class Toolbar extends JToolBar {
    public Toolbar() {
        super(HORIZONTAL);
        setFloatable(false);

        add(LoginView.getInstance().getActionManager().getChangeInfoAction());
        add(LoginView.getInstance().getActionManager().getChangePasswordAction());
        add(LoginView.getInstance().getActionManager().getAddNotificationTypeAction());
        add(LoginView.getInstance().getActionManager().getListNotificationTypesAction());
        add(LoginView.getInstance().getActionManager().getListNotificationAction());
        add(LoginView.getInstance().getActionManager().getListClientAction());
        add(LoginView.getInstance().getActionManager().getAddGymAction());
        add(LoginView.getInstance().getActionManager().getChangeInfoGymAction());
        add(LoginView.getInstance().getActionManager().getAddTerminsAction());
        add(LoginView.getInstance().getActionManager().getListTerminsAction());
        add(LoginView.getInstance().getActionManager().getChangeTerminsManagerAction());
        add(LoginView.getInstance().getActionManager().getLogOutAction());
        add(LoginView.getInstance().getActionManager().getListMyTerminsAction());
    }
}
