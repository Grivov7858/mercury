package com.hesoyam.mercury.util;

import com.hesoyam.mercury.model.CounterPartyInfo;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtil {
    public static final String COUNTERPARTY_INFO_FILE_PATH = "src/db/counterpartyInfo.txt";
    public static final String AVAILABLE_PRODUCTS_FILE_PATH = "src/db/availableProducts.txt";
    public static final String FIRM_INFO_FILE_PATH = "src/db/firmInfo.txt";
    public static final String PRODUCTS_PRICE_FILE_PATH = "src/db/productsPrice.txt";
    public static final String DAILY_TRADES_FILE_PATH = "src/db/dailyTrades.txt";
    public static final String ALL_TRADE_FILES_FILE_PATH = "src/db/allTradeFiles.docx";
    public static final String TRADED_PRODUCTS_FILE_PATH = "src/db/tradedProducts.txt";
    public static final String MONTHLY_INVOICES_FILE_PATH = "src/db/monthlyInvoices.docx";
    public static final Integer NUM_OF_CELLS_FIRM_INFO = 8;
    public static final Integer NUM_OF_CELLS_COUNTERPARTY_INFO = 6;
    public static final Integer UPPER_BOUND = 17;

    public static String[] extractAvailableProducts() {
        List<String> availableProducts = new ArrayList<>();

        //Read Object from file
        try {
            FileInputStream fileIn = new FileInputStream(AVAILABLE_PRODUCTS_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            availableProducts = (List<String>) objectIn.readObject();

            System.out.println("The Object has been read from the file");
            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Read Object from file
        return availableProducts.toArray(new String[0]);
    }

    public static String[] extractAvailableRecipients() {
        List<String> recipientsList = new ArrayList<>();

        //Read Object from file
        try {
            FileInputStream fileIn = new FileInputStream(COUNTERPARTY_INFO_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            List<CounterPartyInfo> counterPartiesInfo = (List<CounterPartyInfo>) objectIn.readObject();

            for (CounterPartyInfo counterPartyInfo : counterPartiesInfo) {
                recipientsList.add(counterPartyInfo.getSchoolName());
            }

            System.out.println("The Object has been read from the file");
            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Read Object from file
        return recipientsList.toArray(new String[0]);
    }

    public static boolean isFileEmpty(String filePath) {

        File myFile = new File(filePath);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(myFile));
            return reader.readLine() == null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void checkIfFileIsEmptyAndChangeLabel(String filePath, JLabel label) {
        try {
            File myFile = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(myFile));
            if (reader.readLine() == null) {
                label.setText("Няма налична информация в базата данни. Моля добавете файл.");
                label.setForeground(Color.RED);
            } else {
                label.setText("Има налична информация в базата!");
                label.setForeground(Color.green);
            }
        } catch (IOException e) {
            e.printStackTrace();
            label.setText("Няма налична информация в базата данни. Моля добавете файл.");
            label.setForeground(Color.RED);
        }
    }
}