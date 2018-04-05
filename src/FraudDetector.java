/*
 * Coding Challenge 6: fraud detection pt. 2
 * Class: CSC 220
 *
 * @author Mai Ngo
 * @version April 5 2018
 *
 * Program Description: Program flags potentially fraudulent transactions in the State of Oklahoma Purchase
 * Card (PCard) Dataset for Fiscal Year 2014. This dataset contains information on purchases made through
 * the purchase card programs administered by the state and higher ed institutions.
 */


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/*
 * Class FraudDetector reads in the dataset from the command line
 * and detect potentially fraudulent transactions.
 */

public class FraudDetector {

    /*
     * Method to parse values in one line in the csv file and store them in an ArrayList.
     * This method solves the problem of having quotes and commas in some String values
     * that the .split() method doesn't account for.
     *
     * @param line One line (one transaction) in the .csv file
     * @return an ArrayList<String> containing the values in this line as elements
     */
    public static ArrayList<String> parseLine(String line) {
        char sep = ',';
        char quote = '"';
        ArrayList<String> result = new ArrayList<String>();
        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        char[] chars = line.toCharArray();

        for (char ch : chars) {
            // if current value is in quotes

             if (inQuotes) {
                startCollectChar = true; // start collecting characters
                if (ch == quote) { // if we run into another quote
                    inQuotes = false; // reading in quotes
                } else {
                    curVal.append(ch);
                }
            } else { //  if currently /not/ reading a value in quotes
                if (ch == quote) { // if find opening quote
                    inQuotes = true;
                } else if (ch == sep) { // if find comma
                    result.add(curVal.toString()); // add value to line
                    curVal = new StringBuffer(); // start recording the value of the next column
                    startCollectChar = false;
                } else {
                    curVal.append(ch);
                }

            }
        }
        result.add(curVal.toString()); // add the very last value
        return result;
    }

    /*
     * Method to flag individual transactions > $50,000
     *
     * @param fileData the ArrayList containing lines in the .csv file as elements
     * @return a list of individual transactions > $50,000
     */
    private static ArrayList<ArrayList<String>> largeIndTrans(ArrayList<ArrayList<String>> fileData) {

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        // loop through the dataset
        for (ArrayList<String> transaction : fileData) {
//
            // select transactions exceeding $50,000
            if (Double.parseDouble(transaction.get(6)) > 50000 ) {
                result.add(transaction);
            }
        }
        return result;
    }

    /*
     * Method to flag transactions with pawn shops/casinos/resorts
     *
     * @param fileData the ArrayList containing lines in the .csv file as elements
     * @return a list of transactions with pawn shops/casinos/resorts
     */
    private static ArrayList<ArrayList<String>> oddMerchant(ArrayList<ArrayList<String>> fileData) {

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        String merchant = "";
        // loop through the dataset
        for (ArrayList<String> transaction : fileData) {
            merchant = transaction.get(10);

            // select transactions with pawn shops/casinos/resorts
            if (merchant.contains("PAWN") || merchant.contains("CASINO") || merchant.contains("RESORT") ) {
                result.add(transaction);
            }
        }
        return result;
    }

    /*
     * Method to flag transactions whose value is unusually "round" (i.e. evenly divisible by $100)
     *
     * @param fileData the ArrayList containing lines in the .csv file as elements
     * @return a list of transactions whose value is unusually "round"
     */
    private static ArrayList<ArrayList<String>> roundTrans(ArrayList<ArrayList<String>> fileData) {

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        // loop through the dataset
        for (ArrayList<String> transaction : fileData) {
//
            // select transactions whose value is unusually "round" (i.e. evenly divisible by $100)
            if (Double.parseDouble(transaction.get(6))%100 == 0) { // value divisible by $100
                result.add(transaction);
            }
        }
        return result;
    }

    /*
     * Method to flag transactions with missing/uninformative Descriptions
     *
     * @param fileData the ArrayList containing lines in the .csv file as elements
     * @return a list of transactions with missing/uninformative Descriptions
     */
    private static ArrayList<ArrayList<String>> misDes(ArrayList<ArrayList<String>> fileData) {

        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        // loop through the dataset
        for (ArrayList<String> transaction : fileData) {

            String des = transaction.get(5);
            // select transactions with missing/uninformative Descriptions
            if ((des.equals("000000000000000000000000")) ||des.equals("- PCE") || (des.contains("PCE| PCE"))) {
                result.add(transaction);
            }
        }
        return result;
    }

    /*
     * Method to search for infrequently used airlines (<10 occurrences)
     *
     * @param fileData the ArrayList containing lines in the .csv file as elements
     * @return a list of transactions with infrequently used airlines
     */
    private static ArrayList<ArrayList<String>> infrqAir (ArrayList<ArrayList<String>> fileData) {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        HashMap<Integer, String> allAirlines = new HashMap<Integer,String>();


        String mcc = ""; // placeholder for current mcc
        String airline = ""; // placeholder for current airline that we're examining
        // compile a list of all airlines with the index of the transaction in the dataset
        for (int i = 0; i < fileData.size(); i++) {
            mcc = fileData.get(i).get(10);
            if (mcc.contains("AIRLINE") || mcc.contains("DELTA")) { // if MCC is airline
                airline = fileData.get(i).get(7).split(" ")[0]; // get only the airline name and not the numbers
                allAirlines.put(i, airline); // add airline name to the list
            }
        }

        // count occurrences of airlines
        HashMap<String,Integer> airlineCount = new HashMap<String,Integer>();
        for (Map.Entry<Integer,String> entry : allAirlines.entrySet()) {

            if (!airlineCount.keySet().contains(entry.getValue())) { // if airline has not appeared in airlineCount
                airlineCount.put(entry.getValue(), 1);
            } else { // if this airline has appeared at least once
                airlineCount.put(entry.getValue(), airlineCount.get(entry.getValue())+1);
            }
        }

        // check: if airline occurs fewer than 10 times, record corresponding transaction
        for (Map.Entry<String,Integer> entry : airlineCount.entrySet()) {
            if (entry.getValue() < 10) {
                airline = entry.getKey();
                for (Map.Entry<Integer,String> e : allAirlines.entrySet()) {
                    if (e.getValue() == airline) {
                        result.add(fileData.get(e.getKey()));
                    }
                }
            }
        }
        return result;
    }

    /*
     * Method to print all possibly fraudulent transactions
     *
     * @param fileData the dataset
     */
    public static void print(ArrayList<ArrayList<String>> fileData) {

        System.out.println("\n[[[ *** INDIVIDUAL TRANSACTIONS EXCEEDING $50,000 *** ]]] \n");
        ArrayList<ArrayList<String>> largeIndTrans = largeIndTrans(fileData);
        for (ArrayList<String> trans : largeIndTrans) {
            System.out.println(trans);
        }

        System.out.println("\n[[[ *** TRANSACTIONS WITH PAWN SHOPS, CASINOS, AND RESORTS *** ]]] \n");
        ArrayList<ArrayList<String>> oddVendor = oddMerchant(fileData);
        for (ArrayList<String> trans : oddVendor) {
            System.out.println(trans);
        }

        System.out.println("\n[[[ *** TRANSACTIONS WHOSE VALUE IS UNUSUALLY ROUND (I.E EVENLY DIVISIBLE BY 100) *** ]]] \n");
        ArrayList<ArrayList<String>> roundTrans = roundTrans(fileData);
        for (ArrayList<String> trans : roundTrans) {
            System.out.println(trans);
        }

        System.out.println("\n[[[ *** TRANSACTIONS WITH MISSING/UNINFORMATIVE DESCRIPTIONS *** ]]] \n");
        ArrayList<ArrayList<String>> misDes = misDes(fileData);
        for (ArrayList<String> trans : misDes) {
            System.out.println(trans);
        }

        System.out.println("\n[[[ *** TRANSACTIONS WITH INFREQUENTLY USED AIRLINES (FEWER THAN 10 OCCURRENCES) *** ]]] \n");
        ArrayList<ArrayList<String>> infrqAir = infrqAir(fileData);
        for (ArrayList<String> trans : infrqAir) {
            System.out.println(trans);
        }


    }

    public static void main(String[] args) {

        // init variables
        String csvFile = null; // the dataset's file path
        BufferedReader br = null; // BufferedReader to read file
        String line = ""; // stores each line
        String sep = ",";
        ArrayList<ArrayList<String>> fileData = new ArrayList<ArrayList<String>>();

        // try-catch block for reading in the file and storing it line by line in fileData
        try {
            csvFile = args[0];
            br = new BufferedReader(new FileReader(csvFile));

            while ((line = br.readLine()) != null) {
                fileData.add(parseLine(line));
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

        fileData.remove(0); // delete the first line that lists all the categories for convenience

        print(fileData);
    }
}


