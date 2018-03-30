package br.com.feirapreta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Created by Antonio on 29/03/2018.
 */

public class Vote {

    @SerializedName("value")
    @Expose
    private Float value;

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

}
