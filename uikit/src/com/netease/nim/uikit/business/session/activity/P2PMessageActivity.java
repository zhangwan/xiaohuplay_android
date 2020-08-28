package com.netease.nim.uikit.business.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.tinytiger.common.base.BaseApp;
import com.tinytiger.common.event.ClassEvent;
import com.tinytiger.common.net.data.msg.UserKitBean;
import com.tinytiger.common.utils.Dp2PxUtils;
import com.tinytiger.common.utils.toast.ToastUtils;
import com.tinytiger.common.widget.dialog.TextDialog;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nim.uikit.mvp.P2pEvent;
import com.netease.nim.uikit.mvp.UserKitContract;
import com.netease.nim.uikit.mvp.UserKitPresenter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends BaseMessageActivity implements UserKitContract.View {

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    private UserKitPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        mPresenter = new UserKitPresenter();
        mPresenter.attachView(this);
        initView();

        displayOnlineState();
        registerObservers(true);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private UserPopup mDemoPopup;
    private TextView tv_title;

    private void initView() {
        tv_title = findView(R.id.tv_title);
        findView(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // 单聊特例话数据，包括个人信息，
        tv_title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        if (sessionId.equals("titixhdj1d50e8d7a924f573bb4954b")){
            findView(R.id.right_iv).setVisibility(View.GONE);
        } else {
          ImageView right_iv=findView(R.id.right_iv);
            right_iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (TextUtils.isEmpty(uid)){
                        //ToastUtils.show(v.getContext(),"获取用户信息失败");
                        return;
                    }

                    if (mDemoPopup == null) {
                        mDemoPopup = new UserPopup(v.getContext());
                        mDemoPopup.setShowAnimation(createScaleAnimation(1f, 1f, 0f, 1f, 0.5f, 0f))
                                .setDismissAnimation(createScaleAnimation(1f, 1f, 1f, 0f, 0.5f, 0f));
                        mDemoPopup.setPopupGravity(Gravity.BOTTOM);
                    }

                    mDemoPopup.setBack(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDemoPopup.dismiss();
                            showBlack();
                        }
                    });

                    int[] location=new int[2];

                    right_iv.getLocationInWindow(location);
                    right_iv.getLocationOnScreen(location);
                    mDemoPopup.setBackground(null)
                            .setBlurBackgroundEnable(false)
                            .showPopupWindow(location[0] - Dp2PxUtils.dip2px(right_iv.getContext(), 70), location[1] + Dp2PxUtils.dip2px(right_iv.getContext(), 30));
                }
            });

            mPresenter.getUserRelation(sessionId);
        }

    }

   private Animation createScaleAnimation(float fromX, float toX, float fromY, float toY, float pivotXValue, float pivotYValue) {
        Animation scaleAnimation = new ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue);
        scaleAnimation.setDuration(200);
        return scaleAnimation;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        if (mDemoPopup!=null){
            mDemoPopup.dismiss();
            mDemoPopup=null;
        }

        mPresenter.detachView();
    }


    private void displayOnlineState() {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
        setSubTitle(detailContent);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMainLoginEvent(P2pEvent event) {
       if (event.type==1 && !TextUtils.isEmpty(uid)&& sessionId.equals(event.sessionId)){
           EventBus.getDefault().post(new ClassEvent("HomepageActivity",uid));
       }
    }



    /**
     * 用户信息变更观察者
     */
    private UserInfoObserver userInfoObserver = new UserInfoObserver() {
        @Override
        public void onUserInfoChanged(List<String> accounts) {
            if (!accounts.contains(sessionId)) {
                return;
            }
            tv_title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    /**
     * 好友资料变更（eg:关系）
     */
    private ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            tv_title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            tv_title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            tv_title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            tv_title.setText(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    /**
     * 好友在线状态观察者
     */
    private OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            if (!accounts.contains(sessionId)) {
                return;
            }
            // 按照交互来展示
            displayOnlineState();
        }
    };

    private void registerObservers(boolean register) {
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        if (NimUIKit.enableOnlineState()) {
            NimUIKit.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
        }
    }


    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new NimToolBarOptions();
        if (options.titleId != 0) {
            tv_title.setText(options.titleId);
        }
        if (!TextUtils.isEmpty(options.titleString)) {
            tv_title.setText(options.titleString);
        }
    }

    @Override
    protected boolean enableSensor() {
        return true;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showErrorMsg(@NotNull String errorMsg, int errorCode) {
        ToastUtils.show(BaseApp.getContext(),errorMsg);
    }

    private void showBlack(){
        TextDialog dialog =new TextDialog();
        dialog.setFragmentManager(getSupportFragmentManager());
        dialog.setMessage("确认要拉黑对方?");
        dialog.setViewListener(new TextDialog.ViewListener(){
            @Override
            public void click() {
                mPresenter.loadAddBlack(uid);
            }
        });
        dialog.show();
    }

    public void addBlack() {
        NIMClient.getService(FriendService.class).addToBlackList(sessionId).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ToastUtils.show(BaseApp.getContext(),"拉黑成功");
                onBackPressed();
            }

            @Override
            public void onFailed(int i) {
                ToastUtils.show(BaseApp.getContext(),"拉黑失败");
                //   LoadingUtils.getInstance().dismiss()
            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    @Override
    public void getBlack(boolean type) {
        if (type) {
            addBlack();
        }
    }

    //#0未关注 1已关注 2相互关注
    private int follow = 0;

    //#私信配置 1所有人 2我关注的人
    private int private_letter = 1;

    private String uid = "";

    @Override
    public void getUserRelation(@NotNull UserKitBean bean) {
        //禁止发送私信
     //   fragment().setNoMsg(bean.data.user_relation.follow, bean.data.user_config.private_letter, bean.data.token_user_config.private_letter);
        uid=bean.data.user_config.user_id;
    }
}
