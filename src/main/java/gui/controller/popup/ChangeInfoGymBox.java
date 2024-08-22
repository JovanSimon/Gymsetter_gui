package gui.controller.popup;

import com.google.gson.Gson;
import gui.utils.GymCreateDto;
import gui.utils.GymDto;
import gui.view.typesOfViews.WorkView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChangeInfoGymBox extends JDialog implements ActionListener {
    private JTextField name, description, numOfCoach;
    private JPanel panel;
    private JButton saveBttn;
    public ChangeInfoGymBox(){
        init();
        display();
    }

    private void init(){
        // Inicijalizacija elemenata suštinskog sadržaja prozora
        name = new JTextField(20); // Postavljanje širine polja
        description = new JTextField(20); // Postavljanje širine polja
        numOfCoach = new JTextField(20);
        panel = new JPanel();
        saveBttn = new JButton("Save changes");
        saveBttn.addActionListener(this);

        // Postavljanje layout manager-a za panel
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Primer postavljanja na BoxLayout

        // Dodavanje elemenata u panel
        panel.add(new JLabel("Unesite name:"));
        panel.add(name);
        panel.add(new JLabel("Unesite description:"));
        panel.add(description);
        panel.add(new JLabel("Unesite broj trenera:"));
        panel.add(numOfCoach);

        panel.add(saveBttn);

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
            String url = "http://localhost:8084/trainings/gym/update";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");

            con.setDoOutput(true);

            // Kreirajte objekat koji želite da pošaljete
            GymDto gymDto = new GymDto();
            gymDto.setName(name.getText());
            gymDto.setDescription(description.getText());
            gymDto.setNumOfCoach(Integer.parseInt(numOfCoach.getText()));

            // Konvertujte objekat u JSON string
            Gson gson = new Gson();
            String postData = gson.toJson(gymDto);

            System.out.println(postData);
            System.out.println("Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));

            // Omogućite slanje podataka
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
