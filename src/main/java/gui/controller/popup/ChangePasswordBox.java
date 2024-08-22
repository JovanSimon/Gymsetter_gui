package gui.controller.popup;

import com.google.gson.Gson;
import gui.utils.ClientUpdatePasswordDto;
import gui.utils.ClientUpdatedDto;
import gui.view.typesOfViews.WorkView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChangePasswordBox extends JDialog implements ActionListener {
    private JPasswordField password, passwordConf;
    private JPanel panel;
    private JButton button;

    public ChangePasswordBox(){
        init();
        display();
    }

    private void init(){
        passwordConf = new JPasswordField(20);
        password = new JPasswordField(20);
        panel = new JPanel();
        button = new JButton("Confirm");
        button.addActionListener(this);

        // Postavljanje layout manager-a za panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Primer postavljanja na BoxLayout

        // Dodavanje elemenata u panel
        panel.add(new JLabel("Unesite password:"));
        panel.add(password);
        panel.add(new JLabel("Potvrdite password:"));
        panel.add(passwordConf);
        panel.add(button);



        // Dodavanje panela u sadržaj prozora
        this.add(panel);
    }

    public void display(){
        this.setTitle("Change password");
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ClientUpdatePasswordDto clientUpdatePasswordDto = new ClientUpdatePasswordDto();

        if (String.valueOf(passwordConf.getPassword()).equals(String.valueOf(password.getPassword()))) {
            clientUpdatePasswordDto.setPassword(String.valueOf(passwordConf.getPassword()));
            sand(clientUpdatePasswordDto);
        } else {
            JOptionPane.showMessageDialog(null, "Niste uneli istu lozinku, pokusajte ponovo!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sand(ClientUpdatePasswordDto clientUpdatePasswordDto) {
        try {
            String url = "http://localhost:8084/users/client/change_password";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Postavite tip zahteva i parametre
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");

            con.setDoOutput(true);
            // Konvertujte objekat u JSON string
            Gson gson = new Gson();
            String postData = gson.toJson(clientUpdatePasswordDto);

            System.out.println(postData);
            System.out.println("Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));

            // Omogućite slanje podataka
            con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
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
        this.dispose();
    }
}
