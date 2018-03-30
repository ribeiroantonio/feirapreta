package br.com.feirapreta.model;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

    @GET("publication/search/{searchedText}")
    Call<ArrayList<Post>> searchByTag(@Path(value = "searchedText") String searchedText);

    @GET("publication/{postId}")
    Call<Post> readPost(@Path(value = "postId") String postId);


    // -- Voting

    @POST("eventscore")
    Call<ResponseBody> voting(@Body Vote value);


    public static final retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
            .baseUrl("http://feirapreta-001-site1.ctempurl.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
