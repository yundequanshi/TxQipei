package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MLMyReplyData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4910101702857064213L;

	@Expose
	public String id;
	
	@Expose
	public String content;
	
	@Expose
	public MLMessageBusinessData company;
	
	@Expose
	public	MLHomeDepotData depot; 
	
	@Expose
	public String mimageId;
	
	@Expose
	public String mtime;
	
	@Expose
	public List<MLMessageCommentData> interactionComment ;
}
