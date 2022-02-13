package com.hesoyam.mercury.reader;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.hesoyam.mercury.util.ServiceUtil.*;

public class AvailableProductsReader {

    public static void getAvailableProducts(JLabel jLabel11, JComboBox<String> productName) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        List<String> availableProducts = new ArrayList<>();

        try {
            FileInputStream fileStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fileStream);
            Sheet sheet = workbook.getSheetAt(0);

            boolean isLast = false;
            int i = 0;
            for (Row row : sheet) {

                for (Cell cell : row) {
                    if (i > 0) {
                        cell.setCellType(CellType.STRING);

                        if (cell.getStringCellValue().equals("")) {
                            isLast = true;
                            break;
                        }

                        availableProducts.add(cell.getStringCellValue());
                    }
                    i++;
                }
                if (isLast) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(AVAILABLE_PRODUCTS_FILE_PATH);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(availableProducts);

            objectOut.close();

            checkIfFileIsEmptyAndChangeLabel(AVAILABLE_PRODUCTS_FILE_PATH, jLabel11);
        } catch (IOException e) {
            e.printStackTrace();
        }

        productName.setModel(new DefaultComboBoxModel(extractAvailableProducts()));
    }
}
