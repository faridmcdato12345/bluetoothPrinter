package com.example.icws.icws;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.RT_Printer.BluetoothPrinter.BLUETOOTH.BluetoothPrintDriver;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public String SelectedBDAddress;
    private GoogleApiClient client;

    private EditText account;
    private EditText fname;
    private EditText mname;
    private EditText lname;
    private EditText calcu;
    private ProgressDialog progress;
    private TextView connectResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        account = (EditText) findViewById(R.id.accountNo);
        fname = (EditText) findViewById(R.id.firstName);
        mname = (EditText) findViewById(R.id.middleName);
        lname  = (EditText) findViewById(R.id.lastName);
        calcu = (EditText) findViewById(R.id.reading);
        connectResponse = (TextView) findViewById(R.id.connectResponse);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new submitAccount().execute();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            Intent l = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(l);
        }
        if (id == R.id.connectToBluetooth) {
            Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            String r= "connected";
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CONNECT_DEVICE_SECURE:
                if(resultCode == Activity.RESULT_OK){
                    BluetoothPrintDriver.close();
                    SelectedBDAddress = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

                    if(!BluetoothPrintDriver.OpenPrinter(SelectedBDAddress)){
                        BluetoothPrintDriver.close();
                        connectResponse.setText(R.string.bluetooth_connect_fail);
                        return;
                    }
                    else{
                        String bluetoothConnectSuccess=MainActivity.this.getResources().getString(R.string.bluetooth_connect_succes);
                        connectResponse.setText(bluetoothConnectSuccess+SelectedBDAddress);
                    }
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if(resultCode == Activity.RESULT_OK){

                }
                break;
        }
    }

    private class submitAccount extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(MainActivity.this);
            progress.setCancelable(true);
            progress.setMessage("Submitting information...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setProgress(0);
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try{
                String paramets = "accountno="+account.getText().toString()+"&fname="+fname.getText()+"&mname="+mname.getText()+"&lname="+lname.getText()+"&calcu="+calcu.getText();
                URL url = new URL("http://www.iligancitywaterworks.tk/webservice/postCostumer.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                DataOutputStream dr = new DataOutputStream(conn.getOutputStream());
                dr.writeBytes(paramets);
                dr.flush();
                dr.close();
                conn.connect();
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                    InputStream is = conn.getInputStream();
                    StringBuilder str = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = null;

                    while((line = br.readLine()) != null){
                        str.append(line);
                    }
                    is.close();
                    return str.toString();
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.dismiss();

            try{
                JSONObject jsonObj = new JSONObject(result);
                int response = jsonObj.getInt("response");
                if(response > 0){
                    JSONArray jsonArray = jsonObj.getJSONArray("users");

                    JSONObject insideObj = jsonArray.getJSONObject(0);
                    String cfname = insideObj.getString("costumer_firstname");
                    String cmname = insideObj.getString("costumer_middlename");
                    String clname = insideObj.getString("costumer_lastname");
                    String accoutNo = insideObj.getString("costumer_account_no");
                    String bill = insideObj.getString("costumer_bill");
                    String arrears = insideObj.getString("costumer_arrears");
                    String houseNo = insideObj.getString("house_no");
                    String street = insideObj.getString("street");
                    String village = insideObj.getString("village");
                    String barangay = insideObj.getString("barangay");
                    int currentbill = jsonObj.getInt("bill");
                    String stringBill = String.valueOf(currentbill);

                    Double arrearsDoub = Double.valueOf(arrears);
                    Double tBill = Double.valueOf(currentbill) + arrearsDoub;

                    String totalBill = String.valueOf(tBill);

                    Intent i = new Intent(getApplicationContext(), PrintActivity.class);
                    i.putExtra("accountno",accoutNo);
                    i.putExtra("fname",cfname);
                    i.putExtra("mname",cmname);
                    i.putExtra("lname",clname);
                    i.putExtra("arrears",arrears);
                    i.putExtra("currentBill",stringBill);
                    i.putExtra("totalBill",totalBill);
                    i.putExtra("street",street);
                    i.putExtra("houseno",houseNo);
                    i.putExtra("village",village);
                    i.putExtra("barangay",barangay);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getBaseContext(), "The information you entered was invalid",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
