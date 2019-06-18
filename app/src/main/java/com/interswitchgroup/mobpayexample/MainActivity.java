package com.interswitchgroup.mobpayexample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.interswitchgroup.mobpaylib.MobPay;
import com.interswitchgroup.mobpaylib.api.model.CardPaymentResponse;
import com.interswitchgroup.mobpaylib.api.model.TransactionResponse;
import com.interswitchgroup.mobpaylib.interfaces.TransactionFailureCallback;
import com.interswitchgroup.mobpaylib.interfaces.TransactionSuccessCallback;
import com.interswitchgroup.mobpaylib.model.Card;
import com.interswitchgroup.mobpaylib.model.CardToken;
import com.interswitchgroup.mobpaylib.model.Customer;
import com.interswitchgroup.mobpaylib.model.Merchant;
import com.interswitchgroup.mobpaylib.model.Mobile;
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
        tokenizeCheckbox = findViewById(R.id.tokenization_checkBox);
        paymentChannels = findViewById(R.id.channels);
        List<String> channelNames = new ArrayList<>();
        for (MobPay.PaymentChannel channel : MobPay.PaymentChannel.class.getEnumConstants()) {
            channelNames.add(channel.value);
        }
        paymentChannels.setItems(channelNames);
        tokensSpinner = findViewById(R.id.tokens);
        cardTokens.clear();
        final CardToken cardToken = new CardToken("C48FA7D7F466914A3E4440DE458AABC1914B9500CC7780BEB4", "02/20");
        cardToken.setPanLast4Digits("1895");
        cardToken.setPanFirst6Digits("506183");
        cardTokens.add(cardToken);
        String[] cardTokenLabelsArray = new String[cardTokens.size()];
        for (int i = 0; i < cardTokens.size(); i++) {
            cardTokenLabelsArray[i] = cardTokens.get(i).toString();
        }
        tokensSpinner.setItems(cardTokenLabelsArray);
        findViewById(R.id.cardPaymentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setEnabled(false);
                String customerEmail = customerEmailField.getText().toString();
                String customerId = customerIdField.getText().toString();
                String amount = amountField.getText().toString();
                String clientId = clientIdField.getText().toString();
                String clientSecret = clientSecretField.getText().toString();
                String merchantId = merchantIdField.getText().toString();
                String domain = domainField.getText().toString();
                String terminalId = terminalIdField.getText().toString();
                String currency = currencyField.getText().toString();
                String cardNumber = cardNumberField.getText().toString();
                String cvv = cvvField.getText().toString();
                String expYear = expYearField.getText().toString();
                String expMonth = expMonthField.getText().toString();
                String preauth = preauthField.getText().toString();
                String orderId = orderIdField.getText().toString();

                int lower = 100000000;
                int upper = 999999999;
                String transactionRef = String.valueOf((int) (Math.random() * (upper - lower)) + lower);

                final Merchant merchant = new Merchant(merchantId, domain);
                final Payment payment = new Payment(amount, transactionRef, "MOBILE", terminalId, "CRD", currency, orderId);
                payment.setPreauth(preauth);
                final Customer customer = new Customer(customerId);
                customer.setEmail(customerEmail);
                Card card = new Card(cardNumber, cvv, expYear, expMonth);
                card.setTokenize(tokenizeCheckbox.isChecked());
                MobPay mobPay;
                try {
                    MobPay.Config config = new MobPay.Config();
                    mobPay = MobPay.getInstance(MainActivity.this, clientId, clientSecret, config);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    view.setEnabled(true);
                    return;
                }
                mobPay.makeCardPayment(
                        card,
                        merchant,
                        payment,
                        customer,
                        new TransactionSuccessCallback() {
                            @Override
                            public void onSuccess(TransactionResponse transactionResponse) {
                                view.setEnabled(true);
                                Snackbar.make(view, "Transaction succeeded, ref:\t" + transactionResponse.getTransactionOrderId(), Snackbar.LENGTH_LONG)
                                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                                        .setAction("Action", null).show();
                                if (transactionResponse instanceof CardPaymentResponse) {
                                    CardPaymentResponse cardPaymentResponse = (CardPaymentResponse) transactionResponse;
                                    if (cardPaymentResponse.getToken() != null && !cardPaymentResponse.getToken().isEmpty()) {
                                        CardToken token = new CardToken(cardPaymentResponse.getToken(), cardPaymentResponse.getExpiry());
                                        token.setPanFirst6Digits(cardPaymentResponse.getPanFirst6Digits());
                                        token.setPanLast4Digits(cardPaymentResponse.getPanLast4Digits());
                                        cardTokens.add(token);
                                        String[] cardTokenLabelsArray = new String[cardTokens.size()];
                                        for (int i = 0; i < cardTokens.size(); i++) {
                                            cardTokenLabelsArray[i] = cardTokens.get(i).toString();
                                        }
                                        tokensSpinner.setItems(cardTokenLabelsArray);
                                    }
                                }
                            }
                        },
                        new TransactionFailureCallback() {
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
        findViewById(R.id.launchUiButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setEnabled(false);
                String customerEmail = customerEmailField.getText().toString();
                String customerId = customerIdField.getText().toString();
                String amount = amountField.getText().toString();
                String clientId = clientIdField.getText().toString();
                String clientSecret = clientSecretField.getText().toString();
                String merchantId = merchantIdField.getText().toString();
                String domain = domainField.getText().toString();
                String terminalId = terminalIdField.getText().toString();
                String currency = currencyField.getText().toString();
                String preauth = preauthField.getText().toString();
                String orderId = orderIdField.getText().toString();

                final Merchant merchant = new Merchant(merchantId, domain);
                int lower = 100000000;
                int upper = 999999999;
                String transactionRef = String.valueOf((int) (Math.random() * (upper - lower)) + lower);

                final Payment payment = new Payment(amount, transactionRef, "MOBILE", terminalId, "CRD", currency, orderId);
                payment.setPreauth(preauth);
                final Customer customer = new Customer(customerId);
                customer.setEmail(customerEmail);
                List<MobPay.PaymentChannel> selectedPaymentChannels = new ArrayList<>();
                for (int selectedIndex : paymentChannels.getSelectedIndicies()) {
                    selectedPaymentChannels.add(MobPay.PaymentChannel.class.getEnumConstants()[selectedIndex]);
                }
                List<CardToken> selectedTokens = new ArrayList<>();
                for (int selectedTokenIndex : tokensSpinner.getSelectedIndicies()) {
                    selectedTokens.add(cardTokens.get(selectedTokenIndex));
                }
                MobPay mobPay;
                try {
                    MobPay.Config config = new MobPay.Config();
                    config.setChannels(selectedPaymentChannels.toArray(new MobPay.PaymentChannel[0]));
                    config.setCardTokens(selectedTokens);
                    mobPay = MobPay.getInstance(MainActivity.this, clientId, clientSecret, config);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    view.setEnabled(true);
                    return;
                }
                mobPay.pay(MainActivity.this,
                        merchant,
                        payment,
                        customer,
                        new TransactionSuccessCallback() {
                            @Override
                            public void onSuccess(TransactionResponse transactionResponse) {
                                view.setEnabled(true);
                                Snackbar.make(view, "Transaction succeeded, ref:\t" + transactionResponse.getTransactionOrderId(), Snackbar.LENGTH_LONG)
                                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                                        .setAction("Action", null).show();
                                if (transactionResponse instanceof CardPaymentResponse) {
                                    CardPaymentResponse cardPaymentResponse = (CardPaymentResponse) transactionResponse;
                                    if (cardPaymentResponse.getToken() != null && !cardPaymentResponse.getToken().isEmpty()) {
                                        CardToken token = new CardToken(cardPaymentResponse.getToken(), cardPaymentResponse.getExpiry());
                                        token.setPanFirst6Digits(cardPaymentResponse.getPanFirst6Digits());
                                        token.setPanLast4Digits(cardPaymentResponse.getPanLast4Digits());
                                        cardTokens.add(token);
                                        String[] cardTokenLabelsArray = new String[cardTokens.size()];
                                        for (int i = 0; i < cardTokens.size(); i++) {
                                            cardTokenLabelsArray[i] = cardTokens.get(i).toString();
                                        }
                                        tokensSpinner.setItems(cardTokenLabelsArray);
                                    }
                                }

                            }
                        },
                        new TransactionFailureCallback() {
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

        findViewById(R.id.mobilePaymentButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                view.setEnabled(false);
                String customerEmail = customerEmailField.getText().toString();
                String customerId = customerIdField.getText().toString();
                String amount = amountField.getText().toString();
                String clientId = clientIdField.getText().toString();
                String clientSecret = clientSecretField.getText().toString();
                String merchantId = merchantIdField.getText().toString();
                String domain = domainField.getText().toString();
                String terminalId = terminalIdField.getText().toString();
                String currency = currencyField.getText().toString();
                String preauth = preauthField.getText().toString();
                String orderId = orderIdField.getText().toString();

                final Merchant merchant = new Merchant(merchantId, domain);
                int lower = 100000000;
                int upper = 999999999;
                String transactionRef = String.valueOf((int) (Math.random() * (upper - lower)) + lower);

                final Payment payment = new Payment(amount, transactionRef, "MOBILE", terminalId, "CRD", currency, orderId);
                payment.setPreauth(preauth);
                final Customer customer = new Customer(customerId);
                customer.setEmail(customerEmail);
                List<MobPay.PaymentChannel> selectedPaymentChannels = new ArrayList<>();
                for (int selectedIndex : paymentChannels.getSelectedIndicies()) {
                    selectedPaymentChannels.add(MobPay.PaymentChannel.class.getEnumConstants()[selectedIndex]);
                }
                Mobile mobile = new Mobile("0713365290", Mobile.Type.MPESA);
                MobPay mobPay;
                try {
                    mobPay = MobPay.getInstance(MainActivity.this, clientId, clientSecret, null);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    view.setEnabled(true);
                    return;
                }
                mobPay.makeMobileMoneyPayment(mobile, merchant,
                        payment,
                        customer,
                        new TransactionSuccessCallback() {
                            @Override
                            public void onSuccess(TransactionResponse transactionResponse) {
                                view.setEnabled(true);
                                Snackbar.make(view, "Transaction succeeded, ref:\t" + transactionResponse.getTransactionOrderId(), Snackbar.LENGTH_LONG)
                                        .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                                        .setAction("Action", null).show();
                            }
                        },
                        new TransactionFailureCallback() {
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
