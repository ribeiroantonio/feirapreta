package br.com.feirapreta.model;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by HyanSiqueira on 01/03/2018.
 */

public interface HighlightService {
    //Escreve aqui o IP que estará o back
    public static final String BASE_URL="000.000.000.000/";

    //Aqui fica definido o metodo que sera feito no highlight
    // no @GET () entre parenteses coloca o final do ip por exemplo o i /highlights para pegar os highlights
    @GET("highlights")
    Call<Post> listHighlight();
}
