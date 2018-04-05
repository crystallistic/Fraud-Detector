# Fraud Detector

Objective: This program flags potentially fraudulent transactions in the State of Oklahoma Purchase Card (PCard) Dataset for Fiscal Year 2014. This dataset contains information on purchases made through the purchase card programs administered by the state and higher ed institutions.

### Language
Java

### How to Run

Compile, run the program with the dataset file path as a command line argument with the following commands in Terminal:

`javac FraudDetector.java` <br />
`java FraudDetector <FILE-PATH>`

### Program Description

- The program utilizes an ArrayList to store the dataset line by line (and each line is an ArrayList of values). Thought process: I thought that I'd like to use a data structure that facilitates look-up, so looking up/removing elements by index is preferable, since I want to loop through the dataset transaction by transaction. Since arrays in Java are immutable, I decided to use ArrayList for fileData.
- When searching for transactions with infrequently used airlines, I intended to 
    1) find all transactions with airlines and record the index of the transaction and the airline name, 
    2) count the occurrences of all airlines 
    3) pick airlines that occur fewer than 10 times, and record all transactions with these airlines (basically backtracking to get the index of these transactions)

    Since all these operations involve recording key-value pairs, I believe it would be most efficient with the use of HashMap.
- When searching for transactions with pawn shops/casinos/hotels, I searched for the keywords "PAWN", "CASINO" and "HOTEL" in the Merchant Category Code column because after looking through the data, I found that this look up method is usually accurate.


### Author

Mai Ngo

#### Note

The project can be found on [GitHub](https://github.com/crystallistic/Fraud-Detector).
