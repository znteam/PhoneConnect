package com.commonlib.phoneconnectlib.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.commonlib.phoneconnectlib.itf.OnItemPosClickListener;

import java.util.List;

/**
 * Created by zning
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<T> dataList;
    protected LayoutInflater inflater;
    protected OnItemPosClickListener<T> listener;

    public void setData(List<T> addList) {
        this.dataList = addList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    protected T getBeanByPos(int position) {
        if (dataList != null && dataList.size() > position) {
            return dataList.get(position);
        }
        return null;
    }

    protected View getInflaterView(ViewGroup parent, int layoutId) {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        return inflater.inflate(layoutId, parent, false);
    }

    public void setListener(OnItemPosClickListener<T> listener) {
        this.listener = listener;
    }

}
