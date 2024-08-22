package gui.controller.popup;

import com.google.gson.Gson;
import gui.utils.ClientDto;
import gui.utils.GymDto;
import gui.utils.NotificationTypeDto;
import gui.utils.TrainingCreateDto;
import gui.view.typesOfViews.WorkView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

public class AddTerminsBox extends JDialog implements ActionListener {
    private JComboBox<String> gymComboBox;
    private JTextField nameField, individualOrGroupField;
    private JTextField typeOfTreningField;
    private JTextField priceField;
    private JTextField datumField;
    private JButton saveButton;

    public AddTerminsBox() {
        init();
        display();
    }

    private void init() {
        // Inicijalizacija elemenata suštinskog sadržaja prozora
        gymComboBox = new JComboBox<>();
        individualOrGroupField = new JTextField(20);
        nameField = new JTextField(20);
        typeOfTreningField = new JTextField(20);
        priceField = new JTextField(20);
        datumField = new JTextField(20);
        saveButton = new JButton("Save");

        saveButton.addActionListener(this);

        // Postavljanje layout manager-a za prozor
        this.setLayout(new BorderLayout());

        // Dodavanje combo box-a u vrh prozora
        this.add(gymComboBox, BorderLayout.NORTH);

        // Dodavanje tekstualnih polja i dugmeta u donji deo prozora
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Type of Training:"));
        inputPanel.add(typeOfTreningField);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(priceField);
        inputPanel.add(new JLabel("Datum:"));
        inputPanel.add(datumField);
        inputPanel.add(new JLabel("Individual or Group:"));
        inputPanel.add(individualOrGroupField);

        // Dodavanje dugmeta za čuvanje
        this.add(saveButton);

        this.add(inputPanel, BorderLayout.SOUTH);
    }

    public void display() {
        this.setTitle("Lista Korisnika");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Popuni combo box sa podacima iz servera
        fetchDataAndPopulateComboBox();
    }

    private void fetchDataAndPopulateComboBox() {
        try {
            String url = "http://localhost:8084/trainings/gym";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Postavljanje tipa zahteva
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
            // Čitanje odgovora
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                System.out.println(response);

                // Konvertovanje JSON odgovora u listu NotificationTypeDto objekata
                Gson gson = new Gson();
                GymDto[] gymArray = gson.fromJson(response.toString(), GymDto[].class);
                List<GymDto> gymList = Arrays.asList(gymArray);

                // Popunjavanje combo box-a
                for (GymDto gym : gymList) {
                    gymComboBox.addItem(gym.getId() + " " + gym.getName());
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            // Provera da li je izabrana stavka u combo box-u
            if (gymComboBox.getSelectedItem() != null) {
                // Dobijanje izabrane stavke iz combo box-a
                String selectedGym = (String) gymComboBox.getSelectedItem();

                // Razdvajanje managerId i gymName
                String[] parts = selectedGym.split(" ");
                Long gymId = Long.valueOf(parts[0]);

                TrainingCreateDto trainingCreateDto = new TrainingCreateDto();

                trainingCreateDto.setDate(datumField.getText());
                trainingCreateDto.setIndividualOrGroup(individualOrGroupField.getText());
                trainingCreateDto.setName(nameField.getText());
                trainingCreateDto.setPrice(Integer.parseInt(priceField.getText()));
                trainingCreateDto.setTypeOfTrening(typeOfTreningField.getText());
                trainingCreateDto.setGymId(gymId);

                try {
                    String url = "http://localhost:8084/trainings/training/add";

                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // Postavite tip zahteva i parametre
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
                    con.setDoOutput(true);

                    // Konvertujte objekat u JSON string
                    Gson gson = new Gson();
                    String postData = gson.toJson(trainingCreateDto);

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
            }
        }
    }
}

