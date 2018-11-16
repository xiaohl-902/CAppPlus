package com.cpigeon.app.modular.guide.view;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.modular.guide.model.bean.GebiEntity;
import com.cpigeon.app.modular.guide.model.bean.GiftBagInfo;
import com.cpigeon.app.modular.guide.model.bean.UserSignInfoEntity;
import com.cpigeon.app.modular.guide.presenter.SignPresenter;
import com.cpigeon.app.modular.guide.view.adapter.SignBottomAdapter;
import com.cpigeon.app.modular.guide.view.adapter.SignRuleAdapter;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.DateUtils;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.view.AlwaysMarqueeTextView;
import com.cpigeon.app.view.CustomAlertDialog2;
import com.cpigeon.app.view.materialcalendarview.CalendarDay;
import com.cpigeon.app.view.materialcalendarview.EventDecorator;
import com.cpigeon.app.view.materialcalendarview.MaterialCalendarView;
import com.cpigeon.app.view.materialcalendarview.format.DateFormatTitleFormatter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 签到
 * Created by Administrator on 2018/5/26.
 */

public class SignActivity extends BaseActivity<SignPresenter> {

    MaterialCalendarView calendarView;
    String[] weekFormat = {"日", "一", "二", "三", "四", "五", "六"};

    ImageView topGif;
    ImageView topImg;
    RelativeLayout rlTop;
    TextView tvSign;
    RecyclerView recyclerView;
    SignBottomAdapter adapter;
    AnimationDrawable animationDrawable;
    private String[] signDay;
    private ArrayList<CalendarDay> dates = new ArrayList<>();

    private void bindData(UserSignInfoEntity userSignInfoEntity) {
        try {
            dates.clear();

            signDay = userSignInfoEntity.getSignDays().split(",");

            //当前日期  月份
            int theDayM = Integer.valueOf(DateUtils.dateToStrM(new Date())) - 1;
            //当前日期  日
            int theDayD = Integer.valueOf(DateUtils.dateToStrD(new Date()));

            for (int i = 0; i < signDay.length; i++) {
                if (theDayD == Integer.valueOf(signDay[i])) {
                    theDaySign();//当天已签到
                    break;
                }
            }

            for (int i = 0; i < signDay.length; i++) {
                dates.add(new CalendarDay(Integer.valueOf(DateUtils.dateToStrY(new Date())), theDayM, Integer.valueOf(signDay[i])));
            }
            calendarView.addDecorator(new EventDecorator(dates));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initGetGiftBag(userSignInfoEntity, signDay);   //初始化获取礼包
    }

    private List<GiftBagInfo> iconState = new ArrayList<>();

    //初始化获取礼包
    private void initGetGiftBag(UserSignInfoEntity userSignInfoEntity, String[] signDay) {
        iconState.clear();
        adapter.getData().clear();
        adapter.notifyDataSetChanged();

        int giftBag = userSignInfoEntity.getGiftSettings().size();
        for (int i = 0; i < giftBag; i++) {
            iconState.add(new GiftBagInfo(userSignInfoEntity.getGiftSettings().get(i).getItems(),
                    userSignInfoEntity.getGiftSettings().get(i).getGb(),
                    userSignInfoEntity.getGiftSettings().get(i).getDw(),
                    userSignInfoEntity.getGiftSettings().get(i).getGid(), true));
        }

        try {
            int giftDataSize = userSignInfoEntity.getGiftdata().size();

            if (signDay.length >= 3) {
                //三天
                for (int i = 0; i < giftDataSize; i++) {
                    if (userSignInfoEntity.getGiftdata().get(i).getItems().equals("累计签到3日礼包")) {
                        iconState.get(0).setChoose(false);
                        break;
                    }

                    if (i == giftDataSize - 1) {
                        iconState.get(0).setChoose(true);
                    }
                }

                if (giftDataSize == 0) {
                    iconState.get(0).setChoose(true);
                }
            }

            if (signDay.length >= 7) {
                //七天
                for (int i = 0; i < giftDataSize; i++) {
                    if (userSignInfoEntity.getGiftdata().get(i).getItems().equals("累计签到7日礼包")) {
                        iconState.get(1).setChoose(false);
                        break;
                    }

                    if (i == giftDataSize - 1) {
                        iconState.get(1).setChoose(true);
                    }
                }

                if (giftDataSize == 0) {
                    iconState.get(1).setChoose(true);
                }
            }

            if (signDay.length >= 15) {
                //十五天
                for (int i = 0; i < giftDataSize; i++) {
                    if (userSignInfoEntity.getGiftdata().get(i).getItems().equals("累计签到15日礼包")) {
                        iconState.get(2).setChoose(false);
                        break;
                    }

                    if (i == giftDataSize - 1) {
                        iconState.get(2).setChoose(true);
                    }
                }
                if (giftDataSize == 0) {
                    iconState.get(2).setChoose(true);
                }
            }

            if (signDay.length >= 28) {
                //二十八天
                for (int i = 0; i < giftDataSize; i++) {
                    if (userSignInfoEntity.getGiftdata().get(i).equals("累计签到28日礼包")) {
                        iconState.get(3).setChoose(false);
                        break;
                    }

                    if (i == giftDataSize - 1) {
                        iconState.get(3).setChoose(true);
                    }
                }

                if (giftDataSize == 0) {
                    iconState.get(3).setChoose(true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter.setNewData(iconState);
    }


    private BottomSheetDialog signRuleDialog;
    private SignRuleAdapter mSignRuleAdapter;

    private void showBottomDialog() {
        signRuleDialog = new BottomSheetDialog(this);
        View view = View.inflate(this, R.layout.dialog_sign_bottom_rule_layout, null);

        RecyclerView sign_sule_recyclerview = (RecyclerView) view.findViewById(R.id.sign_sule_recyclerview);

        sign_sule_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        //签到规则初始化
        mSignRuleAdapter = new SignRuleAdapter(null);

        sign_sule_recyclerview.setAdapter(mSignRuleAdapter);

        view.findViewById(R.id.btn).setOnClickListener(v -> {
            signRuleDialog.dismiss();
        });

        signRuleDialog.setContentView(view);
        signRuleDialog.setOnDismissListener(dialog1 -> {
            animationDrawable.start();

        });

        signRuleDialog.setOnShowListener(dialog1 -> {
            animationDrawable.selectDrawable(0);
            animationDrawable.stop();
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sign_layout;
    }

    @Override
    public SignPresenter initPresenter() {
        return new SignPresenter(this);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);//在当前界面注册一个订阅者

        AlwaysMarqueeTextView title = (AlwaysMarqueeTextView) findViewById(R.id.toolbar_title);
        title.setText("签到");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getMenu().clear();
        toolbar.getMenu().add("规则")
                .setOnMenuItemClickListener(item -> {
                    signRuleDialog.show();
                    return false;
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        topGif = (ImageView) findViewById(R.id.top_gif);
        topImg = (ImageView) findViewById(R.id.top_img);

        rlTop = (RelativeLayout) findViewById(R.id.rl_top);
        calendarView = (MaterialCalendarView) findViewById(R.id.calendar);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        tvSign = (TextView) findViewById(R.id.text_sign);

        topGif.setImageResource(R.drawable.anim_sign_top);
        animationDrawable = (AnimationDrawable) topGif.getDrawable();
        animationDrawable.start();

        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_NONE);

        Calendar instance = Calendar.getInstance();
        calendarView.setSelectedDate(instance.getTime());//设置选定的日期
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);//设置选择的模式

        calendarView.setHeaderTextAppearance(R.style.CalendarText);//设置标题文本的外观
        calendarView.setDateTextAppearance(R.style.CalendarText);//设置日期文本外观
        calendarView.setWeekDayTextAppearance(R.style.CalendarText);//设置工作日文本外观。
        calendarView.setWeekDayLabels(weekFormat);//工作日设置标签
        calendarView.setTitleFormatter(new DateFormatTitleFormatter());

//        calendarView.setTileHeightDp(44);
//        calendarView.setTileWidthDp(47);

        calendarView.setTopTitleColor(getResources().getColor(R.color.color_theme));
        calendarView.dissTopButtom();

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), instance1.get(Calendar.MONTH), instance1.getActualMinimum(Calendar.DAY_OF_MONTH));

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), instance2.get(Calendar.MONTH), instance2.getActualMaximum(Calendar.DAY_OF_MONTH));

        calendarView.state().edit()
                .setMinimumDate(instance1.getTime())
                .setMaximumDate(instance2.getTime())
                .commit();

        //点击签到
        rlTop.setOnClickListener(v -> {
            if (isTheDaySign) return;
            mPresenter.getUserSignInData(o -> {
                theDaySign();

                try {
                    tv_get_gb.setText(o.getGb());
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                requestData();
//                SignActivity.this.dialogPrompt = SweetAlertDialogUtil.showDialog(dialogPrompt, o, this);
            });
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new SignBottomAdapter(this, mPresenter, dialogPrompt);
        adapter.bindToRecyclerView(recyclerView);


        iconState.clear();
        iconState.add(new GiftBagInfo());
        iconState.add(new GiftBagInfo());
        iconState.add(new GiftBagInfo());
        iconState.add(new GiftBagInfo());
        adapter.setNewData(iconState);

        //初始化签到规则
        showBottomDialog();

        initSingSureDialog();

        requestData();
    }

    //请求数据
    private void requestData() {
        RxUtils.delayed(500, aLong -> {
            mPresenter.getUserSignInfoData(userSignInfoEntity -> {
                bindData(userSignInfoEntity);
            });
        });

        RxUtils.delayed(700, aLong -> {
            mPresenter.getSignGuiZeData(signGuiZeEntity -> {
                mSignRuleAdapter.setNewData(signGuiZeEntity);
            });
        });
    }

    ImageView img_anim_sign_success;//签到成功动画图片
    AnimationDrawable animationDrawable2;
    private RelativeLayout ll_anim_sign_success;
    private CustomAlertDialog2 dialog;
    private TextView tv_get_gb;

    //签到确定提示dialog

    private void initSingSureDialog() {
        //layout_dialog_sign_sure

        ll_anim_sign_success = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.layout_dialog_sign_sure, null);
        img_anim_sign_success = (ImageView) ll_anim_sign_success.findViewById(R.id.img_anim_sign_success);
        tv_get_gb = (TextView) ll_anim_sign_success.findViewById(R.id.tv_get_gb);

        img_anim_sign_success.setImageResource(R.drawable.anim_sign_success);
        animationDrawable2 = (AnimationDrawable) img_anim_sign_success.getDrawable();
        animationDrawable2.start();

        ImageView btn_sign_success_sure = (ImageView) ll_anim_sign_success.findViewById(R.id.btn_sign_success_sure);
        btn_sign_success_sure.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog = new CustomAlertDialog2(this, R.style.dialog_style1);
        dialog.setContentView(ll_anim_sign_success);

        //调用这个方法时，按对话框以外的地方不起作用。按返回键还起作用
        dialog.setCanceledOnTouchOutside(false);
    }

    private boolean isTheDaySign = false;//当天是否已签到

    //当天已签到
    private void theDaySign() {
        isTheDaySign = true;
        topGif.setVisibility(View.GONE);
        topImg.setVisibility(View.VISIBLE);
        tvSign.setText("您今日已签到");
        CalendarDay day = CalendarDay.from(DateTool.timeStamp2DateTime(System.currentTimeMillis()));
        calendarView.addDecorator(new EventDecorator(day));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消注册
    }

    @Subscribe //订阅事件FirstEvent
    public void onEventMainThread(GebiEntity mGebiEntity) {

        try {
            tv_get_gb.setText(mGebiEntity.getGb());
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        requestData();
    }
}
