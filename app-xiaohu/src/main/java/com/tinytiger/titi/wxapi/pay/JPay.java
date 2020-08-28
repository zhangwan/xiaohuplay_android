package com.tinytiger.titi.wxapi.pay;

import android.app.Activity;
import android.text.TextUtils;


import com.tinytiger.titi.wxapi.pay.alipay.Alipay;
import com.tinytiger.titi.wxapi.pay.weixin.WeiXinPay;

import org.json.JSONException;
import org.json.JSONObject;


public class JPay {
    private static JPay mJPay;
    private Activity mContext;

    private JPay(Activity context) {
        mContext = context;
    }

    public static JPay getIntance(Activity context) {
        if (mJPay == null) {
            synchronized (JPay.class) {
                if (mJPay == null) {
                    mJPay = new JPay(context);
                }
            }
        }
        return mJPay;
    }

    public interface JPayListener {
        //支付成功
        void onPaySuccess();

        //支付失败
        void onPayError(int error_code, String message);

        //支付取消
        void onPayCancel();
    }

    public enum PayMode {
        WXPAY, ALIPAY
    }

    public void toPay(PayMode payMode, String payParameters, JPayListener listener) {
        if (payMode.name().equalsIgnoreCase(PayMode.WXPAY.name())) {
            toWxPay(payParameters, listener);
        } else if (payMode.name().equalsIgnoreCase(PayMode.ALIPAY.name())) {
            toAliPay(payParameters, listener);
        }
    }


    public void toWxPay(String payParameters, JPayListener listener) {
        if (payParameters != null) {
            JSONObject param = null;
            try {
                param = new JSONObject(payParameters);
            } catch (JSONException e) {
                e.printStackTrace();
                if (listener != null) {
                    listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }

            if (TextUtils.isEmpty(param.optString("appid")) || TextUtils.isEmpty(param.optString("partnerid"))
                    || TextUtils.isEmpty(param.optString("prepayid")) || TextUtils.isEmpty(param.optString("noncestr"))) {
                if (listener != null) {
                    listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
                }
                return;
            }

            WeiXinPay.getInstance(mContext).startWXPay(param.optString("appid"),
                    param.optString("partnerid"), param.optString("prepayid"),
                    param.optString("noncestr"), param.optString("timestamp"),
                    param.optString("sign"), listener);
        } else {
            if (listener != null) {
                listener.onPayError(WeiXinPay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }


    public void toAliPay(String payParameters, JPayListener listener) {
        if (payParameters != null) {
            if (listener != null) {
                Alipay.getInstance(mContext).startAliPay(payParameters, listener);
            }
        } else {
            if (listener != null) {
                listener.onPayError(Alipay.PAY_PARAMETERS_ERROE, "参数异常");
            }
        }
    }
}
