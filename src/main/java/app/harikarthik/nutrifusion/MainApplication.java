package app.harikarthik.nutrifusion;

import app.harikarthik.nutrifusion.dashboard.MainController;
import app.harikarthik.nutrifusion.login.LoginController;
import app.harikarthik.nutrifusion.utils.DietUtils;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainApplication extends Application {

    public static Stage primaryStage;
    private static Scene loginScene;
    private static Scene registrationScene;
    private static Scene registrationSecondScene;
    private static Scene dashboardScene;
    private static Scene dashboardScene2;
    private static Scene dashboardScene3;
    private static Scene dashboardScene4;

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Software Loading...");
        try {
            if (!LoginController.checkfirstJoin()) {
                MainApplication.primaryStage = primaryStage;
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login/registration.fxml")));
                primaryStage.setTitle("Nutri Fusion Auth v1.14.74 stable-build");
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png")));
                primaryStage.getIcons().add(icon);
                registrationScene = new Scene(root);
                primaryStage.setScene(registrationScene);
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
                primaryStage.show();
                switchDashboard();
            }

            else {
                MainApplication.primaryStage = primaryStage;
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login/login.fxml")));
                primaryStage.setTitle("Nutri Fusion Auth v1.14.74 stable-build");
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("logo.png")));
                primaryStage.getIcons().add(icon);
                loginScene = new Scene(root);
                primaryStage.setScene(loginScene);
                primaryStage.setResizable(false);
                primaryStage.centerOnScreen();
                primaryStage.show();
                switchDashboard();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void switchToRegistration() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("login/registration.fxml")));
            registrationScene = new Scene(root);
            primaryStage.setScene(registrationScene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchToRegistrationSecond() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("login/registration-2.fxml")));
            MainApplication.registrationSecondScene = new Scene(root);
            primaryStage.setScene(registrationSecondScene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchToLogin() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("login/login.fxml")));
            MainApplication.loginScene = new Scene(root);
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void switchDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainApplication.class.getResource("dashboard/main.fxml")));
            Parent root = loader.load();
            MainApplication.dashboardScene = new Scene(root);
            MainController controller = loader.getController();
            controller.setUsername();
            controller.setGenderLogo();
            primaryStage.setScene(dashboardScene);
            primaryStage.setResizable(false);
            PauseTransition pause = new PauseTransition(Duration.millis(10));
            pause.setOnFinished(event -> {
                primaryStage.centerOnScreen();
            });
            pause.play();
            primaryStage.setTitle("Nutri Fusion v1.14.74 stable-build");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void switchDashboard2() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainApplication.class.getResource("dashboard/main-page-2.fxml")));
            Parent root = loader.load();
            MainApplication.dashboardScene2 = new Scene(root);
            MainController controller = loader.getController();
            controller.setUsername();
            controller.setGenderLogo();
            primaryStage.setScene(dashboardScene2);
            primaryStage.setResizable(false);
            PauseTransition pause = new PauseTransition(Duration.millis(10));
            pause.setOnFinished(event -> {
                primaryStage.centerOnScreen();
            });
            pause.play();
            primaryStage.setTitle("Nutri Fusion v1.14.74 stable-build");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void switchDashboard3() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainApplication.class.getResource("dashboard/main-page-3.fxml")));
            Parent root = loader.load();
            MainApplication.dashboardScene3 = new Scene(root);
            MainController controller = loader.getController();
            controller.setUsername();
            controller.setGenderLogo();
            primaryStage.setScene(dashboardScene3);
            primaryStage.setResizable(false);
            PauseTransition pause = new PauseTransition(Duration.millis(10));
            pause.setOnFinished(event -> {
                primaryStage.centerOnScreen();
            });
            pause.play();
            primaryStage.setTitle("Nutri Fusion v1.14.74 stable-build");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void switchDashboard4() {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(MainApplication.class.getResource("dashboard/main-page-4.fxml")));
            Parent root = loader.load();
            MainApplication.dashboardScene4 = new Scene(root);
            MainController controller = loader.getController();
            controller.setUsername();
            controller.setGenderLogo();
            primaryStage.setScene(dashboardScene4);
            primaryStage.setResizable(false);
            PauseTransition pause = new PauseTransition(Duration.millis(10));
            pause.setOnFinished(event -> {
                primaryStage.centerOnScreen();
            });
            pause.play();
            primaryStage.setTitle("Nutri Fusion v1.14.74 stable-build");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
