package com.yan.recyclerviewadsorption;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Object> objects = new ArrayList<>();
        ItemAdsorptionAdapter<ItemAdsorption, ItemData> ai1;
        objects.add(ai1 = new ItemAdsorptionAdapter<>(true, new ItemAdsorption("星期一")));
        objects.add(ai1.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ai1.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ai1.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ai1.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ai1.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));

        ItemAdsorptionAdapter<ItemAdsorption, ItemData> ai2;
        objects.add(ai2 = new ItemAdsorptionAdapter<>(true, new ItemAdsorption("星期二")));
        objects.add(ai2.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ai2.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ai2.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ai2.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ai2.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));

        ItemAdsorptionAdapter<ItemAdsorption, ItemData> ai3;
        objects.add(ai3 = new ItemAdsorptionAdapter<>(true, new ItemAdsorption("星期三")));
        objects.add(ai3.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ai3.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ai3.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ai3.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ai3.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));

        final ItemAdsorptionAdapter<ItemAdsorption, ItemData> ai4;
        objects.add(ai4 = new ItemAdsorptionAdapter<>(true, new ItemAdsorption("星期四")));
        objects.add(ai4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ai4.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ai4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ai4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ai4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ai4.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ai4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        final AdsorptionAdapter adsorptionAdapter = new AdsorptionAdapter(this);
        rv.setAdapter(adsorptionAdapter);

        adsorptionAdapter.replace(objects);
    }
}
