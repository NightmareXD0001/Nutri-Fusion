package app.harikarthik.nutrifusion.utils;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
     * Display the BMI gauge and results.
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
     * Method to draw the BMI gauge and additional health information
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

}
