package com.interswitchgroup.mobpayexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.api.model.TransactionResponse;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Card;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Merchant merchant = new Merchant("ISWKEN0001", "ISWKE");
                int lower = 100000000;
                int upper = 999999999;
                String transactionRef = String.valueOf((int) (Math.random() * (upper - lower)) + lower);
                String orderId = "MobP_" + String.valueOf((int) (Math.random() * (upper - lower)) + lower);

                Payment payment = new Payment("127", transactionRef, "MOBILE", "3TLP0001", "CRD", "KES", orderId);
                Customer customer = new Customer("1002");
                customer.setSecondName("Ongeri");
                Card card = new Card("4111111111111111", "123", "20", "02");
                new MobPay("IKIAB8FA9382D1FAC6FCA2F30195029B0A1558A9FECA", "dxdmtf12FhLVIFRz8IzhnuAJzNd6AAFVgx/3LlJHc+4=").makeCardPayment(
                        card,
                        merchant,
                        payment,
                        customer,
                        new TransactionSuccessCallback() {
                            @Override
                            public void onSuccess(TransactionResponse transactionResponse) {
                                Snackbar.make(view, "Transaction succeeded, ref:\t" + transactionResponse.getTransactionReference(), Snackbar.LENGTH_LONG)
                                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                                        .setAction("Action", null).show();
                            }
                        },
                        new TransactionFailureCallback() {
                            @Override
                            public void onError(Throwable error) {
                                Snackbar.make(view, "Transaction failed, reason:\t" + error.getMessage(), Snackbar.LENGTH_LONG)
                                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                                        .setAction("Action", null).show();
                            }
                        });
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
