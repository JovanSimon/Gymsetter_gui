package gui.controller.popup;

import com.google.gson.Gson;
import gui.utils.ClientUpdatedDto;
import gui.utils.TokenRequestDto;
import gui.view.MainFrame;
import gui.view.typesOfViews.WorkView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeInfoBox extends JDialog implements ActionListener {
    private JTextField username, email, name, lastName, dateOfBirth;
    private JPanel panel;
    private JButton button;

    public ChangeInfoBox(){
        init();
        display();
    }

    private void init(){
        // Inicijalizacija elemenata suštinskog sadržaja prozora
        username = new JTextField(20); // Postavljanje širine polja
        email = new JTextField(20); // Postavljanje širine polja
        name = new JTextField(20); // Postavljanje širine polja
        lastName = new JTextField(20); // Postavljanje širine polja
        dateOfBirth = new JTextField(20); // Postavljanje širine polja
        panel = new JPanel();
        button = new JButton("Save changes");
        button.addActionListener(this);

        // Postavljanje layout manager-a za panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Primer postavljanja na BoxLayout

        // Dodavanje elemenata u panel
        panel.add(new JLabel("Unesite username:"));
        panel.add(username);
        panel.add(new JLabel("Unesite email:"));
        panel.add(email);
        panel.add(new JLabel("Unesite name:"));
        panel.add(name);
        panel.add(new JLabel("Unesite lastname:"));
        panel.add(lastName);
        panel.add(new JLabel("Unesite date of birth. format: dd.mm.yyyy. :"));
        panel.add(dateOfBirth);
        panel.add(button);

        // Dodavanje panela u sadržaj prozora
        this.add(panel);
    }

    public void display(){
        this.setTitle("Change info");
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String url = "http://localhost:8084/users/client/update";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Postavite tip zahteva i parametre
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");

            con.setDoOutput(true);

            // Kreirajte objekat koji želite da pošaljete
            ClientUpdatedDto clientUpdatedDto = new ClientUpdatedDto();
            clientUpdatedDto.setUsername(username.getText());
            clientUpdatedDto.setEmail(email.getText());
            clientUpdatedDto.setName(name.getText());
            clientUpdatedDto.setLastName(lastName.getText());
            clientUpdatedDto.setDateOfBirth(dateOfBirth.getText());


            // Konvertujte objekat u JSON string
            Gson gson = new Gson();
            String postData = gson.toJson(clientUpdatedDto);

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
