package com.zuomei.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ml.base.MLBaseConstants;
import cn.ml.base.utils.MLStrUtil;
import com.baichang.android.utils.BCStringUtil;
import com.bumptech.glide.Glide;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.Contants;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qipei.interact.MLInteractVideoAty;
import com.qipei.interact.selectVideo.InteractUtils;
import com.qipei.model.ParseInfoData;
import com.qipei.part.CommentTextView;
import com.qipei.part.CustomLinkMovementMethod;
import com.txsh.R;
import com.txsh.friend.ChatAty;
import com.txsh.utils.GridViewInScrollView;
import com.zuomei.base.BaseApplication;
import com.zuomei.base.BaseLayout;
import com.zuomei.constants.APIConstants;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessageCommentData;
import com.zuomei.model.MLMessageData;
import com.zuomei.model.MLMessageInfo;
import com.zuomei.utils.MLToolUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MLMessageItemView2 extends BaseLayout {

  private Context _context;
  private MLMessageAdapter2 mlMessageAdapter;

  public MLMessageItemView2(Context context, AttributeSet attrs) {
    super(context, attrs);
    _context = context;
    init();
  }

  public MLMessageItemView2(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    _context = context;
    init();
  }

  public MLMessageItemView2(Context context, MLMessageAdapter2 mlMessageAdapter) {
    super(context);
    _context = context;
    this.mlMessageAdapter = mlMessageAdapter;
    init();
  }


  @ViewInject(R.id.message_ly_reply)
  private LinearLayout _replyLy;

  @ViewInject(R.id.message_ib_reply)
  public ImageButton _replyIbtn;
  @ViewInject(R.id.message_ib_delete)
  public ImageButton _ibdelete;

  @ViewInject(R.id.message_tv_name)
  public TextView _nameTv;

  @ViewInject(R.id.message__tv_content)
  public TextView _contentTv;

  @ViewInject(R.id.interact_tv_time)
  public TextView _timeTv;

  @ViewInject(R.id.message_gv_image)
  public GridViewInScrollView _imageGridView;

  @ViewInject(R.id.message_iv_icon)
  public ImageView _iconIv;

  @ViewInject(R.id.iv_video)
  private ImageView ivVideo;

  @ViewInject(R.id.yuyinneirong)
  private ImageView _yuyinneirong;

  @ViewInject(R.id.yuyinshijian)
  public TextView yuyinshijian;
  @ViewInject(R.id.rl_video)
  private RelativeLayout rlVideo;
  @ViewInject(R.id.video_play)
  private ImageView videoPlay;
  @ViewInject(R.id.ll_shang)
  private LinearLayout llShang;
  @ViewInject(R.id.iv_shang_num)
  private ImageView ivShangNum;
  @ViewInject(R.id.iv_jubao)
  private ImageView ivJubao;
  @ViewInject(R.id.iv_dianzan)
  private ImageView ivDianzan;
  @ViewInject(R.id.iv_shoucang)
  private ImageView ivShocang;
  @ViewInject(R.id.tv_dian_number)
  private TextView tvDianNumber;
  @ViewInject(R.id.tv_more_parse)
  private CommentTextView tvMoreParse;
  @ViewInject(R.id.rl_parse)
  RelativeLayout rlParse;
  @ViewInject(R.id.line)
  private View line;

  private int textNum = 0;
  private MLLogin user = new MLLogin();

  private SpannableStringBuilder addClickablePart(List<ParseInfoData> datas) {

    StringBuilder sbBuilder = new StringBuilder();
    String endTxt = "等 " + datas.size() + "个人觉得很赞";
    for (int i = 0; i < datas.size(); i++) {
      if (i == datas.size() - 1) {
        sbBuilder.append(datas.get(i).praiseName);
      } else {
        sbBuilder.append(datas.get(i).praiseName + "、");
      }
    }
    String str = sbBuilder.toString();
    if (sbBuilder.length() > textNum * 2 - endTxt.length()) {
      str = sbBuilder.subSequence(0, textNum * 2 - endTxt.length()).toString();
      if (str.contains("、")) {
        str = str.substring(0, str.lastIndexOf("、"));
      }
    }

    SpannableStringBuilder ssb = new SpannableStringBuilder("");
    ssb.append(str);

    for (final ParseInfoData data : datas) {
      final int start = str.indexOf(data.praiseName);
      if (start != -1) {
        int endLng = start + data.praiseName.length() + 1;
        if (start == str.lastIndexOf("、") + 1) {
          endLng = endLng - 1;
        }
        ssb.setSpan(new ClickableSpan() {
          @Override
          public void onClick(View view) {
            Message m = new Message();
            m.what = 9;
            m.obj = data;
            _handler.sendMessage(m);
          }

          @Override
          public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#0B70D7"));
            // 去掉下划线
            ds.setUnderlineText(false);
          }
        }, start, endLng, 0);
      }
    }
    return ssb.append(endTxt);
  }

  private int calTextView() {
    int num = 0;
    int txtSize = InteractUtils.sp2px(_context, 12);
    int txtMgLeft = InteractUtils.dip2px(_context, 26);
    int windowWidth = InteractUtils.getWindow((Activity) _context);
    num = (windowWidth - txtMgLeft) / txtSize;
    return num;
  }

  private void init() {
    View view = LayoutInflater.from(_context).inflate(R.layout.item_interact_list2, null);
    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    addView(view, params);
    textNum = calTextView();
    user = ((BaseApplication) ((Activity) _context).getApplication()).get_user();
    ViewUtils.inject(this, view);
  }

  @OnClick(R.id.message_ib_reply)
  public void replyClick(View view) {
    Message m = new Message();
    m.arg1 = position;
    m.what = 1;
    _handler.sendMessage(m);
  }

  @OnClick(R.id.message_iv_image)
  public void imageClick(View view) {
    Message m = new Message();
    m.arg1 = position;
    m.what = 2;
    _handler.sendMessage(m);
  }

  private Handler _handler;
  private int position;

  public void setData(final MLMessageData d, Handler handler, int i1) {
    final MLMessageInfo data = d.info;
    if (data == null) {
      return;
    }

    if (data.interactionComment.isEmpty() && d.praiseInfo.isEmpty()) {
      line.setVisibility(View.GONE);
    } else {
      line.setVisibility(View.VISIBLE);
    }
    if (!d.praiseInfo.isEmpty()) {
      rlParse.setVisibility(View.VISIBLE);
      tvMoreParse.setMovementMethod(CustomLinkMovementMethod.getInstance());
      tvMoreParse.setText(addClickablePart(d.praiseInfo), TextView.BufferType.SPANNABLE);
      tvMoreParse.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          Message m = new Message();
          m.what = 10;
          m.obj = d;
          _handler.sendMessage(m);
        }
      });
    } else {
      rlParse.setVisibility(View.GONE);
    }

    _ibdelete.setVisibility(View.VISIBLE);

    if (BCStringUtil.isEmpty(data.voice)) {
      _yuyinneirong.setVisibility(View.GONE);
      yuyinshijian.setVisibility(View.GONE);
    } else {
      if (data.voice.equals("(null)") || data.voice.equals("<null>")) {
        _yuyinneirong.setVisibility(View.GONE);
        yuyinshijian.setVisibility(View.GONE);
      } else {
        _yuyinneirong.setVisibility(View.VISIBLE);
        yuyinshijian.setVisibility(View.VISIBLE);
      }
    }

    _yuyinneirong.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Uri u = Uri.parse(APIConstants.API_IMAGE_SHOW + data.voice);
        openFile(u);
      }

    });

    yuyinshijian.setText(data.length + "″");

    //头像
    String iconUrl = "";
    if (MLToolUtil.isNull(d.user.depotName)) {
      //商家
      iconUrl = APIConstants.API_IMAGE + "?id=" + d.user.logo;
      _nameTv.setText(d.user.companyName);
      llShang.setVisibility(View.VISIBLE);
      if (!BCStringUtil.isEmpty(d.gradeNum)) {
        if (Integer.parseInt(d.gradeNum) == 1) {
          ivShangNum.setImageResource(R.mipmap.msg_members_1);
        } else if (Integer.parseInt(d.gradeNum) == 2) {
          ivShangNum.setImageResource(R.mipmap.msg_members_2);
        } else if (Integer.parseInt(d.gradeNum) == 3) {
          ivShangNum.setImageResource(R.mipmap.msg_members_3);
        } else if (Integer.parseInt(d.gradeNum) == 4) {
          ivShangNum.setImageResource(R.mipmap.msg_members_4);
        }
      } else {
        ivShangNum.setVisibility(View.GONE);
      }
    } else {
      //汽修厂
      iconUrl = APIConstants.API_IMAGE + "?id=" + d.user.userPhoto;
      _nameTv.setText(d.user.depotName);
      llShang.setVisibility(View.GONE);
    }

	/*	if(data.company!=null){
      _nameTv.setText(data.company.companyName);
			iconUrl = APIConstants.API_IMAGE+"?id="+data.company.logo;
		}else{
			_nameTv.setText(data.depot.depotName);
			iconUrl = APIConstants.API_IMAGE+"?id="+data.depot.userPhoto;
		}*/
    if (data.content == "") {
      _contentTv.setVisibility(View.GONE);
    } else {
      _contentTv.setVisibility(View.VISIBLE);
    }
    _contentTv.setText(data.content);
    //	String time = MLStringUtils.friendly_time(data.createTime);
    _timeTv.setText(data.createTime);

    //    bitmapUtils.display(_iconIv, iconUrl, bigPicDisplayConfig);
    //BaseApplication.IMAGE_CACHE.get(iconUrl, _iconIv);
    _iconIv.setTag(iconUrl);
    if (!BaseApplication.IMAGE_CACHE.get(iconUrl, _iconIv)) {
      _iconIv.setImageResource(R.drawable.wodetouxiang);
    }

    ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    if (data.images != null) {
      JSONArray jsonArray;
      try {
        jsonArray = new JSONArray(data.images);
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject jsonObject = new JSONObject(jsonArray.get(i)
              .toString());
          Map<String, Object> map = new HashMap<String, Object>();
          map.put("path",
              APIConstants.API_IMAGE_SHOW
                  + jsonObject.getString("path"));
          list.add(map);
        }
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    ShowAdapter showAdapter = new ShowAdapter(list);
    _imageGridView.setAdapter(showAdapter);

    _iconIv.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        Message m = new Message();
        m.arg1 = position;
        m.what = 3;
        m.obj = d;
        _handler.sendMessage(m);
      }
    });

    _ibdelete.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Message m = new Message();
        m.arg1 = position;
        m.what = 5;
        m.obj = data.id;
        _handler.sendMessage(m);
      }
    });

    //==============评论区==========================
    _replyLy.removeAllViews();
    position = i1;
    _handler = handler;
    //android.widget.LinearLayout.LayoutParams  param = new android.widget.LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT,android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);
    List<MLMessageCommentData> comments = data.interactionComment;
    int size = comments.size();
    for (int i = 0; i < size; i++) {
      MLReplyItemView item = new MLReplyItemView(_context);
      item.setData(comments.get(i));
      //_replyLy.addView(item,param);

      _replyLy.addView(item);
    }

    if (size > 0) {
      _replyLy.setVisibility(View.VISIBLE);

    } else {
      _replyLy.setVisibility(View.GONE);
    }

    if (!BCStringUtil.isEmpty(data.video)) {
      if (data.video.equals("(null)") || data.video.equals("<null>")) {
        ivVideo.setVisibility(View.GONE);
        rlVideo.setVisibility(View.GONE);
      } else {
        ivVideo.setVisibility(View.VISIBLE);
        rlVideo.setVisibility(View.VISIBLE);
        if (!MLStrUtil.isEmpty(data.videoPic)) {
          Glide.with(_context)
              .load(APIConstants.API_IMAGE_SHOW + data.videoPic)
              .error(R.mipmap.chat_video_pressed)
              .into(ivVideo);
        }
        final String path = APIConstants.API_IMAGE_SHOW + data.video
            .replace("\"", "").replace("[", "").replace("]", "");
        videoPlay.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(_context, MLInteractVideoAty.class);
            intent.putExtra(MLBaseConstants.TAG_INTENT_DATA, path);
            _context.startActivity(intent);
          }
        });
      }
    } else {
      videoPlay.setClickable(false);
      ivVideo.setVisibility(View.GONE);
      rlVideo.setVisibility(View.GONE);
    }

    if (!BCStringUtil.isEmpty(data.praiseNum)) {
      if (data.praiseNum.equals("0")) {
        tvDianNumber.setVisibility(View.GONE);
      } else {
        tvDianNumber.setVisibility(View.VISIBLE);
        tvDianNumber.setText(data.praiseNum);
      }
    } else {
      tvDianNumber.setVisibility(View.GONE);
    }
    if (!BCStringUtil.isEmpty(data.isPraise)) {
      if (data.isPraise.equals("1")) {
        ivDianzan.setImageResource(R.mipmap.circle_icon_like);
      } else {
        ivDianzan.setImageResource(R.mipmap.circle_icon_like_default);
      }
    } else {
      ivDianzan.setImageResource(R.mipmap.circle_icon_like_default);
    }
    if (!BCStringUtil.isEmpty(data.isCollection)) {
      if (data.isCollection.equals("1")) {
        ivShocang.setImageResource(R.mipmap.circle_icon_collect);
      } else {
        ivShocang.setImageResource(R.mipmap.circle_icon_collect_default);
      }
    } else {
      ivShocang.setImageResource(R.mipmap.circle_icon_collect_default);
    }
    ivDianzan.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!BCStringUtil.isEmpty(data.praiseNum)) {
          if (BCStringUtil.isEmpty(data.isPraise)) {
            data.isPraise = "1";
            data.praiseNum = "1";
            tvDianNumber.setText("1");
          } else {
            ParseInfoData parseInfoData = new ParseInfoData(user.isDepot ? "0" : "1", user.Id,
                user.name, user.hxUser, user.headPic, user.phone);
            if (data.isPraise.equals("1")) {
              data.isPraise = "0";
              data.praiseNum = (Integer.parseInt(data.praiseNum) - 1) + "";
              d.praiseInfo.remove(parseInfoData);
            } else {
              data.isPraise = "1";
              data.praiseNum = (Integer.parseInt(data.praiseNum) + 1) + "";
              d.praiseInfo.add(parseInfoData);
            }
            tvDianNumber.setText(data.praiseNum);
          }
        } else {
          data.praiseNum = "1";
          data.isPraise = "1";
          tvDianNumber.setText("1");
        }
        Message m = new Message();
        m.obj = d;
        m.what = 7;
        _handler.sendMessage(m);
        mlMessageAdapter.notifyDataSetChanged();
      }
    });

    ivShocang.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!BCStringUtil.isEmpty(data.isCollection)) {
          if (data.isCollection.equals("1")) {
            data.isCollection = "0";
          } else {
            data.isCollection = "1";
          }
        }
        Message m = new Message();
        m.obj = d;
        m.what = 6;
        _handler.sendMessage(m);
        mlMessageAdapter.notifyDataSetChanged();
      }
    });
    ivJubao.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Message m = new Message();
        m.obj = d;
        m.what = 8;
        _handler.sendMessage(m);
      }
    });
  }

  private class ShowAdapter extends BaseAdapter {

    private List<Map<String, Object>> mList;

    public ShowAdapter(List<Map<String, Object>> list) {
      this.mList = list;
    }

    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return mList.size();
    }

    @Override
    public Object getItem(int arg0) {
      // TODO Auto-generated method stub
      return arg0;
    }

    @Override
    public long getItemId(int arg0) {
      // TODO Auto-generated method stub
      return arg0;
    }

    @Override
    public View getView(final int position, View convertView,
        ViewGroup parent) {
      final ViewHolder mHolder;
      if (convertView == null) {
        convertView = LayoutInflater.from(_context).inflate(
            R.layout.tx_interact_image_gridview_imageview, null);
        mHolder = new ViewHolder();
        // mHolder.ll_info = (LinearLayout)
        // convertView.findViewById(R.id.ll_info);
        mHolder.image = (ImageView) convertView
            .findViewById(R.id.tx_interact_imageview);
        // mHolder.imageRleative = (ImageView) convertView
        // .findViewById(R.id.img_relative);
        convertView.setTag(mHolder);
      } else {
        mHolder = (ViewHolder) convertView.getTag();
      }

      // if(mlist.get(position).getPic().isEmpty())
      // {
      // mHolder.imageRleative.setBackgroundResource(R.drawable.pic_2x);
      // }else
      // {
      Glide.with(_context)
          .load(mList.get(position).get("path").toString())
          .error(R.drawable.default_message_header)
          .override(600, 400)
          .into(mHolder.image);

      mHolder.image.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View arg0) {
          // TODO Auto-generated method stub
          List<String> images = new ArrayList<String>();
          for (int i = 0; i < mList.size(); i++) {
            images.add(mList.get(i).get("path").toString());
          }
          Message m = new Message();
          m.arg1 = position;
          m.what = 2;
          m.obj = images;
          _handler.sendMessage(m);

        }
      });
      // }
      //
      // mHolder.active_item_text.setText(mList.get(position).getTitle()
      // + "    活动时间:" + mList.get(position).getStart_time() + "-"
      // + mList.get(position).getEnt_time());
      return convertView;
    }
  }

  class ViewHolder {

    ImageView image;

  }

  // 播放录音
  public void openFile(Uri uri) {
    MediaPlayer mp = new MediaPlayer();
    try {
      mp.reset();
      mp.setDataSource(_context, uri);
      mp.prepare();
      mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        public void onPrepared(MediaPlayer mp) {
          mp.start();
        }
      });
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (SecurityException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalStateException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
