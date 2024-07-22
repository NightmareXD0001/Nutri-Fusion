package app.harikarthik.nutrifusion.dashboard;

import app.harikarthik.nutrifusion.MainApplication;
import app.harikarthik.nutrifusion.login.LoginController;
import com.sun.tools.javac.Main;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import static app.harikarthik.nutrifusion.utils.DietUtils.fetch_diet;


public class MainController {

    public Label diet_one;
    public Label diet_two;
    public Label diet_three;
    @FXML
    private Label account_name;

    @FXML
    private Label welcome_message;

    @FXML
    private ImageView user_logo;

    public void setUsername(){
        String username = LoginController.loadQuickLoginUsername();
        account_name.setText(username);
        welcome_message.setText("Welcome! " + username);
    }

    public void setGenderLogo(){
        String gender = LoginController.loadQuickLoginGender();
        if (gender.equals("male")){
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("male.png")));
            user_logo.setImage(image);
        }if (gender.equals("female")){
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("female.png")));
            user_logo.setImage(image);
        }if (gender.equals("other")){
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("male.png")));
            user_logo.setImage(image);
        }
    }

    public void switchpage2(){
        MainApplication.switchDashboard2();
    }
    public void switchpage1(){
        MainApplication.switchDashboard();
    }

    private void openWorkDialog(String diet_name, String age, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter your job");
        dialog.setHeaderText("Enter a brief description about your job and your daily routines for the diet plan:");
        dialog.setContentText("Prompt:");
        try {
            Field dialogPaneField = dialog.getClass().getDeclaredField("dialogPane");
            dialogPaneField.setAccessible(true);
            Object dialogPane = dialogPaneField.get(dialog);

            Field sceneField = dialogPane.getClass().getDeclaredField("scene");
            sceneField.setAccessible(true);
            Scene dialogScene = (Scene) sceneField.get(dialogPane);

            Platform.runLater(() -> {
                Stage dialogStage = (Stage) dialogScene.getWindow();
                dialogStage.getIcons().add(new Image("src/main/resources/app/harikarthik/nutrifusion/logo.png"));
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // Get the user input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(work -> fetch_diet(diet_name, age, work, stage.getScene()));
    }

    private void openDietNameDialog(Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Name");
        dialog.setHeaderText("Enter a Name for the diet plan:");
        dialog.setContentText("Name:");
        try {
            Field dialogPaneField = dialog.getClass().getDeclaredField("dialogPane");
            dialogPaneField.setAccessible(true);
            Object dialogPane = dialogPaneField.get(dialog);

            Field sceneField = dialogPane.getClass().getDeclaredField("scene");
            sceneField.setAccessible(true);
            Scene dialogScene = (Scene) sceneField.get(dialogPane);

            Platform.runLater(() -> {
                Stage dialogStage = (Stage) dialogScene.getWindow();
                dialogStage.getIcons().add(new Image("src/main/resources/app/harikarthik/nutrifusion/logo.png"));
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // Get the user input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> openAgeialog(name, stage));
    }

    private void openAgeialog(String name, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter age");
        dialog.setHeaderText("Enter your current age:");
        dialog.setContentText("Name:");
        try {
            Field dialogPaneField = dialog.getClass().getDeclaredField("dialogPane");
            dialogPaneField.setAccessible(true);
            Object dialogPane = dialogPaneField.get(dialog);

            Field sceneField = dialogPane.getClass().getDeclaredField("scene");
            sceneField.setAccessible(true);
            Scene dialogScene = (Scene) sceneField.get(dialogPane);

            Platform.runLater(() -> {
                Stage dialogStage = (Stage) dialogScene.getWindow();
                dialogStage.getIcons().add(new Image("src/main/resources/app/harikarthik/nutrifusion/logo.png"));
            });
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // Get the user input
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(age -> openWorkDialog(name, age, stage));
    }

    public void ondietCreateClick(){
        openDietNameDialog(MainApplication.primaryStage);
    }
    public void openDiet1(){
        File file = new File("diet1.txt");
        try {
            openfile(file, diet_one);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void openDiet2(){
        File file = new File("diet2.txt");
        try {
            openfile(file, diet_two);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void openDiet3(){
        File file = new File("diet3.txt");
        try {
            openfile(file, diet_three);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openfile(File file, Label label) throws IOException {
        Platform.runLater(() -> {
            try {
                if (file.exists()) {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(file);
                    } else {
                        System.out.println("Desktop is not supported. Cannot open the file.");
                    }
                } else {
                    label.setText("Diet does not exist. Create it.");
                }
            } catch (IOException e) {
                System.out.println("Error opening file: " + e.getMessage());
            }
        });
    }

}