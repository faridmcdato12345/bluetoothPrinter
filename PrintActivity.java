package com.example.icws.icws;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class PrintActivity extends AppCompatActivity {

    private EditText receipt;
    private Button printBttn;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);

        intenting();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void intenting() {
        Intent i = getIntent();
        final String accountNo = i.getStringExtra("accountno");
        final String fname = i.getStringExtra("fname");
        final String mname = i.getStringExtra("mname");
        final String lname = i.getStringExtra("lname");
        final String arrears = i.getStringExtra("arrears");
        final String currentBill = i.getStringExtra("currentBill");
        final String totalBill = i.getStringExtra("totalBill");
        final String street = i.getStringExtra("street");
        final String houseno = i.getStringExtra("houseno");
        final String village = i.getStringExtra("village");
        final String barangay = i.getStringExtra("barangay");


        final String r_title, r_accountNo, r_fname, r_mname, r_lname, r_arrears,
                r_currentBill, r_totalBill, r_street, r_houseNo,
                r_village, r_barangay, r_address, r_title_bill;
        r_title = "ICWS RECEIPT";
        r_address = "ADDRESS";
        r_accountNo = "Account Number: " + accountNo;
        r_fname = "First Name: " + fname;
        r_mname = "Middle Name: " + mname;
        r_lname = "Surname: " + lname;
        r_arrears = "Arrears: " + arrears;
        r_currentBill = "Current Bill: " + currentBill;
        r_totalBill = "TOTAL BILL: " + totalBill;
        r_street = "Street:" + street;
        r_houseNo = "House Number: " + houseno;
        r_village = "Village/Subd: " + village;
        r_barangay = "Barangay: " + barangay;
        r_title_bill = "BILL";

        receipt = (EditText) findViewById(R.id.printText);
        receipt.setEnabled(false);
        receipt.setTextColor(Color.BLACK);
        receipt.setClickable(false);
        receipt.setSingleLine(false);
        receipt.setText(r_title + "\n" + "\n" + r_accountNo + "\n" + r_fname + "\n" + r_mname + "\n" + r_lname + "\n" + "\n" +
                r_address + "\n" + "\n" + r_houseNo + "\n" + r_street + "\n" + r_village + "\n" + r_barangay + "\n" + "\n" + r_title_bill + "\n" + "\n" +
                r_arrears + "\n" + r_currentBill + "\n" + "\n" + r_totalBill);
        printBttn = (Button) findViewById(R.id.printBttn);
        if (BluetoothPrintDriver.IsNoConnection()) {
            return;
        }
        else {
            printBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String finalReciept = receipt.getText().toString();
                    BluetoothPrintDriver.ImportData(finalReciept);
                    BluetoothPrintDriver.ImportData("\r");
                    BluetoothPrintDriver.excute();
                    BluetoothPrintDriver.ClearData();
                }
            });
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Print Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
