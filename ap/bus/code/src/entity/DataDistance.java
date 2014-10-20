package app.infobus.entity;

import java.util.List;

public class DataDistance {
	public String[] destination_addresses;
	public String[] origin_addresses;
	public List<Rows> rows;
	public String status;

	public class Rows {
		public List<Elements> elements;
	}

	public class Elements {
		public Distance distance;
		public Duration duration;
		public String status;
	}

	public class Distance {
		public String text;
		public int value;
	}

	public class Duration {
		public String text;
		public int value;
	}
}
