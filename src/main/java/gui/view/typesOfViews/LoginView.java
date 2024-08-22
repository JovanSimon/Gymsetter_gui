package gui.view.typesOfViews;

import com.google.gson.Gson;
import gui.controller.ActionManager;
import gui.utils.ClientCreateDto;
import gui.utils.Role;
import gui.utils.TokenRequestDto;
import gui.view.MainFrame;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
@Getter
@Setter

public class LoginView extends JPanel {
    private static LoginView instance;
    private ActionManager actionManager;
    private JLabel welcomeMassage;
    private JButton loginButton, registerButton;
    private LoginView(){
        actionManager = new ActionManager();
    }

    private void initialise(){
        initialiseGui();

    }

    private void initialiseGui() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        setSize(screenWidth - 300 , screenHeight - 300 );
        this.setLayout(new BorderLayout());

        welcomeMassage = new JLabel("Welcome to GymSetter!", SwingConstants.CENTER);
        welcomeMassage.setBorder(new EmptyBorder(200, 0, 0, 0));
        welcomeMassage.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel buttonPanel = new JPanel();
        loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            MainFrame.getInstance().changePanel(RealLoginView.getInstance());
        });
        loginButton.setPreferredSize(new Dimension(200, 100));

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            MainFrame.getInstance().changePanel(RegisterView.getInstance());
        });
        registerButton.setPreferredSize(new Dimension(200, 100));

        buttonPanel.setBorder(new EmptyBorder(100, 0, 0, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        this.add(welcomeMassage, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);

    }

    public static LoginView getInstance(){
        if (instance == null) {
            instance = new LoginView();
            instance.initialise();
        }
        return instance;
    }
}
