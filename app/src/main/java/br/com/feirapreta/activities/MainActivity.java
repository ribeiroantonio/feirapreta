package br.com.feirapreta.activities;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import br.com.feirapreta.R;
import br.com.feirapreta.adapter.HighlightsAdapter;
import br.com.feirapreta.model.Highlight;
import br.com.feirapreta.model.HighlightService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter highlightsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //TAG DO ERRO
    public static final String TAG = "DEU ERRO";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        String data[] = {"@teste1", "@teste2", "@teste3", "@teste4", "@teste5", "@teste6", "@teste7", "@teste8"};

        highlightsAdapter = new HighlightsAdapter(data);
        recyclerView.setAdapter(highlightsAdapter);


        //Aqui eu crio a classe e a variavel do retrofit e eu vou mandar a url base que foi
        //chamada neste caso no highlightService então eu vou no highlight e pego ela
        //no addconverter vou transformar tudo em GSON
        // e dps tudo isso vai ser retornado
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HighlightService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //polimorfismo (mano sla como explicar isso aqui funfo com isso )
        HighlightService service = retrofit.create(HighlightService.class);

        // pode fazer chamda pro end point
        Call<Highlight> requestHighLight = service.listHighlight();
        // da para fazer a chamada de dois jeito eu so entendi  um  que é esse aqui em baixo o outro jeito ta comentado e nao tenho
        //ideia de como é a utilização (os dois tem execução de modo diferente mais com o msm resultado)
        //requestHighLight.execute();
        requestHighLight.enqueue(new Callback<Highlight>() {
            @Override
            public void onResponse(Call<Highlight> call, Response<Highlight> response) {

                //quando tem alguma resposta e se
                if(response.isSuccessful()){
                    //caso retorne mais com erro (.code retorna o codigo)
                    Log.i("TAG","ERRO: " + response.code());
                }else{
                    //retorno da requisição com sucesso
                    Highlight highlight = response.body();

                    //aqui tu ja vai ter os bang então pode colocar em algum lugar exibir sla :v
                    //basicamente o GET é assim tony pelo oq eu entendi eu testei aqui funcionou usando outra API e outro projeto
                    //tenta ver se com isso você ja  consegue qualquer coisa fala cmg dps


                    //alias não vai funcionar ai pq vc tem que arrumar os bangs ip e tal só depois disso que deve funcionar
                    //obs tbm já coloquei a permissão de internet

                }

            }

            @Override
            public void onFailure(Call<Highlight> call, Throwable t) {
                //Mensagem caso de errado
                Log.e(TAG,"ERRO: " + t.getMessage());
            }
        });




    }

}
