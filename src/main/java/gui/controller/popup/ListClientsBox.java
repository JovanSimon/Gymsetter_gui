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

public class ListClientsBox extends JDialog implements ActionListener {
    private JTable table;
    private JButton forbidButton;
    private JButton allowButton;

    public ListClientsBox() {
        init();
        display();
    }

    private void init() {
        // Inicijalizacija elemenata suštinskog sadržaja prozora
        table = new JTable();
        forbidButton = new JButton("Zabrani");
        allowButton = new JButton("Dozvoli");

        forbidButton.addActionListener(this);
        allowButton.addActionListener(this);

        // Postavljanje layout manager-a za prozor
        this.setLayout(new BorderLayout());

        // Dodavanje tabele u centar prozora
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        // Dodavanje dugmadi u donji deo prozora
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(forbidButton);
        buttonPanel.add(allowButton);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void display() {
        this.setTitle("Lista Korisnika");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Popuni tabelu sa podacima iz servera
        fetchDataAndPopulateTable();
    }

    // Implementirajte kod za dohvatanje podataka sa servera i popunjavanje tabele
    private void fetchDataAndPopulateTable() {
        try {
            String url = "http://localhost:8084/users/client";

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
                ClientDto[] clientArray = gson.fromJson(response.toString(), ClientDto[].class);
                List<ClientDto> clientList = Arrays.asList(clientArray);

                // Popunjavanje tabele
                DefaultTableModel model = new DefaultTableModel();
                model.setColumnIdentifiers(new String[]{"ID", "Email", "Username", "Zabrana", "Role"});

                for (ClientDto client : clientList) {
                    model.addRow(new Object[]{client.getId(), client.getEmail(),
                            client.getUsername(), client.getIsZabrana() , client.getRole().getName()});
                }

                fetchDataAndPopulateTable1(model);


            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fetchDataAndPopulateTable1(DefaultTableModel model) {
        try {
            String url = "http://localhost:8084/users/manager";

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
                ManagerDto[] managerArray = gson.fromJson(response.toString(), ManagerDto[].class);
                List<ManagerDto> managerList = Arrays.asList(managerArray);


                for (ManagerDto manager : managerList) {
                    model.addRow(new Object[]{manager.getId(), manager.getEmail(),
                            manager.getUsername(), manager.getIsZabrana() , manager.getRole().getName()});
                }

                table.setModel(model);

            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void zabrani(Long id, Long dozvola, String role) {
        ClientZabranaDto clientZabranaDto = new ClientZabranaDto();
        clientZabranaDto.setIsZabrana(dozvola);
        clientZabranaDto.setUserID(id);
        try {
            String url = "";
            if(role.equals("ROLE_USER"))
                url = "http://localhost:8084/users/client/update-zabrana";
            else if(role.equals("ROLE_MANAGER"))
                url = "http://localhost:8084/users/manager/update-zabrana";
            else
                return ;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Postavite tip zahteva i parametre
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer " + WorkView.getInstance().getToken().substring(10, WorkView.getInstance().getToken().length() - 2));
            con.setDoOutput(true);
            // Konvertujte objekat u JSON string
            Gson gson = new Gson();
            String postData = gson.toJson(clientZabranaDto);

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


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == forbidButton) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Long idZabrana = (Long) table.getModel().getValueAt(selectedRow, 0);
                String uloga = (String) table.getModel().getValueAt(selectedRow,4);
                JOptionPane.showMessageDialog(this, "Korisniku "+idZabrana+" je zabranjen pristup. "  + uloga, "Poruka", JOptionPane.INFORMATION_MESSAGE);
                zabrani(idZabrana, 0L,uloga);
                fetchDataAndPopulateTable();
            } else {
                JOptionPane.showMessageDialog(this, "Molimo vas da prvo izaberete korisnika.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        }

        if (e.getSource() == allowButton) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Long idZabrana = (Long) table.getModel().getValueAt(selectedRow, 0);
                String uloga = (String) table.getModel().getValueAt(selectedRow,4);
                JOptionPane.showMessageDialog(this, "Korisniku "+idZabrana+" je odobren pristup.", "Poruka", JOptionPane.INFORMATION_MESSAGE);
                zabrani(idZabrana, 1L,uloga);
                fetchDataAndPopulateTable();
            } else {
                JOptionPane.showMessageDialog(this, "Molimo vas da prvo izaberete korisnika.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
            }
        }

    }


}
