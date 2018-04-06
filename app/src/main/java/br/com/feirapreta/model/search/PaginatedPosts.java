package br.com.feirapreta.model.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaginatedPosts {

    @SerializedName("page")
    @Expose
    private Page page;
    @SerializedName("publicacao")
    @Expose
    private Publicacao publicacao;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Publicacao getPublicacao() {
        return publicacao;
    }

    public void setPublicacao(Publicacao publicacao) {
        this.publicacao = publicacao;
    }

}
