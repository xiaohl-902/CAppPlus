package com.cpigeon.app.message.ui.history;

import android.os.Bundle;
import android.widget.TextView;

import com.cpigeon.app.R;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.fragment.BaseMVPFragment;
import com.cpigeon.app.entity.MessageEntity;
import com.cpigeon.app.utils.DateTool;
import com.cpigeon.app.utils.IntentBuilder;

/**
 * Created by Zhu TingYu on 2017/11/22.
 */

public class MessageDetailsFragment extends BaseMVPFragment {

    TextView date;
    TextView number;
    TextView content;

    MessageEntity messageEntity;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected boolean isCanDettach() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_message_details_layout;
    }

    @Override
    public void finishCreateView(Bundle state) {
        setTitle("短信详情");

        messageEntity = getActivity().getIntent().getParcelableExtra(IntentBuilder.KEY_DATA);

        date = findViewById(R.id.date);
        number = findViewById(R.id.number);
        content = findViewById(R.id.content);

        bindDate();
    }

    private void bindDate() {
        if(messageEntity != null){
            date.setText(messageEntity.fssj);
            number.setText(getString(R.string.string_text_message_addressee_number,String.valueOf(messageEntity.fscount)));
            content.setText(messageEntity.dxnr);
        }
    }
}
