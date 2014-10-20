package app.infobus.entity;

import java.util.List;

public class clsTaxiEntity {
	public long price1;
	public long price2;
	public long price_begin;
	public int default_km;
	public List<clsTaxiItem> hcm;
	public List<clsTaxiItem> hanoi;
	
	public static class clsTaxiItem{
		
		public String name;
		public String tel;	
		public String img;
	}
}
