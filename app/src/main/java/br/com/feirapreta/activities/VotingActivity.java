package br.com.feirapreta.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import br.com.feirapreta.model.Vote;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.ProgressDialog;
import android.widget.RatingBar;

import br.com.feirapreta.R;
import br.com.feirapreta.model.RetrofitService;

public class VotingActivity extends AppCompatActivity {


    RatingBar ratingBar;
    private ProgressDialog dialog;
    private Vote voto = new Vote();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting2);
    }



    public void onClick(View v) {
        ratingBar = findViewById(R.id.ratingBar);
        Float value = ratingBar.getRating();
        voto.setValue(value);
        dialog = new ProgressDialog(VotingActivity.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);




        final Call<ResponseBody> call = retrofitService.voting(voto);




        call.enqueue(new Callback<ResponseBody>() {
            @Override


            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                if(response.code() == 200){
                    Toast.makeText(getBaseContext(), "Voto Enviado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(VotingActivity.this, "Não foi possível completar seu voto. Por favor, tente novamente." + response.code(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
