package com.hesoyam.mercury.writer;

import com.hesoyam.mercury.model.CounterPartyInfo;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import static com.hesoyam.mercury.util.ServiceUtil.COUNTERPARTY_INFO_FILE_PATH;
import static com.hesoyam.mercury.writer.MonthlyInvoiceWriter.updateInvoice;

public class InvoiceOnlyOrderWriter {

    public static void addForInvoice(JComboBox<String> recipient,
                                     JComboBox<String> productName,
                                     JTextField productQuantity,
                                     JXDatePicker manufactureDate,
                                     JTextField manufactureTime,
                                     JTextField deliveryTime,
                                     JXDatePicker expiryDate,
                                     JXDatePicker deliveryDate) {
        String schoolNumber = "";

        //Read Object from file
        try {
            FileInputStream fileIn = new FileInputStream(COUNTERPARTY_INFO_FILE_PATH);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            List<CounterPartyInfo> counterPartiesInfo = (List<CounterPartyInfo>) objectIn.readObject();

            for (CounterPartyInfo counterPartyInfo : counterPartiesInfo) {
                if (counterPartyInfo.getSchoolName().equals(recipient.getSelectedItem().toString())) {
                    schoolNumber = counterPartyInfo.getSchoolNum();
                }

            }

            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        updateInvoice(schoolNumber, Integer.parseInt(productQuantity.getText().trim()), productName.getSelectedItem().toString());

        productQuantity.setText("1");
        manufactureDate.setDate(null);
        expiryDate.setDate(null);
        deliveryDate.setDate(null);
        manufactureTime.setText("06:00");
        deliveryTime.setText("06:00");
    }
}
