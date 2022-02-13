package com.hesoyam.mercury.reader;

import com.hesoyam.mercury.model.FirmInfo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.hesoyam.mercury.util.ServiceUtil.*;

public class FirmInfoReader {

    public static void getFirmInfo(JLabel jLabel9) {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File file = chooser.getSelectedFile();

        List<String> firmInfo = new ArrayList<>();

        try {
            FileInputStream fileStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fileStream);
            Sheet sheet = workbook.getSheetAt(0);

            int i = 0;
            for (Row row : sheet) {
                for (Cell cell : row) {
                    if (i > NUM_OF_CELLS_FIRM_INFO) {
                        cell.setCellType(CellType.STRING);
                        firmInfo.add(cell.getStringCellValue());
                    }
                    i++;
                    if (i > UPPER_BOUND) {
                        break;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        FirmInfo firmInfoClass = createFirmInfoFromList(firmInfo);
        try {
            FileOutputStream fileOut = new FileOutputStream(FIRM_INFO_FILE_PATH);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(firmInfoClass);
            objectOut.close();

            checkIfFileIsEmptyAndChangeLabel(FIRM_INFO_FILE_PATH, jLabel9);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static FirmInfo createFirmInfoFromList(List<String> firmInfo) {

        return FirmInfo.builder()
                .name(firmInfo.get(0))
                .bulstat(firmInfo.get(1))
                .DDS(firmInfo.get(2))
                .address(firmInfo.get(3))
                .MOL(firmInfo.get(4))
                .mobileNumber(firmInfo.get(5))
                .IBAN(firmInfo.get(6))
                .BIC(firmInfo.get(7))
                .bank(firmInfo.get(8)).build();
    }
}
