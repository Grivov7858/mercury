package com.hesoyam.mercury.reader;

import com.hesoyam.mercury.model.CounterPartyInfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.hesoyam.mercury.util.ServiceUtil.*;

public class CounterpartyInfoReader {

    public static void getCounterpartyInfo(JLabel jLabel10, JComboBox<String> recipient) {
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
                    if (i > NUM_OF_CELLS_COUNTERPARTY_INFO) {
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

        List<CounterPartyInfo> counterPartiesFromList = createCounterPartiesFromList(objects);

        try {
            FileOutputStream fileOut = new FileOutputStream(COUNTERPARTY_INFO_FILE_PATH);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(counterPartiesFromList);

            objectOut.close();

            checkIfFileIsEmptyAndChangeLabel(COUNTERPARTY_INFO_FILE_PATH, jLabel10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        recipient.setModel(new DefaultComboBoxModel(extractAvailableRecipients()));
    }

    private static List<CounterPartyInfo> createCounterPartiesFromList(List<List<String>> objects) {
        List<CounterPartyInfo> counterPartiesInfo = new ArrayList<>();

        for (List<String> object : objects) {
            counterPartiesInfo.add(CounterPartyInfo.builder()
                    .schoolNum(object.get(0))
                    .schoolName(object.get(1))
                    .bulstat(object.get(2))
                    .address(object.get(3))
                    .MOL(object.get(4))
                    .carNumber(object.get(5))
                    .DDS(object.get(6)).build());
        }

        return counterPartiesInfo;
    }
}
