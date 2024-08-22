package gui.controller.popup;

import com.google.gson.Gson;
import gui.utils.ClientUpdatedDto;
import gui.utils.NotificationTypeDto;
import gui.view.typesOfViews.WorkView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddNotificationTypeBox extends JDialog implements ActionListener {
    private JTextField typeName, subject, body;
    private JPanel panel;
    private JButton button;

    public AddNotificationTypeBox(){
        init();
        display();
    }

    private void init(){
        // Inicijalizacija elemenata suštinskog sadržaja prozora
        typeName = new JTextField(20); // Postavljanje širine polja
        subject = new JTextField(20); // Postavljanje širine polja
        body = new JTextField(20); // Postavljanje širine polja
        panel = new JPanel();
        button = new JButton("Add type");
        button.addActionListener(this);

        // Postavljanje layout manager-a za panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Primer postavljanja na BoxLayout

        // Dodavanje elemenata u panel
        panel.add(new JLabel("Unesite type name:"));
        panel.add(typeName);
        panel.add(new JLabel("Unesite subject:"));
        panel.add(subject);
        panel.add(new JLabel("Unesite body:"));
        panel.add(body);

        panel.add(button);

        // Dodavanje panela u sadržaj prozora
        this.add(panel);
    }

    public void display(){
        this.setTitle("Add type");
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String url = "http://localhost:8084/noti/notification-type/add";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Postavite tip zahteva i parametre
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
            con.setDoOutput(true);

            // Kreirajte objekat koji želite da pošaljete
            NotificationTypeDto notificationTypeDto = new NotificationTypeDto();
            notificationTypeDto.setNotificationType(typeName.getText());
            notificationTypeDto.setBody(body.getText());
            notificationTypeDto.setSubject(subject.getText());


            // Konvertujte objekat u JSON string
            Gson gson = new Gson();
            String postData = gson.toJson(notificationTypeDto);

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
