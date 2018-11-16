package com.cpigeon.app.circle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.cpigeon.app.R;
import com.cpigeon.app.circle.LocationManager;
import com.cpigeon.app.circle.PoiSearchManager;
import com.cpigeon.app.circle.adpter.ChooseLocationAdapter;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.utils.IntentBuilder;

/**
 * Created by Zhu TingYu on 2018/1/17.
 */

public class ChooseLocationFragment extends BaseMVPFragment {

    RecyclerView recyclerView;
    ChooseLocationAdapter adapter;
    LocationManager locationManager;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_choose_location_layout;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    public void finishCreateView(Bundle state) {

        setTitle("地点");

        locationManager = new LocationManager(getContext());

        findViewById(R.id.no_visible).setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(IntentBuilder.KEY_DATA, "不显示");
            getActivity().setResult(0,intent);
            finish();
        });

        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChooseLocationAdapter();
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            Intent intent = new Intent();
            intent.putExtra(IntentBuilder.KEY_DATA, adapter.getItem(position).getTitle());
            getActivity().setResult(0,intent);
            finish();
        });
        recyclerView.setAdapter(adapter);

        addItemDecorationLine(recyclerView);

        showLoading();
        locationManager.setLocationListener(aMapLocation -> {
            PoiSearchManager.build(getContext(), aMapLocation.getCityCode())
                    .setBound(aMapLocation.getLatitude(), aMapLocation.getLongitude())
                    .setSearchListener(new PoiSearch.OnPoiSearchListener() {
                        @Override
                        public void onPoiSearched(PoiResult poiResult, int i) {
                            hideLoading();
                            adapter.setNewData(poiResult.getPois());
                        }

                        @Override
                        public void onPoiItemSearched(PoiItem poiItem, int i) {

                        }
                    }).search();
        }).star();

    }
}
