package com.hesoyam.mercury.reader;

import com.hesoyam.mercury.model.ProductPrice;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.hesoyam.mercury.util.ServiceUtil.PRODUCTS_PRICE_FILE_PATH;
import static com.hesoyam.mercury.util.ServiceUtil.checkIfFileIsEmptyAndChangeLabel;

public class ProductsPriceInfoReader {

    public static void getProductsPriceInfo(JLabel jLabel12) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();
        List<List<String>> objects = new ArrayList<>();

        try {
            FileInputStream fileStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fileStream);
            Sheet sheet = workbook.getSheetAt(0);

            boolean isLast = false;
            int i = 0;
            for (Row row : sheet) {
                List<String> obj = new ArrayList<>();
                for (Cell cell : row) {
                    if (i > 2) {
                        cell.setCellType(CellType.STRING);

                        if (cell.getStringCellValue().equals("")) {
                            isLast = true;
                            break;
                        }

                        obj.add(cell.getStringCellValue());
                    }
                    i++;
                }
                if (isLast) {
                    break;
                }
                if (!obj.isEmpty()) {
                    objects.add(obj);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ProductPrice> productPrices = createProductPrice(objects);

        try {
            FileOutputStream fileOut = new FileOutputStream(PRODUCTS_PRICE_FILE_PATH);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(productPrices);

            objectOut.close();

            checkIfFileIsEmptyAndChangeLabel(PRODUCTS_PRICE_FILE_PATH, jLabel12);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<ProductPrice> createProductPrice(List<List<String>> objects) {
        List<ProductPrice> productPrices = new ArrayList<>();

        for (List<String> object : objects) {
            productPrices.add(ProductPrice.builder()
                    .schoolNum(object.get(0))
                    .productName(object.get(1))
                    .price(object.get(2)).build());
        }

        return productPrices;
    }
}
