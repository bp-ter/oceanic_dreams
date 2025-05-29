/*
* File: mainController.java
* Author: Berta Péter
* Copyright: 2025, Berta Péter
* Group: Szoft II-2-N
* Date: 2025-05-29
* Github: https://github.com/bp-ter/
* Licenc: MIT
*/

package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class mainController {

    @FXML
    private DatePicker datumDatePicker;

    @FXML
    private ComboBox<String> kategoriaCombo;

    @FXML
    private TextField megjegyzesField;

    @FXML
    private TextField osszegField;

    @FXML
    private TextField yachtnameField;

    @FXML
    private TableView<YachtKoltseg> koltsegTable;

    @FXML
    private TableColumn<YachtKoltseg, Integer> idColumn;

    @FXML
    private TableColumn<YachtKoltseg, String> yachtnameColumn;

    @FXML
    private TableColumn<YachtKoltseg, LocalDate> datumColumn;

    @FXML
    private TableColumn<YachtKoltseg, String> kategoriaColumn;

    @FXML
    private TableColumn<YachtKoltseg, Double> osszegColumn;

    @FXML
    private TableColumn<YachtKoltseg, String> megjegyzesColumn;

    private ObservableList<YachtKoltseg> koltsegLista = FXCollections.observableArrayList();

    private final String FILENAME = "karbantartas/data/yacht_koltsegek_2024.csv";

    @FXML
    public void initialize() {

        kategoriaCombo.setItems(FXCollections.observableArrayList(
                "Maintenance", "Repairs", "Insurance", "Docking Fees", "Other"
        ));

        idColumn.setCellValueFactory(new PropertyValueFactory<YachtKoltseg, Integer>("id"));
        yachtnameColumn.setCellValueFactory(new PropertyValueFactory<YachtKoltseg, String>("yachtname"));
        datumColumn.setCellValueFactory(new PropertyValueFactory<YachtKoltseg, LocalDate>("datum"));
        kategoriaColumn.setCellValueFactory(new PropertyValueFactory<YachtKoltseg, String>("kategoria"));
        osszegColumn.setCellValueFactory(new PropertyValueFactory<YachtKoltseg, Double>("osszeg"));
        megjegyzesColumn.setCellValueFactory(new PropertyValueFactory<YachtKoltseg, String>("megjegyzes"));

        beolvasKoltsegek();

        koltsegTable.setItems(koltsegLista);
    }

    private void beolvasKoltsegek() {
        koltsegLista.clear();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(FILENAME))) {
            String line = br.readLine(); // fejléc átugrása
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 6) {
                    int id = Integer.parseInt(parts[0]);
                    String yachtname = parts[1];
                    LocalDate datum = LocalDate.parse(parts[2]);
                    String kategoria = parts[3];
                    double osszeg = Double.parseDouble(parts[4].replace(",", "."));
                    String megjegyzes = parts[5];
                    YachtKoltseg koltseg = new YachtKoltseg(id, yachtname, datum, kategoria, osszeg, megjegyzes);
                    koltsegLista.add(koltseg);
                }
            }
        } catch (IOException e) {
            System.err.println("Hiba a fájl beolvasásakor: " + e.getMessage());
        }
    }

    @FXML
    void onClickAddButton(ActionEvent event) {
        if (yachtnameField.getText().isEmpty() || datumDatePicker.getValue() == null || kategoriaCombo.getValue() == null || osszegField.getText().isEmpty()) {
            showAlert("Hiba", "Kérjük, töltsön ki minden kötelező mezőt!");
            return;
        }

        String yachtname = yachtnameField.getText();
        LocalDate datum = datumDatePicker.getValue();
        String kategoria = kategoriaCombo.getValue();

        double osszeg;
        try {
            osszeg = Double.parseDouble(osszegField.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            showAlert("Hiba", "Az összeg nem megfelelő formátumú!");
            return;
        }

        String megjegyzes = megjegyzesField.getText();

        int ujId = koltsegLista.stream().mapToInt(YachtKoltseg::getId).max().orElse(0) + 1;

        YachtKoltseg ujKoltseg = new YachtKoltseg(ujId, yachtname, datum, kategoria, osszeg, megjegyzes);

        koltsegLista.add(ujKoltseg);

        ujraMent();

        yachtnameField.clear();
        datumDatePicker.setValue(null);
        kategoriaCombo.setValue(null);
        osszegField.clear();
        megjegyzesField.clear();
    }

    private void ujraMent() {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(FILENAME))) {
            bw.write("id;yachtname;datum;kategoria;osszeg;megjegyzes");
            bw.newLine();

            for (YachtKoltseg k : koltsegLista) {
                String sor = String.format("%d;%s;%s;%s;%.2f;%s",
                        k.getId(),
                        k.getYachtname(),
                        k.getDatum(),
                        k.getKategoria(),
                        k.getOsszeg(),
                        k.getMegjegyzes() == null ? "" : k.getMegjegyzes());
                bw.write(sor);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Hiba a fájl mentésekor: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
