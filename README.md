# JavaFX Calculator

A simple desktop calculator application built using Java and JavaFX.

## Description
This project is a basic calculator that performs standard arithmetic operations, as well as square root and exponentiation.

## Features
* **Basic Arithmetic:** Addition, subtraction, multiplication and division.
* **Other operations:** Square root and exponentiation.
* **Input:**
    * Clickable buttons for digits and operations.
    * Keyboard support for digits (0 - 9), decimal points (.), operators (+, -, \*, /, ^), equals (=), clear (C or Esc), delete (Del or Backspace).
* **Display:** Shows inputs and results clearly.

## Requirements
* Java Development Kit (JDK) version 17 or later.
* JavaFX version 21.0.7 or higher.
* An IDE like Eclipse, IntelliJ IDEA or VS code.

## How to run

### Option 1
1. Clone the repo
2. Build the project with your preferred IDE
3. Run the application

### Option 2
1. Install Java 17 or higher. 
2. Download JavaFX 21.0.7 or later. Make sure to unzip and store the files in your preferred directory.
3. Download the `Calculator_v1.0.0.jar` via this [link](https://drive.google.com/file/d/1TfnjrFK0Q-e7FENUnJihYUBuDsn1dnRC/view?usp=sharing).
4. Open your command prompt and navigate to the directory where the jar file is located.
5. Type in the following command:
```
java --module-path /path/to/your/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.media,javafx.graphics -jar Calculator_v1.0.0.jar
```

## License
[MIT LICENSE](./LICENSE)
