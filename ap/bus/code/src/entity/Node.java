package app.infobus.entity;

public class Node {
	private String num;
	private boolean start;
	private String before;
	private String after;
	private int count;
	private String street; //duong cat nhau
	private boolean startAfter;

	public Node(String num, boolean start, String before) {
		this.num = num;
		this.start = start;
		this.before = before;
		this.after = "";
		count = 0;
	}

	public void setBefore(String before) {
		this.before = before;
	}

	public String getBefore() {
		return before;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getNum() {
		return this.num;
	}

	public void setStart(boolean start) {
		this.start = start;
	}

	public boolean getStart() {
		return this.start;
	}

	public void setStartAfter(boolean startAfter) {
		this.startAfter = startAfter;
	}

	public boolean getStartAfter() {
		return this.startAfter;
	}

	public String getAfter() {
		return this.after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	public int getCount() {
		return this.count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getStreet() {
		return this.street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
}
