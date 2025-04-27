package application;

import javax.swing.JSpinner.NumberEditor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class Controller {
    @FXML
    private TextField resultTextField;
    @FXML
    private AnchorPane calculatorPane;

    private double number1 = 0;
    private double number2 = 0;
    private String operatorString = "";
    private boolean start = true;
    private boolean operatorPressed = false;

    @FXML
    public void initialize() {
        calculatorPane.setOnKeyPressed(this::handleKeyPress);
        resultTextField.setOnKeyPressed(this::handleKeyPress);
    }

    @FXML
    public void processDigit(ActionEvent event) {
        // If starting fresh or right after an operator, clear the display first
        if (start || operatorPressed) {
            resultTextField.setText("");
            start = false;
            operatorPressed = false;
        }

        String value = ((Button) event.getSource()).getText();
        String currentText = resultTextField.getText();

        // prevent multiple decimal points
        if (value.equals(".") && currentText.contains(".")) {
            return;
        }
        
        // prevent leading zeros unless it is the only digit or decimal
        if (currentText.equals("0") && !value.equals(".")) {
            resultTextField.setText(value);
        } else {
            resultTextField.setText(currentText + value);
        }
    }

    @FXML
    public void processClear(ActionEvent event) {
        resultTextField.setText("0");  // reset display to 0
        start = true;   // start fresh
        operatorString = "";   // no operator
        number1 = 0;    // reset numbers
        number2 = 0;
        operatorPressed = false;   // clear operator cleared flag
    }

    @FXML
    public void processOperator(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();    // this could be +, -, * or / or ^
        double currentDisplayValue = 0;

        // if operator is not the last button pressed,
        // parse the number shown on display
        if (!operatorPressed) {
            try {
                currentDisplayValue = Double.parseDouble(resultTextField.getText());
            } catch (NumberFormatException e) {
                handleError();
                return;
            }
        }

        // scenario 1: chained operation (e.g., 1 + 2 + ...)
        if (!operatorString.isEmpty() && !operatorPressed) {
            number2 = currentDisplayValue;   // set the second number as the current display value
            calculate();
        }
        // scenario 2: first operator after start and equals
        else if (!operatorPressed) {
            number1 = currentDisplayValue;
        }

        operatorString = value;
        operatorPressed = true;
        start = false;   // ensure we're not in the initial start state
    }

    @FXML
    public void processEquals(ActionEvent event) {
        if (operatorString.isEmpty() || operatorPressed) {
            return;   // do nothing if no operator or operator is the last key pressed
        }

        try {
            number2 = Double.parseDouble(resultTextField.getText());
            calculate();
            operatorString = "";   // clear operator for the next calculation
            start = true;   // go back to start state
            operatorPressed = false;
        } catch (NumberFormatException e) {
            handleError();
        }
    }

    @FXML 
    public void processSqrtOperator(ActionEvent event) {
        // String value = ((Button) event.getSource()).getText();
        double number;

        try {
            number = Double.parseDouble(resultTextField.getText());

            if (number < 0) {
                handleError("Error: neg sqrt");
                return;
            }
            number = Math.sqrt(number);

            displayResult(number);
            start = true;
            operatorString = "";
            operatorPressed = false;
        } catch (NumberFormatException e) {
            handleError();
        }
    }

    @FXML
    public void processDeleteLastDigit(ActionEvent event) {
        if (start || operatorPressed) {
            return;
        }

        String currentText = resultTextField.getText();
        if (currentText.length() > 0 && !currentText.contains("Error")) {
            // remove the last character
            currentText = currentText.substring(0, currentText.length() - 1);

            // if the string becomes empty or just a minus sign, reset result text field to 0
            if (currentText.isEmpty() || currentText.equals("-")) {
                resultTextField.setText("0");
                start = true;
            } else {
                resultTextField.setText(currentText);
            }
        } else if (currentText.isEmpty()) {
            resultTextField.setText("0");
            // start = true;
        }
    }

    private void calculate() {
        // calculate can only take place after an operator is pressed.
        if (operatorString.isEmpty()) {
            return;
        }

        try {
            switch (operatorString) {
                case "+":
                    number1 = number1 + number2;
                    break;

                case "-":
                    number1 = number1 - number2;
                    break;

                case "x":
                    number1 = number1 * number2;
                    break;
                
                case "/":
                    if (number2 == 0) {
                        handleError("Error: division by zero");
                        return;
                    }
                    number1 = number1 / number2;
                    break;
                
                case "^":
                    number1 = Math.pow(number1, number2);
                    break;
            
                default:
                    break;
            }
            // check for NaN or infinity results
            if (Double.isNaN(number1) || Double.isInfinite(number1)) {
                handleError("Error: NaN or inf");
            } else {
                displayResult(number1);
            }
        } catch (Exception e) {
            handleError();
        }
    }

    private void displayResult(double result) {
        // display integer without decimal point if it's a whole number
        // add a small epsilon for floating point comparison
        if (Math.abs(result - (long) result) < 1e-9) {
            resultTextField.setText(String.format("%d", (long) result));
        } else {
            // remove trailing zeros and decimal points
            resultTextField.setText(String.format("%.8f", result).replaceAll("\\.?0*$", ""));
        }
    }

    private void handleKeyPress(KeyEvent event) {
        String key = event.getText();
        KeyCode keyCode = event.getCode();   // key code for (non-character keys)

        // handle digit keys (0-9) and decimal point
        if (key.matches("[0-9.]")) {
            Button digitButton = new Button(key);
            // digitButton.setText(key);
            processDigit(new ActionEvent(digitButton, null));
        } 
        else if (key.equals("+")) 
            simulateOperator("+");
            
        else if (key.equals("-")) 
            simulateOperator("-");
            
        else if (key.equals("*") || key.equalsIgnoreCase("x"))
            simulateOperator("x");
        
        else if (key.equals("/")) 
            simulateOperator("/");
            
        else if (key.equals("^")) 
            simulateOperator("^");
        
        // handles equals (Enter key or =) 
        else if (key.equals("=") || keyCode == KeyCode.ENTER) {
            Button equalsButton = new Button("=");
            processEquals(new ActionEvent(equalsButton, null));
        }
        // handle clear (Esc key or C)
        else if (keyCode == KeyCode.ESCAPE || key.equalsIgnoreCase("c")) {
            Button clearButton = new Button("C");
            processClear(new ActionEvent(clearButton, null));
        }
        // handle delete (DEL or BACKSPACE)
        else if (keyCode == KeyCode.BACK_SPACE || keyCode == KeyCode.DELETE) {
            Button deleteButton = new Button("Del");
            processDeleteLastDigit(new ActionEvent(deleteButton, null));
        }

        event.consume();   // consume the event after it was handled
    }

    private void simulateOperator(String operatorSymbol) {
        Button operatorButton = new Button();
        operatorButton.setText(operatorSymbol);
        processOperator(new ActionEvent(operatorButton, null));
    }

    private void handleError(String message) {
        resultTextField.setText(message);
        start = true;
        operatorString = "";
        operatorPressed = false;
    }

    private void handleError() {
        handleError("Error");
    }
}
