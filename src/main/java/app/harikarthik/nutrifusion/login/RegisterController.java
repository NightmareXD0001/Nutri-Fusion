package app.harikarthik.nutrifusion.login;

import app.harikarthik.nutrifusion.MainApplication;
import app.harikarthik.nutrifusion.dashboard.MainController;
import com.mysql.cj.log.Log;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class RegisterController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField identifierField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordFieldVisible;
    @FXML
    private CheckBox showPasswordCheckBox;
    @FXML
    private RadioButton gender_MaleField;
    @FXML
    private RadioButton gender_FemaleField;
    @FXML
    private RadioButton gender_OtherField;
    @FXML
    private DatePicker dobField;
    @FXML
    public void initialize() {
        ToggleGroup genderGroup = new ToggleGroup();
        gender_MaleField.setToggleGroup(genderGroup);
        gender_FemaleField.setToggleGroup(genderGroup);
        gender_OtherField.setToggleGroup(genderGroup);

        // Add event listeners to the RadioButtons
        gender_MaleField.setOnAction(event -> handleGenderSelection("Male"));
        gender_FemaleField.setOnAction(event -> handleGenderSelection("Female"));
        gender_OtherField.setOnAction(event -> handleGenderSelection("Other"));
    }

    private void handleGenderSelection(String gender) {
        System.out.println("Selected Gender: " + gender);
    }
    @FXML
    protected void handleRegistrationSecond() {
        String username = LoginController.loadTempUsername();
        String email = LoginController.email_map.get(username);
        String password = LoginController.password_map.get(username);;
        int dob_year = dobField.getValue().getYear();
        int currentYear = LocalDate.now().getYear();
        int age = (currentYear - dob_year);
        String gender = null;
        if(gender_OtherField.isSelected()){
            gender_FemaleField.setSelected(false);
            gender_MaleField.setSelected(false);
            gender = "other";

        }if(gender_MaleField.isSelected()){
            gender_FemaleField.setSelected(false);
            gender_OtherField.setSelected(false);
            gender = "male";
        }if(gender_FemaleField.isSelected()){
            gender_OtherField.setSelected(false);
            gender_MaleField.setSelected(false);
            gender = "female";
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Registration Confirmation");
        alert.setHeaderText("Confirm Your Details");
        alert.setContentText("Are you sure you want to register with the following details?\n\n" +
                "Username: " + username + "\n" +
                "Email: " + email + "\n" +
                "Age: " + age + "\n" +
                "Gender: " + gender);
        String finalGender = gender;
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                LoginController.email_map.put(username, email);
                LoginController.password_map.put(username, password);
                if (LoginController.registerUser(username, email, password, finalGender, age)) {
                    System.out.println("Username: " + username);
                    System.out.println("Email: " + email);
                    System.out.println("Password: " + password);
                    LoginController.sendWelcomeEmail(email, username);
                    LoginController.saveQuickLoginData(username, email, password, finalGender, age);
                    MainApplication.switchDashboard();
                } else {
                    LoginController.showAlert(Alert.AlertType.WARNING, "Registration Error", "Database Error", "There was an error registering your details. Please try again.");
                }
            }
        });
    }
    @FXML
    protected void switchlogin() {
        MainApplication.switchToLogin();
    }

}
