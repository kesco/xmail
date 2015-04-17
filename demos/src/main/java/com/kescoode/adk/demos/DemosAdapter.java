package com.kescoode.adk.demos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kesco on 15/4/11.
 */
public class DemosAdapter extends RecyclerView.Adapter<DemosAdapter.ViewHolder> {
    private final Context context;
    private final List<String> array = new ArrayList<>(50);

    public DemosAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 50; i++) {
            array.add("haha: " + i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        return new ViewHolder(tv);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.setText(array.get(i));
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setText(String text) {
            ((TextView) itemView).setText(text);
        }
    }
}
