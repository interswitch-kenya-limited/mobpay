package com.interswitchgroup.mobpaylib.api.model;

public class MerchantConfigResponse {

    private String responseCode;
    private String responseMessage;
    private Config config;

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

    public enum TOKENIZATIONTYPE {
        ALWAYS(1), OPTIONAL(2), NEVER(3);
        private int value;

        TOKENIZATIONTYPE(int i) {
            value = i;
        }

        public int getValue() {
            return value;
        }
    }

    public class Config {

        private String merchantId;
        private String merchantName;
        private String clientId;
        private String clientSecret;
        private String cardStatus;
        private String mpesaStatus;
        private String equitelStatus;
        private String tkashStatus;
        private String airtelStatus;
        private String paycodeStatus;
        private String mpesaPaybill;
        private String equitelPaybill;
        private Integer tokenizeStatus;
        private Integer cardauthStatus;
        private Integer cardPreauth;
        private String domain;
        private String mmoStatus;
        private String bnkStatus;

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

        public String getCardStatus() {
            return cardStatus;
        }

        public void setCardStatus(String cardStatus) {
            this.cardStatus = cardStatus;
        }

        public String getMpesaStatus() {
            return mpesaStatus;
        }

        public void setMpesaStatus(String mpesaStatus) {
            this.mpesaStatus = mpesaStatus;
        }

        public String getEquitelStatus() {
            return equitelStatus;
        }

        public void setEquitelStatus(String equitelStatus) {
            this.equitelStatus = equitelStatus;
        }

        public String getTkashStatus() {
            return tkashStatus;
        }

        public void setTkashStatus(String tkashStatus) {
            this.tkashStatus = tkashStatus;
        }

        public String getAirtelStatus() {
            return airtelStatus;
        }

        public void setAirtelStatus(String airtelStatus) {
            this.airtelStatus = airtelStatus;
        }

        public String getPaycodeStatus() {
            return paycodeStatus;
        }

        public void setPaycodeStatus(String paycodeStatus) {
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

        public Integer getTokenizeStatus() {
            return tokenizeStatus;
        }

        public void setTokenizeStatus(Integer tokenizeStatus) {
            this.tokenizeStatus = tokenizeStatus;
        }

        public Integer getCardauthStatus() {
            return cardauthStatus;
        }

        public void setCardauthStatus(Integer cardauthStatus) {
            this.cardauthStatus = cardauthStatus;
        }

        public Integer getCardPreauth() {
            return cardPreauth;
        }

        public void setCardPreauth(Integer cardPreauth) {
            this.cardPreauth = cardPreauth;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getMmoStatus() {
            return mmoStatus;
        }

        public void setMmoStatus(String mmoStatus) {
            this.mmoStatus = mmoStatus;
        }

        public String getBnkStatus() {
            return bnkStatus;
        }

        public void setBnkStatus(String bnkStatus) {
            this.bnkStatus = bnkStatus;
        }

    }

}

