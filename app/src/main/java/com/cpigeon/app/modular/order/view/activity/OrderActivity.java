package com.cpigeon.app.modular.order.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.activity.BasePageTurnActivity;
import com.cpigeon.app.entity.OrderInfoEntity;
import com.cpigeon.app.message.ui.order.ui.OrderPayFragment;
import com.cpigeon.app.modular.order.model.bean.CpigeonOrderInfo;
import com.cpigeon.app.modular.order.presenter.OrderPre;
import com.cpigeon.app.modular.order.view.activity.viewdao.IOrderView;
import com.cpigeon.app.modular.order.view.adapter.OrderAdapter;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.NetUtils;
import com.orhanobut.logger.Logger;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/4/11.
 */

public class OrderActivity extends BasePageTurnActivity<OrderPre, OrderAdapter, CpigeonOrderInfo> implements IOrderView {

    BaseQuickAdapter.OnItemClickListener onItemClickListener = new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            CpigeonOrderInfo orderInfo = (CpigeonOrderInfo) adapter.getData().get(position);

            if(orderInfo.getStatusName().equals("交易过期")) {
                return;
            }

            String strHint = "";
            if (orderInfo.ddtype.indexOf("中鸽助手") != -1) {
                strHint = "中鸽助手订单请在中鸽助手App上支付";
                DialogUtils.createHintDialog(getActivity(),strHint);
                return;
            } else if (orderInfo.ddtype.indexOf("天下鸽谱") != -1) {
                strHint = "天下鸽谱订单请在天下鸽谱App上支付";
                DialogUtils.createHintDialog(getActivity(),strHint);
                return;
            } else if (orderInfo.ddtype.indexOf("网页") != -1) {
                strHint = "网页订单请在网页上支付";
                DialogUtils.createHintDialog(getActivity(),strHint);
                return;
            } else if (orderInfo.ddly.indexOf("中鸽助手") != -1 && orderInfo.ddly.equals("ios")) {
                strHint = "请在中鸽助手ios版本上支付";
                DialogUtils.createHintDialog(getActivity(),strHint);
                return;
            }



            if (!orderInfo.ispaid() && "待支付".equals(orderInfo.getStatusName())) {
                if(orderInfo.getScores() == 0){
                    OrderInfoEntity orderInfoEntity = new OrderInfoEntity();
                    orderInfoEntity.id = String.valueOf(orderInfo.getId());
                    orderInfoEntity.time = orderInfo.getOrderTime();
                    orderInfoEntity.number = orderInfo.getOrderNumber();
                    orderInfoEntity.item = orderInfo.getOrderName();
                    orderInfoEntity.price = String.valueOf(orderInfo.getPrice());
                    orderInfoEntity.scores = String.valueOf(orderInfo.getScores());
                    orderInfoEntity.fpid = orderInfo.fpid;
                    IntentBuilder.Builder()
                            .putExtra(IntentBuilder.KEY_DATA, orderInfoEntity)
                            .startParentActivity(getActivity(), OrderPayFragment.class);
                }else {
                    Intent intent = new Intent(OrderActivity.this, OrderPayActivity.class);
                    intent.putExtra(OrderPayActivity.INTENT_DATA_KEY_ORDERINFO, orderInfo);
                    startActivity(intent);
                }
            }
        }
    };
    private CpigeonData.OnWxPayListener onWxPayListener = new CpigeonData.OnWxPayListener() {
        @Override
        public void onPayFinished(int wxPayReturnCode) {
            if (wxPayReturnCode == ERR_OK)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefresh();
                    }
                }, 400);
            else
                showTips("支付失败", TipType.ToastShort);

        }
    };

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        CpigeonData.getInstance().addOnWxPayListener(onWxPayListener);
    }

    @Override
    public OrderPre initPresenter() {
        return new OrderPre(this);
    }

    @NonNull
    @Override
    public String getTitleName() {
        return "我的订单";
    }

    @Override
    public int getDefaultPageSize() {
        return 8;
    }

    @Override
    protected String getEmptyDataTips() {
        return "您还没有任何订单哦，快去下单吧!";
    }

    @Override
    public String getQuery() {
        return null;
    }

    @Override
    public OrderAdapter getNewAdapterWithNoData() {
        OrderAdapter adapter = new OrderAdapter(null);
        adapter.setOnItemClickListener(onItemClickListener);
        return adapter;
    }

    @Override
    protected void loadDataByPresenter() {
        mPresenter.loadOrder();
    }
}


