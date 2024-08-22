package gui.controller.popup;

import com.google.gson.Gson;
import gui.utils.*;
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

public class ListTerminsBox extends JDialog implements ActionListener {
    private JComboBox<String> gymComboBox;
    private JTable table;
    private JButton osveziBttn, filtrirajButton, zakaziBttn;
    private JTextField typeOfTrainingField;
    private JRadioButton individualniRadioButton, grupniRadioButton, danUNedeljiRadioButton;


    public ListTerminsBox() {
        init();
        display();
    }

    private void init() {
        // Inicijalizacija elemenata suštinskog sadržaja prozora
        table = new JTable();
        gymComboBox = new JComboBox<>();
        osveziBttn = new JButton("Osvezi");
        zakaziBttn = new JButton("Zakazi");
        filtrirajButton = new JButton("Filtriraj");
        typeOfTrainingField = new JTextField(20);

        individualniRadioButton = new JRadioButton("Individualni");
        grupniRadioButton = new JRadioButton("Grupni");
        danUNedeljiRadioButton = new JRadioButton("Dan u nedelji");

        ButtonGroup radioGroup = new ButtonGroup();
        radioGroup.add(individualniRadioButton);
        radioGroup.add(grupniRadioButton);
        radioGroup.add(danUNedeljiRadioButton);

        individualniRadioButton.setSelected(true); // Postavljanje početno selektovanog dugmeta

        osveziBttn.addActionListener(this);
        zakaziBttn.addActionListener(this);
        filtrirajButton.addActionListener(this);

        this.setLayout(new BorderLayout());
        JPanel radioPanel = new JPanel();
        radioPanel.add(new JLabel("Type of training: "));
        radioPanel.add(typeOfTrainingField);
        radioPanel.add(individualniRadioButton);
        radioPanel.add(grupniRadioButton);
        radioPanel.add(danUNedeljiRadioButton);
        radioPanel.add(gymComboBox);
        this.add(radioPanel, BorderLayout.NORTH);

        this.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(osveziBttn);
        buttonPanel.add(filtrirajButton);
        buttonPanel.add(zakaziBttn);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void display() {
        this.setTitle("Lista Korisnika");
        this.setSize(900, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        fetchDataAndPopulateComboBox();
        // Popuni tabelu sa podacima iz servera
        fetchDataAndPopulateTable();
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

    private void fetchDataAndPopulateTable() {
        if (gymComboBox.getSelectedItem() != null) {
            try {
                String selectedGym = (String) gymComboBox.getSelectedItem();

                // Razdvajanje managerId i gymName
                String[] parts = selectedGym.split(" ");
                Long gymId = Long.valueOf(parts[0]);
                String url = "http://localhost:8084/trainings/training/all/" + gymId;

                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                // Postavljanje tipa zahteva
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == osveziBttn)
            fetchDataAndPopulateTable();
        else if (e.getSource() == filtrirajButton) {
            String selectedGym = (String) gymComboBox.getSelectedItem();

            // Razdvajanje managerId i gymName
            String[] parts = selectedGym.split(" ");
            Long gymId = Long.valueOf(parts[0]);
            // Proveri koji je radio button selektovan i izvrši odgovarajuću akciju
            if (!typeOfTrainingField.getText().isEmpty()) {

                try {

                    String url = "http://localhost:8084/trainings/sessions/sort-by-type/"+gymId;

                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // Postavite tip zahteva i parametre
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoOutput(true);

                    SortIt sortIt = new SortIt();
                    sortIt.setSortField(typeOfTrainingField.getText());
                    sortIt.setSortDirection(SortIt.SortDirection.ASC);

                    // Konvertujte objekat u JSON string
                    Gson gson = new Gson();
                    String postData = gson.toJson(sortIt);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(postData);
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
                        TrainingDto[] trainingDtosArray = gson.fromJson(response.toString(), TrainingDto[].class);
                        List<TrainingDto> trainingList = Arrays.asList(trainingDtosArray);

                        // Popunjavanje tabele
                        DefaultTableModel model = new DefaultTableModel();
                        model.setColumnIdentifiers(new String[]{"ID", "Type of training", "Individual Or Group", "Name", "Date", "Price"});

                        for (TrainingDto training : trainingList) {
                            model.addRow(new Object[]{training.getId(), training.getTypeOfTrening(),
                                    training.getIndividualOrGroup(), training.getName(), training.getDate(), training.getPrice()});
                        }

                        setTableModel(model);

                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                System.out.println("Selektovan tip treninga");
            } else if (individualniRadioButton.isSelected()) {
                // Proveri koji je radio button selektovan i izvrši odgovarajuću akciju

                try {
                    String url = "http://localhost:8084/trainings/sessions/sort-by-ig/"+gymId;

                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // Postavite tip zahteva i parametre
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoOutput(true);

                    SortIt sortIt = new SortIt();
                    sortIt.setSortField("INDIVIDUAL");
                    sortIt.setSortDirection(SortIt.SortDirection.ASC);

                    // Konvertujte objekat u JSON string
                    Gson gson = new Gson();
                    String postData = gson.toJson(sortIt);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(postData);
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
                        TrainingDto[] trainingDtosArray = gson.fromJson(response.toString(), TrainingDto[].class);
                        List<TrainingDto> trainingList = Arrays.asList(trainingDtosArray);

                        // Popunjavanje tabele
                        DefaultTableModel model = new DefaultTableModel();
                        model.setColumnIdentifiers(new String[]{"ID", "Type of training", "Individual Or Group", "Name", "Date", "Price"});

                        for (TrainingDto training : trainingList) {
                            model.addRow(new Object[]{training.getId(), training.getTypeOfTrening(),
                                    training.getIndividualOrGroup(), training.getName(), training.getDate(), training.getPrice()});
                        }

                        setTableModel(model);

                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                System.out.println("Selektovan individualni trening");
            } else if (grupniRadioButton.isSelected()) {
                try {
                    String url = "http://localhost:8084/trainings/sessions/sort-by-ig/"+gymId;

                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                    // Postavite tip zahteva i parametre
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setDoOutput(true);

                    SortIt sortIt = new SortIt();
                    sortIt.setSortField("GROUP");
                    sortIt.setSortDirection(SortIt.SortDirection.ASC);

                    // Konvertujte objekat u JSON string
                    Gson gson = new Gson();
                    String postData = gson.toJson(sortIt);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(postData);
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
                        TrainingDto[] trainingDtosArray = gson.fromJson(response.toString(), TrainingDto[].class);
                        List<TrainingDto> trainingList = Arrays.asList(trainingDtosArray);

                        // Popunjavanje tabele
                        DefaultTableModel model = new DefaultTableModel();
                        model.setColumnIdentifiers(new String[]{"ID", "Type of training", "Individual Or Group", "Name", "Date", "Price"});

                        for (TrainingDto training : trainingList) {
                            model.addRow(new Object[]{training.getId(), training.getTypeOfTrening(),
                                    training.getIndividualOrGroup(), training.getName(), training.getDate(), training.getPrice()});
                        }

                        setTableModel(model);

                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
                System.out.println("Selektovan grupni trening");
            } else if (danUNedeljiRadioButton.isSelected()) {
                // Implementiraj logiku za dan u nedelji
                System.out.println("Selektovan dan u nedelji");
            }
        } else if (e.getSource() == zakaziBttn) {
            int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) {
                // Dobavi vrednosti iz selektovanog reda
                Object id = table.getValueAt(selectedRow, 0);
                try {
                    String url = "http://localhost:8084/trainings/training/zakazi/" + id;

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

    private void setTableModel(DefaultTableModel model) {
        this.table.setModel(model);
    }
}
