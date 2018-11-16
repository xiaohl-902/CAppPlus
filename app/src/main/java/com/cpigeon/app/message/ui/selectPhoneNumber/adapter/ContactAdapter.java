/**
 * created by jiang, 12/3/15
 * Copyright (c) 2015, jyuesong@gmail.com All Rights Reserved.
 * *                #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG              #
 * #                                                   #
 */

package com.cpigeon.app.message.ui.selectPhoneNumber.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.cpigeon.app.R;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.message.ui.selectPhoneNumber.model.ContactModel;
import com.cpigeon.app.message.ui.selectPhoneNumber.widget.IndexAdapter;
import com.jiang.android.lib.adapter.BaseAdapter;
import com.jiang.android.lib.adapter.expand.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * Created by jiang on 12/3/15.
 * 根据当前权限进行判断相关的滑动逻辑
 */
public class ContactAdapter extends BaseAdapter<ContactModel.MembersEntity,ContactAdapter.ContactViewHolder>
        implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder>,IndexAdapter
{
    /**
     * 当前处于打开状态的item
     */

    protected List<Integer> selectedPositions;
    protected AppCompatImageView imgChoose;

    private List<ContactModel.MembersEntity> mLists;

    private Context mContext;
    private int mPermission;
    private String createrID;
    private boolean isCreator;


    public static final String OWNER = "1";
    public static final String CREATER = "1";
    public static final String STUDENT = "student";

    public ContactAdapter(Context ct, List<ContactModel.MembersEntity> mLists) {
        this.mLists = mLists;
        mContext = ct;
        this.addAll(mLists);
    }

    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contacts_list_layout, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.ContactViewHolder holder, final int position) {

        ContactModel.MembersEntity entity = getItem(position);

        TextView textView = holder.mName;
        textView.setText(entity.getUsername());

        holder.mNumber.setText(entity.getMobile());

        holder.imgChoose.setVisibility(entity.isChooseVisible ? View.VISIBLE : View.GONE);

        holder.imgChoose.setBackgroundResource(entity.isChoose ? R.drawable.ic_choosed : R.drawable.ic_no_choose);

        holder.itemView.setOnClickListener(v -> {
            setMultiSelectItem(entity, position);
        });

    }

    @Override
    public long getHeaderId(int position) {

        return getItem(position).getSortLetters().charAt(0);

    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemView;
        String showValue = String.valueOf(getItem(position).getSortLetters().charAt(0));
        if ("$".equals(showValue)) {
            textView.setText("群主");
        } else if ("%".equals(showValue)) {
            textView.setText("系统管理员");

        } else {
            textView.setText(showValue);
        }

    }

    protected void setChoose(ContactModel.MembersEntity entity, boolean isChoose) {
        entity.isChoose = isChoose;
    }

    public void setAllChoose(List<ContactModel.MembersEntity> productEntities, boolean isChoose){
        for(ContactModel.MembersEntity productEntity : productEntities){
            setChoose(productEntity, isChoose);
        }
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedPotion(){
        selectedPositions = Lists.newArrayList();
        for(int i = 0; i < mLists.size(); i++){
            if(mLists.get(i).isChoose){
                selectedPositions.add(i);
            }
        }
        return selectedPositions;
    }

    public void setImgChooseVisible(boolean isVisible){
        for (ContactModel.MembersEntity item : mLists) {
            item.isChooseVisible = isVisible;
        }
        notifyDataSetChanged();
    }

    public void setMultiSelectItem(ContactModel.MembersEntity item, int position){
        setChoose(item, !item.isChoose);
        notifyItemChanged(position);
    }


    public int getPositionForSection(char section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = mLists.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;

    }

    public String getPhoneString(){
        StringBuffer phoneString = new StringBuffer();
        List<Integer> positions = getSelectedPotion();
        for(Integer position : positions){
            ContactModel.MembersEntity entity = mLists.get(position);
            phoneString.append(entity.getMobile().replaceAll(" +",""));
            phoneString.append(",");
            phoneString.append(entity.getUsername());
            phoneString.append(",");
            phoneString.append("");
            phoneString.append("|");

        }
        return phoneString.toString();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public TextView mName;
        public TextView mNumber;
        public AppCompatImageView imgChoose;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.title);
            mNumber = (TextView) itemView.findViewById(R.id.number);
            imgChoose = itemView.findViewById(R.id.checkbox);
        }


    }
}
