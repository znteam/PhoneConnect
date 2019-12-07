package com.commonlib.phoneconnectlib.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.commonlib.phoneconnectlib.R;
import com.commonlib.phoneconnectlib.bean.PhoneConnectUserBean;

import java.util.ArrayList;
import java.util.List;

public class PhoneConnectRvAdapter extends BaseRecyclerViewAdapter<PhoneConnectUserBean> {

    private List<PhoneConnectUserBean> selectUserList = new ArrayList<>();

    @Override
    public void setData(List<PhoneConnectUserBean> addList) {
        super.setData(addList);
        selectUserList.clear();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PhoneConnectHolder(getInflaterView(parent, R.layout.item_phone_connect));
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final PhoneConnectUserBean item = getBeanByPos(position);
        if (item == null) {
            return;
        }
        if (holder instanceof PhoneConnectHolder) {
            ((PhoneConnectHolder) holder).setData(item, position);
            ((PhoneConnectHolder) holder).setStatus(selectUserList.contains(item));
            ((PhoneConnectHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectUserList.contains(item)) {
                        selectUserList.remove(item);
                    } else {
                        if (selectUserList.size() >= 10) {
                            Toast.makeText(holder.itemView.getContext(), "一次最多可选10人", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        selectUserList.add(item);
                    }
                    if (listener != null) {
                        listener.onClick(item, position);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    class PhoneConnectHolder extends RecyclerView.ViewHolder {

        private TextView nameTv;
        private TextView phoneTv;
        private View statusView;

        private PhoneConnectUserBean currData;
        private int currPos;

        public PhoneConnectHolder(View itemView) {
            super(itemView);
            statusView = itemView.findViewById(R.id.status_view);
            nameTv = itemView.findViewById(R.id.name_tv);
            phoneTv = itemView.findViewById(R.id.phone_tv);
        }


        public void setData(PhoneConnectUserBean data, int position) {
            this.currData = data;
            this.currPos = position;
            nameTv.setText(data.getName());
            phoneTv.setText(data.getPhone());
        }

        public void setStatus(boolean isSelected) {
            statusView.setSelected(isSelected);
        }
    }

    public List<PhoneConnectUserBean> getSelectedData() {
        return selectUserList;
    }

    public int getSelectedCount() {
        return selectUserList.size();
    }
}
