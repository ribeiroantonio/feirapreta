package br.com.feirapreta.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import br.com.feirapreta.R;
import br.com.feirapreta.adapter.HighlightsAdapter;
import br.com.feirapreta.model.Person;
import br.com.feirapreta.model.Post;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private List<Post> highlights = new ArrayList<>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter highlightsAdapter;
    private RecyclerView.LayoutManager layoutManager;

    //TAG DO ERRO
    public static final String TAG = "DEU ERRO";

    //TOKEN AUTENTICAÇÂO
    private static final String TOKEN = "OTOKENFICAAQUI";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);

        loadHighlights();
        loadRVHighlights();

        //SharedPreferences preferences = getSharedPreferences(getString(R.string.token), 0);



        /*//Aqui eu crio a classe e a variavel do retrofit e eu vou mandar a url base que foi
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
        });*/




    }

    private void loadRVHighlights(){

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        highlightsAdapter = new HighlightsAdapter(highlights);
        recyclerView.setAdapter(highlightsAdapter);

    }

    private void loadHighlights(){
        

        Person user = new Person(1, "@teste", "Nome Completo", "imageURL");
        Post post = new Post(1, "https://www.instagram.com/antoniorib_/", true, "imageLowRes", "https://scontent.cdninstagram.com/vp/f075f763e093c53e2833b118425de020/5B414180/t51.2885-15/e35/p320x320/22581982_1359592954187449_120325497367298048_n.jpg", "", "", user);

        highlights.add(post);
        highlights.add(post);
        highlights.add(post);
        highlights.add(post);
        highlights.add(post);
        highlights.add(post);

    }

}
