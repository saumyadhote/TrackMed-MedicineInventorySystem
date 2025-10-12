package ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> userTypeCombo;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    /**
     * Initialize method - runs when screen loads
     */
    @FXML
    public void initialize() {
        // Populate user type dropdown
        userTypeCombo.getItems().addAll("Hospital", "Supplier");

        // Set default selection
        userTypeCombo.setValue("Hospital");

        // Hide error label initially
        errorLabel.setVisible(false);

        // Add button action
        loginButton.setOnAction(event -> handleLogin());
    }

    /**
     * Handle login button click
     */
    @FXML
    private void handleLogin() {
        // Get input values
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String userType = userTypeCombo.getValue();

        // Validate inputs
        if (username.isEmpty()) {
            showError("Please enter username");
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter password");
            return;
        }

        if (userType == null) {
            showError("Please select user type");
            return;
        }

        // TODO: Call backend authentication service
        // For now, just show success message
        System.out.println("Login attempt:");
        System.out.println("Username: " + username);
        System.out.println("Type: " + userType);

        // TODO: If login successful, open main dashboard
        // openDashboard(userType);

        showError("Login functionality will be connected to backend");
    }

    /**
     * Show error message
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Clear form
     */
    @FXML
    private void clearForm() {
        usernameField.clear();
        passwordField.clear();
        userTypeCombo.setValue("Hospital");
        errorLabel.setVisible(false);
    }
}