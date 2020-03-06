package com.qipei.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.widget.Toast;
import cn.ml.base.MLBaseConstants;
import cn.ml.base.utils.MLDeviceUtils;
import cn.ml.base.utils.MLStrUtil;
import com.baichang.android.utils.BCStringUtil;
import com.bumptech.glide.Glide;
import com.easemob.easeui.model.HxUser;
import com.easemob.easeui.utils.Contants;
import com.fitpopupwindow.DensityUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.qipei.interact.MLInteractVideoAty;
import com.qipei.interact.selectVideo.InteractUtils;
import com.qipei.model.ParseInfoData;
import com.qipei.part.CommentTextView;
import com.qipei.part.CustomLinkMovementMethod;
import com.txsh.R;
import com.txsh.friend.ChatAty;
import com.txsh.utils.GridViewInScrollView;
import com.zuomei.base.BaseApplication;
import com.zuomei.constants.APIConstants;
import com.zuomei.home.MLReplyItemView;
import com.zuomei.model.MLLogin;
import com.zuomei.model.MLMessageCommentData;
import com.zuomei.model.MLMessageData;
import com.zuomei.model.MLMessageInfo;
import com.zuomei.utils.MLToolUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ml.base.MLAdapterBase;
import cn.ml.base.widget.roundedimageview.RoundedImageView;

/**
 * Created by Marcello on 2015/6/3.
 */
public class MLInteractAdapter extends MLAdapterBase<MLMessageData> {

  public Handler _handler;
  public Context _context;
  BitmapUtils bitmapUtils;
  public int textNum = 0;
  private MLLogin user = new MLLogin();

  public MLInteractAdapter(Context context, int viewXml, Handler handler) {
    super(context, viewXml);
    _handler = handler;
    _context = context;
    bitmapUtils = new BitmapUtils(_context);
    textNum = calTextView();
    user = ((BaseApplication) ((Activity) _context).getApplication()).get_user();
    Log.d("textNum", textNum + "");
  }

  @ViewInject(R.id.message_ly_reply)
  private LinearLayout _replyLy;

  @ViewInject(R.id.message_ib_reply)
  public ImageButton _replyIbtn;

  @ViewInject(R.id.message_tv_name)
  public TextView _nameTv;

  @ViewInject(R.id.message__tv_content)
  public TextView _contentTv;

  @ViewInject(R.id.yuyinshijian)
  public TextView yuyinshijian;

  @ViewInject(R.id.interact_tv_time)
  public TextView _timeTv;

  @ViewInject(R.id.message_gv_image)
  public GridViewInScrollView _imageGridView;

  @ViewInject(R.id.message_iv_icon)
  public RoundedImageView _iconIv;

  @ViewInject(R.id.yuyinneirong)
  private ImageView _yuyinneirong;

  @ViewInject(R.id.iv_video)
  private ImageView ivVideo;
  @ViewInject(R.id.ll_shang)
  private LinearLayout llShang;
  @ViewInject(R.id.iv_shang_num)
  private ImageView ivShangNum;
  @ViewInject(R.id.rl_video)
  private RelativeLayout rlVideo;
  @ViewInject(R.id.video_play)
  private ImageView videoPlay;
  @ViewInject(R.id.tv_dian_number)
  private TextView tvDianNumber;
  @ViewInject(R.id.tv_siliao)
  private TextView ivSiliao;
  @ViewInject(R.id.iv_jubao)
  private ImageView ivJubao;
  @ViewInject(R.id.iv_dianzan)
  private ImageView ivDianzan;
  @ViewInject(R.id.iv_shoucang)
  private ImageView ivShocang;
  @ViewInject(R.id.tv_more_parse)
  private CommentTextView tvMoreParse;
  @ViewInject(R.id.rl_parse)
  RelativeLayout rlParse;
  @ViewInject(R.id.line)
  private View line;

  MediaPlayer mp;

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
            m.arg1 = mPosition;
            m.what = 7;
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
    int txtSize = InteractUtils.sp2px(mContext, 12);
    int txtMgLeft = InteractUtils.dip2px(mContext, 26);
    int windowWidth = InteractUtils.getWindow((Activity) mContext);
    num = (windowWidth - txtMgLeft) / txtSize;
    return num;
  }

  @Override
  protected void setItemData(View view, final MLMessageData data,
      final int position) {
    ViewUtils.inject(this, view);
    if (data == null) {
      return;
    }
    final MLMessageInfo mInfo = data.info;
    if (mInfo == null) {
      return;
    }
    if (mInfo.interactionComment.isEmpty() && data.praiseInfo.isEmpty()) {
      line.setVisibility(View.GONE);
    } else {
      line.setVisibility(View.VISIBLE);
    }
    if (!data.praiseInfo.isEmpty()) {
      rlParse.setVisibility(View.VISIBLE);
      tvMoreParse.setMovementMethod(CustomLinkMovementMethod.getInstance());
      tvMoreParse.setText(addClickablePart(data.praiseInfo), TextView.BufferType.SPANNABLE);
      tvMoreParse.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          Message m = new Message();
          m.arg1 = mPosition;
          m.what = 8;
          m.obj = data;
          _handler.sendMessage(m);
        }
      });
    } else {
      rlParse.setVisibility(View.GONE);
    }
    mp = new MediaPlayer();
    if (BCStringUtil.isEmpty(data.info.voice)) {
      _yuyinneirong.setVisibility(View.GONE);
      yuyinshijian.setVisibility(View.GONE);
    } else {
      if (data.info.voice.equals("(null)") || data.info.voice.equals("<null>")) {
        _yuyinneirong.setVisibility(View.GONE);
        yuyinshijian.setVisibility(View.GONE);
      } else {
        _yuyinneirong.setVisibility(View.VISIBLE);
        yuyinshijian.setVisibility(View.VISIBLE);
      }
    }

    yuyinshijian.setText(data.info.length + "″");
    // 头像
    String iconUrl = "";
    if (MLToolUtil.isNull(data.user.depotName)) {
      // 商家
      iconUrl = APIConstants.API_IMAGE + "?id=" + data.user.logo;
      _nameTv.setText(data.user.companyName);
      llShang.setVisibility(View.VISIBLE);
      if (!BCStringUtil.isEmpty(data.gradeNum)) {
        if (Integer.parseInt(data.gradeNum) == 1) {
          ivShangNum.setImageResource(R.mipmap.msg_members_1);
        } else if (Integer.parseInt(data.gradeNum) == 2) {
          ivShangNum.setImageResource(R.mipmap.msg_members_2);
        } else if (Integer.parseInt(data.gradeNum) == 3) {
          ivShangNum.setImageResource(R.mipmap.msg_members_3);
        } else if (Integer.parseInt(data.gradeNum) == 4) {
          ivShangNum.setImageResource(R.mipmap.msg_members_4);
        }
      } else {
        ivShangNum.setVisibility(View.GONE);
      }
    } else {
      // 汽修厂
      iconUrl = APIConstants.API_IMAGE + "?id=" + data.user.userPhoto;
      _nameTv.setText(data.user.depotName);
      llShang.setVisibility(View.GONE);
    }

    if (mInfo.content == "") {
      _contentTv.setVisibility(View.GONE);
    } else {
      _contentTv.setVisibility(View.VISIBLE);
    }
    _contentTv.setText(mInfo.content);
    _timeTv.setText(mInfo.createTime);

    _iconIv.setTag(iconUrl);
    if (!BaseApplication.IMAGE_CACHE.get(iconUrl, _iconIv)) {
      _iconIv.setImageResource(R.drawable.default_message_header);
    }
    ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

    if (mInfo.images != null) {
      JSONArray jsonArray;
      try {
        jsonArray = new JSONArray(mInfo.images);
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
        e.printStackTrace();
      }
    }
    if (list.size() == 0) {
      _imageGridView.setVisibility(View.GONE);
    } else {
      _imageGridView.setVisibility(View.VISIBLE);
    }
    ShowAdapter showAdapter = new ShowAdapter(list);
    _imageGridView.setAdapter(showAdapter);

    _iconIv.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        Message m = new Message();
        m.arg1 = mPosition;
        m.what = 3;
        m.obj = data;
        _handler.sendMessage(m);

      }
    });

    _replyIbtn.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Message m = new Message();
        m.arg1 = position;
        m.what = 1;

        _handler.sendMessage(m);
      }
    });

    _yuyinneirong.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Uri u = Uri.parse(APIConstants.API_IMAGE_SHOW + mInfo.voice);
        openFile(u);
      }

    });

    // ==============评论区==========================
    _replyLy.removeAllViews();

    List<MLMessageCommentData> comments = mInfo.interactionComment;
    int size = comments.size();
    for (int i = 0; i < size; i++) {
      MLReplyItemView item = new MLReplyItemView(mContext);
      item.setData(comments.get(i));
      _replyLy.addView(item);
    }

    if (size > 0) {
      _replyLy.setVisibility(View.VISIBLE);

    } else {
      _replyLy.setVisibility(View.GONE);
    }

    if (!BCStringUtil.isEmpty(data.info.praiseNum)) {
      if (data.info.praiseNum.equals("0")) {
        tvDianNumber.setVisibility(View.GONE);
      } else {
        tvDianNumber.setVisibility(View.VISIBLE);
        tvDianNumber.setText(data.info.praiseNum);
      }
    } else {
      tvDianNumber.setVisibility(View.GONE);
    }
    if (!BCStringUtil.isEmpty(data.info.isPraise)) {
      if (data.info.isPraise.equals("1")) {
        ivDianzan.setImageResource(R.mipmap.circle_icon_like);
      } else {
        ivDianzan.setImageResource(R.mipmap.circle_icon_like_default);
      }
    } else {
      ivDianzan.setImageResource(R.mipmap.circle_icon_like_default);
    }
    if (!BCStringUtil.isEmpty(data.info.isCollection)) {
      if (data.info.isCollection.equals("1")) {
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
        if (!BCStringUtil.isEmpty(data.info.praiseNum)) {
          if (BCStringUtil.isEmpty(data.info.isPraise)) {
            data.info.isPraise = "1";
            data.info.praiseNum = "1";
            tvDianNumber.setText("1");
          } else {
            ParseInfoData parseInfoData = new ParseInfoData(user.isDepot ? "0" : "1", user.Id,
                user.name, user.hxUser, user.headPic, user.phone);
            if (data.info.isPraise.equals("1")) {
              data.info.isPraise = "0";
              data.info.praiseNum = (Integer.parseInt(data.info.praiseNum) - 1) + "";
              data.praiseInfo.remove(parseInfoData);
            } else {
              data.info.isPraise = "1";
              data.info.praiseNum = (Integer.parseInt(data.info.praiseNum) + 1) + "";
              data.praiseInfo.add(parseInfoData);
            }
            tvDianNumber.setText(data.info.praiseNum);
          }
        } else {
          data.info.praiseNum = "1";
          data.info.isPraise = "1";
          tvDianNumber.setText("1");
        }
        Message m = new Message();
        m.obj = data;
        m.what = 5;
        _handler.sendMessage(m);
        notifyDataSetChanged();
      }
    });

    if (!BCStringUtil.isEmpty(data.info.video)) {
      if (data.info.video.equals("(null)") || data.info.video.equals("<null>")) {
        ivVideo.setVisibility(View.GONE);
        rlVideo.setVisibility(View.GONE);
      } else {
        ivVideo.setVisibility(View.VISIBLE);
        rlVideo.setVisibility(View.VISIBLE);
        if (!MLStrUtil.isEmpty(data.info.videoPic)) {
          Glide.with(mContext)
              .load(APIConstants.API_IMAGE_SHOW + data.info.videoPic)
              .error(R.mipmap.chat_video_pressed)
              .into(ivVideo);
        }
        final String path = APIConstants.API_IMAGE_SHOW + data.info.video
            .replace("\"", "").replace("[", "").replace("]", "");
        videoPlay.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(mContext, MLInteractVideoAty.class);
            intent.putExtra(MLBaseConstants.TAG_INTENT_DATA, path);
            mContext.startActivity(intent);
          }
        });
      }
    } else {
      videoPlay.setClickable(false);
      ivVideo.setVisibility(View.GONE);
      rlVideo.setVisibility(View.GONE);
    }

    ivShocang.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!BCStringUtil.isEmpty(data.info.isCollection)) {
          if (data.info.isCollection.equals("1")) {
            data.info.isCollection = "0";
          } else {
            data.info.isCollection = "1";
          }
        }
        Message m = new Message();
        m.obj = data;
        m.what = 4;
        _handler.sendMessage(m);
        notifyDataSetChanged();
      }
    });
    ivSiliao.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        MLLogin user = ((BaseApplication) mContext.getApplicationContext()).get_user();
        if (!BCStringUtil.isEmpty(data.hxUser)) {
          if (!data.hxUser.equals(user.hxUser)) {
            HxUser mHxUser = new HxUser();
            mHxUser.emId = data.hxUser;
            if (BCStringUtil.isEmpty(data.user.companyName)) {
              mHxUser.name = data.user.depotName;
            } else {
              mHxUser.name = data.user.companyName;
            }
            mHxUser.userId = data.user.id;
            Intent intent = new Intent(mContext, ChatAty.class);
            intent.putExtra(Contants.EXTRA_USER, mHxUser);
            mContext.startActivity(intent);
          }
        }
      }
    });
    ivJubao.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Message m = new Message();
        m.obj = data;
        m.what = 6;
        _handler.sendMessage(m);
      }
    });
  }

//  private void initPopup(View anchorView, final MLMessageData data) {
//    //自定义布局
//    ViewGroup contentView = (ViewGroup) LayoutInflater.from(mContext).inflate(
//        R.layout.item_popupwindow_hd, null, true);
//    PopupWindow mPop = new PopupWindow(contentView,
//        DensityUtils.dp2px(200f), LayoutParams.WRAP_CONTENT, true);
//    mPop.setAnimationStyle(R.style.popp_anim);
//    mPop.setBackgroundDrawable(new ColorDrawable(0));
//    mPop.setOutsideTouchable(true);
//    int[] location = new int[2];
//    anchorView.getLocationOnScreen(location);
//    final TextView tvShoucang = (TextView) contentView.findViewById(R.id.tv_dianzan);
//    if (!BCStringUtil.isEmpty(data.info.isCollection)) {
//      if (data.info.isCollection.equals("1")) {
//        tvShoucang.setText("取消收藏");
//      } else {
//        tvShoucang.setText("收藏");
//      }
//    }
//    tvShoucang.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        if (!BCStringUtil.isEmpty(data.info.isCollection)) {
//          if (data.info.isCollection.equals("1")) {
//            data.info.isCollection = "0";
//            tvShoucang.setText("收藏");
//          } else {
//            tvShoucang.setText("取消收藏");
//            data.info.isCollection = "1";
//          }
//        }
//        Message m = new Message();
//        m.obj = data;
//        m.what = 4;
//        _handler.sendMessage(m);
//      }
//    });
//    contentView.findViewById(R.id.tv_jubao).setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        Message m = new Message();
//        m.obj = data;
//        m.what = 6;
//        _handler.sendMessage(m);
//      }
//    });
//    contentView.findViewById(R.id.tv_siliao).setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        MLLogin user = ((BaseApplication) mContext.getApplicationContext()).get_user();
//        if (!BCStringUtil.isEmpty(data.hxUser)) {
//          if (!data.hxUser.equals(user.hxUser)) {
//            HxUser mHxUser = new HxUser();
//            mHxUser.emId = data.hxUser;
//            if (BCStringUtil.isEmpty(data.user.companyName)) {
//              mHxUser.name = data.user.depotName;
//            } else {
//              mHxUser.name = data.user.companyName;
//            }
//            mHxUser.userId = data.user.id;
//            Intent intent = new Intent(mContext, ChatAty.class);
//            intent.putExtra(Contants.EXTRA_USER, mHxUser);
//            mContext.startActivity(intent);
//          }
//        }
//      }
//    });
//    mPop.showAtLocation(anchorView, Gravity.NO_GRAVITY,
//        location[0] - mPop.getWidth(), location[1]);
//  }

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
        mHolder.image = (ImageView) convertView
            .findViewById(R.id.tx_interact_imageview);
        convertView.setTag(mHolder);
      } else {
        mHolder = (ViewHolder) convertView.getTag();
      }

      Glide.with(mContext)
          .load(mList.get(position).get("path").toString())
          .error(R.drawable.default_message_header)
          .override(600, 400)
          .centerCrop()
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

      return convertView;
    }
  }

  class ViewHolder {

    ImageView image;

  }

  // 播放录音
  public void openFile(Uri uri) {

    try {
      mp.reset();
      mp.setDataSource(_context, uri);
      mp.prepareAsync();
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