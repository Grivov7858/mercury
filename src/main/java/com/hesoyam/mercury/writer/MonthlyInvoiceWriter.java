package com.hesoyam.mercury.writer;

import com.hesoyam.mercury.model.CounterPartyInfo;
import com.hesoyam.mercury.model.FirmInfo;
import com.hesoyam.mercury.model.ProductPrice;
import com.hesoyam.mercury.model.invoice.InvoiceInfo;
import com.hesoyam.mercury.model.invoice.Product;
import com.hesoyam.mercury.util.NumToWordsConverter;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

import static com.hesoyam.mercury.util.ServiceUtil.*;

public class MonthlyInvoiceWriter {

    public static void updateInvoice(String schoolNum, Integer productQuantity, String productName) {
        List<InvoiceInfo> invoices = new ArrayList<>();

        //Read Object from file
        try {
            FileInputStream fileIn = new FileInputStream(TRADED_PRODUCTS_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            invoices = (List<InvoiceInfo>) objectIn.readObject();

            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (invoices.isEmpty()) {
            createNewInvoice(schoolNum, productQuantity, productName, invoices);

            try {
                FileOutputStream fileOut = new FileOutputStream(TRADED_PRODUCTS_FILE_PATH);
                ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

                objectOut.writeObject(invoices);

                objectOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        boolean isNewInvoice = true;

        for (InvoiceInfo invoice : invoices) {
            if (invoice.getSchoolNum().equals(schoolNum)) {
                List<Product> products = invoice.getProducts();

                boolean isNewProduct = true;
                for (Product product : products) {
                    if (product.getName().equals(productName)) {
                        product.setQuantity(product.getQuantity() + productQuantity);
                        isNewProduct = false;
                    }
                }

                if (isNewProduct) {
                    products.add(new Product(productName, productQuantity));
                }
                isNewInvoice = false;
            }
        }

        if (isNewInvoice) {
            createNewInvoice(schoolNum, productQuantity, productName, invoices);
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(TRADED_PRODUCTS_FILE_PATH);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(invoices);

            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void extractInvoicesDoc() {
        List<InvoiceInfo> invoices = new ArrayList<>();
        List<CounterPartyInfo> counterPartiesInfo = extractCounterPartiesInfo();
        FirmInfo firmInfo = extractFirmInfo();
        List<ProductPrice> productPrices = extractProductPrices();

        //Read Object from file
        try {
            FileInputStream fileIn = new FileInputStream(TRADED_PRODUCTS_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            invoices = (List<InvoiceInfo>) objectIn.readObject();

            objectIn.close();

            XWPFDocument document = new XWPFDocument();
            FileOutputStream out = new FileOutputStream(new File(MONTHLY_INVOICES_FILE_PATH));

            for (InvoiceInfo invoice : invoices) {
                CounterPartyInfo currentCounterPartyInfo = CounterPartyInfo.builder().build();
                for (CounterPartyInfo counterPartyInfo : counterPartiesInfo) {
                    if (counterPartyInfo.getSchoolNum().equals(invoice.getSchoolNum())) {
                        currentCounterPartyInfo = counterPartyInfo;
                    }
                }

                //First paragraph - Firms info
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun run = paragraph.createRun();
                run.setBold(true);
                run.setText("                                    Фактура");
                run.setFontSize(18);
                XWPFRun run1 = paragraph.createRun();
                run1.setText("                                 № " + invoice.getInvoiceNum());
                run1.addCarriageReturn();
                XWPFRun run2 = paragraph.createRun();
                run2.setBold(true);
                run2.setFontSize(17);
                run2.setText("Оригинал");
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun run3 = paragraph.createRun();
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                String fileDate = formatter.format(new Date());
                run3.setText("Дата: " + fileDate);

                //Second paragraph - Title
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun secondRun = paragraph.createRun();
                secondRun.setBold(true);
                secondRun.setText("Продавач                                                                                        Купувач");
                XWPFTable firmInfoTable = document.createTable(9, 4);
                firmInfoTable.getRow(0).getCell(0).setText(" Име: ");
                firmInfoTable.getRow(0).getCell(1).setText(" " + firmInfo.getName());
                firmInfoTable.getRow(0).getCell(2).setText(" Име ");
                firmInfoTable.getRow(0).getCell(3).setText(" " + currentCounterPartyInfo.getSchoolName());

                firmInfoTable.getRow(1).getCell(0).setText(" Булстат ");
                firmInfoTable.getRow(1).getCell(1).setText(" " + firmInfo.getBulstat());
                firmInfoTable.getRow(1).getCell(2).setText(" Булстат ");
                firmInfoTable.getRow(1).getCell(3).setText(" " + currentCounterPartyInfo.getBulstat());

                firmInfoTable.getRow(2).getCell(0).setText(" ДДС ");
                firmInfoTable.getRow(2).getCell(1).setText(" " + firmInfo.getDDS());
                firmInfoTable.getRow(2).getCell(2).setText(" ДДС ");
                firmInfoTable.getRow(2).getCell(3).setText(" " + currentCounterPartyInfo.getDDS());

                firmInfoTable.getRow(3).getCell(0).setText(" Адрес на фирмата ");
                firmInfoTable.getRow(3).getCell(1).setText(" " + firmInfo.getAddress());
                firmInfoTable.getRow(3).getCell(2).setText(" Адрес на фирмата ");
                firmInfoTable.getRow(3).getCell(3).setText(" " + currentCounterPartyInfo.getAddress());

                firmInfoTable.getRow(4).getCell(0).setText(" МОЛ ");
                firmInfoTable.getRow(4).getCell(1).setText(" " + firmInfo.getMOL());
                firmInfoTable.getRow(4).getCell(2).setText(" МОЛ ");
                firmInfoTable.getRow(4).getCell(3).setText(" " + currentCounterPartyInfo.getMOL());

                firmInfoTable.getRow(5).getCell(0).setText(" тел. ");
                firmInfoTable.getRow(5).getCell(1).setText(" " + firmInfo.getMobileNumber());
                firmInfoTable.getRow(5).getCell(2).setText(" тел. ");

                firmInfoTable.getRow(6).getCell(0).setText(" Банка ");
                firmInfoTable.getRow(6).getCell(1).setText(" " + firmInfo.getBank());

                firmInfoTable.getRow(7).getCell(0).setText(" IBAN ");
                firmInfoTable.getRow(7).getCell(1).setText(" " + firmInfo.getIBAN());

                firmInfoTable.getRow(8).getCell(0).setText(" BIC ");
                firmInfoTable.getRow(8).getCell(1).setText(" " + firmInfo.getBIC());

                //Third paragraph - products info
                List<Product> products = invoice.getProducts();
                BigDecimal allProductsPrice = BigDecimal.ZERO;

                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun thirdRun = paragraph.createRun();
                XWPFTable productsInfoTable = document.createTable(products.size() + 1, 5);

                productsInfoTable.getRow(0).getCell(0).setText(" Име ");
                productsInfoTable.getRow(0).getCell(1).setText(" к-во ");
                productsInfoTable.getRow(0).getCell(2).setText(" цена ");
                productsInfoTable.getRow(0).getCell(3).setText(" стойност ");
                productsInfoTable.getRow(0).getCell(4).setText(" ДДС ");


                for (int j = 0; j < products.size(); j++) {
                    ProductPrice currentProductPrice = ProductPrice.builder().build();
                    for (ProductPrice productPrice : productPrices) {

                        if (productPrice.getProductName().equals("всички")) {
                            currentProductPrice = productPrice;
                            break;
                        }

                        if (productPrice.getProductName().equals(products.get(j).getName())) {
                            currentProductPrice = productPrice;
                        }
                    }

                    BigDecimal productPrice = BigDecimal.valueOf(Double.parseDouble(currentProductPrice.getPrice()));
                    BigDecimal totalPrice = productPrice.multiply(BigDecimal.valueOf(products.get(j).getQuantity()));
                    allProductsPrice = allProductsPrice.add(totalPrice);

                    productsInfoTable.getRow(j + 1).getCell(0).setText(" " + products.get(j).getName());
                    productsInfoTable.getRow(j + 1).getCell(1).setText(" " + products.get(j).getQuantity().toString());
                    productsInfoTable.getRow(j + 1).getCell(2).setText(" " + currentProductPrice.getPrice());
                    productsInfoTable.getRow(j + 1).getCell(3).setText(" " + totalPrice);
                    productsInfoTable.getRow(j + 1).getCell(4).setText(" 20% ");
                }

                //Fourth paragraph - full price
                BigDecimal DDS = allProductsPrice.divide(BigDecimal.valueOf(5), 2, RoundingMode.CEILING);
                BigDecimal total = allProductsPrice.add(DDS);
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);

                XWPFRun fourthRun = paragraph.createRun();
                XWPFRun fourthRun2 = paragraph.createRun();

                fourthRun.setBold(true);
                fourthRun.setText("Стойност: ");
                fourthRun2.setText(allProductsPrice.toString());
                fourthRun2.addCarriageReturn();
                XWPFRun fourthRun3 = paragraph.createRun();
                XWPFRun fourthRun4 = paragraph.createRun();
                fourthRun3.setBold(true);
                fourthRun3.setText("ДДС: ");
                fourthRun4.setText(DDS.toString());
                fourthRun4.addCarriageReturn();
                XWPFRun fourthRun5 = paragraph.createRun();
                XWPFRun fourthRun6 = paragraph.createRun();
                fourthRun5.setBold(true);
                fourthRun5.setText("ТОТАЛ: ");
                fourthRun6.setText(total.toString());

                //Fifth paragraph - how to pay
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.CENTER);
                XWPFTable howToPayTable = document.createTable(4, 2);

                howToPayTable.getRow(0).getCell(0).setText(" Начин на плащане: ");
                howToPayTable.getRow(0).getCell(1).setText(" По банков път ");

                howToPayTable.getRow(1).getCell(0).setText(" Сумата словом: ");
                howToPayTable.getRow(1).getCell(1).setText(" " + NumToWordsConverter.convertAmountToWords(total.intValue(), Integer.parseInt(total.toString().substring(total.toString().length() - 2))));

                howToPayTable.getRow(2).getCell(0).setText(" Забележка: ");

                howToPayTable.getRow(3).getCell(0).setText(" Дата на ВДС: ");
                howToPayTable.getRow(3).getCell(1).setText(" " + fileDate);

                //Sixth paragraph - signature
                paragraph = document.createParagraph();
                paragraph.setAlignment(ParagraphAlignment.LEFT);
                XWPFTable signatureTable = document.createTable(2, 4);
                signatureTable.getRow(0).getCell(1).setText(" " + firmInfo.getMOL() + "  ");
                signatureTable.getRow(0).getCell(3).setText(" " + currentCounterPartyInfo.getMOL() + "  ");

                signatureTable.getRow(1).setHeight(500);
                signatureTable.getRow(1).getCell(0).setText(" Подпис: ");
                signatureTable.getRow(1).getCell(2).setText(" Подпис: ");

                signatureTable.getRow(1).getCell(0).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);
                signatureTable.getRow(1).getCell(2).setVerticalAlignment(XWPFTableCell.XWPFVertAlign.BOTTOM);

                signatureTable.getRow(0).getCell(2).setText(" Получил: ");
            }

            document.write(out);
            out.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }
        List<InvoiceInfo> emptyTradedProductsFile = new ArrayList<>();
        try {
            FileOutputStream fileOut = new FileOutputStream(TRADED_PRODUCTS_FILE_PATH);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(emptyTradedProductsFile);

            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<CounterPartyInfo> extractCounterPartiesInfo() {
        List<CounterPartyInfo> counterPartiesInfo = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream(COUNTERPARTY_INFO_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            counterPartiesInfo = (List<CounterPartyInfo>) objectIn.readObject();

            System.out.println("The Object has been read from the file");
            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return counterPartiesInfo;
    }

    private static FirmInfo extractFirmInfo() {
        FirmInfo firmInfoClass = null;
        try {
            FileInputStream fileIn = new FileInputStream(FIRM_INFO_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            firmInfoClass = (FirmInfo) objectIn.readObject();

            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return firmInfoClass;
    }

    private static List<ProductPrice> extractProductPrices() {
        List<ProductPrice> productPrices = new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream(PRODUCTS_PRICE_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            productPrices = (List<ProductPrice>) objectIn.readObject();

            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return productPrices;
    }

    private static void createNewInvoice(String schoolNum, Integer productQuantity, String productName, List<InvoiceInfo> invoices) {
        List<Product> newProducts = new ArrayList<>();
        newProducts.add(new Product(productName, productQuantity));

        invoices.add(new InvoiceInfo(createInvoiceNumber(), schoolNum, newProducts));
    }

    public static String createInvoiceNumber() {
        Preferences prefs = Preferences.userNodeForPackage(TradeFilesWriter.class);

        final String PREF_NAME = "invoice_number";

        String defaultValue = "1";
        String propertyValue = prefs.get(PREF_NAME, defaultValue);
        int invoiceNumber = Integer.parseInt(propertyValue);
        int increasedFileNumber = invoiceNumber + 1;

        prefs.put(PREF_NAME, Integer.toString(increasedFileNumber));

        return "0".repeat(10 - String.valueOf(invoiceNumber).length()) +
                invoiceNumber;
    }
}
