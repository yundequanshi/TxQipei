package com.zuomei.auxiliary;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.txsh.R;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseFragment;
import com.zuomei.constants.MLConstants;
import com.zuomei.http.ZMHttpError;
import com.zuomei.http.ZMHttpRequestMessage;
import com.zuomei.http.ZMHttpType.RequestType;
import com.zuomei.http.ZMRequestParams;
import com.zuomei.model.MLDepotPartAddData;
import com.zuomei.model.MLDepotParts;
import com.zuomei.model.MLHomeCatalogData;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLRegister;
import com.zuomei.services.MLPartServices;
import com.zuomei.utils.MLToolUtil;

import java.util.List;

import cn.ml.base.utils.IEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 汽修厂-配件报价
 *
 * @author Marcello
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MLDepotPartAdd extends BaseFragment {

  public static MLDepotPartAdd INSTANCE = null;

  public static MLDepotPartAdd instance() {
    //	if(INSTANCE==null){
    INSTANCE = new MLDepotPartAdd();
    //	}
    return INSTANCE;
  }

  @ViewInject(R.id.mListView)
  private ListView mListPart;


  //配件名称
  @ViewInject(R.id.et_part_name)
  private EditText mEtPartName;

  //配件数量
  @ViewInject(R.id.et_part_num)
  private EditText mEtPartNum;

  //汽修厂名称
  @ViewInject(R.id.et_name)
  private EditText mEtDepotName;


  //车型
  @ViewInject(R.id.et_cartype)
  private TextView mCarTV;

  //子车型
  @ViewInject(R.id.et_carchild)
  private EditText mChildEt;

  //车架号
  @ViewInject(R.id.et_chejia)
  private EditText mChejiaEt;

  //年份
  @ViewInject(R.id.et_year)
  private EditText mYearEt;

  //备注
  @ViewInject(R.id.et_content)
  private EditText mRemarkEt;

  //完成
  @ViewInject(R.id.btn_list)
  private Button mOkBtn;

  private MLDepotAddAdapter _adapter;

  private Context _context;

  private MLDepotPartAddData _partData;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.depot_part, null);
    ViewUtils.inject(this, view);

    _context = inflater.getContext();

    initView();
    //	initData();
    return view;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    EventBus.getDefault().register(this);
  }


  private void initData() {
		/*MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		  ZMRequestParams params = new ZMRequestParams();
		  
		  
			if(user.isDepot){
				params.addParameter("depotId",user.Id);
			}else{
				params.addParameter("companyId",user.Id);
			}
			
		
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_PACKET_INFO, null, params, _handler,HTTP_RESPONSE_MYPACK, MLMyServices.getInstance());
	       loadData(_context, message2);*/
  }

  private void initView() {
    MLLogin user = BaseApplication.getInstance().get_user();
    mEtDepotName.setText(user.name);
    _adapter = new MLDepotAddAdapter(_context);
    mListPart.setAdapter(_adapter);
    setListViewHeightBasedOnChildren(mListPart);

    mListPart.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
          long arg3) {

        MLDepotParts data = (MLDepotParts) arg0.getAdapter().getItem(arg2);
        String message = String.format("是否删除<font color=\"#c42b20\">%s</font>?", data.name);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),
            AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("提示");
        builder.setMessage(Html.fromHtml(message));
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {

          @Override
          public void onClick(DialogInterface dialog, int which) {
            _adapter.getList().remove(arg2);
            _adapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(mListPart);
          }
        });
        builder.setPositiveButton("取消", null);
        builder.show();
      }
    });
  }


  @OnClick(R.id.btn_list)
  public void submitOnClick(View view) {
    String contactName = mEtDepotName.getText().toString();
    if (MLToolUtil.isNull(contactName)) {
      showMessage(" 汽修厂名称不能为空!");
      return;
    }

    if (mCatalogData == null) {
      showMessage("请选择车型");
      return;
    }

    String childType = mChildEt.getText().toString();
    if (MLToolUtil.isNull(childType)) {
      showMessage(" 子车型不能为空!");
      return;
    }

    String carNum = mChejiaEt.getText().toString();
    if (MLToolUtil.isNull(carNum)) {
      showMessage(" 车架号不能为空!");
      return;
    }

    String particularYear = mYearEt.getText().toString();
    if (MLToolUtil.isNull(particularYear)) {
      showMessage(" 年份不能为空!");
      return;
    }
    String remark = mRemarkEt.getText().toString();
/*		String remark =  mRemarkEt.getText().toString();
		if(MLToolUtil.isNull(remark)){
			showMessage(" 备注不能为空!");
			return;
		}*/

    List<MLDepotParts> parts = _adapter.getList();
    if (parts == null || parts.size() == 0) {
      showMessage(" 请添加配件!");
      return;
    }

    _partData = new MLDepotPartAddData();
    _partData.contactName = contactName;
    _partData.childType = childType;
    _partData.typeId = mCatalogData.id;
    _partData.particularYear = particularYear;
    _partData.carNum = carNum;
    _partData.depotUserId = BaseApplication.getInstance().get_user().Id;
    _partData.remark = remark;
    _partData.accidentParts = parts;

    Gson gson = new Gson();

    ZMRequestParams params = new ZMRequestParams();
    params.addParameter("data", gson.toJson(_partData));

    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.DEPOT_PART_ADD, null,
        params, _handler, HTTP_RESPONSE_UPDATE, MLPartServices.getInstance());
    loadDataWithMessage(_context, "数据提交中...", message2);


  }


  private void request(String datas) {
		/*MLLogin user = ((BaseApplication)getActivity().getApplication()).get_user();
		ZMRequestParams params = new ZMRequestParams();
			params.addParameter("depotUser.id",user.Id);
		params.addParameter("company.id",_data.userId+"");
		params.addParameter("sendTime",_timeTv.getText().toString()+"");
		params.addParameter("images",datas);
		
	    ZMHttpRequestMessage message2 = new ZMHttpRequestMessage(RequestType.MY_PHONE_BUISNESS, null, params, _handler,HTTP_RESPONSE_UPDATE, MLMyServices.getInstance());
	    loadData(_context, message2);*/
  }


  @OnClick(R.id.top_back)
  public void backOnClick(View view) {
    ((MLAuxiliaryActivity) _context).finish();
  }

  @OnClick(R.id.et_cartype)
  public void carOnClick(View view) {
    toActivity(_context, MLConstants.MY_PART_CAR, null);
  }


  //	private List<MLDepotParts> mAddDatas = new ArrayList<MLDepotParts>();
  @OnClick(R.id.btn_add)
  public void addOnClick(View view) {
    String name = mEtPartName.getText().toString();
    String num = mEtPartNum.getText().toString();
    if (MLToolUtil.isNull(name)) {
      showMessageWarning("请填写配件名称!");
      return;
    }
    if (MLToolUtil.isNull(num)) {
      showMessageWarning("请填写配件数量!");
      return;
    }
    MLDepotParts data = new MLDepotParts(name, num);
    _adapter.addData(data);
    //记录
    //	mAddDatas.add(data);
    setListViewHeightBasedOnChildren(mListPart);
    mEtPartName.setText("");
    mEtPartNum.setText("1");
    MLToolUtil.hideKeyboard(_context);
  }


  @Override
  public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }

  private static final int HTTP_RESPONSE_UPDATE = 0;
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
        case HTTP_RESPONSE_UPDATE: {
          MLRegister ret = (MLRegister) msg.obj;
          if (ret.datas) {
            showMessageSuccess("提交成功!");
            //	_event.onEvent(null, MLConstants.MY_BILL2_BUSINESS_LIST);
            ((MLAuxiliaryActivity) _context).finish();
          } else {
            showMessageError("提交失败!");
          }
          break;
        }

        default:
          break;
      }
    }
  };

  private MLHomeCatalogData mCatalogData;

  @Subscribe
  public void setCarInfo(MLHomeCatalogData data) {
    if (data == null) {
      return;
    }
    mCatalogData = data;
    mCarTV.setText(data.name);
  }

  private IEvent<Object> _event;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    _event = (IEvent<Object>) activity;
  }


  public static void setListViewHeightBasedOnChildren(ListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      // pre-condition
      return;
    }

    int totalHeight = 0;
    int size = listAdapter.getCount();
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.setLayoutParams(params);
  }
}
