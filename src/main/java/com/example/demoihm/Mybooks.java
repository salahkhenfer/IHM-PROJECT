package com.example.demoihm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Mybooks implements Initializable {

    @FXML
    private VBox bookPaneContainerAdmin;

    public class Book {
        private String title;
        private String author;
        private int numberOfCopies;

        public Book(String title, String author, int numberOfCopies) {
            this.title = title;
            this.author = author;
            this.numberOfCopies = numberOfCopies;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public int getNumberOfCopies() {
            return numberOfCopies;
        }
    }

    private final List<Book> bookList = new ArrayList<>();

    @FXML





    private void removeBookFromUI(String bookName) {
        List<Node> nodesToRemove = new ArrayList<>();

        // Collect the nodes to remove without modifying the original list
        for (Node node : bookPaneContainerAdmin.getChildren()) {
            if (node instanceof Pane) {
                String paneBookName = ((Pane) node).getChildren().stream()
                        .filter(child -> child instanceof Label)
                        .map(child -> ((Label) child).getText())
                        .findFirst()
                        .orElse(null);

                if (paneBookName != null && paneBookName.equals(bookName)) {
                    nodesToRemove.add(node);
                }
            }
        }

        // Remove the collected nodes
        bookPaneContainerAdmin.getChildren().removeAll(nodesToRemove);
    }

    @FXML

    private void rejactButtonClicked(ActionEvent event) {
        Button rejectButton = (Button) event.getSource();
        String bookName = rejectButton.getId(); // Assuming id is set to book title

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("src/data.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            ArrayNode loansNode = (ArrayNode) rootNode.get("loanRequests");

            // Find the loan node and remove it from loans
            for (int i = 0; i < loansNode.size(); i++) {
                JsonNode loanNode = loansNode.get(i);
                if (loanNode.get("book").get("title").asText().equals(bookName)) {
                    loansNode.remove(i);

                    // Write the updated data back to the JSON file
                    objectMapper.writeValue(jsonFile, rootNode);

                    // Update the UI by removing the book from the loans list
                    removeBookFromUI(bookName);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error appropriately (e.g., display an error message to the user)
        }
    }




    private boolean matchesBookJson(JsonNode bookNode, Book book) {
        // Implement logic to check if the JSON node represents the given book
        // You might compare relevant fields like title, author, etc.
        return false;
    }

    private Pane createBookPane(String bookName, String studentName, int loanDuration , boolean avilabel) {
        VBox pane = new VBox();  // Use VBox for vertical layout
        pane.setPadding(new Insets(10));  // Add padding for breathing room
        pane.setSpacing(8);  // Add spacing between elements
        pane.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; -fx-border-width: 1px;");  // Set background and border

        // Create buttons with a modern, rounded style
        HBox buttonBox = new HBox(10);  // Group buttons together
        buttonBox.setAlignment(Pos.CENTER_RIGHT);  // Align buttons to the right

        Button rejectButton = new Button("return");
        rejectButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: #ffffff; -fx-border-radius: 5px;");
rejectButton.setId(bookName);
        rejectButton.setOnAction(this::rejactButtonClicked);
        buttonBox.getChildren().addAll( rejectButton);

        // Create text elements with a distinct font and color
        Label bookLabel = new Label(bookName);  // Use Label for better text handling
        bookLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bookLabel.setTextFill(Color.DARKBLUE);
        Label studentLabel = new Label("Student: " + studentName);
        studentLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 16));
        Label durationLabel = new Label("Loan Duration: " + loanDuration + " days");
        durationLabel.setFont(Font.font("Arial", 14));
        Label avilabelLabel = new Label("available : " + avilabel );
        avilabelLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 16));

        // Arrange elements vertically
        pane.getChildren().addAll(bookLabel, studentLabel, durationLabel,avilabelLabel, buttonBox);

        return pane;
    }


    @FXML
    private void loadBooksFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/data.json"));
            JsonNode loansNode = rootNode.get("loanRequests");

            for (JsonNode loanNode : loansNode) {
                JsonNode bookNode = loanNode.get("book");
                String title = bookNode.get("title").asText();

                  int loanDuration = loanNode.get("duration").asInt();  // Assuming "duration" is the loan duration field


                Pane bookPane = createBookPane(title, "", loanDuration ,true);
                bookPaneContainerAdmin.getChildren().add(bookPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void Home() throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("studant.fxml");


    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadBooksFromJson();
    }
}
