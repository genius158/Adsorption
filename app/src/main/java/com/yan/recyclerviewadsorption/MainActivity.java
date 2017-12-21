package com.yan.recyclerviewadsorption;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final List<Object> objects = new ArrayList<>();
        ItemAdsorptionAdapter<ItemAdsorption, ItemData> ia1;
        objects.add(ia1 = new ItemAdsorptionAdapter<>(true, new ItemAdsorption("星期一")));
        objects.add(ia1.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia1.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia1.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia1.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia1.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));

        ItemAdsorptionAdapter<ItemAdsorption, ItemData> ia2;
        objects.add(ia2 = new ItemAdsorptionAdapter<>(true, new ItemAdsorption("星期二")));
        objects.add(ia2.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia2.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia2.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia2.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia2.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));

        ItemAdsorptionAdapter<ItemAdsorption, ItemData> ia3;
        objects.add(ia3 = new ItemAdsorptionAdapter<>(true, new ItemAdsorption("星期三")));
        objects.add(ia3.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia3.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia3.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia3.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia3.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));

        final ItemAdsorptionAdapter<ItemAdsorption, ItemData> ia4;
        objects.add(ia4 = new ItemAdsorptionAdapter<>(true, new ItemAdsorption("星期四")));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher)));
        objects.add(ia4.clone().setItemData(new ItemData(R.mipmap.ic_launcher_round)));

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new SmoothScrollLayoutManager(this));
        final AdsorptionAdapter adsorptionAdapter = new AdsorptionAdapter(this);
        rv.setAdapter(adsorptionAdapter);

        adsorptionAdapter.replace(objects);
    }

}
