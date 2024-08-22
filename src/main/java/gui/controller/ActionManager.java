package gui.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActionManager {
    private ChangeInfoAction changeInfoAction;
    private ChangePasswordAction changePasswordAction;
    private AddNotificationTypeAction addNotificationTypeAction;
    private ListNotificationTypesAction listNotificationTypesAction;
    private ListNotificationAction listNotificationAction;
    private ListClientAction listClientAction;
    private AddGymAction addGymAction;
    private ChangeInfoGymAction changeInfoGymAction;
    private AddTerminsAction addTerminsAction;
    private ListTerminsAction listTerminsAction;
    private ChangeTerminsManagerAction changeTerminsManagerAction;
    private LogOutAction logOutAction;
    private ListMyTerminsAction listMyTerminsAction;

    public ActionManager(){
        changeInfoAction = new ChangeInfoAction();
        changePasswordAction = new ChangePasswordAction();
        addNotificationTypeAction = new AddNotificationTypeAction();
        listNotificationTypesAction = new ListNotificationTypesAction();
        listNotificationAction = new ListNotificationAction();
        listClientAction = new ListClientAction();
        addGymAction = new AddGymAction();
        changeInfoGymAction = new ChangeInfoGymAction();
        addTerminsAction = new AddTerminsAction();
        listTerminsAction = new ListTerminsAction();
        changeTerminsManagerAction = new ChangeTerminsManagerAction();
        logOutAction = new LogOutAction();
        listMyTerminsAction = new ListMyTerminsAction();
    }
}
