package com.cpigeon.app.message.ui.order.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.GXTMessagePrice;
import com.cpigeon.app.message.ui.order.ui.presenter.MessageCreateOrderPre;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.utils.http.CommonUitls;

import java.util.List;

/**
 * Created by Zhu TingYu on 2017/12/7.
 */

public class CreateMessageOrderFragment extends BaseMVPFragment<MessageCreateOrderPre>{

    AppCompatTextView tvExplain;
    AppCompatTextView tvPrice;
    AppCompatButton btnLook;
    AppCompatEditText edCount;

    TextView btn;


    List<AppCompatTextView> selectTvs;

    List<Integer> tvIds;
    GXTMessagePrice price;


    @Override
    protected MessageCreateOrderPre initPresenter() {
        return new MessageCreateOrderPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        initView();
    }



    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_creat_message_order_layout;
    }

    private void initView() {

        setTitle("短信充值");

        toolbar.getMenu().add("充值记录")
                .setOnMenuItemClickListener(item -> {
                    IntentBuilder.Builder().startParentActivity(getActivity(), RechargeHistoryFragment.class);
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        tvIds = Lists.newArrayList(R.id.tv_select1,R.id.tv_select2,R.id.tv_select3
                ,R.id.tv_select4,R.id.tv_select5);
        selectTvs = Lists.newArrayList();

        tvExplain = findViewById(R.id.tv_explain);
        tvPrice = findViewById(R.id.order_price);

        edCount = findViewById(R.id.ed_count);
        edCount.setCursorVisible(false);

        btn = findViewById(R.id.text_btn);

        btn.setOnClickListener(v -> {
            if(mPresenter.messageCount < 1){
                DialogUtils.createHintDialog(getActivity(), "请确认充值的数量");
                return;
            }
            showLoading("正在创建订单");
            mPresenter.createGXTMessageOrder(r -> {
                hideLoading();
                if(r.status){
                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_DATA, r.data)
                            .startParentActivity(getActivity(), OrderPayFragment.class);
                    finish();
                }else {
                    error(r.msg);
                }
            });
        });


        for (int i = 0, len = tvIds.size(); i < len; i++) {
            AppCompatTextView textView = findViewById(tvIds.get(i));
            selectTvs.add(textView);
        }

        for (int i = 0, len = selectTvs.size() - 1; i < len; i++) {
            int finalI = i;
            selectTvs.get(i).setOnClickListener(v -> {
                setTvExplainListener(finalI);
            });
        }

        edCount.setOnClickListener(v -> {
            edCount.setCursorVisible(true);
            setTvExplainListener(4);
        });


        showLoading();
        mPresenter.getGXTMessagePrice(r -> {
            hideLoading();
            if(r.status){
                price = r.data;
                tvExplain.setText(getString(R.string.string_text_create_message_order_explain,String.valueOf(price.money)));
                bindUi(RxUtils.textChanges(edCount), mPresenter.setMessageCount(integer -> {
                    mPresenter.price = price.money * (double)integer;
                    tvPrice.setText(String.valueOf(mPresenter.price) + "元");
                }));
            }else {
                error(r.msg);
            }
        });

    }

    private void setTvExplainListener(int position){

        if(position == 4){
            tvPrice.setText(String.valueOf(0.0)+"元");
            mPresenter.price = 0;
            mPresenter.messageCount = 0;
        }else {
            edCount.setCursorVisible(false);
        }



        for (int i = 0, len = selectTvs.size(); i < len; i++) {
            AppCompatTextView textView = selectTvs.get(i);
            if(position == i){
                Drawable drawable = getResources().getDrawable(R.drawable.ic_blue_hook);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                textView.setCompoundDrawables(drawable,null,null,null);
                textView.setSelected(true);
            }else {
                textView.setTextColor(getResources().getColor(R.color.text_color));
                textView.setCompoundDrawables(null,null,null,null);
                textView.setSelected(false);
            }
        }

        if(position != selectTvs.size() - 1){
            mPresenter.messageCount = position + 1;
            mPresenter.price = price.money * (double)(position + 1);
            tvPrice.setText(String.valueOf(mPresenter.price) + "元");
        }
    }
}
