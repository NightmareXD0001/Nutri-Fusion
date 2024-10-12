package app.harikarthik.nutrifusion.dashboard;

import app.harikarthik.nutrifusion.MainApplication;
import app.harikarthik.nutrifusion.login.LoginController;
import app.harikarthik.nutrifusion.utils.MathUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
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
    @FXML
    private Label comingSoonLabel;


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
    public void switchpage3(){
        MainApplication.switchDashboard3();
    }public void switchpage4(){
        MainApplication.switchDashboard4();
    }


    private void openWorkDialog(String diet_name, String age, String gender, String weight, String height, String primary_goal, String dietary_preferences, String food_allergies, String activity_level, String daily_calorie_intake, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter your job");
        dialog.setHeaderText("Enter a brief description about your job :");
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
        result.ifPresent(job -> fetch_diet(diet_name, age, gender, weight, height, primary_goal, dietary_preferences, food_allergies, activity_level, daily_calorie_intake, job, stage.getScene()));
    }

    private void openCalorieIntakeDialog(String diet_name, String age, String gender, String weight, String height, String primary_goal, String dietary_preferences, String food_allergies, String activity_level, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Daily Calorie Intake");
        dialog.setHeaderText("Enter your desired daily calorie intake:");
        dialog.setContentText("Daily Calorie Intake:");
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
        result.ifPresent(daily_calorie_intake -> openWorkDialog(diet_name, age, gender, weight, height, primary_goal, dietary_preferences, food_allergies, activity_level, daily_calorie_intake, stage));
    }

    private void openActivityLevelDialog(String diet_name, String age, String gender, String weight, String height, String primary_goal, String dietary_preferences, String food_allergies, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Activity Level");
        dialog.setHeaderText("Enter your current activity level (e.g., sedentary, lightly active, moderately active, very active):");
        dialog.setContentText("Activity Level:");
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
        result.ifPresent(activity_level -> openCalorieIntakeDialog(diet_name, age, gender, weight, height, primary_goal, dietary_preferences, food_allergies, activity_level, stage));
    }

    private void openFoodAllergiesDialog(String diet_name, String age, String gender, String weight, String height, String primary_goal, String dietary_preferences, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Food Allergies");
        dialog.setHeaderText("Enter any food allergies or intolerances you have (e.g., gluten, dairy, nuts):");
        dialog.setContentText("Food Allergies:");
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
        result.ifPresent(food_allergies -> openActivityLevelDialog(diet_name, age, gender, weight, height, primary_goal, dietary_preferences, food_allergies, stage));
    }

    private void openDietaryPreferencesDialog(String diet_name, String age, String gender, String weight, String height, String primary_goal, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Dietary Preferences");
        dialog.setHeaderText("Enter your dietary preferences (e.g., vegetarian, vegan, keto, paleo):");
        dialog.setContentText("Dietary Preferences:");
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
        result.ifPresent(dietary_preferences -> openFoodAllergiesDialog(diet_name, age, gender, weight, height, primary_goal, dietary_preferences, stage));
    }

    private void openPrimaryGoalDialog(String diet_name, String age, String gender, String weight, String height, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Primary Goal");
        dialog.setHeaderText("Enter your primary health goal (e.g., weight loss, muscle gain, maintenance, improved energy levels):");
        dialog.setContentText("Primary Goal:");
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
        result.ifPresent(primary_goal -> openDietaryPreferencesDialog(diet_name, age, gender, weight, height, primary_goal, stage));
    }

    private void openHeightDialog(String diet_name, String age, String gender, String weight, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Height");
        dialog.setHeaderText("Enter your height (in cm):");
        dialog.setContentText("Height:");
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
        result.ifPresent(height -> openPrimaryGoalDialog(diet_name, age, gender, weight, height, stage));
    }

    private void openWeightDialog(String diet_name, String age, String gender, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Weight");
        dialog.setHeaderText("Enter your weight (in kg):");
        dialog.setContentText("Weight:");
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
        result.ifPresent(weight -> openHeightDialog(diet_name, age, gender, weight, stage));
    }

    private void openGenderDialog(String diet_name, String age, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Gender");
        dialog.setHeaderText("Enter your gender:");
        dialog.setContentText("Gender:");
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
        result.ifPresent(gender -> openWeightDialog(diet_name, age, gender, stage));
    }

    private void openAgeDialog(String diet_name, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Age");
        dialog.setHeaderText("Enter your current age:");
        dialog.setContentText("Age:");
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
        result.ifPresent(age -> openGenderDialog(diet_name, age, stage));
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
        result.ifPresent(diet_name -> openAgeDialog(diet_name, stage));
    }


    public void ondietCreateClick(){
        openDietNameDialog(MainApplication.primaryStage);
    }
    public void onbmiCalculateClick(){
        openBMICalculator(MainApplication.primaryStage);
    }

    private void openBMICalculator(Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Weight");
        dialog.setHeaderText("Enter Your Weight (in kg):");
        dialog.setContentText("Weight:");
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
        result.ifPresent(weight -> openBMICalculatorHeight(Integer.parseInt(weight), stage));
    }
    private void openBMICalculatorHeight(int weight, Stage stage) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Enter Height");
        dialog.setHeaderText("Enter Your Height (in cm):");
        dialog.setContentText("Height:");
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
        result.ifPresent(height -> MathUtils.calculateBMI(weight, Integer.parseInt(height)));
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

    public void onCalorieCalculateClick(){
        MathUtils.openCalorieCalculator();
    }
}