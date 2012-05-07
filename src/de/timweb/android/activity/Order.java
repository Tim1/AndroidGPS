package de.timweb.android.activity;

public class Order {

	private String orderName;
	private String orderStatus;
	
	//icon?
	private String orderDate;
	private String orderDistance;
	private String orderTime;

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderDistance() {
		return orderDistance;
	}

	public void setOrderDistance(String orderDistance) {
		this.orderDistance = orderDistance;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

}
