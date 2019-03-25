***MobPay - An Android library for Interswitch payments integration***

This SDK library enables you to integrate Interswitch payments to your mobile app

**Adding to project**

To get the library add the following dependency to your module gradle file(app.gradle):

```groovy
implementation 'com.github.interswitch-kenya-limited:mobpay:0.0.2.001'
```

*Ensure that the jitpack io repository is part of your project* by adding it to the root build.gradle in the allprojects => repositories section

```groovy
maven { url "https://jitpack.io" }
```

**Usage examples**

To complete a payment using the SDK instantiate a mobpay object by calling 

```java
// Initialize payment and merchant details
final Merchant merchant = new Merchant(merchantId, domain);
final Payment payment = new Payment(amount, transactionRef, "MOBILE", terminalId, "CRD", currency, orderId);
payment.setPreauth(preauth);
final Customer customer = new Customer(customerId);
customer.setEmail(customerEmail);
Card card = new Card(cardNumber, cvv, expYear, expMonth);
card.setTokenize(tokenizeCheckbox.isChecked());

MobPay.Config config = new MobPay.Config();// May be used for advanced configuration, can be null
MobPay.getInstance(clientId, clientSecret, config)// Instantiate the mobpay library object to make a payment and get the results in the callbacks
        .pay(
        card,
        merchant,
        payment,
        customer,
        new TransactionSuccessCallback() { // Callback object to handle success
            @Override
            public void onSuccess(TransactionResponse transactionResponse) {
                // Your code to proceed to after success
            },
        new TransactionFailureCallback() {
            @Override
            public void onError(Throwable error) { // Callback object to handle failure
                // Code to handle failure
            }
        });
```