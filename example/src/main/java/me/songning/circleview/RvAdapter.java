package me.songning.circleview;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.songning.library.CircleView;

/**
 * Created by Nicholas on 2016/12/21.
 */

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {

    List<String> mList;

    public RvAdapter(List<String> list) {
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList == null)
            return;
        String text = mList.get(position);
        holder.mTextView.setText(text);
        holder.mCircleView.setText(text);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public CircleView mCircleView;
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCircleView = (CircleView) itemView.findViewById(R.id.circle_view);
            mTextView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

}
