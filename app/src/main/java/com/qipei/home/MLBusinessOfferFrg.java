package com.qipei.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.adapter.MLBusinessOfferAdapter;
import com.txsh.R;
import com.zuomei.auxiliary.MLBusinessInfoAdapter;
import com.zuomei.auxiliary.MLMyStockAddPop;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLHomeBusinessData;
import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMyStockDetail;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLHomeServices;

import java.util.ArrayList;
import java.util.List;

import cn.ml.base.utils.IEvent;
import cn.ml.base.utils.MLDialogUtils;
import cn.ml.base.utils.MLStrUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Marcello on 2015/6/15.
 */
@SuppressLint("ValidFragment")
public class MLBusinessOfferFrg extends BaseFragment {


    private MLHomeBusinessData mData;
    private View view;
    private MLBusinessInfoAdapter _productAdapter;
    private Context _context;
    private MLLogin _user;

    @ViewInject(R.id.offer_tv_add)
    private TextView mTvAdd;

    @ViewInject(R.id.offer_tv_type)
    private TextView mTvType;

    @ViewInject(R.id.offer_tv_child)
    private EditText mEtChild;

    @ViewInject(R.id.offer_et_year)
    private EditText mEtYear;
    @ViewInject(R.id.offer_et_frame)
    private EditText mEtFrame;

    @ViewInject(R.id.offer_lv)
    private ListView mList;
    @ViewInject(R.id.root)
    private RelativeLayout _root;

    private List<MLMyStockDetail> mDataList;

    private MLBusinessOfferAdapter mAdapter;

    public MLBusinessOfferFrg(MLHomeBusinessData data) {
        mData = data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _context = inflater.getContext();
        _user =	((BaseApplication)getActivity().getApplication()).get_user();
        view = inflater.inflate(R.layout.qp_business_offer, null);
        ViewUtils.inject(this, view);

        initView();
        return view;
    }

    private void initView() {
        mDataList = new ArrayList<MLMyStockDetail>();
        mAdapter = new MLBusinessOfferAdapter(_context,R.layout.item_offer_add);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String s[] = {"删除"};

                AlertDialog builder = MLDialogUtils.getAlertDialog(_context)
                        .setItems(s, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDataList.remove(position);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .setTitle("操作").create();
                builder.show();
            }
        });
    }

    @OnClick(R.id.accident_btn_next)
    public void submitOnClick(View view){


        Gson gson = new GsonBuilder().enableComplexMapKeySerialization()
                .create();


        if(mCatalogData==null){
            showMessage("请先选择车型");
            return;
        }

        String  child = mEtChild.getText().toString();
        String  year = mEtYear.getText().toString();
        String frame = mEtFrame.getText().toString();



        if(MLStrUtil.isEmpty(child)){
            showMessage("子车型不能为空");
            return;
        }
        if(MLStrUtil.isEmpty(year)){
            showMessage("排量年款不能为空");
            return;
        }
        if(MLStrUtil.isEmpty(frame)){
            showMessage("车架号不能为空");
            return;
        }
        if(mDataList==null||mDataList.size()==0){
            showMessage("请先选择配件");
            return;
        }

        MLLogin user = BaseApplication.getInstance().get_user();
        ZMRequestParams params = new ZMRequestParams();

        params.addParameter("userId",user.Id);
        params.addParameter("companyId",mData.id);
        params.addParameter("typeId",mCatalogData.id);
        params.addParameter("childType",child);
        params.addParameter("displacement",child);
        params.addParameter("frameNumber", frame);
        params.addParameter("spareParts", gson.toJson(mDataList));

        ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(ZMHttpType.RequestType.OFFER_ADD, null, params, _handler,HTTP_RESPONSE_OFFER_ADD , MLHomeServices.getInstance());
        loadDataWithMessage(_context, "正在发布...", message2);
    }

    private static final int HTTP_RESPONSE_OFFER_ADD= 0;
    private Handler _handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgressDialog();
            if (msg == null || msg.obj == null) {
                showMessage(R.string.loading_data_failed);
                return;
            }
            if (msg.obj instanceof ZMHttpError) {
                ZMHttpError error = (ZMHttpError) msg.obj;
                showMessage(error.errorMessage);
                return;
            }
            switch (msg.what) {
                case  HTTP_RESPONSE_OFFER_ADD:{
                    MLRegister ret = (MLRegister) msg.obj;
                    if(ret.datas){
                        showMessageSuccess("提交成功!");
                        refreshView();
                    }else{
                        showMessageError("提交失败!");
                    }
                    break;
                }


                default:
                    break;
            }
        }
    };

    private void refreshView() {
        mTvType.setText("");
        mEtChild.setText("");
        mEtYear.setText("");
        mEtFrame.setText("");
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.offer_tv_type)
    public void choiseOnClick(View view){
        toActivity(_context, MLConstants.MY_PART_CAR, null);
    }

    private MLMyStockAddPop _addPop;
    @OnClick(R.id.offer_tv_add)
    public void addOnClick(View view){
        _addPop = new MLMyStockAddPop(_context, new IEvent<String>() {
            @Override
            public void onEvent(Object source, String eventArg) {
                if(source==null) return;
                mDataList.add((MLMyStockDetail)source);

                mAdapter.setData(mDataList);
            }
        });
        _addPop.showAtLocation(_root, Gravity.CENTER, 0, 0);
    }


    private MLHomeCatalogData mCatalogData;
    @Subscribe
    public void setCarInfo(MLHomeCatalogData data) {
        if(data==null) return;
        mCatalogData = data;
        mTvType.setText(data.name);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
