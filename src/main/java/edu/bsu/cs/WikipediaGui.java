package edu.bsu.cs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.InputStream;

public class WikipediaGui extends Application {

    private TextField articleField;
    private Button searchButton;
    private TextArea resultsArea;

    @Override
    public void start(Stage stage) {

        Label titleLabel =
                new Label("Wikipedia Revision Reporter");

        articleField = new TextField();
        articleField.setPromptText(
                "Enter a Wikipedia article");

        searchButton = new Button("Search");

        resultsArea = new TextArea();
        resultsArea.setEditable(false);
        resultsArea.setWrapText(true);
        resultsArea.setPrefHeight(400);

        searchButton.setOnAction(event -> search());
        articleField.setOnAction(event -> search());

        VBox layout = new VBox(
                10,
                titleLabel,
                articleField,
                searchButton,
                resultsArea);

        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 650, 500);

        stage.setTitle("Wikipedia Revision Reporter");
        stage.setScene(scene);
        stage.show();
    }

    private void search() {

        String articleTitle =
                articleField.getText().trim();

        if (articleTitle.isEmpty()) {
            showError(
                    "No Page Requested",
                    "Please enter a Wikipedia article.");
            return;
        }

        setControlsDisabled(true);
        resultsArea.clear();

        Thread requestThread = new Thread(() -> {
            try {
                WikipediaClient client =
                        new WikipediaClient();

                InputStream response =
                        client.getArticleRevisions(articleTitle);

                WikipediaResult result =
                        new RevisionParser().parseResult(response);

                Platform.runLater(() ->
                        displayResult(result));

            } catch (Exception exception) {

                Platform.runLater(() ->
                        showError(
                                "Network Error",
                                "Unable to retrieve Wikipedia data."));

            } finally {

                Platform.runLater(() ->
                        setControlsDisabled(false));
            }
        });

        requestThread.setDaemon(true);
        requestThread.start();
    }

    private void displayResult(WikipediaResult result) {

        RevisionFormatter formatter = new RevisionFormatter();

        resultsArea.setText(formatter.format(result));
    }

    private void setControlsDisabled(boolean disabled) {
        articleField.setDisable(disabled);
        searchButton.setDisable(disabled);
    }

    private void showError(
            String title,
            String message) {

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}