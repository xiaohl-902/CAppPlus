package com.cpigeon.app.message.ui.order.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.OrderInfoEntity;
import com.cpigeon.app.entity.VoiceEntity;
import com.cpigeon.app.event.GXTUserInfoEvent;
import com.cpigeon.app.event.WXPayResultEvent;
import com.cpigeon.app.message.ui.order.adpter.OrderPayAdapter;
import com.cpigeon.app.message.ui.order.ui.presenter.PayOrderPre;
import com.cpigeon.app.message.ui.order.ui.vocice.VoiceListFragment;
import com.cpigeon.app.modular.home.view.activity.WebActivity;
import com.cpigeon.app.modular.usercenter.view.activity.SetPayPwdActivity;
import com.cpigeon.app.utils.CPigeonApiUrl;
import com.cpigeon.app.utils.DialogUtils;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.SendWX;
import com.cpigeon.app.utils.StringUtil;
import com.cpigeon.app.utils.ToastUtil;
import com.cpigeon.app.viewholder.OderInfoViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Zhu TingYu on 2017/12/7.
 */

public class OrderPayFragment extends BaseMVPFragment<PayOrderPre> {

    private static final int CODE_SELECT_VOICE = 0x123;

    RecyclerView recyclerView;
    OrderPayAdapter adapter;
    OderInfoViewHolder holder;
    TextView tvPay;
    EditText edPassword;

    String type;

    public TextView orderId;
    public TextView orderContent;
    public TextView orderTime;
    public TextView orderPrice;
    public LinearLayout llBottom;
    public TextView orderPayText;

    public CheckBox checkBox;
    public TextView tvProtocol;
    /*@BindView(R.id.etBusinessName)
    EditText etBusinessName;
    @BindView(R.id.etTFN)
    EditText etTFN;
    @BindView(R.id.etBankName)
    EditText etBankName;
    @BindView(R.id.etBankNumber)
    EditText etBankNumber;
    @BindView(R.id.llInVoiceContent)
    LinearLayout llInVoiceContent;
    @BindView(R.id.imgArrow)
    AppCompatImageView imgArrow;*/
    @BindView(R.id.imgNeedInVoice)
    ImageView imgNeedInVoice;
    @BindView(R.id.tvSelectVoice)
    TextView tvSelectVoice;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_order_pay_layout;
    }


    @Override
    protected PayOrderPre initPresenter() {
        return new PayOrderPre(getActivity());
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {
        EventBus.getDefault().register(this);
        type = getActivity().getIntent().getStringExtra(IntentBuilder.KEY_TYPE);
        initView();
    }

    protected void initView() {
        setTitle("订单支付");
        initHeadView();
        bindHeadData();
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderPayAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setNewData(Lists.newArrayList("余额支付", "微信支付"));


        adapter.setOnItemClickListener((adapter1, view, position) -> {

            if (checkBox.isChecked()) {
                if (position == 0) {
                    mPresenter.getUserBalance(userBalanceEntity -> {
                        showPayDialog(String.format("%.2f", userBalanceEntity.ye), mPresenter.orderInfoEntity.price);
                        bindUi(RxUtils.textChanges(edPassword), mPresenter.setPassword());
                    });

                } else {
                    showLoading("正在创建订单");
                    mPresenter.getWXOrder(r -> {
                        hideLoading();
                        if (r.status) {
                            SendWX sendWX = new SendWX(getActivity());
                            sendWX.payWeiXin(r.data.getPayReq());
                        } else {
                            error(r.msg);
                        }

                    });
                }
            } else {
                error("请同意中鸽网支付协议");
            }

        });
        showLoading();
        mPresenter.getVoice(voiceEntity -> {
            hideLoading();
            tvSelectVoice.setText(voiceEntity.dwmc);
            setBindVoice(true);
        });

    }

    private void initHeadView() {

        orderId = findViewById(R.id.tv_order_number_content);
        orderContent = findViewById(R.id.tv_order_name_content);
        orderTime = findViewById(R.id.tv_order_time_content);
        orderPrice = findViewById(R.id.tv_order_price_content);
        llBottom = findViewById(R.id.ll1);
        orderPayText = findViewById(R.id.text1);

        checkBox = findViewById(R.id.cb_order_protocol);
        tvProtocol = findViewById(R.id.tv_order_protocol);

        llBottom.setVisibility(View.VISIBLE);
        orderPayText.setVisibility(View.VISIBLE);
        tvProtocol.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), WebActivity.class);
            intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, CPigeonApiUrl.getInstance().getServer() + "/APP/Protocol?type=pay");
            intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "订单支付");
            startActivity(intent);
        });

        tvSelectVoice.setOnClickListener(v -> {
            IntentBuilder.Builder().startParentActivity(getActivity(), VoiceListFragment.class, CODE_SELECT_VOICE);
        });

        /*chkNeedInVoice.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                if(TextUtils.isEmpty(mPresenter.voiceId)){
                    DialogUtils.createHintDialog(getActivity(),"请选择发票信息");
                }else {
                    mPresenter.setIsBindVoice(true);
                    mPresenter.bindVoice(s -> {
                    });
                }

            }else {
                if(!TextUtils.isEmpty(mPresenter.voiceId)){
                    mPresenter.setIsBindVoice(false);
                    mPresenter.bindVoice(s -> {
                    });
                }
            }
        });*/

        imgNeedInVoice.setOnClickListener(v -> {
            if(!imgNeedInVoice.isSelected()){
                if(TextUtils.isEmpty(mPresenter.voiceId)){
                    DialogUtils.createHintDialog(getActivity(),"请选择发票信息");
                }else {
                    setBindVoice(true);
                }
            }else {
                if(!TextUtils.isEmpty(mPresenter.voiceId)){
                   setBindVoice(false);
                }else {
                    imgNeedInVoice.setSelected(false);
                }
            }
        });

    }

    private void setBindVoice(boolean isBindVoice){

        if(Integer.valueOf(mPresenter.voiceId) == 0){
            DialogUtils.createHintDialog(getActivity(),"请选择发票信息！");
            return;
        }

        showLoading();
        mPresenter.setIsBindVoice(isBindVoice);
        mPresenter.bindVoice(s -> {
            hideLoading();
            imgNeedInVoice.setSelected(isBindVoice);
        });
    }

    protected void bindHeadData() {
        OrderInfoEntity entity = mPresenter.orderInfoEntity;
        orderId.setText(entity.number);
        orderContent.setText(entity.item);
        orderTime.setText(entity.time);
        orderPrice.setText(entity.price + "元" + "   (微信手续费" + StringUtil.twoPoint(Double.parseDouble(entity.price) * 0.01) + "元)");
    }

    protected void showPayDialog(String balance, String price) {
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        View view = View.inflate(getContext(), R.layout.dialogfragment_pay, null);

        ImageView colse = findViewById(view, R.id.iv_pay_close);
        TextView title = findViewById(view, R.id.tv_yue_prompt);
        edPassword = findViewById(view, R.id.et_paypwd);
        tvPay = findViewById(view, R.id.tv_pay);
        TextView meanPassword = findViewById(view, R.id.tv_mean_for_paypwd);
        TextView forgotPassword = findViewById(view, R.id.tv_forget_paypwd);

        bindUi(RxUtils.textChanges(edPassword), mPresenter.setPassword());

        title.setText(getString(R.string.format_pay_account_balance_tips, balance, price));

        colse.setOnClickListener(v -> {
            dialog.dismiss();
        });

        tvPay.setOnClickListener(v -> {
            payByBalance();
        });

        meanPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            String url = CPigeonApiUrl.getInstance().getServer() + "/APP/Help?type=help&id=172";
            intent.putExtra(WebActivity.INTENT_DATA_KEY_URL, url);
            intent.putExtra(WebActivity.INTENT_DATA_KEY_BACKNAME, "付款");
            startActivity(intent);
        });

        forgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SetPayPwdActivity.class));
        });


        dialog.setContentView(view);
        dialog.show();
    }

    private void payByBalance() {
        mPresenter.payOrderByBalance(r -> {
            if (r.status) {
                EventBus.getDefault().post(new GXTUserInfoEvent());
                DialogUtils.createDialog(getContext(), r.msg, sweetAlertDialog -> {
                    sweetAlertDialog.dismiss();
                    finish();
                });
            } else {
                error(r.msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(WXPayResultEvent event) {
        if (WXPayResultEvent.CODE_OK == event.code) {
            DialogUtils.createDialog(getContext(), "支付成功", sweetAlertDialog -> {
                sweetAlertDialog.dismiss();
                finish();
            });
        } else if (WXPayResultEvent.CODE_ERROR == event.code) {
            ToastUtil.showLongToast(getContext(), "支付失败");
        } else {
            ToastUtil.showLongToast(getContext(), "取消支付");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_SELECT_VOICE) {
            if (data != null && data.hasExtra(IntentBuilder.KEY_DATA)) {
                VoiceEntity voiceEntity = data.getParcelableExtra(IntentBuilder.KEY_DATA);
                tvSelectVoice.setText(voiceEntity.dwmc);
                mPresenter.voiceId= voiceEntity.id;
                setBindVoice(true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
