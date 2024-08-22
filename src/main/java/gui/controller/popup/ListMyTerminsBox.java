package gui.controller.popup;

import com.google.gson.Gson;
import gui.utils.GymDto;
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

public class ListMyTerminsBox extends JDialog implements ActionListener {
    private JTable table;

    private JButton otkaziBttn;
    private JButton otkaziMng;


    public ListMyTerminsBox() {
        init();
        display();
    }

    private void init() {
        // Inicijalizacija elemenata suštinskog sadržaja prozora
        table = new JTable();
        otkaziBttn = new JButton("Otkazi");
        otkaziMng = new JButton("Menager otkazi");
        otkaziMng.addActionListener(this);
        otkaziBttn.addActionListener(this);

        this.setLayout(new BorderLayout());


        this.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(otkaziBttn);
        buttonPanel.add(otkaziMng);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void display() {
        this.setTitle("Lista Korisnika");
        this.setSize(900, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Popuni tabelu sa podacima iz servera
        fetchDataAndPopulateTable();
    }

    private void fetchDataAndPopulateTable() {
        try {
            String url = "http://localhost:8084/trainings/training/find-for-user";

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
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) {
                // Dobavi vrednosti iz selektovanog reda
                Object id = table.getValueAt(selectedRow, 0);
                try {
                    String url = "http://localhost:8084/trainings/training/otkazi/" + id;
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
