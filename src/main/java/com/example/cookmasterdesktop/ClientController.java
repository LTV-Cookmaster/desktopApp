package com.example.cookmasterdesktop;

import com.google.gson.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientController {

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

    String[] types = {"Free plan", "Starter plan", "Master plan"};

    String[] formations = {"Professionnal formation", "Online workshop", "Other"};
/*    @FXML
    void fetch(ActionEvent event)
    {
        // Générer des données aléatoires pour le graphique à barres
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
        series2.setName("Types de plan");
        for (int i = 0; i < types.length; i++)
        {
            series2.getData().add(new XYChart.Data<>(types[i], Math.random() * 1000));
        }
        //Générer des données aléatoire pour le graphique en ligne
        LineChart.getData().clear();
        XYChart.Series<String, Double> series3 = new XYChart.Series<>();
        LineChart.setLegendVisible(false);
        series3.setName("CA par Mois");
        for (int i = 0; i < month.length; i++)
        {
            series3.getData().add(new XYChart.Data<>(month[i], Math.random() * 1000));
        }

        //Génération de données aléatoire pour le graphique en camembert
        PieChart.getData().clear();
        PieChart.setTitle("Formations");
        for(int i = 0; i < formations.length; i++)
        {
            PieChart.getData().add(new PieChart.Data(formations[i], Math.random() * 1000));
        }


        AreaChart.getData().add(series);
        BarChart.getData().add(series2);
        LineChart.getData().add(series3);
    }*/

    @FXML
    public void fetch(ActionEvent actionEvent)
    {
        try {
            // Crée l'URL de l'API
            URL url = new URL("https://cockmaster.fr/api/users");

            // Ouvre la connexion HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Vérifie le code de réponse
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lecture de la réponse de l'API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Désérialisation de la réponse JSON
                Gson gson = new GsonBuilder().create();
                JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
                JsonArray dataArray = jsonObject.getAsJsonArray("data");

                // Transformation du tableau JSON en une liste d'objets User
                List<User> userList = new ArrayList<>();
                for (JsonElement element : dataArray) {
                    User user = gson.fromJson(element, User.class);
                    userList.add(user);
                }

                //Trim create_at to get only the month and year
                for (User user : userList)
                {
                    user.setCreatedAt(user.getCreatedAt().substring(0,7));
                }

                //Get the number of users per month
                Map<String, Integer> usersPerMonth = new HashMap<>();
                for (User user : userList)
                {
                    if (usersPerMonth.containsKey(user.getCreatedAt()))
                    {
                        usersPerMonth.put(user.getCreatedAt(), usersPerMonth.get(user.getCreatedAt()) + 1);
                    }
                    else
                    {
                        usersPerMonth.put(user.getCreatedAt(), 1);
                    }
                }

                System.out.println(usersPerMonth);
                
                // Utiliser la map pour générer les données du graphique
                AreaChart.getData().clear();
                XYChart.Series<String, Double> series = new XYChart.Series<>();
                AreaChart.setLegendVisible(false);
                series.setName("Connexion par Mois");
                for (Map.Entry<String, Integer> entry : usersPerMonth.entrySet())
                {
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue().doubleValue()));
                }

                AreaChart.getData().add(series);


            } else {
                System.out.println("Erreur de requête : " + responseCode);
            }

            // Ferme la connexion
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                contentStream.newLineAtOffset(0, -20);

                contentStream.showText("CA / mois");
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

                contentStream.showText("Types de formations");
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
                contentStream.newLineAtOffset(250, 420); // Passer à la deuxième colonne

                contentStream.showText("Types de clients");
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
                contentStream.showText("Nombre de connexion par mois");
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