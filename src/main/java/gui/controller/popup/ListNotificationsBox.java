package gui.controller.popup;

import com.google.gson.Gson;
import gui.utils.NotificationDto;
import gui.view.typesOfViews.WorkView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ListNotificationsBox extends JDialog implements ActionListener {
    private JTable table;
    private JButton closeButton, fillterButton;
    private JTextField filterText;

    public ListNotificationsBox() {
        init();
        display();
    }

    private void init() {
        // Inicijalizacija elemenata suštinskog sadržaja prozora
        table = new JTable();




        // Dodavanje JTextField-a za unos emaila
        filterText = new JTextField();
        filterText.setColumns(10);

        closeButton = new JButton("Close");
        fillterButton = new JButton("Filter");


        closeButton.addActionListener(e -> {
            this.dispose();
        });

        // Postavljanje layout manager-a za prozor
        this.setLayout(new BorderLayout());

        // Dodavanje komponenti u prozor
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Text:"));
        filterPanel.add(filterText);
        filterPanel.add(fillterButton);
        filterPanel.add(closeButton);

        this.add(filterPanel, BorderLayout.NORTH);
        this.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public void display() {
        this.setTitle("Lista Tipova Notifikacija");
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Popuni tabelu sa podacima iz servera
        fetchDataAndPopulateTable();
    }

    private void fetchDataAndPopulateTable() {
        try {
            String url = "http://localhost:8084/noti/notification";

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Postavljanje tipa zahteva
            con.setRequestMethod("GET");

            // Postavljanje Authorization zaglavlja
            con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
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
                NotificationDto[] notificationArray = gson.fromJson(response.toString(), NotificationDto[].class);
                List<NotificationDto> notificationTypeList = Arrays.asList(notificationArray);

                // Popunjavanje tabele
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"Email", "Tip Notifikacije", "Subject", "Body", "Date"});

                for (NotificationDto notification : notificationTypeList) {
                    model.addRow(new Object[]{notification.getEmailTo(), notification.getNotificationType().getNotificationType(),
                            notification.getNotificationType().getSubject(), notification.getNotificationType().getBody(), notification.getCreatedDate()});
                }

                applyFilters(model);
                this.fillterButton.addActionListener(e -> applyFilters(model));

                table.setModel(model);

            } else {
                try {
                    url = "http://localhost:8084/noti/notification/get_notify";

                    obj = new URL(url);
                    con = (HttpURLConnection) obj.openConnection();

                    // Postavljanje tipa zahteva
                    con.setRequestMethod("GET");

                    // Postavljanje Authorization zaglavlja
                    con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
                    con.setRequestProperty("Content-Type", "application/json");

                    // Čitanje odgovora
                    responseCode = con.getResponseCode();
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
                        NotificationDto[] notificationArray = gson.fromJson(response.toString(), NotificationDto[].class);
                        List<NotificationDto> notificationTypeList = Arrays.asList(notificationArray);

                        // Popunjavanje tabele
                        DefaultTableModel model = new DefaultTableModel();
                        model.setColumnIdentifiers(new String[]{"Email", "Tip Notifikacije", "Subject", "Body", "Date"});

                        for (NotificationDto notification : notificationTypeList) {
                            model.addRow(new Object[]{notification.getEmailTo(), notification.getNotificationType().getNotificationType(),
                                    notification.getNotificationType().getSubject(), notification.getNotificationType().getBody(), notification.getCreatedDate()});
                        }

                        applyFilters(model);
                        this.fillterButton.addActionListener(e -> applyFilters(model));

                        table.setModel(model);

                    } else {
                        System.out.println("GET request failed. Response Code: " + responseCode);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyFilters(DefaultTableModel model) {

        String filterText = this.filterText.getText();


        // Kreiranje filtera za email
        RowFilter<Object, Object> emailFilter =RowFilter.regexFilter("(?i)" + filterText); // 0 je indeks kolone za email

        // Postavljanje filtera na sorter
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        sorter.setRowFilter(emailFilter);
        table.setRowSorter(sorter);
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

}
