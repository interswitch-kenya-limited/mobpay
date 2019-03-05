package com.interswitchgroup.mobpaylib.api.model;

public class MerchantConfigResponse {

    private String responseCode;
    private String responseMessage;
    private Config config = new Config();

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public class Config {

        private String merchantId = "";
        private String merchantName = "";
        private String clientId = "";
        private String clientSecret = "";
        private int cardStatus;
        private int mpesaStatus;
        private int equitelStatus;
        private int tkashStatus;
        private int airtelStatus;
        private int paycodeStatus;
        private String mpesaPaybill = "";
        private String equitelPaybill = "";

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getMerchantName() {
            return merchantName;
        }

        public void setMerchantName(String merchantName) {
            this.merchantName = merchantName;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public int getCardStatus() {
            return cardStatus;
        }

        public void setCardStatus(int cardStatus) {
            this.cardStatus = cardStatus;
        }

        public int getMpesaStatus() {
            return mpesaStatus;
        }

        public void setMpesaStatus(int mpesaStatus) {
            this.mpesaStatus = mpesaStatus;
        }

        public int getEquitelStatus() {
            return equitelStatus;
        }

        public void setEquitelStatus(int equitelStatus) {
            this.equitelStatus = equitelStatus;
        }

        public int getTkashStatus() {
            return tkashStatus;
        }

        public void setTkashStatus(int tkashStatus) {
            this.tkashStatus = tkashStatus;
        }

        public int getAirtelStatus() {
            return airtelStatus;
        }

        public void setAirtelStatus(int airtelStatus) {
            this.airtelStatus = airtelStatus;
        }

        public int getPaycodeStatus() {
            return paycodeStatus;
        }

        public void setPaycodeStatus(int paycodeStatus) {
            this.paycodeStatus = paycodeStatus;
        }

        public String getMpesaPaybill() {
            return mpesaPaybill;
        }

        public void setMpesaPaybill(String mpesaPaybill) {
            this.mpesaPaybill = mpesaPaybill;
        }

        public String getEquitelPaybill() {
            return equitelPaybill;
        }

        public void setEquitelPaybill(String equitelPaybill) {
            this.equitelPaybill = equitelPaybill;
        }

    }
}

