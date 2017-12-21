package com.yan.recyclerviewadsorption;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yan on 2017/11/5
 */
public class AdsorptionAdapter extends BaseDiffAdapter<Object, RecyclerView.ViewHolder> {
    private static final String TAG = "AdsorptionAdapter";

    private final int ADSORPTION_TYPE = 1;
    private final int DATA_TYPE = 2;

    private Context context;

    private RecyclerView recyclerView;

    private View adsorptionView;

    private AsyncTask adsorptionPositionTask;

    public AdsorptionAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (((ItemAdsorptionAdapter) items.get(position)).isAdsorption) {
            return ADSORPTION_TYPE;
        }
        return DATA_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ADSORPTION_TYPE) {
            View typeView = LayoutInflater.from(context).inflate(R.layout.adsorption, parent, false);
            return new RecyclerView.ViewHolder(typeView) {
            };
        }
        return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final View item = holder.itemView;
        item.setTag(R.id.tag_adsorption, items.get(position));
        item.setTag(R.id.tag_position, position);
        ItemAdsorptionAdapter ada = (ItemAdsorptionAdapter) items.get(position);
        if (getItemViewType(position) == ADSORPTION_TYPE) {
            TextView tv = item.findViewById(R.id.tv);
            tv.setText(((ItemAdsorption) ada.adsorptionData).strIndex);
            return;
        }

        ImageView iv = item.findViewById(R.id.iv);
        iv.setImageDrawable(ContextCompat.getDrawable(context, ((ItemData) ada.itemData).resId));
    }

    private void onAdsorptionViewLoad() {
        try {
            if (recyclerView.getParent() == null) {
                throw new RuntimeException("RecyclerView must put in a ViewGroup");
            }
            ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
            if (adsorptionView == null) {
                FrameLayout frameLayout = new FrameLayout(context);
                frameLayout.setLayoutParams(recyclerView.getLayoutParams());
                viewGroup.addView(frameLayout);

                adsorptionView = LayoutInflater.from(context).inflate(R.layout.adsorption, frameLayout, false);
                adsorptionView.setOnClickListener(onAdsorptionClick);

                frameLayout.addView(adsorptionView);
                frameLayout.setVisibility(View.INVISIBLE);
            }
            recyclerView.addOnScrollListener(onScrollListener);

        } catch (Exception e) {
            Log.w(TAG, "onAttachedToRecyclerView: " + e.getMessage());
        }
    }

    private final View.OnClickListener onAdsorptionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int adsorptionPosition = (int) v.getTag();
            recyclerView.scrollToPosition(adsorptionPosition);
        }
    };

    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            View adsorptionOutView = recyclerView.findChildViewUnder(0, 0);
            View adsorptionAreaView = recyclerView.findChildViewUnder(0, adsorptionView.getHeight());
            if (adsorptionOutView == null || adsorptionAreaView == null) {
                return;
            }
            ItemAdsorptionAdapter adsorption = (ItemAdsorptionAdapter) adsorptionAreaView.getTag(R.id.tag_adsorption);

            if (adsorption.isAdsorption) {
                setAdsorptionData(adsorptionOutView);
                adsorptionView.setTranslationY(adsorptionAreaView.getY() - adsorptionView.getHeight());
            } else {
                setAdsorptionData(adsorptionAreaView);
                if (adsorptionView.getTranslationY() != 0) {
                    adsorptionView.setTranslationY(0);
                }
            }
        }
    };

    @SuppressLint("StaticFieldLeak")
    private void setAdsorptionData(View v) {
        ItemAdsorptionAdapter adsorption = (ItemAdsorptionAdapter) v.getTag(R.id.tag_adsorption);
        TextView tv = adsorptionView.findViewById(R.id.tv);
        String data = ((ItemAdsorption) adsorption.adsorptionData).strIndex;
        if (adsorptionView.getTag() != null && TextUtils.equals(tv.getText(), data)) {
            return;
        }
        tv.setText(((ItemAdsorption) adsorption.adsorptionData).strIndex);

        int position = (int) v.getTag(R.id.tag_position);

        if (adsorptionPositionTask != null && !adsorptionPositionTask.isCancelled()) {
            adsorptionPositionTask.cancel(true);
        }
        adsorptionPositionTask = new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... datas) {
                int position = datas[0];
                ItemAdsorptionAdapter dataAdapter = (ItemAdsorptionAdapter) items.get(position);
                if (dataAdapter.isAdsorption) {
                    return position;
                }

                ItemAdsorption ai = (ItemAdsorption) dataAdapter.adsorptionData;
                for (int i = 0; i < items.size(); i++) {
                    ItemAdsorptionAdapter ada = (ItemAdsorptionAdapter) items.get(i);
                    if (ada.itemData == null && ada.adsorptionData == ai) {
                        return i;
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                if (integer != null) {
                    adsorptionView.setTag(integer);
                }
            }
        }.execute(position);
    }

    @Override
    public void replace(List<Object> update) {
        super.replace(update);
        View parent = (View) adsorptionView.getParent();
        parent.setVisibility((update == null || update.isEmpty()) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        onAdsorptionViewLoad();
    }

    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if ((oldItem instanceof ItemAdsorption) && (newItem instanceof ItemAdsorption)) {
            return ((ItemAdsorption) oldItem).strIndex.equals(((ItemAdsorption) newItem).strIndex);

        } else if ((oldItem instanceof ItemData) && (newItem instanceof ItemData)) {
            return ((ItemData) oldItem).resId == ((ItemData) newItem).resId;
        }
        return false;
    }

    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return areItemsTheSame(oldItem, newItem);
    }
}