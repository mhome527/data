package app.infobus;

import java.util.ArrayList;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import app.infobus.adapter.NumAdapter;
import app.infobus.entity.Node;
import app.infobus.entity.clsListName;
import app.infobus.entity.clsNameStreet;
import app.infobus.entity.clsPathBus;
import app.infobus.entity.searchEntity;
import app.infobus.utils.Common;
import app.infobus.utils.Constant;
import app.infobus.utils.ULog;

public class SearchActivity extends AbstractActivity {

	public String tag = SearchActivity.class.getSimpleName();
	private ImageView imgChange;
	private Button btnSearch;
	private ListView listSearch;
	private String beginPath = "Bùi Công Trừng", endPath = "Tân Hòa Đông";
	private ArrayList<Node> arrBegin, arrEnd;
	private ArrayList<ArrayList<Node>> listBusPath = new ArrayList<ArrayList<Node>>();
	private ArrayList<String> listAdd = new ArrayList<String>();
	private ArrayList<searchEntity> arrStreet;

	private AutoCompleteTextView edtAuto1, edtAuto2;
	private AutoCompleteAdapter adapter;

	private ArrayList<String> listSearched;
	ArrayList<Node> listFound = new ArrayList<Node>();

	// public SearchActivity(){
	//
	// }
	@Override
	protected int getViewLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.search_bus;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		boolean isCheck = false;
		try {
			imgChange = (ImageView) findViewById(R.id.imgChange);
			edtAuto1 = (AutoCompleteTextView) findViewById(R.id.edtAuto1);
			edtAuto2 = (AutoCompleteTextView) findViewById(R.id.edtAuto2);
			btnSearch = (Button) findViewById(R.id.btnSearch);
			listSearch = (ListView) findViewById(R.id.listSearch);

			// cty = getIntent().getStringExtra(Constant.KEY_CITY);
			isCheck = getIntent().getBooleanExtra(Constant.KEY_CITY, true);
			if(isCheck)
				rbtnHcm.setChecked(true);
			else
				rbtnHN.setChecked(true);
			// ///////ad
			AdView adView = (AdView) this.findViewById(R.id.adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);

			imgChange.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String tmp;
					tmp = edtAuto1.getText().toString();
					edtAuto1.setText(edtAuto2.getText().toString());
					edtAuto2.setText(tmp);
				}
			});

			// listSearched = new ArrayList<String>();
			btnSearch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					beginPath = edtAuto1.getText().toString().trim();
					endPath = edtAuto2.getText().toString().trim();
					if (beginPath.equals("") && endPath.equals("")) {
						showMsg("Vui lòng nhập tên đường");
					} else if (!beginPath.equals("") && !endPath.equals("")) {
						getBusPathInit();
					} else {
						searchBus();
					}

				}
			});

			rbtnHcm.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					ULog.i(tag, "initView ...HCM");
					new LoadData(SearchActivity.this).execute();
				}
			});

			// rbtnHN.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//
			// @Override
			// public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// LogUtil.i(tag, "initView ...HN");
			// new LoadData(SearchActivity.this).execute();
			// }
			// });
			// //test
			new LoadData(this).execute();

			// //////////////////
			// adapter = new AutoCompleteAdapter(this);
			// edtAuto1.setAdapter(adapter);
			// edtAuto2.setAdapter(adapter);
		} catch (Exception e) {
			ULog.e(tag, "initView ...Error:" + e.getMessage());
			e.printStackTrace();
		}
	}

	private void searchBus() {
		listSearched = new ArrayList<String>();
		ArrayList<Node> arrNode = null;
		try {
			if (!beginPath.equals("") && endPath.equals("")) {
				arrBegin = searchListBus(beginPath);
			}
			if (beginPath.equals("") && !endPath.equals("")) {
				arrEnd = searchListBus(endPath);
			}

			if (arrBegin != null && arrBegin.size() > 0)
				arrNode = arrBegin;
			else if (arrEnd != null && arrEnd.size() > 0)
				arrNode = arrEnd;

			if (arrNode != null && arrNode.size() > 0) {
				for (Node node : arrNode)
					listSearched.add(node.getNum());

				if (listSearched != null && listSearched.size() > 0)
					listSearch.setAdapter(new NumAdapter(this, listSearched));
				else
					showMsg("Không tìm thấy tên đường nào");
			} else
				showMsg("Không tìm thấy tên đường nào");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getBusPathInit() {

		listBusPath = new ArrayList<ArrayList<Node>>();
		listSearched = new ArrayList<String>();

		// luu nhung xe bus di ngang qua duong dau tien
		arrBegin = searchListBus(beginPath);

		// luu nhung xe bus di ngang qua duong cuoi cung
		arrEnd = searchListBus(endPath);
		if (checkBus1()) {
			// have bus

		} else if (checkPathBegin()) {
			// have bus
		} else {
			for (int i = 0; i < InfoBusActivity.arrPathBus.size() / 2; i++) {
				if (getListPath())
					break;
			}
		}
		getPath();

	}

	private void getPath() {
		String path = "";
		// Node node;
		// while (isCheck) {
		String arrTmp[];
		String str = "";
		int count = 0;
		ArrayList<Node> listNode = listBusPath.get(listBusPath.size() - 1);
		for (Node node : listNode) {
			if (node.getAfter() != null && !node.getAfter().equals("")) {
				if (node.getAfter().equals(node.getNum()))
					path = node.getNum();
				else
					path = node.getAfter() + " - " + node.getNum();
				path += getPath(node.getBefore());
				arrTmp = path.split("-");
				for (int i = arrTmp.length - 1; i >= 0; i--) {
					if (!arrTmp[i].trim().equals("")) {
						if (count == 0)
							str = arrTmp[i];
						else
							str += " --> " + arrTmp[i];
						count++;
					}
				}

				listSearched.add(str);
				// listPath.add(path);

				ULog.i(tag, "getPath path:" + str);
				// tvBus.setText(tvBus.getText() + " || " + str);
				path = "";
				count = 0;
				node.setAfter("");
			}
		}

		if (listSearched != null && listSearched.size() > 0) {
			listSearch.setAdapter(new NumAdapter(this, listSearched));
		}

		// if (listFound != null && listFound.size() > 0) {
		// listSearch.setAdapter(new NumAdapter2(this, listFound));
		// }
	}

	private String getPath(String num) {
		String tmp;
		for (ArrayList<Node> arrNode : listBusPath) {
			for (Node nodetmp : arrNode) {
				if (nodetmp.getNum().equals(num) && !nodetmp.getBefore().equals("")) {
					// listPath.add(nodetmp.getBefore());
					tmp = nodetmp.getBefore();
					if (nodetmp.getCount() < 2) {
						nodetmp.setBefore("");
						nodetmp.setNum("");
					} else
						nodetmp.setCount(nodetmp.getCount() - 1);
					return " - " + num + " - " + getPath(tmp);
				}
			}
		}
		return "";
	}

	private boolean checkBus1() {
		String[] path1;
		ArrayList<Node> arrPath = new ArrayList<Node>();
		clsPathBus bus1;
		ArrayList<String> arrAdded = new ArrayList<String>();
		boolean isCheckStreet = false, isStart = false;
		for (Node nodeT : arrBegin) {
			if (!isCheckAdd(nodeT.getNum(), arrAdded)) {// kiem tra da chon bus hay chua
				bus1 = getBus(nodeT.getNum());
				// if (nodeT.getStart())
				path1 = bus1.getPathStart();
				// else
				// path1 = bus1.getPathBack();

				for (String str : path1) {
					if (str.equals(beginPath))
						isCheckStreet = true;

					if (isCheckStreet && str.equals(endPath)) {
						nodeT.setAfter(nodeT.getNum());
						arrPath.add(nodeT);
						arrAdded.add(nodeT.getNum());
						isStart = true;
						break;
					}
				}

				isCheckStreet = false;
				if (!isStart) {
					path1 = bus1.getPathBack();
					for (String str : path1) {
						if (str.equals(beginPath))
							isCheckStreet = true;

						if (isCheckStreet && str.equals(endPath)) {
							nodeT.setAfter(nodeT.getNum());
							arrPath.add(nodeT);
							arrAdded.add(nodeT.getNum());

							break;
						}
					}
				}
				isCheckStreet = false;
			}
		}

		if (arrPath.size() > 0) {
			listBusPath.add(arrPath);
			return true;
		}
		return false;
	}

	private boolean isCheckAdd(String num) {
		for (String str : listAdd) {
			if (str.equals(num)) {
				return true;
			}
		}
		return false;
	}

	private boolean isCheckAdd(String num, ArrayList<String> arrStr) {
		for (String str : arrStr) {
			if (str.equals(num)) {
				return true;
			}
		}
		return false;
	}

	private boolean getListPath() {
		ArrayList<Node> arrBusTmp = new ArrayList<Node>();
		clsPathBus bus1;
		ArrayList<Node> arrBus = listBusPath.get(listBusPath.size() - 1);
		String path1[], path2[];
		Node nodeTemp, nodeCheck;
		boolean start = false;
		boolean isCheckEnd = false;
		String strTmp;
		int count = 0;
		for (clsPathBus pathBus : InfoBusActivity.arrPathBus) {
			if (!isCheckAdd(pathBus.getNum())) {// kiem tra da chon bus hay chua
				for (Node node : arrBus) {
					bus1 = getBus(node.getNum());

					if (count >= 307)
						strTmp = "";

					// if (listBusPath.size() == 1) {
					path1 = bus1.getPathStart();
					path2 = pathBus.getPathStart();
					// startB = true;
					start = true;
					strTmp = comparePathS(node.getStreet(), path1, path2);
					if (strTmp.equals("")) {
						// startB = false;
						start = true;
						path1 = bus1.getPathBack();
						strTmp = comparePathS(node.getStreet(), path1, path2);
					}

					if (strTmp.equals("")) {
						// startB = true;
						start = false;
						path1 = bus1.getPathStart();
						path2 = pathBus.getPathBack();
						strTmp = comparePathS(node.getStreet(), path1, path2);
					}

					if (strTmp.equals("")) {
						// startB = false;
						start = false;
						path1 = bus1.getPathBack();
						path2 = pathBus.getPathBack();
						strTmp = comparePathS(node.getStreet(), path1, path2);
					}

					if (!strTmp.equals("")) {
						count++;
						nodeTemp = new Node(pathBus.getNum(), start, node.getNum());
						nodeTemp.setStreet(strTmp);
						// kiem tra bus moi co cat duong bus cuoi cung hay khong
						nodeCheck = checkPathEnd(strTmp, path2);
						if (nodeCheck != null) {
							strTmp = nodeCheck.getStreet();
							isCheckEnd = true;
							nodeTemp.setAfter(nodeCheck.getNum());
							nodeTemp.setStreet(strTmp);

						}
						nodeTemp.setStart(true);
						arrBusTmp.add(nodeTemp);
						node.setCount(node.getCount() + 1);
						listAdd.add(pathBus.getNum());
					}
				} // for
			} // if
		}// for

		// check path end

		if (arrBusTmp.size() > 0)
			listBusPath.add(arrBusTmp);

		return isCheckEnd;
	}

	// tim diem dau va cuoi ma cung co tren 1 tuyen bus
	public boolean checkPathBegin() {
		clsPathBus clsBus, clsBus2;
		String arrPath1[], arrPath2[];
		String strTmp;
		boolean isBus = false, start = false, after = false;

		Node node;
		ArrayList<Node> arrBus = new ArrayList<Node>();
		for (Node nodeB : arrBegin) {
			clsBus = getBus(nodeB.getNum());
			node = new Node(nodeB.getNum(), true, "0");
			node.setStreet(beginPath);
			// search start
			// arrPath = clsBus.getPathStart();
			// for (int i = 0; i < arrPath.length; i++) {
			for (Node nodeE : arrEnd) {
				clsBus2 = getBus(nodeE.getNum());

				start = true;
				after = true;
				arrPath1 = clsBus.getPathStart();
				arrPath2 = clsBus2.getPathStart();
				strTmp = comparePathS(nodeB.getStreet(), arrPath1, arrPath2);

				if (strTmp.equals("")) {
					start = false;
					after = true;
					arrPath1 = clsBus.getPathBack();
					strTmp = comparePathS(nodeB.getStreet(), arrPath1, arrPath2);
				}

				if (strTmp.equals("")) {
					start = true;
					after = false;
					arrPath1 = clsBus.getPathStart();
					arrPath2 = clsBus2.getPathBack();
					strTmp = comparePathS(nodeB.getStreet(), arrPath1, arrPath2);
				}

				if (strTmp.equals("")) {
					start = false;
					after = false;
					arrPath1 = clsBus.getPathBack();
					arrPath2 = clsBus2.getPathBack();
					strTmp = comparePathS(nodeB.getStreet(), arrPath1, arrPath2);
				}

				if (!strTmp.equals("")) {
					isBus = true;
					node.setStart(start);
					node.setStartAfter(after);
					node.setAfter(nodeE.getNum());
					node.setStreet(strTmp);
					arrBus.add(node);
					// break;
				}
			}

			if (!isBus) {
				arrBus.add(node);
				listAdd.add(nodeB.getNum());
			}
		}

		// if (arrBus.size() > 0) {
		listBusPath.add(arrBus);
		// }
		return isBus;
	}

	// kiem tra tuyen duong cua xe khac co cat voi tuyen duong xe di ngang wa diem cuoi cung
	private Node checkPathEnd(String street, String[] path) {
		clsPathBus clsBus;
		Node node = null;
		String strTmp;
		boolean start = true;
		for (Node nodeE : arrEnd) {
			clsBus = getBus(nodeE.getNum());
			strTmp = comparePathS(street, path, clsBus.getPathStart());
			if (strTmp.equals("")) {
				start = false;
				strTmp = comparePathS(street, path, clsBus.getPathBack());
			}

			if (!strTmp.equals("")) {
				node = new Node(nodeE.getNum(), start, "");
				node.setStreet(strTmp);
				return node;
			}
		}

		return node;
	}

	private ArrayList<Node> searchListBus(String street) {
		Node node;
		String arrPath[];
		boolean isCheck = false;
		ArrayList<Node> arrListBus = new ArrayList<Node>();
		for (clsPathBus pathBus : InfoBusActivity.arrPathBus) {
			arrPath = pathBus.getPathStart();
			for (int i = 0; i < arrPath.length; i++) {
				if (arrPath[i].equals(street)) {
					node = new Node(pathBus.getNum(), true, "");
					node.setStart(false);
					node.setStreet(street);
					arrListBus.add(node);
					listAdd.add(pathBus.getNum());
					isCheck = true;
					break;
				}
			}
			if (!isCheck) {
				arrPath = pathBus.getPathBack();
				for (int i = 0; i < arrPath.length; i++) {
					if (arrPath[i].equals(street)) {
						node = new Node(pathBus.getNum(), false, "");
						node.setStart(false);
						node.setStreet(street);
						arrListBus.add(node);
						listAdd.add(pathBus.getNum());
						break;
					}
				}
			}
		}
		return arrListBus;
	}

	// kiem tra 2 duong cat nhau va path1 phai cat street truoc khi cat path2
	private String comparePathS(String street, String[] path1, String[] path2) {
		boolean isCheck = false;
		for (String pathA : path1) {
			if (pathA.equals(street))
				isCheck = true;
			if (isCheck)
				for (String pathB : path2) {
					if (pathA.equals(pathB))
						return pathA;
				}
		}

		return "";
	}

	/*
	 * private boolean comparePath(String[] path1, String[] path2) { for (String pathA : path1) { for (String pathB : path2) { if (pathA.equals(pathB)) return true; } }
	 * 
	 * return false; }
	 */

	private clsPathBus getBus(String num) {

		for (clsPathBus pathBus : InfoBusActivity.arrPathBus) {
			if (pathBus.getNum().equals(num)) {
				return pathBus;
			}
		}
		return null;
	}

	private class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {

		CusFilter myFilter;

		public AutoCompleteAdapter(Context context) {
			super(context, android.R.layout.simple_dropdown_item_1line);
			ULog.i("SearchActivity", "adapter ");
			myFilter = new CusFilter(this);

		}

		@Override
		public Filter getFilter() {
			return myFilter;
		}
	}

	private boolean checkExist(String path, ArrayList<String> arrFilter) {
		for (String str : arrFilter) {
			if (str.equals(path))
				return true;
		}
		return false;
	}

	public class CusFilter extends Filter {
		private AutoCompleteAdapter adapterAuto;
		private ArrayList<String> arrFilter;
		public FilterResults filterResults = null;

		public CusFilter(AutoCompleteAdapter adapterAuto) {
			this.adapterAuto = adapterAuto;

		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			ULog.i("SearchActivity-HuynhTD", "performFiltering...");
			new FilterStreet(this, constraint).execute();
			return null;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			try {
				if (results == null || arrFilter == null || arrFilter.size() == 0)
					return;
				adapterAuto.clear();
				ULog.i(tag, "publishResults add data size: " + arrFilter.size());
				// Add data to AutoCompleteTextView (have to add)
				for (int i = 0; i < arrFilter.size(); i++) {
					adapterAuto.add(arrFilter.get(i));
				}
				// adapterAuto.notifyAll();
				arrFilter = null;

			} catch (Exception e) {
				ULog.e(tag, "publishResults error:" + e.getMessage());
			}
		}

		public void createFilterStreet(CharSequence constraint) {
			String key;
			try {
				arrFilter = new ArrayList<String>();
				for (searchEntity entity : arrStreet) {
					key = entity.getKey();
					if (constraint != null && key != null && (key.contains(constraint.toString()))) {
						// || constraint.toString().contains(str)) {
						if (!checkExist(entity.getName(), arrFilter))
							arrFilter.add(entity.getName());
					}
				}

				filterResults = new FilterResults();
				filterResults.values = arrFilter;
				filterResults.count = arrFilter.size();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////////

	public class FilterStreet extends AsyncTask<Object, Object, Object> {
		private CusFilter cusFilter;
		private CharSequence constraint;

		public FilterStreet(CusFilter cusFilter, CharSequence constraint) {
			this.constraint = constraint;
			this.cusFilter = cusFilter;
		}

		@Override
		protected Object doInBackground(Object... params) {
			cusFilter.createFilterStreet(constraint);
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			cusFilter.publishResults(constraint, cusFilter.filterResults);
		}

	}

	// ////////////////////////////
	// /////////////////////////////////
	// //////////////////////////////////////

	private void showMsg(String str) {
		try {

			AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
			alt_bld.setMessage(str).setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int id) {
					// Action for 'Yes' Button

				}
			});
			AlertDialog alert = alt_bld.create();
			// Title for AlertDialog
			alert.setTitle("Search");
			alert.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {

					ULog.i(tag, "Dialog cancel");
					dialog.cancel();
				}
			});
			alert.setCanceledOnTouchOutside(false);
			alert.show();
		} catch (Exception e) {
			ULog.e(tag, "confirmReceive error:" + e);
		}
	}

	private class LoadData extends AsyncTask<Object, Object, Boolean> {
		clsListName clsList;
		private Activity activity;

		public LoadData(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(Object... params) {

			try {
				ULog.i(tag, "dataloading.....hcm:" + rbtnHcm.isChecked());

				// arrPathBus = ReadData.getPathData(activity);
				// clsList = Common.getNameHCM(activity);
				if (rbtnHcm.isChecked())
					clsList = Common.getListName(activity, Constant.LIST_STREET_HCM);
				else
					clsList = Common.getListName(activity, Constant.LIST_STREET_HN);

				if (clsList == null) {
					ULog.e(tag, "dataloading Load Error");
					return false;
				} else
					return true;
				// return getPathData(activity);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			searchEntity entity;
			super.onPostExecute(result);
			if (result) {
				ULog.i(tag, "postExe.....");
				arrStreet = new ArrayList<searchEntity>();
				for (clsNameStreet item : clsList.list) {
					entity = new searchEntity(item.getName(), item.getKey());
					arrStreet.add(entity);
				}

				adapter = new AutoCompleteAdapter(SearchActivity.this);
				edtAuto1.setAdapter(adapter);
				edtAuto2.setAdapter(adapter);

			} else
				ULog.i(tag, "Loading-PostExecute Load fail");
			ULog.i(tag, "Loading-PostExecute done");
		}
	}

}
