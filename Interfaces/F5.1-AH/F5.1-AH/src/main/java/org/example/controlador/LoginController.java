package org.example.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.HelloFX;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblError;

    @FXML
    private void onEntrarClick(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();

        // Validación: no permitir campos vacíos
        if (usuario.isEmpty() || password.isEmpty()) {
            lblError.setText("Error: Debes rellenar usuario y contraseña.");
            return;
        }

        // Navegación a la pantalla principal
        try {
            FXMLLoader loader = new FXMLLoader(HelloFX.class.getResource("/vistas/main.fxml"));
            Scene scene = new Scene(loader.load(), 850, 550);

            // Obtener el Stage (ventana) actual desde el botón pulsado
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Gestor de Tareas - Panel Principal");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.centerOnScreen();
        } catch (IOException e) {
            lblError.setText("Error al cargar la pantalla principal.");
            e.printStackTrace();
        }
    }
}