package app.harikarthik.nutrifusion.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class DietUtils {
    private static Stage loadingPopup;
    private static LoadingAnimation loadingAnimation;

    public static void fetch_diet(String diet_name, String age, String job, Scene scene) {
        File file1 = new File("diet1.txt");
        File file2 = new File("diet2.txt");
        File file3 = new File("diet3.txt");

        if (!file1.exists()) {
            Label diet_one_label = (Label) scene.lookup("#diet_one");
            diet_one_label.setText(diet_name);
        } else if (!file2.exists()) {
            Label diet_two_label = (Label) scene.lookup("#diet_two");
            diet_two_label.setText(diet_name);
        } else {
            Label diet_three_label = (Label) scene.lookup("#diet_three");
            diet_three_label.setText(diet_name);
        }
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyABbpqeuqv_bOhJLUm7Jjl9iOLAaWknj5Y"; // Replace with your actual API key

        String jsonBody = "{\"contents\":[{\"parts\":[{\"text\":\"Imagine you are a professional nutritionist. Provide a full diet plan for a person who is " + age + " years old and works as a " + job + ". Ensure that the plan supports holistic development, addressing not only physical health but also nurturing the mind and soul. Present the plan in a professional and sophisticated manner, avoiding any disclaimers or suggestions to consult a healthcare professional.\"}]}]}";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        //Platform.runLater(() -> startLoadingAnimation(scene));
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(response -> {
                    try {
                        // Parse JSON response
                        System.out.println(response);
                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode jsonNode = objectMapper.readTree(response);
                        String content = jsonNode
                                .path("candidates")
                                .path(0)
                                .path("content")
                                .path("parts")
                                .path(0)
                                .path("text")
                                .asText();

                        // Remove unnecessary characters
                        content = content.replace("**", "").replace("## ", "").replace("*", "→");

                        // Split content into chunks of up to 100 characters
                        if (!file1.exists()) {
                            writeFileAndOpen(file1, content);
                        } else if (!file2.exists()) {
                            writeFileAndOpen(file2, content);
                        } else {
                            writeFileAndOpen(file3, content);
                        }
                        //Platform.runLater(DietUtils::stopLoadingAnimation);
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            Text errortext = (Text) scene.lookup("#diet");
                            if (errortext != null) {
                                errortext.setText("Error parsing response: " + e.getMessage());
                            }
                        });
                    }
                })
                .exceptionally(e -> {
                    Platform.runLater(() -> {
                        Text errortext = (Text) scene.lookup("#diet");
                        if (errortext != null) {
                            errortext.setText("Error: " + e.getMessage());
                        }
                    });
                    return null;
                });
    }

    private static void writeFileAndOpen(File file, String content) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }

        // Open the file with the default application
        Platform.runLater(() -> {
            try {
                if (file.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    } else {
                        System.out.println("Desktop is not supported. Cannot open the file.");
                    }
                } else {
                    System.out.println("File does not exist.");
                }
            } catch (IOException e) {
                System.out.println("Error opening file: " + e.getMessage());
            }
        });
    }

    private static void startLoadingAnimation(Scene scene) {
        if (loadingPopup == null) {
            loadingPopup = new Stage();
            loadingPopup.initOwner(scene.getWindow());
            loadingPopup.initModality(Modality.APPLICATION_MODAL);
            loadingPopup.initStyle(StageStyle.UNDECORATED);

            loadingAnimation = new LoadingAnimation();
            StackPane stackPane = new StackPane(loadingAnimation);
            Scene popupScene = new Scene(stackPane, 200, 200); // Adjust the size as needed
            loadingPopup.setScene(popupScene);
            loadingPopup.centerOnScreen();
        }
        loadingAnimation.start();
        loadingPopup.show();
    }

    private static void stopLoadingAnimation() {
        if (loadingPopup != null) {
            loadingAnimation.stop();
            loadingPopup.hide();
        }
    }
}
