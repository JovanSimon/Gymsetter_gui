package gui.controller.popup;

import com.google.gson.Gson;
import gui.utils.TrainingDto;
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
import java.util.Arrays;
import java.util.List;

public class ChangeTerminsManagerBox extends JDialog implements ActionListener {
    private JTable table;
    private JButton azurirajBttn;
    private JButton otkaziMng;

    public ChangeTerminsManagerBox() {
        init();
        display();
    }

    private void init() {
        // Inicijalizacija elemenata suštinskog sadržaja prozora
        table = new JTable();
        azurirajBttn = new JButton("Azuriraj");
        otkaziMng = new JButton("Otkazi");
        azurirajBttn.addActionListener(this);
        otkaziMng.addActionListener(this);
        this.setLayout(new BorderLayout());


        this.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(azurirajBttn);
        buttonPanel.add(otkaziMng);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void display() {
        this.setTitle("Lista Treninga");
        this.setSize(900, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Popuni tabelu sa podacima iz servera
        fetchDataAndPopulateTable();
    }

    private void fetchDataAndPopulateTable() {
        try {

            String url = "http://localhost:8085/trainings/gym/manager-find";

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
                TrainingDto[] trainingDtosArray = gson.fromJson(response.toString(), TrainingDto[].class);
                List<TrainingDto> trainingList = Arrays.asList(trainingDtosArray);
                // Popunjavanje tabele
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"ID", "Type of training", "Individual Or Group", "Name", "Date", "Price"});

                for (TrainingDto training : trainingList) {
                    model.addRow(new Object[]{training.getId(), training.getTypeOfTrening(),
                            training.getIndividualOrGroup(), training.getName(), training.getDate(), training.getPrice()});
                }

                table.setModel(model);
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Provera da li je dugme azurirajBttn kliknuto
        if (e.getSource() == azurirajBttn) {
            // Dobijanje selektovanog reda iz tabele
            int selectedRow = table.getSelectedRow();

            // Provera da li je red selektovan
            if (selectedRow >= 0) {
                // Dobijanje vrednosti iz selektovanog reda
                Object id = table.getValueAt(selectedRow, 0);

                // Otvaranje novog prozora za unos podataka
                JFrame inputFrame = new JFrame("Unesite nove vrednosti");
                inputFrame.setSize(300, 200);
                inputFrame.setLayout(new GridLayout(5, 2));

                // Dodavanje JLabel i JTextField za svako polje
                JLabel nameLabel = new JLabel("Name:");
                JTextField nameField = new JTextField();
                JLabel priceLabel = new JLabel("Price:");
                JTextField priceField = new JTextField();
                JLabel typeLabel = new JLabel("Type of training:");
                JTextField typeField = new JTextField();
                JLabel individualGroupLabel = new JLabel("Individual or Group:");
                JTextField individualGroupField = new JTextField();

                // Dodavanje komponenti u prozor
                inputFrame.add(nameLabel);
                inputFrame.add(nameField);
                inputFrame.add(priceLabel);
                inputFrame.add(priceField);
                inputFrame.add(typeLabel);
                inputFrame.add(typeField);
                inputFrame.add(individualGroupLabel);
                inputFrame.add(individualGroupField);

                // Dugme za potvrdu unosa
                JButton confirmButton = new JButton("Potvrdi");
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Čuvanje unetih vrednosti
                        String newName = nameField.getText();
                        String newPrice = priceField.getText();
                        String newType = typeField.getText();
                        String newIndividualGroup = individualGroupField.getText();

                        // Zatvaranje prozora za unos podataka
                        inputFrame.dispose();

                        try {
                            String url = "http://localhost:8084/trainings/training/update";

                            URL obj = new URL(url);
                            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                            // Postavite tip zahteva i parametre
                            con.setRequestMethod("POST");
                            con.setRequestProperty("Content-Type", "application/json");
                            con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
                            con.setDoOutput(true);

                            TrainingDto trainingDto = new TrainingDto();
                            trainingDto.setId((Long) id);
                            trainingDto.setName(newName);
                            trainingDto.setPrice(Integer.valueOf(newPrice));
                            trainingDto.setTypeOfTrening(newType);
                            trainingDto.setIndividualOrGroup(newIndividualGroup);

                            // Konvertujte objekat u JSON string
                            Gson gson = new Gson();
                            String postData = gson.toJson(trainingDto);

                            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                            wr.writeBytes(postData);
                            wr.flush();
                            wr.close();

                            // Pročitajte odgovor ako je potrebno
                            int responseCode = con.getResponseCode();
                            System.out.println("Response Code: " + responseCode);

                            fetchDataAndPopulateTable();

                        } catch (Exception ee) {
                            ee.printStackTrace();
                        }
                    }
                });

                inputFrame.add(confirmButton);

                // Prikazivanje novog prozora
                inputFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Molimo vas da selektujete trening iz tabele.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (e.getSource() == otkaziMng) {
            int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) {
                // Dobavi vrednosti iz selektovanog reda
                Object id = table.getValueAt(selectedRow, 0);
                try {
                    String url = "http://localhost:8084/trainings/training/otkazi-manager/" + id;
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // Postavite tip zahteva i parametre
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
                    con.setDoOutput(true);


                    // Konvertujte objekat u JSON string
                    Gson gson = new Gson();

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.flush();
                    wr.close();

                    // Pročitajte odgovor ako je potrebno
                    int responseCode = con.getResponseCode();
                    System.out.println("Response Code: " + responseCode);

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
                        gson = new Gson();
//                        TrainingDto[] trainingDtosArray = gson.fromJson(response.toString(), TrainingDto[].class);
//                        List<TrainingDto> trainingList = Arrays.asList(trainingDtosArray);
//
//                        JOptionPane.showMessageDialog(null,
//                                "Uspesno ste zakazi termin " + trainingList.get(0).getTypeOfTrening()
//                                        + " za datum: " + trainingList.get(0).getDate() , "Info"
//                                , JOptionPane.INFORMATION_MESSAGE);
//
                        fetchDataAndPopulateTable();
                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }


            } else {
                // Nije izabran red, obavesti korisnika ili preduzmi odgovarajuće akcije
                System.out.println("Nije izabran red.");
            }

        }

    }
}

