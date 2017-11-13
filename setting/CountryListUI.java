package com.lingtuan.firefly.setting;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lingtuan.firefly.R;
import com.lingtuan.firefly.base.BaseActivity;
import com.lingtuan.firefly.util.Utils;

public class CountryListUI extends BaseActivity implements OnItemClickListener{

	private ListView listview;
	private CountryAdapter adapter;
	
	private String selectCountryName;
	@Override
	protected void setContentView() {
		setContentView(R.layout.country_layout);
	}

	@Override
	protected void findViewById() {
		listview = (ListView) findViewById(R.id.listview);

	}

	@Override
	protected void setListener() {
		listview.setOnItemClickListener(this);
	}

	@Override
	protected void initData() {
		setTitle(getResources().getString(R.string.select_country_title));
		adapter = new CountryAdapter(this, getResources().getStringArray(R.array.country_list));
	    listview.setAdapter(adapter);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

		selectCountryName = adapter.getItem(position);
		
		if(adapter.getItem(position).equals("中国")){
			Intent intentArea = new Intent(new Intent(this, CityListUI.class));
			intentArea.putExtra("country", adapter.getItem(position-1));
			startActivityForResult(intentArea,100);
			Utils.openNewActivityAnim(this, false);
		}else if(adapter.getItem(position).equals("中国台湾")){
			Intent intentArea = new Intent(new Intent(this, AreaListUI.class));
			intentArea.putExtra("city", adapter.getItem(position-1));
			startActivityForResult(intentArea,100);
			Utils.openNewActivityAnim(this, false);
		}else if(adapter.getItem(position).equals("中国香港")){
			Intent intentArea = new Intent(new Intent(this, AreaListUI.class));
			intentArea.putExtra("city", adapter.getItem(position-1));
			startActivityForResult(intentArea,100);
			Utils.openNewActivityAnim(this, false);
		}else{
			saveAdress(adapter.getItem(position),null,null);
		}
		
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode ==100 && resultCode ==RESULT_OK){
			String area = data.getStringExtra("area");
			String city = data.getStringExtra("city");
			saveAdress(selectCountryName,city,area);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	private void saveAdress(String country,String city,String area){
		StringBuilder address = new StringBuilder();
	    address.append(country);
	    if(!TextUtils.isEmpty(city)){
	    	 address.append(" ");
	    	 address.append(city);
	    }
	    if(!TextUtils.isEmpty(area)){
   		  address.append(" ");
	      address.append(area);
		}
		Intent i = new Intent();
		i.putExtra("address", address.toString());
		setResult(RESULT_OK, i);
		finish();
	}
	
}