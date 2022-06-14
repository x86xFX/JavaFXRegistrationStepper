package controllers;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.MFXStepper.MFXStepperEvent;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Validated;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    private final MFXTextField registrationEmail;
    private final MFXTextField loginEmail;
    private final MFXPasswordField registrationPasswordField;
    private final MFXPasswordField loginPassword;
    private final MFXTextField firstName;
    private final MFXTextField lastName;
    private final MFXComboBox<String> genderCombo;
    private final MFXCheckbox checkbox;
    private MFXTextField genderLabel2;
    @FXML
    private MFXStepper RegistrationStepper, LoginStepper;
    @FXML
    private StackPane registrationPane, loginPane;
    @FXML
    private MFXButton registrationBtn, loginBtn;
    @FXML
    private Label quoteArea;

    public RegistrationController() {
        registrationEmail = new MFXTextField();
        loginEmail = new MFXTextField();
        registrationPasswordField = new MFXPasswordField();
        loginPassword = new MFXPasswordField();
        firstName = new MFXTextField();
        lastName = new MFXTextField();
        genderCombo = new MFXComboBox<>();
        checkbox = new MFXCheckbox("Confirm Data?");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registrationEmail.setPromptText("Email...");
        registrationEmail.getValidator().constraint("The email must be at least 6 characters long", registrationEmail.textProperty().length().greaterThanOrEqualTo(6));
        registrationEmail.setLeadingIcon(new MFXIconWrapper("mfx-user", 16, Color.web("#4D4D4D"), 24));

        registrationPasswordField.getValidator().constraint("The password must be at least 8 characters long", registrationPasswordField.textProperty().length().greaterThanOrEqualTo(8));
        registrationPasswordField.setPromptText("Password...");

        firstName.setPromptText("First Name...");
        firstName.getValidator().constraint("Please enter first name", firstName.textProperty().isNotEmpty());

        lastName.setPromptText("Last Name...");
        lastName.getValidator().constraint("Please enter last name", lastName.textProperty().isNotEmpty());

        genderCombo.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));

        List<MFXStepperToggle> stepperToggles = createSteps();
        RegistrationStepper.getStepperToggles().addAll(stepperToggles);

        //login stepper
        loginEmail.setPromptText("Email...");
        loginEmail.getValidator().constraint("The email must be at least 6 characters long", loginEmail.textProperty().length().greaterThanOrEqualTo(6));
        loginEmail.setLeadingIcon(new MFXIconWrapper("mfx-user", 16, Color.web("#4D4D4D"), 24));

        loginPassword.getValidator().constraint("The password must be at least 8 characters long", loginPassword.textProperty().length().greaterThanOrEqualTo(8));
        loginPassword.setPromptText("Password...");

        List<MFXStepperToggle> loginStepperToggles = createLoginSteps();
        LoginStepper.getStepperToggles().addAll(loginStepperToggles);

        //initialize signPane, loginPane
        registrationBtn.setVisible(false);
        loginPane.setVisible(false);

        //quote thread
        new quoteGenerator().setQuotes();

        //Access Login details
        LoginStepper.setOnLastNext(event -> getLoginInputs());
    }

    private List<MFXStepperToggle> createSteps() {
        MFXStepperToggle step1 = new MFXStepperToggle("Step 1", new MFXFontIcon("mfx-lock", 16, Color.web("#f1c40f")));
        VBox step1Box = new VBox(20, wrapNodeForValidation(registrationEmail), wrapNodeForValidation(registrationPasswordField));
        step1Box.setAlignment(Pos.CENTER);
        step1.setContent(step1Box);
        step1.getValidator().dependsOn(registrationEmail.getValidator()).dependsOn(registrationPasswordField.getValidator());

        MFXStepperToggle step2 = new MFXStepperToggle("Step 2", new MFXFontIcon("mfx-user", 16, Color.web("#49a6d7")));
        VBox step2Box = new VBox(20, firstName, lastName, genderCombo);
        step2Box.setAlignment(Pos.CENTER);
        step2.setContent(step2Box);
        step2.getValidator().dependsOn(firstName.getValidator()).dependsOn(lastName.getValidator()).dependsOn(genderCombo.getValidator());

        MFXStepperToggle step3 = new MFXStepperToggle("Step 3", new MFXFontIcon("mfx-variant7-mark", 16, Color.web("#85CB33")));
        Node step3Grid = createGrid();
        step3.setContent(step3Grid);
        step3.getValidator().constraint("Data must be confirmed", checkbox.selectedProperty());

        return List.of(step1, step2, step3);
    }

    private List<MFXStepperToggle> createLoginSteps() {
        MFXStepperToggle loginStep1 = new MFXStepperToggle("Login Step1", new MFXFontIcon("mfx-lock", 16, Color.web("#f1c40f")));
        VBox loginStepBox1 = new VBox(20, wrapNodeForValidation(loginEmail), wrapNodeForValidation(loginPassword));
        loginStepBox1.setAlignment(Pos.CENTER);
        loginStep1.setContent(loginStepBox1);
        loginStep1.getValidator().dependsOn(loginEmail.getValidator()).dependsOn(loginPassword.getValidator());

        return List.of(loginStep1);
    }

    private <T extends Node & Validated> Node wrapNodeForValidation(T node) {
        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setManaged(false);
        RegistrationStepper.addEventHandler(MFXStepperEvent.VALIDATION_FAILED_EVENT, event -> {
            MFXValidator validator = node.getValidator();
            List<Constraint> validate = validator.validate();
            if (!validate.isEmpty()) {
                errorLabel.setText(validate.get(0).getMessage());
            }
        });
        RegistrationStepper.addEventHandler(MFXStepperEvent.NEXT_EVENT, event -> errorLabel.setText(""));
        VBox wrap = new VBox(9, node, errorLabel) {
            @Override
            protected void layoutChildren() {
                super.layoutChildren();

                double x = node.getBoundsInParent().getMinX();
                double y = node.getBoundsInParent().getMaxY() + getSpacing();
                double width = getWidth();
                double height = errorLabel.prefHeight(-1);
                errorLabel.resizeRelocate(x, y, width, height);
            }

            @Override
            protected double computePrefHeight(double width) {
                return super.computePrefHeight(width) + errorLabel.getHeight() + getSpacing();
            }
        };
        wrap.setAlignment(Pos.CENTER);
        return wrap;
    }

    private Node createGrid() {
        MFXTextField emailLabel1 = createLabel("Email: ");
        MFXTextField usernameLabel2 = createLabel("");
        usernameLabel2.textProperty().bind(registrationEmail.textProperty());

        MFXTextField firstNameLabel1 = createLabel("First Name: ");
        MFXTextField firstNameLabel2 = createLabel("");
        firstNameLabel2.textProperty().bind(firstName.textProperty());

        MFXTextField lastNameLabel1 = createLabel("Last Name: ");
        MFXTextField lastNameLabel2 = createLabel("");
        lastNameLabel2.textProperty().bind(lastName.textProperty());

        MFXTextField genderLabel1 = createLabel("Gender: ");
        genderLabel2 = createLabel("");
        genderLabel2.textProperty().bind(Bindings.createStringBinding(
                () -> genderCombo.getValue() != null ? genderCombo.getValue() : "Can't Say",
                genderCombo.valueProperty()
        ));

        emailLabel1.getStyleClass().add("header-label");
        firstNameLabel1.getStyleClass().add("header-label");
        lastNameLabel1.getStyleClass().add("header-label");
        genderLabel1.getStyleClass().add("header-label");

        MFXTextField completedLabel = MFXTextField.asLabel("Completed!");
        completedLabel.getStyleClass().add("completed-label");
        completedLabel.setAlignment(Pos.CENTER);
        Label completedRedirectLabel = new Label("Redirecting...");
        completedLabel.getStyleClass().add("completed-label");

        HBox b1 = new HBox(emailLabel1, usernameLabel2);
        HBox b2 = new HBox(firstNameLabel1, firstNameLabel2);
        HBox b3 = new HBox(lastNameLabel1, lastNameLabel2);
        HBox b4 = new HBox(genderLabel1, genderLabel2);

        b1.setMaxWidth(Region.USE_PREF_SIZE);
        b2.setMaxWidth(Region.USE_PREF_SIZE);
        b3.setMaxWidth(Region.USE_PREF_SIZE);
        b4.setMaxWidth(Region.USE_PREF_SIZE);

        VBox box = new VBox(10, b1, b2, b3, b4, checkbox);
        box.setAlignment(Pos.CENTER);
        StackPane.setAlignment(box, Pos.CENTER);

        RegistrationStepper.setOnLastNext(event -> {
            box.getChildren().setAll(completedLabel, completedRedirectLabel);
            RegistrationStepper.setMouseTransparent(true);
            getRegistrationInputs();
        });
        RegistrationStepper.setOnBeforePrevious(event -> {
            if (RegistrationStepper.isLastToggle()) {
                checkbox.setSelected(false);
                box.getChildren().setAll(b1, b2, b3, b4, checkbox);
            }
        });

        return box;
    }

    private MFXTextField createLabel(String text) {
        MFXTextField label = MFXTextField.asLabel(text);
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPrefWidth(200);
        label.setMinWidth(Region.USE_PREF_SIZE);
        label.setMaxWidth(Region.USE_PREF_SIZE);
        return label;
    }

    public void openRegistrationPane() {
        registrationPane.setVisible(true);
        registrationBtn.setVisible(false);
        loginPane.setVisible(false);
        loginBtn.setVisible(true);
    }

    public void openLoginPane() {
        loginPane.setVisible(true);
        loginBtn.setVisible(false);
        registrationPane.setVisible(false);
        registrationBtn.setVisible(true);
    }

    class quoteGenerator {
        private int i = 1;
        public void setQuotes() {
            String[] quotes = {"The greatest glory in living lies not in never falling, but in rising every time we fall. -Nelson Mandela",
                    "The way to get started is to quit talking and begin doing. -Walt Disney",
                    "Your time is limited, so don't waste it living someone else's life. Don't be trapped by dogma – which is living with the results of other people's thinking. -Steve Jobs",
                    "If life were predictable it would cease to be life, and be without flavor. -Eleanor Roosevelt",
                    "If you look at what you have in life, you'll always have more. If you look at what you don't have in life, you'll never have enough. -Oprah Winfrey",
                    "If you set your goals ridiculously high and it's a failure, you will fail above everyone else's success. -James Cameron",
                    "Life is what happens when you're busy making other plans. -John Lennon"
            };

            quoteArea.setText(quotes[1]);
            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event ->{
                quoteArea.setText(quotes[i]);
                i ++;
                if (i <= 6) {
                    pause.play();
                } else {
                    quoteArea.setText(quotes[6]);
                }
            });
            pause.play();
        }

    }

    //Input Details
    public void getRegistrationInputs() {
        System.out.println(registrationEmail.getText());
        System.out.println(registrationPasswordField.getText());
        System.out.println(firstName.getText());
        System.out.println(lastName.getText());
        System.out.println(genderLabel2.getText());
    }

    public void getLoginInputs () {
        System.out.println(loginEmail.getText());
        System.out.println(loginPassword.getText());
    }
}