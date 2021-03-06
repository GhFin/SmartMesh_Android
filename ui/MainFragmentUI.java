package com.lingtuan.firefly.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lingtuan.firefly.NextApplication;
import com.lingtuan.firefly.R;
import com.lingtuan.firefly.base.BaseFragment;
import com.lingtuan.firefly.db.user.FinalUserDataBase;
import com.lingtuan.firefly.fragment.MainContactFragmentUI;
import com.lingtuan.firefly.fragment.MainFoundFragmentUI;
import com.lingtuan.firefly.fragment.MainMessageFragmentUI;
import com.lingtuan.firefly.fragment.MySelfFragment;
import com.lingtuan.firefly.login.LoginUtil;
import com.lingtuan.firefly.offline.AppNetService;
import com.lingtuan.firefly.service.LoadDataService;
import com.lingtuan.firefly.util.Constants;
import com.lingtuan.firefly.util.MyToast;
import com.lingtuan.firefly.util.Utils;
import com.lingtuan.firefly.vo.ChatMsg;
import com.lingtuan.firefly.vo.UserInfoVo;
import com.lingtuan.firefly.wallet.fragment.AccountFragment;
import com.lingtuan.firefly.wallet.fragment.NewWalletFragment;
import com.lingtuan.firefly.wallet.util.WalletStorage;
import com.lingtuan.firefly.xmpp.XmppAction;
import com.lingtuan.firefly.xmpp.XmppUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created on 2017/8/23.
 */

public class MainFragmentUI extends AppCompatActivity implements View.OnClickListener {

    private TextView main_tab_chats;//The message
    private TextView main_tab_contact; //The address book
    private TextView main_tab_account;//The wallet
    private TextView main_tab_found;//found
    private TextView main_tab_setting;//my


    private MsgReceiverListener msgReceiverListener;

    private Stack<String> mStack = new Stack<>();
    private Map<String, BaseFragment> map = new HashMap<>();

    private long firstTime;
    private int totalUnreadCount = 0;
    private TextView mMsgUnread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById();
        setListener();
        initData();
    }

    	@Override
	protected void onSaveInstanceState(Bundle outState) {

	}

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Utils.settingLanguage(MainFragmentUI.this);
        Utils.updateViewLanguage(findViewById(android.R.id.content));
    }

    private void findViewById() {
        main_tab_chats = (TextView) findViewById(R.id.main_tab_chats);
        main_tab_contact = (TextView) findViewById(R.id.main_tab_contact);
        main_tab_account = (TextView) findViewById(R.id.main_tab_account);
        main_tab_found = (TextView) findViewById(R.id.main_tab_found);
        main_tab_setting = (TextView) findViewById(R.id.main_tab_setting);
        mMsgUnread = (TextView) findViewById(R.id.main_unread);
    }
    private void setListener() {
        main_tab_chats.setOnClickListener(this);
        main_tab_contact.setOnClickListener(this);
        main_tab_account.setOnClickListener(this);
        main_tab_found.setOnClickListener(this);
        main_tab_setting.setOnClickListener(this);
    }

    private void initData() {

        IntentFilter filter = new IntentFilter();
        filter.addAction(LoadDataService.ACTION_START_CONTACT_LISTENER);
        filter.addAction(XmppAction.ACTION_MAIN_UNREADMSG_UPDATE_LISTENER);
        filter.addAction(Constants.CHANGE_LANGUAGE);//Refresh the page
        filter.addAction(Constants.ACTION_CLOSE_MAIN);//Shut down
        filter.addAction(Constants.ACTION_NETWORK_RECEIVER);//Network monitoring
        filter.addAction(Constants.WALLET_SUCCESS);//Refresh the page
        filter.addAction(Constants.WALLET_REFRESH_DEL);//Refresh the page
        registerReceiver(mBroadcastReceiver, filter);

        main_tab_chats.setSelected(true);

        showFragment(MainMessageFragmentUI.class);

        //Start without social network service
        startService(new Intent(this, AppNetService.class));

        if (Utils.isConnectNet(MainFragmentUI.this) && NextApplication.myInfo != null && TextUtils.isEmpty(NextApplication.myInfo.getToken()) && TextUtils.isEmpty(NextApplication.myInfo.getMid())){
            LoginUtil.getInstance().initContext(MainFragmentUI.this);
            LoginUtil.getInstance().register(NextApplication.myInfo.getUsername(),NextApplication.myInfo.getPassword(),null,NextApplication.myInfo.getLocalId(),null);
        }

    }

    private void registerReceive() {
        IntentFilter filter = new IntentFilter(XmppAction.ACTION_MESSAGE_EVENT_LISTENER);
        filter.addAction(XmppAction.ACTION_OFFLINE_MESSAGE_LIST_EVENT_LISTENER);
        msgReceiverListener = new MsgReceiverListener();
        registerReceiver(msgReceiverListener, filter);
    }

    class MsgReceiverListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && XmppAction.ACTION_MESSAGE_EVENT_LISTENER.equals(intent.getAction())) {
                Bundle bundle = intent.getBundleExtra(XmppAction.ACTION_MESSAGE_EVENT_LISTENER);
                if (bundle != null) {
                    ChatMsg msg = (ChatMsg) bundle.getSerializable("chat");
                    if (msg != null) {
                        if (TextUtils.equals("system-0", msg.getChatId()) && msg.getUnread() == 0)//已有的添加好友消息
                        {
                            return;
                        } else if (msg.getGroupMask() && !TextUtils.equals("system-0", msg.getChatId()) && !TextUtils.equals("system-1", msg.getChatId()) && !TextUtils.equals("system-3", msg.getChatId()) && !TextUtils.equals("system-4", msg.getChatId())) {//屏蔽的信息不与管理
                            return;
                        } else if ("system-2".equals(msg.getChatId())) {
                            return;
                        }
                    }
                    int unread = bundle.getInt("unread", 1000);
                    if (unread != 1000) {
                        totalUnreadCount = totalUnreadCount + unread;
                        Utils.formatUnreadCount(mMsgUnread, totalUnreadCount);
                        return;
                    }
                }

                Utils.formatUnreadCount(mMsgUnread, ++totalUnreadCount);
            } else if (intent != null && XmppAction.ACTION_OFFLINE_MESSAGE_LIST_EVENT_LISTENER.equals(intent.getAction())) {
                int totalCount = intent.getIntExtra("totalCount", 0);
                totalUnreadCount += totalCount;
                Utils.formatUnreadCount(mMsgUnread, totalUnreadCount);
            }
        }
    }

    private void getUnreadCount() {
        Map<String, Integer> map = FinalUserDataBase.getInstance().getUnreadMap();
        totalUnreadCount = map.get("totalunread");
        Utils.formatUnreadCount(mMsgUnread, totalUnreadCount);
    }




    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && (Constants.CHANGE_LANGUAGE.equals(intent.getAction()))) {
                Utils.updateViewLanguage(findViewById(R.id.main_linear));
            } else if (intent != null && LoadDataService.ACTION_START_CONTACT_LISTENER.equals(intent.getAction())) {
                getContentResolver().registerContentObserver( ContactsContract.Contacts.CONTENT_URI, true, mObserver);
            } else if (intent != null && XmppAction.ACTION_MAIN_UNREADMSG_UPDATE_LISTENER.equals(intent.getAction())) {
                getUnreadCount();
            }else if (intent != null && Constants.ACTION_CLOSE_MAIN.equals(intent.getAction())) {
                finish();
            }else if (intent != null && Constants.ACTION_NETWORK_RECEIVER.equals(intent.getAction())) {
                if (Constants.isConnectNet){
                    if (NextApplication.myInfo != null && TextUtils.isEmpty(NextApplication.myInfo.getToken()) && !TextUtils.isEmpty(NextApplication.myInfo.getMid())){
                        LoginUtil.getInstance().initContext(MainFragmentUI.this);
                        LoginUtil.getInstance().login(NextApplication.myInfo.getMid(),NextApplication.myInfo.getPassword(),false);
                    }else if (NextApplication.myInfo != null && TextUtils.isEmpty(NextApplication.myInfo.getToken()) && TextUtils.isEmpty(NextApplication.myInfo.getMid())){
                        LoginUtil.getInstance().initContext(MainFragmentUI.this);
                        LoginUtil.getInstance().register(NextApplication.myInfo.getUsername(),NextApplication.myInfo.getPassword(),null,NextApplication.myInfo.getLocalId(),null);
                    }
                }
            }else if (intent != null && (Constants.WALLET_SUCCESS.equals(intent.getAction()))){
                if(WalletStorage.getInstance(getApplicationContext()).get().size()>0){
                    showFragment(AccountFragment.class);
                } else{
                    showFragment(NewWalletFragment.class);
                }
                selectChanged(R.id.main_tab_account);
            }else if (intent != null && (Constants.WALLET_REFRESH_DEL.equals(intent.getAction()))){
                if(WalletStorage.getInstance(getApplicationContext()).get().size()>0){
                    showFragment(AccountFragment.class);
                } else{
                    showFragment(NewWalletFragment.class);
                }
                selectChanged(R.id.main_tab_account);
            }
        }
    };

    //The monitoring objects to monitor contact data
    private ContentObserver mObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            // When change contacts table for the corresponding operation
            /**Open directory upload service*/
            Utils.intentServiceAction(MainFragmentUI.this, LoadDataService.ACTION_UPLOAD_CONTACT, null);
        }
    };

    @Override
    public void onClick(View v){
        switch (v.getId()){

            case R.id.main_tab_chats:
                showFragment(MainMessageFragmentUI.class);
                break;
            case R.id.main_tab_contact:
                showFragment(MainContactFragmentUI.class);
                break;
            case R.id.main_tab_account:
                if(WalletStorage.getInstance(getApplicationContext()).get().size()>0){
                    showFragment(AccountFragment.class);
                } else{
                    showFragment(NewWalletFragment.class);
                }
                break;
            case R.id.main_tab_found:
                showFragment(MainFoundFragmentUI.class);
                break;
            case R.id.main_tab_setting:
                showFragment(MySelfFragment.class);
                break;
        }
        XmppUtils.loginXmppForNextApp(this);
        selectChanged(v.getId());
    }

    private void selectChanged(final int resId) {
        main_tab_chats.setSelected(false);
        main_tab_contact.setSelected(false);
        main_tab_account.setSelected(false);
        main_tab_found.setSelected(false);
        main_tab_setting.setSelected(false);

        switch (resId) {
            case R.id.main_tab_chats:
                main_tab_chats.setSelected(true);
                break;

            case R.id.main_tab_contact:
                main_tab_contact.setSelected(true);
                break;

            case R.id.main_tab_found:
                main_tab_found.setSelected(true);
                break;

            case R.id.main_tab_setting:
                main_tab_setting.setSelected(true);
                break;

            case R.id.main_tab_account:
                main_tab_account.setSelected(true);
                break;
        }
    }

    /**
     * Show the new fragments
     *
     * @param c
     */
    private void showFragment(Class<? extends BaseFragment> c) {
        try {
            BaseFragment fragment;
            // If mStack greater than 0, the hidden
            if (mStack.size() > 0) {
                getSupportFragmentManager().beginTransaction().hide(map.get(mStack.pop())).commitAllowingStateLoss();
            }
            mStack.add(c.getSimpleName());
            // If it already exists before the show, if not, create new one
            if (map.containsKey(c.getSimpleName())) {
                fragment = map.get(c.getSimpleName());
                getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
            } else {
                fragment = c.newInstance();
                map.put(c.getSimpleName(), fragment);
                getSupportFragmentManager().beginTransaction().add(R.id.main_frame, fragment, c.getSimpleName()).commitAllowingStateLoss();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstTime > 2000){
            MyToast.showToast(MainFragmentUI.this,getString(R.string.exit_app));
            firstTime = System.currentTimeMillis();
        }else{
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100){
            if(WalletStorage.getInstance(getApplicationContext()).get().size()>0){
                showFragment(AccountFragment.class);
            } else{
                showFragment(NewWalletFragment.class);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NextApplication.myInfo == null) {//When the application memory is being reclaimed myinfo is empty to copy
            NextApplication.myInfo = new UserInfoVo().readMyUserInfo(this);
        }
        registerReceive();
        XmppUtils.loginXmppForNextApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (msgReceiverListener != null) {
            unregisterReceiver(msgReceiverListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        LoginUtil.getInstance().destory();
    }

}
