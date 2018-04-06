package br.com.feirapreta.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Publicacao {

    @SerializedName("result")
    @Expose
    private ArrayList<Result> result = null;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("isCanceled")
    @Expose
    private Boolean isCanceled;
    @SerializedName("isCompleted")
    @Expose
    private Boolean isCompleted;
    @SerializedName("isCompletedSuccessfully")
    @Expose
    private Boolean isCompletedSuccessfully;
    @SerializedName("creationOptions")
    @Expose
    private Integer creationOptions;
    @SerializedName("isFaulted")
    @Expose
    private Boolean isFaulted;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
        this.result = result;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsCanceled() {
        return isCanceled;
    }

    public void setIsCanceled(Boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public Boolean getIsCompletedSuccessfully() {
        return isCompletedSuccessfully;
    }

    public void setIsCompletedSuccessfully(Boolean isCompletedSuccessfully) {
        this.isCompletedSuccessfully = isCompletedSuccessfully;
    }

    public Integer getCreationOptions() {
        return creationOptions;
    }

    public void setCreationOptions(Integer creationOptions) {
        this.creationOptions = creationOptions;
    }

    public Boolean getIsFaulted() {
        return isFaulted;
    }

    public void setIsFaulted(Boolean isFaulted) {
        this.isFaulted = isFaulted;
    }


}
