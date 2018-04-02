package br.com.feirapreta.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmar Voto");
        builder.setMessage("Você em certeza que deseja dar essa nota?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {


                ratingBar = findViewById(R.id.ratingBar);
                Float value = ratingBar.getRating();
                voto.setValue(value);
                dialog = new ProgressDialog(VotingActivity.this);

                RetrofitService retrofitService = RetrofitService.retrofit.create(RetrofitService.class);




                final Call<ResponseBody> call = retrofitService.voting(voto);




                call.enqueue(new Callback<ResponseBody>() {
                    @Override


                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        if(response.code() == 200){
                            Toast.makeText(getBaseContext(), "Voto Enviado com sucesso", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(VotingActivity.this, "Não foi possível completar seu voto. Por favor, tente novamente." + response.code(), Toast.LENGTH_SHORT).show();
                        }
                        Intent aboutIntent = new Intent(VotingActivity.this, MainActivity.class);
                        startActivity(aboutIntent);


                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                            Toast.makeText(getBaseContext(), "Não foi possível fazer a conexão", Toast.LENGTH_SHORT).show();

                    }
                });

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

}
