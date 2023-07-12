package com.example.cookmasterdesktop;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import java.io.File;
import java.io.IOException;



public class WorkshopController {

    @FXML
    private AreaChart<String, Double> AreaChart;

    @FXML
    private BarChart<String, Double> BarChart;

    @FXML
    private LineChart<String, Double> LineChart;

    @FXML
    private PieChart PieChart;

    @FXML
    private Button fetch;

    @FXML
    private Button exportButton;

    String[] month = {"Jan","Fev", "Mar", "Avr", "Mai", "Juin", "Juil", "Aout", "Sep", "Oct", "Nov", "Dec"};
    String[] days = {"Lundi","Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
    @FXML
    void fetch(ActionEvent event)
    {
        // Générer des données aléatoires pour le graphique en aires
        AreaChart.getData().clear();
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        AreaChart.setLegendVisible(false);
        series.setName("Connexion par Mois");
        for (int i = 0; i < month.length; i++)
        {
            series.getData().add(new XYChart.Data<>(month[i], Math.random() * 1000));
        }

        //Générer des données aléatoire pour le graphique en barres
        BarChart.getData().clear();
        XYChart.Series<String, Double> series2 = new XYChart.Series<>();
        BarChart.setLegendVisible(false);
        series2.setName("Connexion par Jour");
        for (int i = 0; i < days.length; i++)
        {
            series2.getData().add(new XYChart.Data<>(days[i], Math.random() * 1000));
        }
        //Générer des données aléatoire pour le graphique en ligne
        LineChart.getData().clear();
        XYChart.Series<String, Double> series3 = new XYChart.Series<>();
        LineChart.setLegendVisible(false);
        series3.setName("Connexion par mois");
        for (int i = 0; i < month.length; i++)
        {
            series3.getData().add(new XYChart.Data<>(month[i], Math.random() * 1000));
        }

        //Génération de données aléatoire pour le graphique en camembert
        PieChart.getData().clear();
        PieChart.setTitle("Connexion par mois");
        for(int i = 0; i < month.length; i++)
        {
            PieChart.getData().add(new PieChart.Data(month[i], Math.random() * 1000));
        }


        AreaChart.getData().add(series);
        BarChart.getData().add(series2);
        LineChart.getData().add(series3);
    }

    private void showAlert(String title, String content)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void exportToPdf(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter en PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(exportButton.getScene().getWindow());

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);

                PDPageContentStream contentStream = new PDPageContentStream(document, page);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);

                // Ajout du titre du graphique
                contentStream.showText("Statistiques de Cookmaster");
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Graphique en ligne");
                contentStream.newLineAtOffset(0, -20);
                // Ajout des données du graphique en ligne
                contentStream.newLineAtOffset(0, -20);
                ObservableList<XYChart.Series<String, Double>> lineChartData = LineChart.getData();
                StringBuilder dataText3 = new StringBuilder();
                for (XYChart.Series<String, Double> data : lineChartData) {
                    for (XYChart.Data<String, Double> data2 : data.getData()) {
                        dataText3.append(data2.getXValue()).append(": ").append(data2.getYValue()).append(System.lineSeparator());
                        contentStream.showText(data2.getXValue() + ": " + String.format("%.2f", data2.getYValue()));
                        contentStream.newLineAtOffset(0, -20);
                    }
                }
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("Graphique en camembert");
                contentStream.newLineAtOffset(0, -20);

                // Ajout des données du graphique en camembert
                contentStream.newLineAtOffset(0, -20);
                ObservableList<PieChart.Data> pieChartData = PieChart.getData();
                StringBuilder dataText = new StringBuilder();
                for (PieChart.Data data : pieChartData) {
                    double value = data.getPieValue();
                    dataText.append(data.getName()).append(": ").append(data.getPieValue()).append(System.lineSeparator());
                    contentStream.showText(data.getName() + ": " + String.format("%.2f", value));
                    contentStream.newLineAtOffset(0, -20);
                }
                contentStream.newLineAtOffset(0, -20);
                contentStream.newLineAtOffset(250, 600); // Passer à la deuxième colonne

                contentStream.showText("Graphique en barres");
                contentStream.newLineAtOffset(0, -20);

                //Ajout des données du graphique en barres
                contentStream.newLineAtOffset(0, -20);
                ObservableList<XYChart.Series<String, Double>> barChartData = BarChart.getData();
                StringBuilder dataText2 = new StringBuilder();
                for (XYChart.Series<String, Double> data : barChartData) {
                    for (XYChart.Data<String, Double> data2 : data.getData()) {
                        dataText2.append(data2.getXValue()).append(": ").append(data2.getYValue()).append(System.lineSeparator());
                        contentStream.showText(data2.getXValue() + ": " + String.format("%.2f", data2.getYValue()));
                        contentStream.newLineAtOffset(0, -20);
                    }
                }
                contentStream.newLineAtOffset(0, -20);

                //Ajout des données du graphique en aires
                contentStream.showText("Graphique en aires");
                contentStream.newLineAtOffset(0, -20);

                ObservableList<XYChart.Series<String, Double>> areaChartData = AreaChart.getData();
                StringBuilder dataText4 = new StringBuilder();
                for (XYChart.Series<String, Double> data : areaChartData) {
                    for (XYChart.Data<String, Double> data2 : data.getData()) {
                        dataText4.append(data2.getXValue()).append(": ").append(data2.getYValue()).append(System.lineSeparator());
                        contentStream.showText(data2.getXValue() + ": " + String.format("%.2f", data2.getYValue()));
                        contentStream.newLineAtOffset(0, -20);
                    }
                }
                contentStream.newLineAtOffset(0, -20);

                contentStream.endText();
                contentStream.close();

                document.save(file);
                document.close();
                showAlert("Export réussi", "Les données ont été exportées en PDF avec succès.");

            } catch (IOException e) {
                showAlert("Erreur", "Une erreur est survenue lors de l'export en PDF : " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

}