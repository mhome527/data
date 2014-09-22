package app.infobus.entity;

import java.util.List;

public class clsTaxiEntity {
	public List<clsTaxiItem> hcm;
	public List<clsTaxiItem> hanoi;
	
	public static class clsTaxiItem{
		
		public String name;
		public String tel;	
		public String img;
	}
}
