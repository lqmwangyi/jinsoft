package jinsoftms.database.wechat;

public class PageInfo {
	private int pageSize;
	private int pageNumber;
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public PageInfo(){
		
	}

	public PageInfo(int pageSize,int pageNumber) {
		this.pageSize=pageSize;
		this.pageNumber = pageSize;
	}
	
}
