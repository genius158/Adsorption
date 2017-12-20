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
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yan on 2017/11/5
 * recyclerView do not set marginTop or paddingTop
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
        if (((AdsorptionDataAdapter) items.get(position)).isAdsorption) {
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
        return new RecyclerView.ViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item, parent, false)) {
        };
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final View item = holder.itemView;
        item.setTag(R.id.tag_adsorption, items.get(position));
        item.setTag(R.id.tag_position, position);

        if (getItemViewType(position) == ADSORPTION_TYPE) {
            TextView tv = item.findViewById(R.id.tv);
            tv.setText(((AdsorptionItem) ((AdsorptionDataAdapter) items.get(position)).adsorptionData).strIndex);
            return;
        }

        ImageView iv = item.findViewById(R.id.iv);
        iv.setImageDrawable(ContextCompat.getDrawable(context, ((DataItem) ((AdsorptionDataAdapter) items.get(position)).itemData).resId));
    }

    private void onAdsorptionViewLoad() {
        try {
            if (recyclerView.getParent() == null) {
                throw new RuntimeException("RecyclerView must put in a ViewGroup");
            }
            ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
            if (adsorptionView == null) {
                adsorptionView = LayoutInflater.from(context).inflate(R.layout.adsorption, viewGroup, false);
                adsorptionView.setOnClickListener(onAdsorptionClick);

                viewGroup.addView(adsorptionView);
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
            AdsorptionDataAdapter adsorption = (AdsorptionDataAdapter) adsorptionAreaView.getTag(R.id.tag_adsorption);

            float baseY = recyclerView.getY();

            if (adsorption.isAdsorption) {
                adsorptionView.setTranslationY(adsorptionAreaView.getY() - adsorptionView.getHeight() + baseY);
                setAdsorptionData(adsorptionOutView);
            } else {
                setAdsorptionData(adsorptionAreaView);
                if (adsorptionView.getTranslationY() != baseY) {
                    adsorptionView.setTranslationY(baseY);
                }
            }
        }
    };

    @SuppressLint("StaticFieldLeak")
    private void setAdsorptionData(View v) {
        AdsorptionDataAdapter adsorption = (AdsorptionDataAdapter) v.getTag(R.id.tag_adsorption);
        TextView tv = adsorptionView.findViewById(R.id.tv);
        String data = ((AdsorptionItem) adsorption.adsorptionData).strIndex;
        if (adsorptionView.getTag() != null && TextUtils.equals(tv.getText(), data)) {
            return;
        }
        tv.setText(((AdsorptionItem) adsorption.adsorptionData).strIndex);

        int position = (int) v.getTag(R.id.tag_position);

        if (adsorptionPositionTask != null && !adsorptionPositionTask.isCancelled()) {
            adsorptionPositionTask.cancel(true);
        }
        adsorptionPositionTask = new AsyncTask<Integer, Void, Integer>() {
            @Override
            protected Integer doInBackground(Integer... datas) {
                int position = datas[0];
                AdsorptionDataAdapter dataAdapter = (AdsorptionDataAdapter) items.get(position);
                if (dataAdapter.isAdsorption) {
                    return position;
                }

                AdsorptionItem ai = (AdsorptionItem) dataAdapter.adsorptionData;
                for (int i = 0; i < items.size(); i++) {
                    AdsorptionDataAdapter ada = (AdsorptionDataAdapter) items.get(i);
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
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        onAdsorptionViewLoad();
    }

    @Override
    protected boolean areItemsTheSame(Object oldItem, Object newItem) {
        if ((oldItem instanceof AdsorptionItem) && (newItem instanceof AdsorptionItem)) {
            return ((AdsorptionItem) oldItem).strIndex.equals(((AdsorptionItem) newItem).strIndex);

        } else if ((oldItem instanceof DataItem) && (newItem instanceof DataItem)) {
            return ((DataItem) oldItem).resId == ((DataItem) newItem).resId;
        }
        return false;
    }

    @Override
    protected boolean areContentsTheSame(Object oldItem, Object newItem) {
        return areItemsTheSame(oldItem, newItem);
    }
}