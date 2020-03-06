package com.txsh.model;

import java.util.List;

public class FileUpload {

	public String msg;
	public ImageUploadRes res;
	public Boolean state;
	public String time;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ImageUploadRes getRes() {
		return res;
	}

	public void setRes(ImageUploadRes res) {
		this.res = res;
	}

	public Boolean getState() {
		return state;
	}

	public void setState(Boolean state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public class ImageUploadRes {
		String code;
		List<ImageUploadResData> data;
		String state;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public List<ImageUploadResData> getData() {
			return data;
		}

		public void setData(List<ImageUploadResData> data) {
			this.data = data;
		}

	}

	public class ImageUploadResData {
		String msg;
		String name;
		String path;
		String state;

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
	}
}
