package sjpn4.vn.model.subModel;

import com.google.gson.annotations.SerializedName;

public class VocabularyList {
	@SerializedName("jp")
	public String wordJP;
	
	@SerializedName("hiragana")
	public String wordHiragana;

	@SerializedName("ex")
	public String wordEX;
	
	@SerializedName("en")
	public String wordEN;

	@SerializedName("vn")
	public String wordVN;

	@SerializedName("viet")
	public String wordViet;

	@SerializedName("link")
	public String wordLink;

	@SerializedName("media")
	public String wordMedia;

	@SerializedName("day")
	public String wordDay;
	
	
	public String ID;
	
	public String wordLean;
	public String wordView;

}
