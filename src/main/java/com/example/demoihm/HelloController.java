package com.example.demoihm;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.text.Text;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class HelloController {

    @FXML
    private Text wrongLogIn;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;



    public void studantLogIn(ActionEvent event) throws IOException {
        studant();

    }
    private void studant() throws IOException {
        HelloApplication m = new HelloApplication ();
        m.changeScene("Studant.fxml");


    }

    public void userLogIn(ActionEvent event) throws IOException {
        checkLogin();

    }
    private void checkLogin() throws IOException {
        HelloApplication m = new HelloApplication ();

        if(username.getText().toString().equals("j") ) {
            wrongLogIn.setText("Success!");


            m.changeScene("AdminBook.fxml");

        }

        else if(username.getText().isEmpty() && password.getText().isEmpty()) {
            wrongLogIn.setText("Please enter your data.");
        }


        else {
            wrongLogIn.setText("Wrong username or password!");
        }
    }


    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }}