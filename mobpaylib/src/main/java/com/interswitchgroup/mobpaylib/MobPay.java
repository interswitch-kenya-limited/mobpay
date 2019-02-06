package com.interswitchgroup.mobpaylib;

import com.interswitchgroup.mobpaylib.model.Invoice;
import com.interswitchgroup.mobpaylib.model.Transaction;
import com.interswitchgroup.mobpaylib.utils.NullChecker;

public class MobPay {
    /**
     * This method should take an invoice and launch a ui to take the user's preferred method of payment
     * and related details e.g. payment mode card and required card details.
     * Then once collected it will call the interswitch backend to make the payment and
     * call onSuccess or onFailure of the passed transaction callback
     *
     * @param invoice - The invoice to be paid
     */
    public static void pay(Invoice invoice, TransactionCallback transactionCallback) {
        NullChecker.checkNull(invoice, "Invoice must not be null");
        NullChecker.checkNull(transactionCallback, "Transaction callback must not be null");
        /*
        Launch ui
        collect card data
        build card payment payload
        send card payment request
        on response call appropriate transaction callback method
         */
    }

    public interface TransactionCallback {
        void onSuccess(Transaction transaction);

        void onUserCancel();

        void onError(Throwable error, Transaction transaction);
    }
}
