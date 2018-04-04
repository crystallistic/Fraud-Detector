/*
 * Coding Challenge 6: fraud detection pt. 2
 *
 * Author: Mai Ngo
 * Class: CSC 220
 *
 * Program Description: Program flags potentially fraudulent transactions in the State of Oklahoma Purchase
 * Card (PCard) Dataset for Fiscal Year 2014. This dataset contains information on purchases made through
 * the purchase card programs administered by the state and higher ed institutions.
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;


/*
 * Class FraudDetector reads in the dataset from the command line
 * and detect potentially fraudulent transactions.
 */

public class FraudDetector {




    public static void main(String[] args) {

        // init variables
        String csvFile = null; // the dataset's file path
        BufferedReader br = null; // BufferedReader to read file
        String line = ""; // stores each line
        String sep = ",";
        LinkedList<String> fileData = new LinkedList<String>();

        // try-catch block for reading in the file and storing it line by line in fileData
        try {
            csvFile = args[0];
            br = new BufferedReader(new FileReader(csvFile));

            while ((line = br.readLine()) != null) {
                fileData.add(line);
            }
            br.close();
        } catch (ArrayIndexOutOfBoundsException e) { // thrown when no file name is given
            System.out.println("Error: Please input file name on the command line as an argument.");
        } catch (FileNotFoundException e) { // thrown when file cannot be found
            System.out.println("Error: File not found.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



    }
}


