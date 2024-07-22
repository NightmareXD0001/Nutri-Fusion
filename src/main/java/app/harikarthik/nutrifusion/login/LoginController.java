package app.harikarthik.nutrifusion.login;

import app.harikarthik.nutrifusion.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {

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
    private Button loginButton;
    @FXML
    private Button registerButton;

    private static final String FROM_EMAIL = "nutrifusion2024@outlook.com";
    private static final String FROM_EMAIL_PASSWORD = "XNightmareXD123";

    private static final String DB_URL = "jdbc:mysql://uyb.h.filess.io:3307/NutriFusion_writerwise";
    private static final String DB_USER = "NutriFusion_writerwise";
    private static final String DB_PASSWORD = "a56eeb0d7ec2c1c9d605150d8e3a40b536aa35ff";
    public static HashMap<String, String> email_map = new HashMap<>();
    public static HashMap<String, String> password_map = new HashMap<>();

    @FXML
    protected void handleRegistration() {
        String username = usernameField.getText();
        String email = emailField.getText().toLowerCase();
        String password = showPasswordCheckBox.isSelected() ? passwordFieldVisible.getText() : passwordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Incomplete Form", "All fields are required, and the password field cannot be empty!");
        } else if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Invalid Email", "The email address is not valid. Please enter a valid email address.");
        } else if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Password Requirements not met", "The password must contain:\n" +
                    "- One number\n" +
                    "- One symbol\n" +
                    "- One lowercase letter\n" +
                    "- One uppercase letter\n" +
                    "- 8-50 characters\n" +
                    "- Only Latin letters");
        } else {
            usernameField.clear();
            emailField.clear();
            passwordField.clear();
            passwordFieldVisible.clear();
            showPasswordCheckBox.setSelected(false);
            email_map.put(username, email);
            password_map.put(username, password);
            saveTempData(username);
            switchRegisterSecond();
        }
    }

    private static boolean checkIfTableExists(Connection conn, String tableName) throws SQLException {
        boolean exists = false;
        String checkTableSql = "SHOW TABLES LIKE ?";
        try (PreparedStatement checkTableStmt = conn.prepareStatement(checkTableSql)) {
            checkTableStmt.setString(1, tableName);
            try (ResultSet rs = checkTableStmt.executeQuery()) {
                if (rs.next()) {
                    exists = true;
                }
            }
        }
        return exists;
    }

    // Method to create the table if it doesn't exist
    private static void createTable(Connection conn) throws SQLException {
        String createTableSql = "CREATE TABLE users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(255) NOT NULL, " +
                "email VARCHAR(255) NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "age INT, " +
                "gender VARCHAR(255), " +
                "UNIQUE (username), " +
                "UNIQUE (email)" +
                ")";
        try (Statement createStmt = conn.createStatement()) {
            createStmt.execute(createTableSql);
        }
    }

    public static boolean registerUser(String username, String email, String password, String gender, int age) {
        String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
        String insertUserSql = "INSERT INTO users (username, email, password, gender, age) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if user already exists
            if (!checkIfTableExists(conn, "users")) {
                createTable(conn);
            }
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {

                checkStmt.setString(1, username);
                checkStmt.setString(2, email);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        // User already exists
                        showAlert(Alert.AlertType.ERROR, "Registration Error", "User Already Exists", "A user with this username or email is already registered.");
                        return false;
                    }
                }
            }
            // Insert new user
            try (PreparedStatement insertStmt = conn.prepareStatement(insertUserSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, email);
                insertStmt.setString(3, password);
                insertStmt.setString(4, gender);
                insertStmt.setInt(5, age);

                int affectedRows = insertStmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    private void toggleShowPassword() {
        if (showPasswordCheckBox.isSelected()) {
            passwordFieldVisible.setText(passwordField.getText());
            passwordFieldVisible.setVisible(true);
            passwordFieldVisible.setManaged(true);
            passwordField.setVisible(false);
            passwordField.setManaged(false);
        } else {
            passwordField.setText(passwordFieldVisible.getText());
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordFieldVisible.setVisible(false);
            passwordFieldVisible.setManaged(false);
        }
    }

    private boolean isValidEmail(String email) {
        // Regex pattern for basic email validation
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 50) {
            return false;
        }
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSymbol = password.matches(".*[!@#$%^&*()_+\\-\\[\\]{};':\"\\\\|,.<>/?].*");
        boolean hasLowercase = password.matches(".*[a-z].*");
        boolean hasUppercase = password.matches(".*[A-Z].*");

        return hasNumber && hasSymbol && hasLowercase && hasUppercase;
    }

    public static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    @FXML
    protected void handleLogin() {
        String identifier = identifierField.getText();
        String password = passwordField.getText();

        if (identifier.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Incomplete Form", "Both fields are required!");
            return;
        }

        boolean isAuthenticated;
        if (isValidEmail(identifier)) {
            isAuthenticated = authenticateUser(identifier, password, true);
        } else {
            isAuthenticated = authenticateUser(identifier, password, false);
        }

        if (isAuthenticated) {
            sendLoginNotificationEmail(identifier);
            clearFields();
            switchDashboard();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Invalid Credentials", "The username or password is incorrect. Please try again.");
        }
    }

    public static void sendLoginNotificationEmail(String usernameOrEmail) {
        String host = "smtp.office365.com";
        String port = "587";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "Nutri Fusion Dev"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(usernameOrEmail));
            message.setSubject("Login Notification - Nutri Fusion");
            message.addHeader("Importance", "High");
            message.addHeader("X-Priority", "1");
            message.addHeader("Priority", "Urgent");

            String content = "<div style=\"background-color: #B0FE95; padding: 20px; font-family: Arial, sans-serif;\">"
                    + "<div style=\"max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">"
                    + "<div style=\"text-align: center; padding-bottom: 20px;\">"
                    + "<img src='https://i.postimg.cc/1tCpfbS2/logo.png' alt='Nutri Fusion Logo' style='width: 100px;'>"
                    + "</div>"
                    + "<h2 style=\"color: #333;\">Hello,</h2>"
                    + "<p style=\"color: #000;\">A login attempt was detected for your account.<br>"
                    + "If this was you, no further action is needed. If you did not login from this location, please secure your account.</p>"
                    + "</div>"
                    + "</div>";

            message.setContent(content, "text/html");

            Transport.send(message);
            System.out.println("Email sent successfully");

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void clearFields() {
        identifierField.clear();
        passwordField.clear();
        passwordFieldVisible.clear();
        showPasswordCheckBox.setSelected(false);
    }

    private boolean authenticateUser(String identifier, String password, boolean isEmail) {
        String loginSql = isEmail
                ? "SELECT username, password, age, gender, email FROM users WHERE email = ?"
                : "SELECT email, password, age, gender, username FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement loginStmt = conn.prepareStatement(loginSql)) {

            loginStmt.setString(1, identifier);

            try (ResultSet rs = loginStmt.executeQuery()) {
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    if (storedPassword.equals(password)) {
                        String username = rs.getString("username");
                        String email = rs.getString("email");
                        String gender = rs.getString("gender");
                        int age = rs.getInt("age");
                        saveQuickLoginData(username, email, password, gender, age);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Database Connection Failed", "Could not connect to the database. Please try again later.");
        }
        return false;
    }

    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final String ENCRYPTION_KEY = "195325685623562398523956"; // Example key, should be securely generated and stored

    public static void saveQuickLoginData(String username, String email, String password, String gender, int age) {
        try {
            // Generate a secret key
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);

            // Create a cipher instance and initialize it
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Encrypt the data
            byte[] encryptedUsername = cipher.doFinal(username.getBytes());
            byte[] encryptedEmail = cipher.doFinal(email.getBytes());
            byte[] encryptedPassword = cipher.doFinal(password.getBytes());
            byte[] encryptedGender = cipher.doFinal(gender.getBytes());
            byte[] encryptedAge = cipher.doFinal(String.valueOf(age).getBytes());
            byte[] encryptedFirstLogin = cipher.doFinal("true".getBytes());

            // Create a JSON object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", Base64.getEncoder().encodeToString(encryptedUsername));
            jsonObject.put("email", Base64.getEncoder().encodeToString(encryptedEmail));
            jsonObject.put("password", Base64.getEncoder().encodeToString(encryptedPassword));
            jsonObject.put("gender", Base64.getEncoder().encodeToString(encryptedGender));
            jsonObject.put("age", Base64.getEncoder().encodeToString(encryptedAge));
            jsonObject.put("login-before", Base64.getEncoder().encodeToString(encryptedFirstLogin));

            // Save the JSON object to a file
            Path path = Paths.get("data.json");
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.write(path, jsonObject.toString().getBytes(), StandardOpenOption.CREATE);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void saveTempData(String username) {
        try {
            // Generate a secret key
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);

            // Create a cipher instance and initialize it
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Encrypt the data
            byte[] encryptedUsername = cipher.doFinal(username.getBytes());

            // Create a JSON object
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", Base64.getEncoder().encodeToString(encryptedUsername));
            Path path = Paths.get("temp-data.json");
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            Files.write(path, jsonObject.toString().getBytes(), StandardOpenOption.CREATE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadTempUsername() {
        try {
            // Read the JSON object from the file
            Path path = Paths.get("temp-data.json");
            String jsonString = new String(Files.readAllBytes(path));
            Files.delete(path);
            // Parse the JSON object
            JSONObject jsonObject = new JSONObject(jsonString);
            String encryptedUsernameBase64 = jsonObject.getString("username");

            // Decode the base64 string to get the encrypted bytes
            byte[] encrypted = Base64.getDecoder().decode(encryptedUsernameBase64);

            // Generate a secret key
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);

            // Create a cipher instance and initialize it
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decrypt the username
            byte[] decrypted = cipher.doFinal(encrypted);

            // Return the decrypted username
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String loadQuickLoginUsername() {
        try {
            // Read the JSON object from the file
            String jsonString = new String(Files.readAllBytes(Paths.get("data.json")));

            // Parse the JSON object
            JSONObject jsonObject = new JSONObject(jsonString);
            String encryptedUsernameBase64 = jsonObject.getString("username");

            // Decode the base64 string to get the encrypted bytes
            byte[] encrypted = Base64.getDecoder().decode(encryptedUsernameBase64);

            // Generate a secret key
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);

            // Create a cipher instance and initialize it
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decrypt the username
            byte[] decrypted = cipher.doFinal(encrypted);

            // Return the decrypted username
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadQuickLoginEmail() {
        try {
            // Read the JSON object from the file
            String jsonString = new String(Files.readAllBytes(Paths.get("data.json")));

            // Parse the JSON object
            JSONObject jsonObject = new JSONObject(jsonString);
            String encryptedUsernameBase64 = jsonObject.getString("email");

            // Decode the base64 string to get the encrypted bytes
            byte[] encrypted = Base64.getDecoder().decode(encryptedUsernameBase64);

            // Generate a secret key
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);

            // Create a cipher instance and initialize it
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decrypt the username
            byte[] decrypted = cipher.doFinal(encrypted);

            // Return the decrypted username
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String loadQuickLoginPassword() {
        try {
            // Read the JSON object from the file
            String jsonString = new String(Files.readAllBytes(Paths.get("data.json")));

            // Parse the JSON object
            JSONObject jsonObject = new JSONObject(jsonString);
            String encryptedUsernameBase64 = jsonObject.getString("password");

            // Decode the base64 string to get the encrypted bytes
            byte[] encrypted = Base64.getDecoder().decode(encryptedUsernameBase64);

            // Generate a secret key
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);

            // Create a cipher instance and initialize it
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decrypt the username
            byte[] decrypted = cipher.doFinal(encrypted);

            // Return the decrypted username
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String loadQuickLoginGender() {
        try {
            // Read the JSON object from the file
            String jsonString = new String(Files.readAllBytes(Paths.get("data.json")));

            // Parse the JSON object
            JSONObject jsonObject = new JSONObject(jsonString);
            String encryptedUsernameBase64 = jsonObject.getString("gender");

            // Decode the base64 string to get the encrypted bytes
            byte[] encrypted = Base64.getDecoder().decode(encryptedUsernameBase64);

            // Generate a secret key
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);

            // Create a cipher instance and initialize it
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decrypt the username
            byte[] decrypted = cipher.doFinal(encrypted);

            // Return the decrypted username
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static int loadQuickLoginAge() {
        try {
            // Read the JSON object from the file
            String jsonString = new String(Files.readAllBytes(Paths.get("data.json")));

            // Parse the JSON object
            JSONObject jsonObject = new JSONObject(jsonString);
            String encryptedUsernameBase64 = jsonObject.getString("age");

            // Decode the base64 string to get the encrypted bytes
            byte[] encrypted = Base64.getDecoder().decode(encryptedUsernameBase64);

            // Generate a secret key
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);

            // Create a cipher instance and initialize it
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decrypt the username
            byte[] decrypted = cipher.doFinal(encrypted);

            String decryptedString = new String(decrypted);
            // Return the decrypted username
            return Integer.parseInt(decryptedString);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
    public static boolean checkfirstJoin() throws IOException {
        try {
            // Read the JSON object from the file
            String jsonString = new String(Files.readAllBytes(Paths.get("data.json")));

            // Parse the JSON object
            JSONObject jsonObject = new JSONObject(jsonString);
            String encryptedBase64 = jsonObject.getString("login-before");

            // Decode the base64 string to get the encrypted bytes
            byte[] encrypted = Base64.getDecoder().decode(encryptedBase64);

            // Generate a secret key
            SecretKeySpec secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(), ENCRYPTION_ALGORITHM);

            // Create a cipher instance and initialize it
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decrypt the username
            byte[] decrypted = cipher.doFinal(encrypted);

            // Return the decrypted username
            return Boolean.parseBoolean(new String(decrypted));
        } catch (Exception e) {
            return false;
        }

    }


    @FXML
    protected void switchRegister() {
        MainApplication.switchToRegistration();
    }
    @FXML
    protected void switchRegisterSecond() {
        MainApplication.switchToRegistrationSecond();
    }
    @FXML
    protected void switchlogin() {
        MainApplication.switchToLogin();
    }
    @FXML
    protected void switchDashboard() {
        MainApplication.switchDashboard();
    }

    @FXML
    private void handleEnterKey_login(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            loginButton.fire();
        }
    }
    @FXML
    private void handleEnterKey_Register(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            registerButton.fire();
        }
    }

    public static void sendWelcomeEmail(String toEmail, String username) {
        String host = "smtp.office365.com";
        String port = "587";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_EMAIL_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "Nutri Fusion"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Welcome to Nutri Fusion");

            String content = "<div style=\"background-color: #B0FE95; padding: 20px; font-family: Arial, sans-serif;\">"
                    + "<div style=\"max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\">"
                    + "<div style=\"text-align: center; padding-bottom: 20px;\">"
                    + "<img src='https://i.postimg.cc/1tCpfbS2/logo.png' alt='Nutri Fusion Logo' style='width: 100px;'>"
                    + "</div>"
                    + "<h2 style=\"color: #333;\">Hello " + username + ",</h2>"
                    + "<h4 style=\"color: #000;\">Welcome to <span style=\"background: linear-gradient(to right, #69f512, #69f512); -webkit-background-clip: text; background-clip: text; color: transparent;\">Nutri Fusion</span> v1.3.7. <br>We're thrilled to have you as a part of our community dedicated to enhancing your nutritional journey! With personalized diet recommendations and seamless management tools, Nutri Fusion is here to support you every step of the way. <br>" +
                    "<br>" +
                    "Let's embark on this journey together towards a healthier you!" +
                    "<br>" +
                    "Best regards, <br>" +
                    "Nutri Fusion Team</h4>"
                    + "</div>"
                    + "</div>";

            message.setContent(content, "text/html");

            Transport.send(message);
            System.out.println("Email sent successfully");

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


}