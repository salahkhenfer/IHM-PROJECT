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
import java.util.ResourceBundle;

public class Student implements Initializable {

    @FXML
    private VBox bookPaneContainerAdmin;

    private final ArrayList<Book> bookList = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectNode rootNode = objectMapper.createObjectNode();
    private final ArrayNode loansNode = rootNode.putArray("loans");
    private int loanCounter = 1; // Initialize the loan counter

    public void gotoListAccept() throws IOException {
        gotoListAcceptCheck();
    }

    private void gotoListAcceptCheck() throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("accepteBook.fxml");
    }

    @FXML
    private void logOut() throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("hello-view.fxml");
    }

    private Book findBookByTitle(String title) {
        for (Book book : bookList) {
            if (book.getTitle().equals(title)) {
                return book;
            }
        }
        return null; // Book not found
    }

    @FXML
    private void borrowButtonClicked(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String bookTitle = sourceButton.getId();

        // Assuming you have the corresponding Book object for the clicked button
        Book selectedBook = findBookByTitle(bookTitle);

        if (selectedBook != null && selectedBook.isAvailable()) {
            // Create loan information
            ObjectNode loanNode = objectMapper.createObjectNode();
            loanNode.put("loanNumber", generateLoanNumber());
            loanNode.put("available", true);
            loanNode.put("duration", 10); // Get the duration from the TextField

            // Create student information
            ObjectNode studentNode = objectMapper.createObjectNode();
            studentNode.put("studentNumber", 101); // Replace with the actual student number
            studentNode.put("lastName", "YourLastName"); // Replace with the actual last name
            studentNode.put("firstName", "YourFirstName"); // Replace with the actual first name

            // Create book information
            ObjectNode bookNode = objectMapper.createObjectNode();
            bookNode.put("serialNumber", generateSerialNumber());
            bookNode.put("title", selectedBook.getTitle());

            // Add student and book information to the loan node
            loanNode.set("student", studentNode);
            loanNode.set("book", bookNode);

            // Add the loan node to the loans array
            loansNode.add(loanNode);

            // Update the JSON file
            saveToJsonFile(rootNode, "src/data.json");
        }
    }

    private int generateLoanNumber() {
        return loanCounter++;
    }

    private int generateSerialNumber() {
        // Implement your own method to generate unique serial numbers
        return 0;
    }

    private void saveToJsonFile(ObjectNode rootNode, String filePath) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Pane createBookPane(String bookName, String author, int numberOfCopies) {
        VBox pane = new VBox();  // Use VBox for vertical layout
        pane.setPadding(new Insets(10));  // Add padding for breathing room
        pane.setSpacing(8);  // Add spacing between elements
        pane.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; -fx-border-width: 1px;");  // Set background and border

        // Create text elements with a distinct font and color
        Label bookLabel = new Label(bookName);  // Use Label for better text handling
        bookLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        bookLabel.setTextFill(Color.DARKBLUE);
        Label authorLabel = new Label("by: " + author);
        authorLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 16));
        Label copiesLabel = new Label("Copies: " + numberOfCopies);
        copiesLabel.setFont(Font.font("Arial", 14));

        // Create input for duration
        TextField durationInput = new TextField();
        durationInput.setPromptText("Enter Duration");
        durationInput.setMaxWidth(120);

        // Apply some styling to the TextField
        durationInput.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 5px; -fx-padding: 5px;");

        // Create buttons with a modern, rounded style
        HBox buttonBox = new HBox(10);  // Group buttons together
        buttonBox.setAlignment(Pos.CENTER_RIGHT);  // Align buttons to the right
        Button borrowButton = new Button("Borrow request");
        borrowButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #ffffff; -fx-border-radius: 5px;");
        borrowButton.setId(bookName);

        borrowButton.setOnAction(this::borrowButtonClicked);

        buttonBox.getChildren().addAll(borrowButton);

        // Arrange elements vertically
        pane.getChildren().addAll(bookLabel, authorLabel, copiesLabel, durationInput, buttonBox);

        return pane;
    }

    private void loadBooksFromJson() {
        try {
            JsonNode rootNode = objectMapper.readTree(new File("src/data.json"));
            JsonNode booksNode = rootNode.get("books");

            for (JsonNode bookNode : booksNode) {
                String title = bookNode.get("title").asText();
                String author = bookNode.get("authorName").asText();
                int numberOfCopies = bookNode.get("availableCopies").asInt();
                Book bookItem = new Book(title, author, numberOfCopies, true);
                bookList.add(bookItem);

                Pane bookPane = createBookPane(title, author, numberOfCopies);
                bookPaneContainerAdmin.getChildren().add(bookPane);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadBooksFromJson();
    }

    public static class Book {
        private final String title;
        private final String author;
        private final int numberOfCopies;
        private final boolean available;

        public Book(String title, String author, int numberOfCopies, boolean available) {
            if (numberOfCopies < 0) {
                throw new IllegalArgumentException("Number of copies cannot be negative.");
            }

            this.title = title;
            this.author = author;
            this.numberOfCopies = numberOfCopies;
            this.available = available;
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

        public boolean isAvailable() {
            return available;
        }
    }
}
