package com.tinytiger.titi.widget.popup;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.tinytiger.common.event.ClassEvent;
import com.tinytiger.common.utils.FastClickUtil;
import com.tinytiger.common.utils.preference.SpUtils;
import com.tinytiger.common.utils.toast.ToastUtils;
import com.tinytiger.titi.R;
import com.tinytiger.titi.utils.CheckUtils;
import org.greenrobot.eventbus.EventBus;
import razerdp.basepopup.BasePopupWindow;


/**
 * @author zhw_luke
 * @date 2020/3/7 0007 下午 4:44
 * @Copyright 小虎互联科技
 * @doc 输入法弹出
 * @since 2.0.0
 */
public class PopupInput extends BasePopupWindow {
    private EditText et_send;
    private TextView tv_send;

    public PopupInput(Context context, OnPopupInputListener listener) {
        super(context);

        et_send = findViewById(R.id.et_send);
        tv_send = findViewById(R.id.tv_send);

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClickTiming()) {
                    return;
                }

                if (TextUtils.isEmpty(SpUtils.getString(R.string.token, ""))) {
                    EventBus.getDefault().post(new ClassEvent("LoginActivity"));
                    return;
                }

                String keyWord = et_send.getText().toString().trim();
                if (keyWord.isEmpty()) {
                    ToastUtils.show(context, "请输入内容");
                    return;
                }

                if (CheckUtils.checkLocalAntiSpam(keyWord)) {
                    ToastUtils.show(context, "含有敏感词汇");
                    et_send.setText("");
                    return;
                }
                et_send.setText("");
                listener.clickInput(keyWord);
                dismiss();
            }
        });

    }


    public void setText(String text) {
        et_send.setText(text);
    }

    public void setHint(String text) {
        et_send.setHint(text);
    }

    /**
     *
     * @param type 1 无色背景
     */
    public void setSentBg(int type) {
        if (type==1){
            tv_send.setTextColor(ContextCompat.getColor(getContext(),R.color.color_ffcc03));
            tv_send.setBackground(null);
        }
    }

    private EditText etSend;
    public void setEtSend(EditText send) {
        etSend= send;
    }


    public interface OnPopupInputListener {
        void clickInput(String str);
    }


    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_input);
    }


    @Override
    protected Animation onCreateShowAnimation() {
        return getTranslateVerticalAnimation(1f, 0f, 200);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getTranslateVerticalAnimation(0f, 1f, 200);
    }

    @Override
    public void onDismiss() {
        if (etSend!=null){
            etSend.setText(et_send.getText().toString().trim());
        }
        super.onDismiss();
    }
}
