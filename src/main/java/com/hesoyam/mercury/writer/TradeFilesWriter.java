package com.hesoyam.mercury.writer;

import com.hesoyam.mercury.model.CounterPartyInfo;
import com.hesoyam.mercury.model.TradeFileInfo;
import org.apache.poi.xwpf.usermodel.*;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

import static com.hesoyam.mercury.util.ServiceUtil.*;

public class TradeFilesWriter {

    public static void createDailyTrade(JComboBox<String> recipient,
                                        JComboBox<String> productName,
                                        JTextField productQuantity,
                                        JXDatePicker manufactureDate,
                                        JTextField manufactureTime,
                                        JTextField deliveryTime,
                                        JXDatePicker expiryDate) {
        String carNumber = "";

        //Read Object from file
        try {
            FileInputStream fileIn = new FileInputStream(COUNTERPARTY_INFO_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            List<CounterPartyInfo> counterPartiesInfo = (List<CounterPartyInfo>) objectIn.readObject();

            for (CounterPartyInfo counterPartyInfo : counterPartiesInfo) {
                if (counterPartyInfo.getSchoolName().equals(recipient.getSelectedItem().toString())) {
                    carNumber = counterPartyInfo.getCarNumber();
                }

            }

            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Integer fileNumber = getFileNumberFromPreference();

        TradeFileInfo fileInfo = TradeFileInfo.builder()
                .fileNumber(fileNumber)
                .fileDate(new Date())
                .productName(productName.getSelectedItem().toString())
                .productQuantity(Integer.valueOf(productQuantity.getText()))
                .manufactureDate(manufactureDate.getDate())
                .manufactureTime(manufactureTime.getText())
                .expiryDate(expiryDate.getDate())
                .recipient(recipient.getSelectedItem().toString())
                .carNumber(carNumber)
                .deliveryTime(deliveryTime.getText()).build();

        List<TradeFileInfo> tradeFileInfos = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream(DAILY_TRADES_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            tradeFileInfos = (List<TradeFileInfo>) objectIn.readObject();

            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        tradeFileInfos.add(fileInfo);

        try {
            FileOutputStream fileOut = new FileOutputStream(DAILY_TRADES_FILE_PATH);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(tradeFileInfos);

            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        productQuantity.setText("1");
        manufactureDate.setDate(null);
        expiryDate.setDate(null);
        manufactureTime.setText("06:00");
        deliveryTime.setText("06:00");
    }

    private static Integer getFileNumberFromPreference() {
        Preferences prefs = Preferences.userNodeForPackage(TradeFilesWriter.class);

        final String PREF_NAME = "file_number";

        String defaultValue = "1";
        String propertyValue = prefs.get(PREF_NAME, defaultValue);
        int fileNumber = Integer.parseInt(propertyValue);
        int increasedFileNumber = fileNumber + 1;

        prefs.put(PREF_NAME, Integer.toString(increasedFileNumber));

        return fileNumber;
    }

    public static void createAllTradesFiles() {
        List<TradeFileInfo> tradeFileInfos = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream(DAILY_TRADES_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            tradeFileInfos = (List<TradeFileInfo>) objectIn.readObject();

            objectIn.close();

            XWPFDocument document = new XWPFDocument();
            FileOutputStream out = new FileOutputStream(new File(ALL_TRADE_FILES_FILE_PATH));

            for (int i = 0; i < tradeFileInfos.size(); i++) {
                //First paragraph - firm info
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun run = paragraph.createRun();
                run.setText("„ДОЙЧЕВ-МТВ“ ЕООД");
                run.addCarriageReturn();
                run.setText("ПРОИЗВОДСТВО НА ТЕСТЕНИ ЗАКУСКИ, САНДВИЧИ, ХАМБУРГЕРИ И ХЛЯБ");
                run.addCarriageReturn();
                run.setText("ПРОИЗВОДСТВЕНА БАЗА: гр. СЛИВЕН, ул. „ПАВЕЛ МИЛЮКОВ“ 1,  рег. № 20010034");
                run.addCarriageReturn();
                run.setText("ТЕЛ.: 0888242636");
                run.addCarriageReturn();

                //Second paragraph - Title
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun secondRun = paragraph.createRun();
                secondRun.setBold(true);
                secondRun.setFontSize(20);
                secondRun.setText("ТЪРГОВСКИ ДОКУМЕНТ №" + tradeFileInfos.get(i).getFileNumber().toString());

                //Third paragraph - doc number and date
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun thirdRun = paragraph.createRun();
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                String fileDate = formatter.format(tradeFileInfos.get(i).getFileDate());
                thirdRun.setBold(true);
                thirdRun.setText(fileDate);

                //Fourth paragraph - product info
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFTable productTable = document.createTable(2, 2);
                productTable.getRow(0).getCell(0).setText(" Наименование на продукта  ");
                productTable.getRow(0).getCell(1).setText(" Брой доставен продукт  ");
                productTable.getRow(1).getCell(0).setText(" " + tradeFileInfos.get(i).getProductName() + " ");
                productTable.getRow(1).getCell(1).setText(" " + tradeFileInfos.get(i).getProductQuantity().toString() + " ");

                //Fifth paragraph - manufacture info
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun fifthRun = paragraph.createRun();
                fifthRun.setBold(true);
                fifthRun.addCarriageReturn();
                fifthRun.setText("Дата и час на производство:  ");
                String manufactureDate = formatter.format(tradeFileInfos.get(i).getManufactureDate());
                XWPFRun fifthRun2 = paragraph.createRun();
                fifthRun2.setText(manufactureDate + " " + tradeFileInfos.get(i).getManufactureTime());
                fifthRun2.addCarriageReturn();

                //Sixth paragraph - expiryDate info
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun sixthRun = paragraph.createRun();
                sixthRun.setBold(true);
                String expiryDate = formatter.format(tradeFileInfos.get(i).getExpiryDate());
                SimpleDateFormat batchNumberFormatter = new SimpleDateFormat("ddMMyyyy");
                String batchNumber = batchNumberFormatter.format(tradeFileInfos.get(i).getExpiryDate());
                sixthRun.setText("Партиден номер/Използвай преди:  ");
                XWPFRun sixthRun2 = paragraph.createRun();
                sixthRun2.setText(batchNumber + " / " + expiryDate);
                sixthRun2.addCarriageReturn();

                //Seventh paragraph - recipient
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun seventhRun = paragraph.createRun();
                seventhRun.setBold(true);
                seventhRun.setText("Получател:  ");
                XWPFRun seventhRun2 = paragraph.createRun();
                seventhRun2.setText(tradeFileInfos.get(i).getRecipient());
                seventhRun2.addCarriageReturn();

                //Eighth paragraph - carNumber
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun eighthRun = paragraph.createRun();
                eighthRun.setBold(true);
                eighthRun.setText("Транспортно средство №:  ");
                XWPFRun eighthRun2 = paragraph.createRun();
                eighthRun2.setText(tradeFileInfos.get(i).getCarNumber());
                eighthRun2.addCarriageReturn();

                //Ninth paragraph - delivery time
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun ninthRun = paragraph.createRun();
                ninthRun.setBold(true);
                ninthRun.setText("Час на доставка:  ");
                XWPFRun ninthRun2 = paragraph.createRun();
                ninthRun2.setText(tradeFileInfos.get(i).getDeliveryTime());
                ninthRun2.addCarriageReturn();

                //Tenth paragraph - small font
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun tenthRun = paragraph.createRun();
                tenthRun.setFontSize(8);
                tenthRun.setText("Доставения продукт е предназначен за човешка консумация и отговаря  по показатели на съответната фирмена документация.");
                tenthRun.addCarriageReturn();
                tenthRun.setText("Фирмата гарантира за контрол и безопасност на доставка на всеки асортимент.");
                tenthRun.addCarriageReturn();
                tenthRun.addCarriageReturn();
                tenthRun.addCarriageReturn();

                //Eleventh paragraph - signatures
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun eleventhRun = paragraph.createRun();
                eleventhRun.setText("Подпис /предал/:                                                                         Подпис /приел/: ");

                if (i != tradeFileInfos.size() - 1) {
                    eleventhRun.addBreak(BreakType.PAGE);
                }
            }

            document.write(out);
            out.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
    }
}
