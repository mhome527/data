package puzzle.slider.vn.Entity;

/**
 * 
 * @sine Mar 25, 2013
 */
public class Size {
	private int height;
	private int width;
	
	/**
	 * 
	 * @since Mar 25, 2013
	 */
	public Size() {
		this.height = 0;
		this.width = 0;
	}
	/**
	 * 
	 * @since Mar 25, 2013 - 11:46:25 AM
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * 
	 * @since Mar 25, 2013 - 11:46:28 AM
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	/**
	 * 
	 * @since Mar 25, 2013 - 11:46:29 AM
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
	
}
