package com.example.demoihm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AdminBook implements Initializable {

    @FXML
    private VBox bookPaneContainerAdmin;

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

    private final ArrayList<Book> bookList = new ArrayList<>();

    public void gotoListAccept(ActionEvent event) throws IOException {
        gotoListAcceptCheck();
    }

    private void gotoListAcceptCheck() throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("accepteBook.fxml");
    }
    @FXML
    private void LogOut() throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("hello-view.fxml");
    }
    

//    private Pane createBookPane(String bookName, String author, int numberOfCopies) {
//        Pane pane = new Pane();
//        pane.setPrefHeight(40.0);
//        pane.setPrefWidth(1000.0);
//
//        Button editButton = new Button("Edit");
//        editButton.setLayoutX(360.0);
//        editButton.setLayoutY(5.0);
//        editButton.setMnemonicParsing(false);
//        editButton.setPrefHeight(30.0);
//        editButton.setPrefWidth(60.0);
//        editButton.setStyle("-fx-background-color: #0720ff;");
//        editButton.setTextFill(Color.WHITE);
//        editButton.setId(bookName);
//        editButton.setOnAction(this::editButtonClicked);
//
//        Button deleteButton = new Button("Delete");
//        deleteButton.setLayoutX(437.0);
//        deleteButton.setLayoutY(5.0);
//        deleteButton.setMnemonicParsing(false);
//        deleteButton.setPrefHeight(30.0);
//        deleteButton.setPrefWidth(60.0);
//        deleteButton.setStyle("-fx-background-color: f80000;");
//        deleteButton.setTextFill(Color.WHITE);
//        deleteButton.setId(bookName);
//        deleteButton.setOnAction(this::deleteButtonClicked);
//
//        Text bookText = new Text(69.0, 31.0, bookName);
//        bookText.setStrokeType(StrokeType.OUTSIDE);
//        bookText.setStrokeWidth(0.0);
//        bookText.setFont(new Font(23.0));
//
//        Text authorText = new Text(200.0, 31.0, "by " + author);
//        authorText.setStrokeType(StrokeType.OUTSIDE);
//        authorText.setStrokeWidth(0.0);
//        authorText.setFont(new Font(18.0));
//
//
//        Text copiesText = new Text(400.0, 31.0, "Copies: " + numberOfCopies);
//        copiesText.setStrokeType(StrokeType.OUTSIDE);
//        pane.setPrefWidth(500);
//        copiesText.setFont(new Font(18.0));
//
//        Separator separator = new Separator();
//        separator.setLayoutX(30.0);
//        separator.setLayoutY(37.0);
//        separator.setPrefHeight(20.0);
//        separator.setPrefWidth(516.0);
//
//        pane.getChildren().addAll(editButton, deleteButton, bookText, separator);
//
//        return pane;
//    }


    private void gotoListAccept() throws IOException {
        HelloApplication m = new HelloApplication();
        m.changeScene("accepteBook.fxml");


    }
private Pane createBookPane(String bookName, String author, int numberOfCopies) {
    VBox pane = new VBox();  // Use VBox for vertical layout
    pane.setPadding(new Insets(10));  // Add padding for breathing room
    pane.setSpacing(8);  // Add spacing between elements
    pane.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; -fx-border-width: 1px;");  // Set background and border

    // Create buttons with a modern, rounded style
    HBox buttonBox = new HBox(10);  // Group buttons together
    buttonBox.setAlignment(Pos.CENTER_RIGHT);  // Align buttons to the right
    Button editButton = new Button("Edit");
    editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: #ffffff; -fx-border-radius: 5px;");
    Button deleteButton = new Button("Delete");
    deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: #ffffff; -fx-border-radius: 5px;");
    editButton.setId(bookName);
    deleteButton.setId(bookName);
    editButton.setOnAction(this::editButtonClicked);
    deleteButton.setOnAction(this::deleteButtonClicked);
    buttonBox.getChildren().addAll(editButton, deleteButton);

    // Create text elements with a distinct font and color
    Label bookLabel = new Label(bookName);  // Use Label for better text handling
    bookLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
    bookLabel.setTextFill(Color.DARKBLUE);
    Label authorLabel = new Label("by : " + author);
    authorLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 16));
    Label copiesLabel = new Label("Copies: " + numberOfCopies);
    copiesLabel.setFont(Font.font("Arial", 14));

    // Arrange elements vertically
    pane.getChildren().addAll(bookLabel, authorLabel, copiesLabel, buttonBox);

    return pane;
}

    @FXML
    private void AddBook(ActionEvent event) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Book Information");
        dialog.setHeaderText("Please enter book information:");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        TextField bookNameField = new TextField();
        bookNameField.setPromptText("Book Name");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        TextField copiesField = new TextField();
        copiesField.setPromptText("Number of Copies");

        dialog.getDialogPane().setContent(new VBox(10, bookNameField, authorField, copiesField));

        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        bookNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue.trim().isEmpty());
        });

        Platform.runLater(bookNameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Pair<>(bookNameField.getText(), authorField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            String bookName = pair.getKey();
            String author = pair.getValue();
            int numberOfCopies = Integer.parseInt(copiesField.getText());
            Book newBook = new Book(bookName, author, numberOfCopies, true);
            bookList.add(newBook);
            addBookToJson(newBook);
            Pane bookPane = createBookPane(bookName, author, numberOfCopies);
            bookPaneContainerAdmin.getChildren().add(bookPane);
        });
    }

    private void addBookToJson(Book book) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read existing JSON file
            JsonNode rootNode = objectMapper.readTree(new File("src/data.json"));

            // Extract the "books" array from the JsonNode
            ArrayNode booksNode = (ArrayNode) rootNode.get("books");

            // Create a new JSON object for the new book
            ObjectNode newBookNode = objectMapper.createObjectNode();
            newBookNode.put("title", book.getTitle());
            newBookNode.put("authorName", book.getAuthor());
            newBookNode.put("availableCopies", book.getNumberOfCopies());

            // Add the new book to the "books" array
            booksNode.add(newBookNode);

            // Write the updated JsonNode back to the JSON file
            objectMapper.writeValue(new File("src/data.json"), rootNode);
        } catch (IOException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }

    private void loadBooksFromJson() {
        ObjectMapper objectMapper = new ObjectMapper();

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

    @FXML
//    private void deleteButtonClicked(ActionEvent event) {
//        Button deleteButton = (Button) event.getSource();
//        Book selectedBook = getSelectedBook(deleteButton.getId());
//
//        if (selectedBook != null) {
//            removeBookFromUI(selectedBook);
//            removeBookFromJson(selectedBook);
//        }
//    }


    private  void  loadData() throws IOException {

        bookPaneContainerAdmin.getChildren().clear();
        bookList.clear();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(new File("src/data.json"));
        JsonNode booksNode = rootNode.get("books");

        for (JsonNode bookNode : booksNode) {
            String title = bookNode.get("title").asText();
            String author = bookNode.get("authorName").asText();
            int numberOfCopies = bookNode.get("availableCopies").asInt();
            Book bookItem = new Book(title, author, numberOfCopies, true);

            Pane bookPane = createBookPane(title, author, numberOfCopies);
            bookPaneContainerAdmin.getChildren().add(bookPane);
            bookList.add(bookItem);
        }

    }
    private void deleteButtonClicked(ActionEvent event) {
        // Ensure null safety:
        Button deleteButton = (Button) event.getSource();
        if (deleteButton == null) {
            return;  // Handle the unexpected case
        }

        String bookId = deleteButton.getId();
        Book selectedBook = getSelectedBook(bookId);

        if (selectedBook != null) {
            try {
                removeBookFromUI(selectedBook);
                removeBookFromJson(selectedBook);

                // Clear existing books before re-adding updated ones:
                loadData();


            } catch (Exception e) {
                // Handle potential errors gracefully:
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error Deleting Book");
                errorAlert.setHeaderText("An error occurred while deleting the book");
                errorAlert.setContentText("Please try again or check the logs for details.");
                errorAlert.showAndWait();
                // Optionally, log the error for debugging:
                System.err.println("Error deleting book: " + e.getMessage());
            }
        } else {
            // Inform the user if no book was found:
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Book Not Found");
            alert.setHeaderText("No book found with the given ID");
            alert.showAndWait();
        }
    }

    private void removeBookFromUI(Book book) {
        Iterator<Node> iterator = bookPaneContainerAdmin.getChildren().iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                if (matchesBookPane(pane, book)) {
                    iterator.remove(); // Safely remove from the iterator
                    break;  // No need to continue iterating
                }
            }
        }
    }

    private boolean matchesBookPane(Pane pane, Book book) {
        // Adjust for Label:
        Label bookLabel = (Label) pane.getChildren().get(2);  // Assuming Label is at index 2
        return bookLabel.getText().equals(book.getTitle());
    }


    private void removeBookFromJson(Book book) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read existing JSON file
            File jsonFile = new File("src/data.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);

            // Extract the "books" array from the JsonNode
            ArrayNode booksNode = (ArrayNode) rootNode.get("books");

            // Remove the book directly using ArrayNode methods
            Iterator<JsonNode> iterator = booksNode.iterator();
            while (iterator.hasNext()) {
                JsonNode bookNode = iterator.next();
                if (matchesBookJson(bookNode, book)) {
                    iterator.remove(); // Efficiently remove the matching book node
                    break; // Assuming there is at most one matching book
                }
            }

            // Write the updated JsonNode back to the JSON file
            objectMapper.writeValue(jsonFile, rootNode);
        } catch (IOException e) {
            // Handle the exception more gracefully:
            System.err.println("Error removing book from JSON: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Saving Changes");
            alert.setHeaderText("Failed to remove book from data storage");
            alert.setContentText("Please try again or contact support.");
            alert.showAndWait();
            // Optionally, log the error for debugging:
            e.printStackTrace();
        }
    }

    private boolean matchesBookJson(JsonNode bookNode, Book book) {
        return bookNode.get("title").asText().equals(book.getTitle());
    }

    private Book getSelectedBook(String bookId) {
        for (Book book : bookList) {
            if (book.getTitle().equals(bookId)) {
                return book;
            }
        }
        return null;
    }



    // edit book
    @FXML
    private void editButtonClicked(ActionEvent event) {
        Button editButton = (Button) event.getSource();
        Book selectedBook = getSelectedBook(editButton.getId());

        if (selectedBook != null) {
            // Create the custom edit dialog
            Dialog<Pair<String, String>> dialog = createEditDialog(selectedBook);

            // Show the dialog and wait for the user's input
            Optional<Pair<String, String>> result = dialog.showAndWait();

            // Process the input if the Confirm button is clicked
            result.ifPresent(pair -> {
                // Update book information
                String newTitle = pair.getKey();
                String newAuthor = pair.getValue();
                int newNumberOfCopies = selectedBook.getNumberOfCopies(); // You can add a field for the number of copies in the edit dialog

                // Update the book in the U
                // Update the book in the JSON file
                updateBookInJson(selectedBook, newTitle, newAuthor, newNumberOfCopies);
                try {
                    loadData();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private Dialog<Pair<String, String>> createEditDialog(Book selectedBook) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Book");
        dialog.setHeaderText("Please edit book information:");

        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        TextField bookNameField = new TextField(selectedBook.getTitle());
        bookNameField.setPromptText("Book Name");
        TextField authorField = new TextField(selectedBook.getAuthor());
        authorField.setPromptText("Author");
        // Add a field for the number of copies if necessary
        TextField copiesField = new TextField(String.valueOf(selectedBook.getNumberOfCopies()));
        copiesField.setPromptText("Number of Copies");

        dialog.getDialogPane().setContent(new VBox(10, bookNameField, authorField, copiesField));

        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);

        // Bind the confirmation button's disable property to the text fields' values
        confirmButton.disableProperty().bind(
                bookNameField.textProperty().isEmpty().or(authorField.textProperty().isEmpty())
        );

        Platform.runLater(bookNameField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Pair<>(bookNameField.getText(), authorField.getText());
            }
            return null;
        });

        return dialog;
    }


    private void updateBookInJson(Book selectedBook, String newTitle, String newAuthor, int newNumberOfCopies) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read JSON file and parse it into a JsonNode
            File jsonFile = new File("src/data.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);

            // Extract the "books" array from the JsonNode
            ArrayNode booksNode = (ArrayNode) rootNode.get("books");

            // Iterate over each book in the "books" array
            for (JsonNode bookNode : booksNode) {
                if (matchesBookJson(bookNode, selectedBook)) {
                    // Update the book information in the JSON array
                    ((ObjectNode) bookNode).put("title", newTitle);
                    ((ObjectNode) bookNode).put("authorName", newAuthor);
                    ((ObjectNode) bookNode).put("availableCopies", newNumberOfCopies);

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
            // Handle exception gracefully
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error Updating Book");
            errorAlert.setHeaderText("An error occurred while updating the book");
            errorAlert.setContentText("Please try again or check the logs for details.");
            errorAlert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadBooksFromJson();
    }
}
