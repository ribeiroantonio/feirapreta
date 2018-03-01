package br.com.feirapreta.activities;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import br.com.feirapreta.R;
import br.com.feirapreta.adapter.HighlightsAdapter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter highlightsAdapter;
    private RecyclerView.LayoutManager layoutManager;

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

    }
}
