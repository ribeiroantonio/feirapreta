package br.com.feirapreta.model;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by HyanSiqueira on 01/03/2018.
 */

public interface RetrofitService {
    //Escreve aqui o IP que estará o back
    String BASE_URL="http://feirapreta-001-site1.ctempurl.com/api/";

    //Aqui fica definido o metodo que sera feito no highlight
    // no @GET () entre parenteses coloca o final do ip por exemplo o i /highlights para pegar os highlights
    @GET("publication")
    Call<ArrayList<Post>> listHighlight();

    @POST("publication/search/")
    Call<ArrayList<Post>> searchByTag(@Field("search") String tag);
}
