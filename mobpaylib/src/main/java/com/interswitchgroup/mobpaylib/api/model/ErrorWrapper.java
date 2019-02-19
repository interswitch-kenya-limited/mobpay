package com.interswitchgroup.mobpaylib.api.model;


import java.util.ArrayList;
import java.util.List;

public class ErrorWrapper {

    private List<Error> errors = new ArrayList<>();
    private Error error;

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

}
