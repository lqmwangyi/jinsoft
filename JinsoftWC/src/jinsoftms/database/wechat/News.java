package jinsoftms.database.wechat;

public class News {
	/**
		Title	否	图文消息标题
		Description	否	图文消息描述
		PicUrl	否	图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
		Url	否	点击图文消息跳转链接
	 */
	private String Title;
	private String Description;
	private String PicUrl;
	private String Url;
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	
}
