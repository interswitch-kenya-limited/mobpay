package com.interswitchgroup.mobpayexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.api.model.TransactionResponse;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
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
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Merchant merchant = new Merchant("101", "lQVuoq1grpVZeQw7g/ztgiEn+XgmEatIO6tcVNZpP+I2l2fcTw0ZKIhkrxxajaivgY25ljyueNOBzqF/13lLlTKN/KVp4p391bEBsorCesKpxnji1k9GkIaL/QydGA+gC5h4GWtryslvFD/aBLYZ0YLzRIwBbHdK9UzTel2EgP5vjFonoXUngRnT9nIg0iDwBumZPN1hW6hcxflK7WmJ+nAX9oZK0z2Vi6LgIxfmgG2YGo4youb7EILZwh5xMMTiCHjyL7Vi4ZTkyKaJS/Xd1vvF6KJfsy7QER0qfDEo2NjyWBZcQRHsPG5KVWoH4W+mCHe0EpFyNKciBYgrSI8pYw==", "ISWKE");
        Payment payment = new Payment("100", "1234890", "MOBILE", "3TLP0001", "CRD", "KES");
        Customer customer = new Customer();
        new MobPay("IKIAB8FA9382D1FAC6FCA2F30195029B0A1558A9FECA", "dxdmtf12FhLVIFRz8IzhnuAJzNd6AAFVgx/3LlJHc+4=").pay(
                merchant,
                payment,
                customer,
                new TransactionSuccessCallback() {
                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {
                        Toast.makeText(getApplicationContext(), "Transaction succeeded, ref:\t" + transactionResponse.getTransactionReference(), Toast.LENGTH_LONG).show();
                    }
                },
                new TransactionFailureCallback() {
                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(getApplicationContext(), "Transaction failed, reason:\t" + error.getMessage(), Toast.LENGTH_LONG).show();
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
