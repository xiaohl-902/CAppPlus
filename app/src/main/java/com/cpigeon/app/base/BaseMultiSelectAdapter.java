package com.cpigeon.app.base;


import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.cpigeon.app.R;
import com.cpigeon.app.entity.MultiSelectEntity;
import com.cpigeon.app.utils.Lists;

import java.util.List;

/** checkbox 列表基类
 * Created by Zhu TingYu on 2017/11/20.
 */

public class BaseMultiSelectAdapter<K extends MultiSelectEntity, B extends BaseViewHolder> extends BaseQuickAdapter<K, B> {

    protected List<Integer> selectedPositions;
    protected AppCompatImageView imgChoose;

    public BaseMultiSelectAdapter(@LayoutRes int id, List<K> data) {
        super(id, data);
    }

    @Override
    protected void convert(B holder, K item) {

        imgChoose = holder.findViewById(R.id.checkbox);

        imgChoose.setVisibility(item.isChooseVisible ? View.VISIBLE : View.GONE);

        imgChoose.setBackgroundResource(item.isChoose ? R.drawable.ic_choosed : R.drawable.ic_no_choose);
    }

    protected void setChoose(MultiSelectEntity entity, boolean isChoose) {
        entity.isChoose = isChoose;
    }

    private void setAllChoose(List<K> productEntities, boolean isChoose){
        for(MultiSelectEntity productEntity : productEntities){
            setChoose(productEntity, isChoose);
        }
    }

    public List<Integer> getSelectedPotion(){
        selectedPositions = Lists.newArrayList();
        for(int i = 0; i < mData.size(); i++){
            if(mData.get(i).isChoose){
                selectedPositions.add(i);
            }
        }
        return selectedPositions;
    }

    public List<K> getSelectedEntity(){
        List<K> list = Lists.newArrayList();
        for(int i = 0; i < mData.size(); i++){
            if(mData.get(i).isChoose){
                list.add(mData.get(i));
            }
        }
        return list;
    }

    public void deleteChoose() {
        for (int i = 0; i < mData.size();) {
            if (mData.get(i).isChoose) {
                remove(i);
                continue;
            }
            i++;
        }
    }

    public void isChooseAll(boolean isChooseAll) {
        setAllChoose(mData ,isChooseAll);
        notifyDataSetChanged();
    }

    public void setImgChooseVisible(boolean isVisible){
        for (K item : mData) {
            item.isChooseVisible = isVisible;
        }
        notifyDataSetChanged();
    }

    public void setChooseGone(K item, int position){
        item.isChooseVisible = false;
        notifyItemChanged(position);
    }

    public void setMultiSelectItem(K item, int position){
        setChoose(item, !item.isChoose);
        notifyItemChanged(position);
    }

    public void setSingleItem(K item, int position){
        for (int i = 0; i < mData.size(); i++) {
            if(mData.get(i).isChoose){
                mData.get(i).isChoose = false;
            }
        }
        setChoose(item, true);
        notifyDataSetChanged();
    }
}
