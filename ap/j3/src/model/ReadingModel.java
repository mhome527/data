package sjpn3.vn.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import sjpn3.vn.model.subModel.ReadingList;

public class ReadingModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("title")
	public String title;
	
	@SerializedName("title2")
	public String title2;

	@SerializedName("reading")
	public String reading;
	
	@SerializedName("img")
	public String img;

	@SerializedName("note")
	public String note;
	
	public List<ReadingList> readingList;
	
//	@SerializedName("id")
//	public long id;*/
	

}
