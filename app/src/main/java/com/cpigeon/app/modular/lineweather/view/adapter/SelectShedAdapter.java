package com.cpigeon.app.modular.lineweather.view.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.message.ui.selectPhoneNumber.model.ContactModel;
import com.cpigeon.app.message.ui.selectPhoneNumber.widget.IndexAdapter;
import com.cpigeon.app.utils.ToastUtil;
import com.jiang.android.lib.adapter.BaseAdapter;
import com.jiang.android.lib.adapter.expand.StickyRecyclerHeadersAdapter;

import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9.
 */
public class SelectShedAdapter extends BaseAdapter<ContactModel.MembersEntity, SelectShedAdapter.ContactViewHolders>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>, IndexAdapter {

    private List<ContactModel.MembersEntity> mLists;
    private Context mContext;

    public SelectShedAdapter(Context ct, List<ContactModel.MembersEntity> mLists) {
        this.mLists = mLists;
        mContext = ct;
        this.addAll(mLists);
    }

    @Override
    public void addAll(Collection<ContactModel.MembersEntity> collection) {
        super.addAll(collection);
        this.mLists = (List<ContactModel.MembersEntity>) collection;
    }

    @Override
    public ContactViewHolders onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_gp_coordinate_info, viewGroup, false);
        return new ContactViewHolders(view);
    }

    @Override
    public void onBindViewHolder(ContactViewHolders contactViewHolders, int position) {
        try {
            ContactModel.MembersEntity entity = getItem(position);
            contactViewHolders.mName.setText(entity.getUsername());
            contactViewHolders.item_llz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!mLists.get(position).getLo().isEmpty() && mLists.get(position).getLa() != null) {
                            Intent intent = new Intent();
                            intent.putExtra("data", mLists.get(position));
                            ((Activity) mContext).setResult(0x0033, intent);
                            ((Activity) mContext).finish();
                        } else {
                            ToastUtil.showLongToast(mContext, "当前暂无位置坐标");
                        }

                        Log.d("sousuo", "onClick: " + mLists.get(position).getUsername() + "  lo-->" + mLists.get(position).getLo() + "  la-->" + mLists.get(position).getLa());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getSortLetters().charAt(0);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.contact_header2, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        TextView textView = (TextView) viewHolder.itemView;
        String showValue = String.valueOf(getItem(position).getSortLetters().charAt(0));
        textView.setText(showValue);
    }

    public class ContactViewHolders extends RecyclerView.ViewHolder {
        public TextView mName;
        public LinearLayout item_llz;


        public ContactViewHolders(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.tv_gp_name);
            item_llz = itemView.findViewById(R.id.item_llz);
        }
    }
}


