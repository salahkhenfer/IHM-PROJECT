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
import javafx.scene.control.*;
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

public class AccepteBook implements Initializable {

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
    private void accepteButtonClicked(ActionEvent event) {
        Button accepteButton = (Button) event.getSource();
        String bookName = accepteButton.getId(); // Assuming id is set to book title

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("src/data.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            ArrayNode loansNode = (ArrayNode) rootNode.get("loans");
            ArrayNode loanRequestsNode = (ArrayNode) rootNode.get("loanRequests");

            // Find the loan node and remove it from loans
            for (int i = 0; i < loansNode.size(); i++) {
                JsonNode loanNode = loansNode.get(i);
                if (loanNode.get("book").get("title").asText().equals(bookName)) {
                    // Create a new loan request node with just title and duration
                    ObjectNode loanRequestNode = objectMapper.createObjectNode();
                    loanRequestNode.put("title", bookName);
                    loanRequestNode.put("duration", loanNode.get("duration").asInt());


                    loansNode.remove(i);

                    // Add a new loan to the "loans" array (adjust serialNumber as needed)
                    ObjectNode newLoanNode = objectMapper.createObjectNode();
                    newLoanNode.put("loanNumber", generateNewLoanNumber()); // Implement a method to generate a new loan number
                    newLoanNode.put("duration", loanNode.get("duration").asInt());

                    ObjectNode bookNode = objectMapper.createObjectNode();
                    bookNode.put("serialNumber", generateNewSerialNumber()); // Implement a method to generate a new serial number
                    bookNode.put("title", bookName);

                    newLoanNode.set("book", bookNode);

                    loanRequestsNode.add(newLoanNode);
                    break;
                }
            }

            // Write the updated data back to the JSON file
            objectMapper.writeValue(jsonFile, rootNode);

            // Update the UI by removing the book from the loans list
            removeBookFromUI(bookName);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error appropriately (e.g., display an error message to the user)
        }
    }

    private int generateNewLoanNumber() {
        // Implement logic to generate a new unique loan number
        // You may use a counter or any other strategy based on your requirements
        return 0; // Placeholder, replace with actual logic
    }

    private int generateNewSerialNumber() {
        // Implement logic to generate a new unique serial number
        // You may use a counter or any other strategy based on your requirements
        return 0; // Placeholder, replace with actual logic
    }

    private void removeBookFromUI(String bookName) {
        // Assuming you have a method or logic to remove the book from the UI
        // Iterate through bookPaneContainerAdmin and remove the corresponding UI element
        for (Node node : bookPaneContainerAdmin.getChildren()) {
            if (node instanceof Pane) {
                String paneBookName = ((Pane) node).getChildren().stream()
                        .filter(child -> child instanceof Label)
                        .map(child -> ((Label) child).getText())
                        .findFirst()
                        .orElse(null);

                if (paneBookName != null && paneBookName.equals(bookName)) {
                    // Remove the corresponding UI element
                    bookPaneContainerAdmin.getChildren().remove(node);
                    break;
                }
            }
        }
    }

    @FXML
    private void rejactButtonClicked(ActionEvent event) {
        Button rejectButton = (Button) event.getSource();
        String bookName = rejectButton.getId(); // Assuming id is set to book title

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File jsonFile = new File("src/data.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            ArrayNode loansNode = (ArrayNode) rootNode.get("loans");

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


    private Book getBookByName(List<Book> bookList, String bookName) {
        return this.bookList.stream()
                .filter(book -> book.getTitle().equals(bookName))
                .findFirst()
                .orElse(null);
    }

    private void updateBookInJson(Book selectedBook, boolean accepted) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File jsonFile = new File("src/data.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);
            ArrayNode booksNode = (ArrayNode) rootNode.get("books");

            for (JsonNode bookNode : booksNode) {
                if (matchesBookJson(bookNode, selectedBook)) {
                    // Update the book information in the JSON array
                    ((ObjectNode) bookNode).put("accepted", accepted);

                    // Write the updated JsonNode back to the JSON file
                    objectMapper.writeValue(jsonFile, rootNode);

                    return; // Exit the method as the book has been updated
                }
            }

            // Handle the case where the book was not found
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Book Not Found");
            alert.setHeaderText("The book could not be found in the JSON file.");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error Updating Book");
            errorAlert.setHeaderText("An error occurred while updating the book");
            errorAlert.setContentText("Please try again or check the logs for details.");
            errorAlert.showAndWait();
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
        Button acceptButton = new Button("Accept");
        acceptButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #ffffff; -fx-border-radius: 5px;");
        Button rejectButton = new Button("Reject");
        rejectButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: #ffffff; -fx-border-radius: 5px;");
        acceptButton.setId(bookName);
        rejectButton.setId(bookName);
        acceptButton.setOnAction(this::accepteButtonClicked);
        rejectButton.setOnAction(this::rejactButtonClicked);
        buttonBox.getChildren().addAll(acceptButton, rejectButton);

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
            JsonNode loansNode = rootNode.get("loans");

            for (JsonNode loanNode : loansNode) {
                JsonNode bookNode = loanNode.get("book");
                String title = bookNode.get("title").asText();
                JsonNode bookNodestudent = loanNode.get("student");

                String studant = bookNodestudent.get("lastName").asText();  // You need to decide how to handle author information in the loan structure
                int loanDuration = loanNode.get("duration").asInt();  // Assuming "duration" is the loan duration field
                 boolean avilabelLabel = loanNode.get("avilable").asBoolean();


                Pane bookPane = createBookPane(title, studant, loanDuration ,avilabelLabel);
                bookPaneContainerAdmin.getChildren().add(bookPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void gotoListAccept() throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("AdminBook.fxml");


    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadBooksFromJson();
    }
}
