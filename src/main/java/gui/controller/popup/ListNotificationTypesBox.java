package gui.controller.popup;

import com.google.gson.Gson;
import gui.utils.NotificationTypeDto;
import gui.view.typesOfViews.WorkView;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ListNotificationTypesBox extends JDialog implements ActionListener {
    private JTable table;
    private JButton deleteButton;
    private JButton updateButton;

    public ListNotificationTypesBox() {
        init();
        display();
    }

    private void init() {
        // Inicijalizacija elemenata suštinskog sadržaja prozora
        table = new JTable();
        deleteButton = new JButton("Obriši");
        updateButton = new JButton("Ažuriraj");

        deleteButton.addActionListener(this);
        updateButton.addActionListener(this);

        // Postavljanje layout manager-a za prozor
        this.setLayout(new BorderLayout());

        // Dodavanje tabele u centar prozora
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        // Dodavanje dugmadi u donji deo prozora
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void display() {
        this.setTitle("Lista Tipova Notifikacija");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Popuni tabelu sa podacima iz servera
        fetchDataAndPopulateTable();
    }

    // Implementirajte kod za dohvatanje podataka sa servera i popunjavanje tabele
    private void fetchDataAndPopulateTable() {
        try {
            String url = "http://localhost:8084/noti/notification-type";

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
                NotificationTypeDto[] notificationTypesArray = gson.fromJson(response.toString(), NotificationTypeDto[].class);
                List<NotificationTypeDto> notificationTypeList = Arrays.asList(notificationTypesArray);

                // Popunjavanje tabele
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"ID", "Tip Notifikacije", "Subject", "Body"});

                for (NotificationTypeDto notificationType : notificationTypeList) {
                    model.addRow(new Object[]{notificationType.getId(), notificationType.getNotificationType(),
                            notificationType.getSubject(), notificationType.getBody()});
                }

                table.setModel(model);

            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteNotificationType(Long idToDelete) {
        try {
            String url = "http://localhost:8084/noti/notification-type/" + idToDelete;

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Postavljanje tipa zahteva
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json");

            // Čitanje odgovora
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Notification Type with ID " + idToDelete + " deleted successfully.");
            } else {
                System.out.println("DELETE request failed. Response Code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateNotificationType(Long id, String newNotificationType, String newSubjectType, String newBodyType) {
        try {
            String url = "http://localhost:8084/noti/notification-type/" + id;

            System.out.println(newNotificationType + newSubjectType + newBodyType);


            // Postavljanje tipa zahteva na PUT
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
            con.setDoOutput(true);

            // Postavljanje podataka za ažuriranje u JSON formatu
            NotificationTypeDto notificationTypeDto = new NotificationTypeDto();
            notificationTypeDto.setNotificationType(newNotificationType);
            notificationTypeDto.setBody(newBodyType);
            notificationTypeDto.setSubject(newSubjectType);


            Gson gson = new Gson();
            String postData = gson.toJson(notificationTypeDto);

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postData);
            wr.flush();
            wr.close();

            // Pročitajte odgovor ako je potrebno
            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == deleteButton) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Long idToDelete = (Long) table.getModel().getValueAt(selectedRow, 0);
                deleteNotificationType(idToDelete);
                fetchDataAndPopulateTable();
            } else {
                JOptionPane.showMessageDialog(this, "Molimo vas da prvo izaberete red za brisanje.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == updateButton) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            Long idToUpdate = (Long) table.getModel().getValueAt(selectedRow, 0);

            // Unesite nove vrednosti koristeći dijalog za unos
            String[] newValues = showUpdateDialog();
            if (newValues != null) {
                updateNotificationType(idToUpdate, newValues[0], newValues[1], newValues[2]);
                fetchDataAndPopulateTable();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Molimo vas da prvo izaberete red za ažuriranje.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
        }
    }

}

    private String[] showUpdateDialog() {
        JTextField notificationTypeField = new JTextField();
        JTextField subjectField = new JTextField();
        JTextField bodyField = new JTextField();

        Object[] message = {
                "Novi Notification Type:", notificationTypeField,
                "Novi Subject Type:", subjectField,
                "Novi Body Type:", bodyField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Ažuriranje Notification Type-a", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String newNotificationType = notificationTypeField.getText().trim();
            String newSubjectType = subjectField.getText().trim();
            String newBodyType = bodyField.getText().trim();


            // Vrati sve tri unete vrednosti
            return new String[]{newNotificationType, newSubjectType, newBodyType};
        }
        return null;
    }

}
