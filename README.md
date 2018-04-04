# Fraud Detector

Objective: This program flags potentially fraudulent transactions in the State of Oklahoma Purchase Card (PCard) Dataset for Fiscal Year 2014. This dataset contains information on purchases made through the purchase card programs administered by the state and higher ed institutions.

### Language
Java

### How to Run

Make sure that the dataset is in the same directory. Compile, run the program with the dataset as a command line argument with the following commands in Terminal:

`javac FraudDetector.java` <br />
`java FraudDetector <FILE-PATH>`

### Program Description

- The program utilizes a LinkedList<String> to store the dataset line by line. Thought process: I thought that I'd like to use a data structure that facilitates look-up, so looking up/removing elements by index is preferable, since I want to loop through the dataset transaction by transaction. Since arrays in Java are immutable, I decided to use LinkedList. 

### Author

Mai Ngo

#### Note

The project can be found on [GitHub](https://github.com/crystallistic/Fraud-Detector).