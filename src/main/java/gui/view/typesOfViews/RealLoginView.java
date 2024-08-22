package gui.view.typesOfViews;

import com.google.gson.Gson;
import gui.utils.Role;
import gui.utils.TokenRequestDto;
import gui.view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RealLoginView extends JPanel{
    private static RealLoginView instance;
    private JLabel welcomeMassage;
    private JTextField username;
    private JPasswordField password;
    private JButton loginButton, backButton;
    private RealLoginView(){

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

        welcomeMassage = new JLabel("Login to GymSetter!", SwingConstants.CENTER);
        welcomeMassage.setBorder(new EmptyBorder(200, 0, 0, 0));
        welcomeMassage.setFont(new Font("Arial", Font.BOLD, 18));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        username = new JTextField();
        username.setMaximumSize(new Dimension(400, 30));
        password = new JPasswordField();
        password.setMaximumSize(new Dimension(400, 30));
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(400, 50));
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(400, 50));
        backButton.addActionListener(e -> {
            MainFrame.getInstance().changePanel(LoginView.getInstance());
        });

        buttonPanel.setBorder(new EmptyBorder(30, 683, 0, 0));
        buttonPanel.add(new JLabel("Enter your username: "));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(username);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(new JLabel("Enter your password: "));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(password);
        buttonPanel.add(loginButton);
        buttonPanel.add(backButton);

        this.add(welcomeMassage, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e ->{
            try {
                String url = "http://localhost:8084/users/login";

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // Postavite tip zahteva i parametre
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");

                // Kreirajte objekat koji želite da pošaljete
                TokenRequestDto tokenRequestDto = new TokenRequestDto();
                tokenRequestDto.setUsername(this.username.getText());
                tokenRequestDto.setPassword(String.valueOf(this.password.getPassword()));

                // Konvertujte objekat u JSON string
                Gson gson = new Gson();
                String postData = gson.toJson(tokenRequestDto);

                System.out.println(postData);

                // Omogućite slanje podataka
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postData);
                wr.flush();
                wr.close();

                // Pročitajte odgovor ako je potrebno
                int responseCode = con.getResponseCode();

                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // Ovde možete raditi sa odgovorom koji ste dobili
                    System.out.println("iz clienta Response Body: " + response.toString());
                    if(response.toString().equals("") )
                        System.out.println("LOS UNOS");
                    else {
                        WorkView.getInstance().setToken(response.toString());
                        MainFrame.getInstance().changePanel(WorkView.getInstance());
                    }
                } else {
//                    try {
//                        url = "http://localhost:8050/api/manager/login";
//
//                        obj = new URL(url);
//                        con = (HttpURLConnection) obj.openConnection();
//
//                        // Postavite tip zahteva i parametre
//                        con.setRequestMethod("POST");
//                        con.setRequestProperty("Content-Type", "application/json");
//
//                        // Kreirajte objekat koji želite da pošaljete
//                        tokenRequestDto = new TokenRequestDto();
//                        tokenRequestDto.setUsername(this.username.getText());
//                        tokenRequestDto.setPassword(String.valueOf(this.password.getPassword()));
//
//                        // Konvertujte objekat u JSON string
//                        gson = new Gson();
//                        postData = gson.toJson(tokenRequestDto);
//
//                        System.out.println(postData);
//
//                        // Omogućite slanje podataka
//                        con.setDoOutput(true);
//                        wr = new DataOutputStream(con.getOutputStream());
//                        wr.writeBytes(postData);
//                        wr.flush();
//                        wr.close();
//
//                        // Pročitajte odgovor ako je potrebno
//                        responseCode = con.getResponseCode();
//
//                        if (responseCode == 200) {
//                            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//                            String inputLine;
//                            StringBuilder response = new StringBuilder();
//
//                            while ((inputLine = in.readLine()) != null) {
//                                response.append(inputLine);
//                            }
//                            in.close();
//
//                            // Ovde možete raditi sa odgovorom koji ste dobili
//                            System.out.println("Response Body: " + response.toString());
//                            WorkView.getInstance().setToken(response.toString());
//
//                            MainFrame.getInstance().changePanel(WorkView.getInstance());
//                        }
//
//                        System.out.println("iz managera Response Code: " + responseCode);
//                    } catch (Exception ee) {
//                        ee.printStackTrace();
//                    }

                }

                System.out.println("Response Code: " + responseCode);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        });
    }

    public static RealLoginView getInstance(){
        if (instance == null) {
            instance = new RealLoginView();
            instance.initialise();
        }
        return instance;
    }
}