package app.harikarthik.nutrifusion.utils;

import app.harikarthik.nutrifusion.MainApplication;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class MathUtils {

    /**
     * Calculate BMI given weight and height and display the BMI gauge.
     *
     * @param weight in kilograms (kg)
     * @param height in centimeters (cm)
     */
    public static void calculateBMI(int weight, int height) {
        if (weight <= 0 || height <= 0) {
            throw new IllegalArgumentException("Weight and height must be positive numbers.");
        }

        // Convert height from centimeters to meters
        double heightInMeters = height / 100.0;

        // Calculate BMI using weight (kg) and height (m)
        double bmi = weight / (heightInMeters * heightInMeters);

        // Calculate the healthy weight range
        double healthyWeightMin = 17.7 * heightInMeters * heightInMeters;
        double healthyWeightMax = 24.9 * heightInMeters * heightInMeters;

        // Calculate the weight loss needed to reach a BMI of 24.9
        double targetWeight = 24.9 * heightInMeters * heightInMeters;
        double weightToLose = weight - targetWeight;

        // Calculate the Ponderal Index (weight/height³)
        double ponderalIndex = weight / Math.pow(heightInMeters, 3);

        // Show the BMI gauge and other calculated metrics
        showBMIGauge(bmi, healthyWeightMin, healthyWeightMax, weightToLose, ponderalIndex);
    }

    /**
     * Display the BMI results.
     *
     * @param bmi              the calculated BMI
     * @param healthyWeightMin the minimum healthy weight
     * @param healthyWeightMax the maximum healthy weight
     * @param weightToLose     the weight loss goal to reach a BMI of 24.9
     * @param ponderalIndex    the calculated Ponderal Index
     */
    public static void showBMIGauge(double bmi, double healthyWeightMin, double healthyWeightMax, double weightToLose, double ponderalIndex) {
        // Create a new stage for the second screen
        Stage gaugeStage = new Stage();
        gaugeStage.setTitle("BMI Calculator Results");

        // Create the canvas and graphics context
        Canvas canvas = new Canvas(500, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Draw the BMI gauge and additional information
        tellBMI(gc, bmi, healthyWeightMin, healthyWeightMax, weightToLose, ponderalIndex);

        Group root = new Group();
        root.getChildren().add(canvas);
        gaugeStage.setScene(new Scene(root));
        gaugeStage.centerOnScreen();
        gaugeStage.setResizable(false);
        gaugeStage.show();

    }

    /**
     * Method to tell the BMI and additional health information
     *
     * @param gc               the graphics context to draw on
     * @param bmi              the BMI value to show on the gauge
     * @param healthyWeightMin the minimum healthy weight
     * @param healthyWeightMax the maximum healthy weight
     * @param weightToLose     the weight loss goal
     * @param ponderalIndex    the calculated Ponderal Index
     */
    private static void tellBMI(GraphicsContext gc, double bmi, double healthyWeightMin, double healthyWeightMax, double weightToLose, double ponderalIndex) {
        gc.setFont(new Font("Arial", 20));
        gc.setFill(Color.BLACK);

        // Calculate the center of the canvas
        double canvasWidth = gc.getCanvas().getWidth();

        // Display the calculated BMI
        String bmiText = "BMI = " + String.format("%.1f", bmi);
        gc.fillText(bmiText, (canvasWidth - gc.getFont().getSize() * bmiText.length() / 2) / 2, 80);

        // Display the healthy BMI range
        String healthyRangeText = "Healthy BMI range: 17.7 - 24.9 kg/m²";
        gc.fillText(healthyRangeText, (canvasWidth - gc.getFont().getSize() * healthyRangeText.length() / 2) / 2, 120);

        // Display the healthy weight range
        String weightRangeText = String.format("Healthy weight for the height: %.1f kg - %.1f kg", healthyWeightMin, healthyWeightMax);
        gc.fillText(weightRangeText, (canvasWidth - gc.getFont().getSize() * weightRangeText.length() / 2) / 2, 160);

        // Display the message about weight loss goal to reach a BMI of 24.9
        if (weightToLose > 1) {
            String weightLossText = String.format("Lose %.1f kg to reach a BMI of 24.9 kg/m²", weightToLose);
            gc.fillText(weightLossText, (canvasWidth - gc.getFont().getSize() * weightLossText.length() / 2) / 2, 200);
        } else {
            String withinRangeText = "You are within the healthy BMI range.";
            gc.fillText(withinRangeText, (canvasWidth - gc.getFont().getSize() * withinRangeText.length() / 2) / 2, 200);
        }

        // Display the ponderal index
        String ponderalIndexText = String.format("Ponderal Index: %.1f kg/m³", ponderalIndex);
        gc.fillText(ponderalIndexText, (canvasWidth - gc.getFont().getSize() * ponderalIndexText.length() / 2) / 2, 240);
    }

    /**
     * Calculate the caloric needs based on user input.
     *
     * @param weight        the weight in kilograms
     * @param height        the height in centimeters
     * @param age           the age in years
     * @param gender        the gender of the user
     * @param activityLevel the activity level of the user
     * @param outputArea    the TextArea to display results
     */
    public static void calculateCalories(double weight, double height, int age, String gender, String activityLevel, TextArea outputArea) {
        // Calculate BMR
        double bmr = calculateBMR(weight, height, age, gender);

        // Calculate TDEE based on activity level
        double tdee = calculateTDEE(bmr, activityLevel);

        // Calculate caloric needs for weight maintenance and weight loss
        double maintenanceCalories = tdee;
        double mildWeightLoss = tdee - 250; // 0.25 kg/week
        double moderateWeightLoss = tdee - 500; // 0.5 kg/week
        double extremeWeightLoss = tdee - 1000; // 1 kg/week

        // Prepare output
        StringBuilder output = new StringBuilder();
        output.append(String.format("Maintenance Calories: %.0f cal/day (100%%)\n", maintenanceCalories));
        output.append(String.format("Mild Weight Loss: %.0f cal/day (88%%)\n", mildWeightLoss));
        output.append(String.format("Moderate Weight Loss: %.0f cal/day (77%%)\n", moderateWeightLoss));
        output.append(String.format("Extreme Weight Loss: %.0f cal/day (53%%)\n", extremeWeightLoss));

        outputArea.setText(output.toString());
    }

    private static double calculateBMR(double weight, double height, int age, String gender) {
        if (gender.equals("Male")) {
            // BMR for men
            return 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            // BMR for women
            return 10 * weight + 6.25 * height - 5 * age - 161;
        }
    }

    private static double calculateTDEE(double bmr, String activityLevel) {
        double activityFactor;

        switch (activityLevel) {
            case "Sedentary (little or no exercise)":
                activityFactor = 1.2;
                break;
            case "Lightly active (light exercise/sports 1-3 days/week)":
                activityFactor = 1.375;
                break;
            case "Moderately active (moderate exercise/sports 3-5 days/week)":
                activityFactor = 1.55;
                break;
            case "Very active (hard exercise/sports 6-7 days a week)":
                activityFactor = 1.725;
                break;
            case "Super active (very hard exercise/physical job)":
                activityFactor = 1.9;
                break;
            default:
                activityFactor = 1.0;
        }

        return bmr * activityFactor;
    }

    public static void openCalorieCalculator() {

        Stage calculatorStage = new Stage();
        calculatorStage.setTitle("Calorie Calculator");

        // Create UI elements
        TextField weightField = new TextField();
        TextField heightField = new TextField();
        TextField ageField = new TextField();
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female");
        ComboBox<String> activityLevelCombo = new ComboBox<>();
        activityLevelCombo.getItems().addAll(
                "Sedentary (little or no exercise)",
                "Lightly active (light exercise/sports 1-3 days/week)",
                "Moderately active (moderate exercise/sports 3-5 days/week)",
                "Very active (hard exercise/sports 6-7 days a week)",
                "Super active (very hard exercise/physical job)"
        );

        Button calculateButton = new Button("Calculate Calories");
        calculateButton.autosize();
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        // Set up grid layout for the calculator
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label("Weight (kg):"), 0, 0);
        grid.add(weightField, 1, 0);
        grid.add(new Label("Height (cm):"), 0, 1);
        grid.add(heightField, 1, 1);
        grid.add(new Label("Age (years):"), 0, 2);
        grid.add(ageField, 1, 2);
        grid.add(new Label("Gender:"), 0, 3);
        grid.add(genderCombo, 1, 3);
        grid.add(new Label("Activity Level:"), 0, 4);
        grid.add(activityLevelCombo, 1, 4);
        grid.add(calculateButton, 0, 5);
        grid.add(new Label("Caloric Needs:"), 0, 6);
        grid.add(outputArea, 1, 6);

        // Calculate button action
        calculateButton.setOnAction(e -> {
            try {
                double weight = Double.parseDouble(weightField.getText());
                double height = Double.parseDouble(heightField.getText());
                int age = Integer.parseInt(ageField.getText());
                String gender = genderCombo.getValue();
                String activityLevel = activityLevelCombo.getValue();

                if (gender == null || activityLevel == null) {
                    showAlert("Input Error", "Please select gender and activity level.");
                    return;
                }

                calculateCalories(weight, height, age, gender, activityLevel, outputArea);
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Please enter valid numbers for weight, height, and age.");
            }
        });

        // Set up the scene for the calculator
        Scene calculatorScene = new Scene(grid, 500, 400);
        calculatorStage.setScene(calculatorScene);
        calculatorStage.setResizable(false);
        calculatorStage.centerOnScreen();
        Image icon = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("logo.png")));
        calculatorStage.getIcons().add(icon);
        calculatorStage.show();
    }


    public static void openMacroCalculator() {
        Stage calculatorStage = new Stage();
        calculatorStage.setTitle("Calorie & Macro Calculator");

        // Create UI elements
        TextField weightField = new TextField();
        TextField heightField = new TextField();
        TextField ageField = new TextField();
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female");
        ComboBox<String> activityLevelCombo = new ComboBox<>();
        activityLevelCombo.getItems().addAll(
                "Sedentary (little or no exercise)",
                "Lightly active (light exercise/sports 1-3 days/week)",
                "Moderately active (moderate exercise/sports 3-5 days/week)",
                "Very active (hard exercise/sports 6-7 days a week)",
                "Super active (very hard exercise/physical job)"
        );

        Button calculateButton = new Button("Calculate Macros");
        calculateButton.autosize();
        TextArea outputArea = new TextArea();
        outputArea.setEditable(false);

        // Set up grid layout for the calculator
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(new Label("Weight (kg):"), 0, 0);
        grid.add(weightField, 1, 0);
        grid.add(new Label("Height (cm):"), 0, 1);
        grid.add(heightField, 1, 1);
        grid.add(new Label("Age (years):"), 0, 2);
        grid.add(ageField, 1, 2);
        grid.add(new Label("Gender:"), 0, 3);
        grid.add(genderCombo, 1, 3);
        grid.add(new Label("Activity Level:"), 0, 4);
        grid.add(activityLevelCombo, 1, 4);
        grid.add(calculateButton, 0, 5);
        grid.add(new Label("Caloric Needs and Macronutrient Breakdown:"), 0, 6);
        grid.add(outputArea, 1, 6);

        // Calculate button action
        calculateButton.setOnAction(e -> {
            try {
                double weight = Double.parseDouble(weightField.getText());
                double height = Double.parseDouble(heightField.getText());
                int age = Integer.parseInt(ageField.getText());
                String gender = genderCombo.getValue();
                String activityLevel = activityLevelCombo.getValue();

                if (gender == null || activityLevel == null) {
                    showAlert("Input Error", "Please select gender and activity level.");
                    return;
                }

                calculateCaloriesAndMacros(weight, height, age, gender, activityLevel, outputArea);
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Please enter valid numbers for weight, height, and age.");
            }
        });

        // Set up the scene for the calculator
        Scene calculatorScene = new Scene(grid, 500, 400);
        calculatorStage.setScene(calculatorScene);
        calculatorStage.setResizable(false);
        calculatorStage.centerOnScreen();
        Image icon = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("logo.png")));
        calculatorStage.getIcons().add(icon);
        calculatorStage.show();
    }

    private static void calculateCaloriesAndMacros(double weight, double height, int age, String gender, String activityLevel, TextArea outputArea) {
        // Calculate BMR using Mifflin-St Jeor Equation
        double bmr;
        if (gender.equals("Male")) {
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        // Adjust BMR based on activity level
        double activityMultiplier = getActivityMultiplier(activityLevel);
        double totalCalories = bmr * activityMultiplier;

        // Calculate macronutrients based on common macro distribution (example: 30% protein, 40% carbs, 30% fats)
        double proteinCalories = totalCalories * 0.30;
        double carbsCalories = totalCalories * 0.40;
        double fatsCalories = totalCalories * 0.30;

        // Convert calories to grams
        double proteinGrams = proteinCalories / 4; // 1g of protein = 4 calories
        double carbsGrams = carbsCalories / 4;     // 1g of carbs = 4 calories
        double fatsGrams = fatsCalories / 9;       // 1g of fat = 9 calories

        // Display results in the output area
        outputArea.setText(String.format("Total Daily Calories: %.2f kcal\n\n", totalCalories) +
                String.format("Protein: %.2f grams (30%%)\n", proteinGrams) +
                String.format("Carbohydrates: %.2f grams (40%%)\n", carbsGrams) +
                String.format("Fats: %.2f grams (30%%)\n", fatsGrams));
    }

    private static double getActivityMultiplier(String activityLevel) {
        switch (activityLevel) {
            case "Sedentary (little or no exercise)":
                return 1.2;
            case "Lightly active (light exercise/sports 1-3 days/week)":
                return 1.375;
            case "Moderately active (moderate exercise/sports 3-5 days/week)":
                return 1.55;
            case "Very active (hard exercise/sports 6-7 days a week)":
                return 1.725;
            case "Super active (very hard exercise/physical job)":
                return 1.9;
            default:
                return 1.0; // Default to no adjustment if unknown activity level
        }
    }



    private static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
