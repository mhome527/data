package app.infobus.entity;

public class clsPathBus
//implements Parcelable
{
	String num;
	String[] pathStart;
	String[] pathBack;
	String info;
	String namePath;
	
    // example constructor that takes a Parcel and gives you an object populated with it's values
//    private clsPathBus(Parcel in) {
//        mData = in.readInt();
//        num = in.readtring(); 
//        num = in.readString();
//        num = in.readString();
//        num = in.readString();
//        num = in.readString();
//    }

	public clsPathBus(){
		
	}
	public clsPathBus(String num, String[] pathStart, String[] pathBack){
		this.num = num;
		this.pathStart = pathStart;
		this.pathBack = pathBack;
	}
	
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num  = num ;
	}
	
	public String getNamePath() {
		return namePath;
	}

	public void setNamePath(String namePath) {
		this.namePath = namePath;
	}

	public String[] getPathStart() {
		return pathStart;
	}

	public void setPathStart(String[] pathStart) {
		this.pathStart = pathStart;
	}

	public String[] getPathBack() {
		return pathBack;
	}

	public void setPathBack(String[] pathBack) {
		this.pathBack = pathBack;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

//	@Override
//	public int describeContents() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel arg0, int arg1) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	 // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
//    public static final Parcelable.Creator<clsPathBus> CREATOR = new Parcelable.Creator<clsPathBus>() {
//        public clsPathBus createFromParcel(Parcel in) {
//            return new clsPathBus(in);
//        }
//
//        public clsPathBus[] newArray(int size) {
//            return new clsPathBus[size];
//        }
//    };


}
