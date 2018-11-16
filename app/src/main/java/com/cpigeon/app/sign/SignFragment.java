//package com.cpigeon.app.sign;
//
//import android.graphics.drawable.AnimationDrawable;
//import android.os.Bundle;
//import android.support.design.widget.BottomSheetDialog;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.cpigeon.app.R;
//import com.cpigeon.app.commonstandard.presenter.BasePresenter;
//import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
//import com.cpigeon.app.entity.MultiSelectEntity;
//import com.cpigeon.app.modular.guide.view.adapter.SignBottomAdapter;
//import com.cpigeon.app.utils.DateTool;
//import com.cpigeon.app.utils.Lists;
//import com.cpigeon.app.view.materialcalendarview.CalendarDay;
//import com.cpigeon.app.view.materialcalendarview.EventDecorator;
//import com.cpigeon.app.view.materialcalendarview.MaterialCalendarView;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//
///**
// * Created by Zhu TingYu on 2017/12/27.
// */
//
//public class SignFragment extends BaseMVPFragment {
//
//    MaterialCalendarView calendarView;
//    String[] weekFormat = {"日","一","二","三","四","五","六"};
//
//    ImageView topGif;
//    ImageView topImg;
//    RelativeLayout rlTop;
//    TextView tvSign;
//
//    RecyclerView recyclerView;
//
//    SignBottomAdapter adapter;
//
//    AnimationDrawable animationDrawable;
//
//    @Override
//    protected BasePresenter initPresenter() {
//        return null;
//    }
//
//    @Override
//    protected boolean isCanDettach() {
//        return false;
//    }
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.fragment_sign_layout;
//    }
//
//    @Override
//    public void finishCreateView(Bundle state) {
//
//        setTitle("签到");
//
//        toolbar.getMenu().clear();
//        toolbar.getMenu().add("规则")
//                .setOnMenuItemClickListener(item -> {
//                    showBottomDialog();
//                    return false;
//                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//
//        topGif = findViewById(R.id.top_gif);
//        topImg = findViewById(R.id.top_img);
//        rlTop = findViewById(R.id.rl_top);
//        calendarView = findViewById(R.id.calendar);
//        recyclerView = findViewById(R.id.list);
//        tvSign = findViewById(R.id.text_sign);
//
//        topGif.setImageResource(R.drawable.anim_sign_top);
//        animationDrawable = (AnimationDrawable) topGif.getDrawable();
//        animationDrawable.start();
//
//
//        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_NONE);
//
//        Calendar instance = Calendar.getInstance();
//        calendarView.setSelectedDate(instance.getTime());
//        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_NONE);
//
//        calendarView.setHeaderTextAppearance(R.style.CalendarText);
//        calendarView.setDateTextAppearance(R.style.CalendarText);
//        calendarView.setWeekDayTextAppearance(R.style.CalendarText);
//        calendarView.setWeekDayLabels(weekFormat);
//
//        calendarView.setTopTitleColor(getResources().getColor(R.color.colorPrimary));
//        calendarView.dissTopButtom();
//        Calendar instance1 = Calendar.getInstance();
//        instance1.set(instance1.get(Calendar.YEAR), Calendar.JANUARY, 1);
//
//        Calendar instance2 = Calendar.getInstance();
//        instance2.set(instance2.get(Calendar.YEAR), Calendar.DECEMBER, 31);
//
//        calendarView.state().edit()
//                .setMinimumDate(instance1.getTime())
//                .setMaximumDate(instance2.getTime())
//                .commit();
//
//        rlTop.setOnClickListener(v -> {
//            topGif.setVisibility(View.GONE);
//            topImg.setVisibility(View.VISIBLE);
//            tvSign.setText("您今日已签到");
//            CalendarDay day = CalendarDay.from(DateTool.timeStamp2DateTime(System.currentTimeMillis()));
//            calendarView.addDecorator(new EventDecorator(day));
//        });
//
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),4){
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
//
//        recyclerView.setLayoutManager(gridLayoutManager);
//        adapter = new SignBottomAdapter(getActivity());
//        adapter.bindToRecyclerView(recyclerView);
//
//        bindData();
//    }
//
//    private void bindData() {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH, -2);
//        ArrayList<CalendarDay> dates = new ArrayList<>();
//        for (int i = 0; i < 30; i++) {
//            CalendarDay day = CalendarDay.from(calendar);
//            dates.add(day);
//            calendar.add(Calendar.DATE, 5);
//        }
//        calendarView.addDecorator(new EventDecorator(dates));
//
//        List<MultiSelectEntity> iconState = Lists.newArrayList(new MultiSelectEntity(),new MultiSelectEntity(),
//                new MultiSelectEntity(),new MultiSelectEntity());
//
//        for (int i = 0; i < 2; i++) {
//            iconState.get(i).isChoose = true;
//        }
//
//        adapter.setNewData(iconState);
//    }
//
//    private void showBottomDialog(){
//        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
//        View view = View.inflate(getContext(),R.layout.dialog_sign_bottom_rule_layout,null);
//        findViewById(view, R.id.btn).setOnClickListener(v -> {
//            dialog.dismiss();
//        });
//        dialog.setContentView(view);
//        dialog.setOnDismissListener(dialog1 -> {
//            animationDrawable.start();
//
//        });
//
//        dialog.setOnShowListener(dialog1 -> {
//            animationDrawable.selectDrawable(0);
//            animationDrawable.stop();
//        });
//        dialog.show();
//    }
//
//}
