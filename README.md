# MobPay - An Android library for integrating card and mobile payments through Interswitch

This SDK library enables you to integrate Interswitch payments to your mobile app

## Adding it to a project

To get the library add the following dependency to your module gradle file(app.gradle):

```groovy
implementation 'com.github.interswitch-kenya-limited:mobpay:0.3.0'
```

*Ensure that the jitpack io repository is part of your project* by adding it to the root build.gradle in the allprojects => repositories section

```groovy
maven { url "https://jitpack.io" }
```
### Configuring
Edit you app manifest file to add the following configuration metadata inside the application tag, the values given here are for test and you will need to change them once you are ready to go live.

```xml
        <meta-data
            android:name="interswitch-kenya-limited.mobpay.base_url"
            android:value="https://testids.interswitch.co.ke:18082/api/v1/" />
        <meta-data
            android:name="interswitch-kenya-limited.mobpay.mqtt_url"
            android:value="tcp://testmerchant.interswitch-ke.com:1883" />
        <meta-data
            android:name="interswitch-kenya-limited.mobpay.cardinal_url"
            android:value="https://testmerchant.interswitch-ke.com/sdkcardinal" />
        <meta-data
            android:name="interswitch-kenya-limited.mobpay.modulus"
            android:value="9c7b3ba621a26c4b02f48cfc07ef6ee0aed8e12b4bd11c5cc0abf80d5206be69e1891e60fc88e2d565e2fabe4d0cf630e318a6c721c3ded718d0c530cdf050387ad0a30a336899bbda877d0ec7c7c3ffe693988bfae0ffbab71b25468c7814924f022cb5fda36e0d2c30a7161fa1c6fb5fbd7d05adbef7e68d48f8b6c5f511827c4b1c5ed15b6f20555affc4d0857ef7ab2b5c18ba22bea5d3a79bd1834badb5878d8c7a4b19da20c1f62340b1f7fbf01d2f2e97c9714a9df376ac0ea58072b2b77aeb7872b54a89667519de44d0fc73540beeaec4cb778a45eebfbefe2d817a8a8319b2bc6d9fa714f5289ec7c0dbc43496d71cf2a642cb679b0fc4072fd2cf" />
        <meta-data
            android:name="interswitch-kenya-limited.mobpay.exponent"
            android:value="010001" />
```
Finally ensure data binding is enabled in the root gradle file of your projects by adding the following config in its android section

```groovy
dataBinding {
    enabled = true
}
```

## Usage examples

Get an interswitch client Id and client secret for your interswitch merchant account then instantiate a mobpay object by doing the following:

```java
// Initialize payment and merchant details
final Merchant merchant = new Merchant(merchantId, domain);
final Payment payment = new Payment(amount, transactionRef, "MOBILE", terminalId, "CRD", currency, orderId);
payment.setPreauth(preauth);
final Customer customer = new Customer(customerId);
customer.setEmail(customerEmail);

MobPay.Config config = new MobPay.Config();// May be used for advanced configuration, can be null

config.setIconUrl('YOUR ICONS URL') can be used to change the logo


mobPay = MobPay.getInstance(MainActivity.this, clientId, clientSecret, config);// Instantiate the mobpay library object to make a payment and get the results in the callbacks
mobPay.pay(MainActivity.this,// The instance of an activity that will be active until the payment is completed
        merchant,
        payment,
        customer,
        new TransactionSuccessCallback() {
            @Override
            public void onSuccess(TransactionResponse transactionResponse) {
                // Your code to proceed to after success
            }
        },
        new TransactionFailureCallback() {
            @Override
            public void onError(Throwable error) {
                // Code to handle failure
            }
        });
```

## Source code

Visit https://github.com/interswitch-kenya-limited/mobpay/ to get the source code and releases of this project if you want to try a manual integration process that does not make use of gradle.
