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
        private Integer cardStatus;
        private Integer mpesaStatus;
        private Integer equitelStatus;
        private Integer tkashStatus;
        private Integer airtelStatus;
        private Integer paycodeStatus;
        private String mpesaPaybill;
        private String equitelPaybill;
        private Integer tokenizeStatus;
        private Integer cardauthStatus;
        private Integer cardPreauth;

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

        public Integer getCardStatus() {
            return cardStatus;
        }

        public void setCardStatus(Integer cardStatus) {
            this.cardStatus = cardStatus;
        }

        public Integer getMpesaStatus() {
            return mpesaStatus;
        }

        public void setMpesaStatus(Integer mpesaStatus) {
            this.mpesaStatus = mpesaStatus;
        }

        public Integer getEquitelStatus() {
            return equitelStatus;
        }

        public void setEquitelStatus(Integer equitelStatus) {
            this.equitelStatus = equitelStatus;
        }

        public Integer getTkashStatus() {
            return tkashStatus;
        }

        public void setTkashStatus(Integer tkashStatus) {
            this.tkashStatus = tkashStatus;
        }

        public Integer getAirtelStatus() {
            return airtelStatus;
        }

        public void setAirtelStatus(Integer airtelStatus) {
            this.airtelStatus = airtelStatus;
        }

        public Integer getPaycodeStatus() {
            return paycodeStatus;
        }

        public void setPaycodeStatus(Integer paycodeStatus) {
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
    }
}

