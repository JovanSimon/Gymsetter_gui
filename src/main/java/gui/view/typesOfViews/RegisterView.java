package gui.view.typesOfViews;

import com.google.gson.Gson;
import gui.utils.ClientCreateDto;
import gui.utils.Role;
import gui.view.MainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterView extends JPanel {
    private static RegisterView instance;
    private JLabel welcomeMassage;
    private JTextField username, email;
    private JPasswordField password;
    private JButton registerButton, backButton;
    private JRadioButton managerRadio, userRadio;
    private RegisterView(){

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

        welcomeMassage = new JLabel("Register to GymSetter!", SwingConstants.CENTER);
        welcomeMassage.setBorder(new EmptyBorder(150, 0, 0, 0));
        welcomeMassage.setFont(new Font("Arial", Font.BOLD, 18));

        managerRadio = new JRadioButton("Register as manager");
        userRadio = new JRadioButton("Register as user");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(managerRadio);
        buttonGroup.add(userRadio);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        username = new JTextField();
        email = new JTextField();
        email.setMaximumSize(new Dimension(400, 30));
        username.setMaximumSize(new Dimension(400, 30));
        password = new JPasswordField();
        password.setMaximumSize(new Dimension(400, 30));
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(400, 50));
        backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(400, 50));
        backButton.addActionListener(e -> {
            MainFrame.getInstance().changePanel(LoginView.getInstance());
        });

        buttonPanel.setBorder(new EmptyBorder(30, 683, 0, 0));
        buttonPanel.add(managerRadio);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(userRadio);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(new JLabel("Enter your email: "));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(email);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(new JLabel("Enter your username: "));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(username);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(new JLabel("Enter your password: "));
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(password);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);

        this.add(welcomeMassage, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.CENTER);


        registerButton.addActionListener(e ->{
            try {
                String selectedRoleName = "", url = "";
                if (userRadio.isSelected()){
                    url = "http://localhost:8084/users/client/register";
                    selectedRoleName = "ROLE_USER";
                }else if(managerRadio.isSelected()){
                    url = "http://localhost:8084/users/manager/register";
                    selectedRoleName = "ROLE_MANAGER";
                }
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // Postavite tip zahteva i parametre
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");

                // Kreirajte objekat koji želite da pošaljete
                ClientCreateDto clientDto = new ClientCreateDto();
                clientDto.setEmail(this.email.getText());
                clientDto.setPassword(String.valueOf(this.password.getPassword()));
                clientDto.setUsername(this.username.getText());

                Role role = fetchRoleByName(selectedRoleName);

                if (role != null) {
                    clientDto.setRole(role);
                }

                // Konvertujte objekat u JSON string
                Gson gson = new Gson();
                String postData = gson.toJson(clientDto);

                System.out.println(postData);

                // Omogućite slanje podataka
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postData);
                wr.flush();
                wr.close();

                // Pročitajte odgovor ako je potrebno
                int responseCode = con.getResponseCode();
                System.out.println("Response Code: " + responseCode);

            } catch (Exception ee) {
                ee.printStackTrace();
            }
        });

    }

    private Role fetchRoleByName(String roleName) throws IOException {
        String url = "http://localhost:8084/users/role?name=" + roleName;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // Postavite tip zahteva
        con.setRequestMethod("GET");

        // Pročitaj odgovor
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            // Pretvori odgovor u objekat Role
            Gson gson = new Gson();
            return gson.fromJson(response.toString(), Role.class);
        } else {
            // Ukoliko GET zahtev nije uspeo, možeš vratiti null ili obraditi grešku
            return null;
        }
    }

    public static RegisterView getInstance(){
        if (instance == null) {
            instance = new RegisterView();
            instance.initialise();
        }
        return instance;
    }
}
