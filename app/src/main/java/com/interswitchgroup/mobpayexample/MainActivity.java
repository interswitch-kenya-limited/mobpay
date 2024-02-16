package com.interswitchgroup.mobpayexample;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.interswitchgroup.mobpaylib.Config;
import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.api.model.TransactionResponse;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.CardToken;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Payment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText customerEmailField;
    private EditText customerIdField;
    private EditText amountField;
    private EditText clientIdField;
    private EditText clientSecretField;
    private EditText merchantIdField;
    private EditText domainField;
    private EditText terminalIdField;
    private EditText currencyField;
    private EditText cardNumberField;
    private EditText cvvField;
    private EditText expYearField;
    private EditText expMonthField;
    private EditText preauthField;
    private EditText orderIdField;
    private EditText transactionRefField;
    private EditText customIconUrlField;
    private CheckBox tokenizeCheckbox;
    private MultiSelectionSpinner paymentChannels;
    private MultiSelectionSpinner tokensSpinner;
    final ArrayList<CardToken> cardTokens = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customerEmailField = findViewById(R.id.customer_email);
        customerIdField = findViewById(R.id.customer_id);
        amountField = findViewById(R.id.amount);
        clientIdField = findViewById(R.id.client_id);
        clientSecretField = findViewById(R.id.client_secret);
        merchantIdField = findViewById(R.id.merchant_id);
        domainField = findViewById(R.id.domain);
        terminalIdField = findViewById(R.id.terminal_id);
        currencyField = findViewById(R.id.currency);
        cardNumberField = findViewById(R.id.card_field);
        cvvField = findViewById(R.id.cvv);
        expYearField = findViewById(R.id.expYear);
        expMonthField = findViewById(R.id.expMonth);
        preauthField = findViewById(R.id.preauth);
        orderIdField = findViewById(R.id.orderIdField);
        transactionRefField = findViewById(R.id.transactionRefField);
        customIconUrlField = findViewById(R.id.customUrlField);
        tokenizeCheckbox = findViewById(R.id.tokenization_checkBox);
        paymentChannels = findViewById(R.id.channels);
        List<String> channelNames = new ArrayList<>();
        for (MobPay.PaymentChannel channel : MobPay.PaymentChannel.class.getEnumConstants()) {
            channelNames.add(channel.value);
        }
        paymentChannels.setItems(channelNames);
        tokensSpinner = findViewById(R.id.tokens);
        cardTokens.clear();
        CardToken cardToken = new CardToken("C48FA7D7F466914A3E4440DE458AABC1914B9500CC7780BEB4", "02/20");
        cardToken.setPanLast4Digits("1895");
        cardToken.setPanFirst6Digits("506183");
        if (!cardTokens.contains(cardToken)) {
            cardTokens.add(cardToken);
        }
        cardToken = new CardToken("BE1C0A36255388EFB9AA39696CE32C6554FB1D88A77648A59C", "02/20");
        cardToken.setPanLast4Digits("1111");
        cardToken.setPanFirst6Digits("411111");
        if (!cardTokens.contains(cardToken)) {
            cardTokens.add(cardToken);
        }
        String[] cardTokenLabelsArray = new String[cardTokens.size()];
        for (int i = 0; i < cardTokens.size(); i++) {
            cardTokenLabelsArray[i] = cardTokens.get(i).toString();
        }
        tokensSpinner.setItems(cardTokenLabelsArray);
        findViewById(R.id.confirmTransactionPayment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setEnabled(false);
                String clientId = clientIdField.getText().toString();
                String clientSecret = clientSecretField.getText().toString();
                int lower = 100000000;
                int upper = 999999999;
                String transactionRef = String.valueOf((int) (Math.random() * (upper - lower)) + lower);

                MobPay mobPay;
                try {
                    mobPay = MobPay.getInstance(MainActivity.this, clientId, clientSecret, null);
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    view.setEnabled(true);
                    return;
                }
                mobPay.confirmTransactionPayment(transactionRef, new TransactionSuccessCallback() {
                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {
                        view.setEnabled(true);

                        Snackbar.make(view, "Transaction succeeded, ref:\t" + transactionResponse.getTransactionOrderId(), Snackbar.LENGTH_LONG)
                                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                                .setAction("Action", null).show();
                    }
                }, new TransactionFailureCallback() {
                    @Override
                    public void onError(Throwable error) {
                        view.setEnabled(true);

                        Snackbar.make(view, "Transaction failed, reason:\t" + error.getMessage(), Snackbar.LENGTH_LONG)
                                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                                .setAction("Action", null).show();
                    }
                });
            }
        });

        findViewById(R.id.launchPayWithWeb).setOnClickListener(view -> {
            view.setEnabled(false);
            MobPay mobPay;
            String clientId = clientIdField.getText().toString();
            String clientSecret = clientSecretField.getText().toString();
            String customerEmail = customerEmailField.getText().toString();
            String customerId = customerIdField.getText().toString();
            String amount = amountField.getText().toString();
            String merchantId = merchantIdField.getText().toString();
            String domain = domainField.getText().toString();
            String terminalId = terminalIdField.getText().toString();
            String currency = currencyField.getText().toString();
            String preauth = preauthField.getText().toString();
            String orderId = orderIdField.getText().toString();
            Config config = new Config();
            config.setIconUrl(customIconUrlField.getText().toString());
            config.setPrimaryAccentColor("#467ad2");
            config.setProviderIconUrl(customIconUrlField.getText().toString());
            config.setRedirectUrl("https://twitter.com/i/timeline");
            try {
                mobPay = MobPay.getInstance(MainActivity.this, clientId, clientSecret, config);
            }catch (Exception e){
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                view.setEnabled(true);
                return;
            }
            final Merchant merchant = new Merchant(merchantId, domain);
            int lower = 100000000;
            int upper = 999999999;
            String transactionRef = String.valueOf((int) (Math.random() * (upper - lower)) + lower);
            final Payment payment = new Payment(amount, transactionRef, "MOBILE", terminalId, "CRD", currency, orderId);
            payment.setPreauth(preauth);
            final Customer customer = new Customer(customerId,"firstName","secondName","allanbmageto@gmail.com","0713805241","NBI","KEN","00100","KIBIKO","KENYA");
            customer.setEmail(customerEmail);
            mobPay.pay(MainActivity.this,merchant, payment, customer, transactionResponse -> {
                view.setEnabled(true);
                Snackbar.make(view, "Transaction succeeded, ref:\t" + transactionResponse.getTransactionOrderId(), Snackbar.LENGTH_LONG)
                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                        .setAction("Action", null).show();
            }, error -> {
                view.setEnabled(true);
                Snackbar.make(view, "Transaction failed, reason:\t" + error.getMessage(), Snackbar.LENGTH_LONG)
                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                        .setAction("Action", null).show();
            });
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
