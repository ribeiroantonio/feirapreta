package br.com.feirapreta.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    private Float voto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting2);
    }

    public void onClick(View v) {
        ratingBar = findViewById(R.id.ratingBar);
        voto = ratingBar.getRating();
        dialog = new ProgressDialog(VotingActivity.this);
        dialog.setMessage("Carregando...");
        dialog.setCancelable(false);
        dialog.show();
        RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);

        final Call<Void> call = retrofitService.voting(voto);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(getBaseContext(), "Voto Enviado com sucesso", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
