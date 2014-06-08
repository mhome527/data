package sjpn3.vn.model.subModel;

import java.util.List;
import sjpn3.vn.model.ReadingModel;

import com.google.gson.annotations.SerializedName;

public class ReadingDay {
	@SerializedName("reading")
	public List<ReadingModel> day;
}
