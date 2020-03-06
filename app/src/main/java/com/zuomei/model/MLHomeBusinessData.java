package com.zuomei.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class MLHomeBusinessData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3575463679999420536L;
	
	//===20个商家个别字段====
	@Expose
	public String id;
	
	@Expose
	public String companyName;
	
	@Expose
	public String compayName;
	//=======
	
	//=======
	
	public boolean isCollectParam=false;
	//=======
	
	
	@Expose
	public String logo;
	@Expose
	public String phone;
	@Expose
	public double lon;
	@Expose
	public String barGainNum;
	@Expose
	public boolean isCollect;
	@Expose
	public String phone2;
	@Expose
	public String userID;
	@Expose
	public List<String> majorOperate;
	@Expose
	public String  concurrenOperate;
	@Expose
	public String address;
	@Expose
	public String satisfaction;
	@Expose
	public double lan;
	@Expose
	public String userName;
	@Expose
	public String phone1;
	@Expose
	public String declaration;
	@Expose
	public  List<MLHomeProductData>products;
	@Expose
	public  List<MLHomeProductData> imageIds;
	
	
	
	
	//=====================
	@Expose
	public String commentCount;
	@Expose
	public String allCount;
	
	@Expose
	public String compayId;

	
	//===二期==================
	@Expose
	public int redNum;
	@Expose
	public String redMoney;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompayName() {
		return compayName;
	}

	public void setCompayName(String compayName) {
		this.compayName = compayName;
	}

	public boolean isCollectParam() {
		return isCollectParam;
	}

	public void setCollectParam(boolean isCollectParam) {
		this.isCollectParam = isCollectParam;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getBarGainNum() {
		return barGainNum;
	}

	public void setBarGainNum(String barGainNum) {
		this.barGainNum = barGainNum;
	}

	public boolean isCollect() {
		return isCollect;
	}

	public void setCollect(boolean isCollect) {
		this.isCollect = isCollect;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public List<String> getMajorOperate() {
		return majorOperate;
	}

	public void setMajorOperate(List<String> majorOperate) {
		this.majorOperate = majorOperate;
	}

	public String getConcurrenOperate() {
		return concurrenOperate;
	}

	public void setConcurrenOperate(String concurrenOperate) {
		this.concurrenOperate = concurrenOperate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}

	public double getLan() {
		return lan;
	}

	public void setLan(double lan) {
		this.lan = lan;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public List<MLHomeProductData> getProducts() {
		return products;
	}

	public void setProducts(List<MLHomeProductData> products) {
		this.products = products;
	}

	public List<MLHomeProductData> getImageIds() {
		return imageIds;
	}

	public void setImageIds(List<MLHomeProductData> imageIds) {
		this.imageIds = imageIds;
	}

	public String getCompayId() {
		return compayId;
	}

	public void setCompayId(String compayId) {
		this.compayId = compayId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
