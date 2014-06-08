package sjpn4.vn.model.subModel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ReadingList {
	@SerializedName("question")
	public String question;
	
	@SerializedName("ans")
	public String ans;
	
	@SerializedName("listQuestion")
	public List<SubReadingList> listQuestion;
	// @SerializedName("qa")
	// public String qa;
	//
	// @SerializedName("ans")
	// public String ans;

}
