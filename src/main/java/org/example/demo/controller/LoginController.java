package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.demo.controller.HomePageController;
import org.example.demo.dao.DBConnection;


public class LoginController implements Initializable {

    @FXML
    private Button forgotpassword;

    @FXML
    private Button login;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    private Connection connection;

    private DBConnection dbConnection;

    private PreparedStatement pst;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnection = new DBConnection();
    }

    public void handleSignupButton(javafx.event.ActionEvent actionEvent) throws IOException {
        login.getScene().getWindow().hide();
        Stage signup = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/signup.fxml"));
        Scene scene = new Scene(root);
        signup.setScene(scene);
        signup.show();
    }

    public void handleLoginAction(javafx.event.ActionEvent actionEvent) throws IOException {
        connection = dbConnection.getInstance();
        String query = "SELECT * FROM users WHERE username=? AND password=?";
        try {
            pst = connection.prepareStatement(query);
            pst.setString(1, username.getText());
            pst.setString(2, password.getText());
            ResultSet rs = pst.executeQuery();
            int count = 0;
            while (rs.next()) {
                HomePageController.name = rs.getString("name");
                count = 1;
            }
            if (count == 1) {
                login.getScene().getWindow().hide();
                Stage signup = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/homepage.fxml"));
                Scene scene = new Scene(root);
                signup.setScene(scene);
                signup.show();
            } else {
                OptionPane("Username or Password is not Correct", "Error Message");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleForgotAction(javafx.event.ActionEvent actionEvent) throws IOException {
        login.getScene().getWindow().hide();
        Stage signup = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/demo/forgotpassword.fxml"));
        Scene scene = new Scene(root);
        signup.setScene(scene);
        signup.show();
    }

    private void OptionPane(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}