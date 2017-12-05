package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mob.shop.ShopSDK;
import com.mob.shop.datatype.entity.ExpressDescribe;
import com.mob.shop.datatype.entity.ExpressInfo;
import com.mob.shop.gui.R;
import com.mob.shop.gui.SGUIOperationCallback;
import com.mob.shop.gui.pages.TransportMsgPage;
import com.mob.shop.gui.pages.dialog.ProgressDialog;
import com.mob.shop.gui.themes.defaultt.components.TitleView;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;

/**
 * 查看物流信息
 */

public class ShowTransportMessagePageAdapter extends DefaultThemePageAdapter<TransportMsgPage> {
	private TextView transPortName, transPortNum;
	private ListView listView;
	private TitleView titleView;
	private TransportMsgPage page;
	private TextView tvEmptyTip;
	private ExpressAdapter adapter;
	private ProgressDialog pd;

	@Override
	public void onCreate(TransportMsgPage page, Activity activity) {
		super.onCreate(page, activity);
		this.page = page;
		View view = LayoutInflater.from(activity).inflate(R.layout.shopsdk_default_showtransport_msg, null);
		listView = (ListView) view.findViewById(R.id.listView);
		transPortName = (TextView) view.findViewById(R.id.transportName);
		transPortNum = (TextView) view.findViewById(R.id.transportNum);
		titleView = (TitleView) view.findViewById(R.id.titleView);
		tvEmptyTip = (TextView) view.findViewById(R.id.tvEmptyTip);
		activity.setContentView(view);

		titleView.initTitleView(page, "shopsdk_default_check_logistics", null, null, true);
		adapter = new ExpressAdapter();
		listView.setAdapter(adapter);
		listView.setEmptyView(tvEmptyTip);

		getExpress();
	}

	private void getExpress() {
		pd = new ProgressDialog.Builder(page.getContext(), page.getTheme()).show();
		ShopSDK.queryExpress(String.valueOf(page.getTransportId()), new SGUIOperationCallback<ExpressInfo>(page.getCallback()) {
			@Override
			public void onSuccess(ExpressInfo data) {
				super.onSuccess(data);
				if(pd != null && pd.isShowing() ){
					pd.dismiss();
				}
				if (data != null) {
					transPortName.setText(data.getExpressCompanyName());
					transPortNum.setText(String.valueOf(data.getExpressNo()));
					ArrayList<ExpressDescribe> expressDescribeList = data.getExpressDescribe();
					if (expressDescribeList != null && expressDescribeList.size() > 0) {
						adapter.setList(expressDescribeList);
					}
				}
			}

			@Override
			public boolean onResultError(Throwable t) {
				if(pd != null && pd.isShowing() ){
					pd.dismiss();
				}
				page.toastMessage(ResHelper.getStringRes(page.getContext(), "shopsdk_default_express_detail_failed"));
				return super.onResultError(t);
			}
		});

	}

	@Override
	public void onDestroy(TransportMsgPage page, Activity activity) {
		super.onDestroy(page, activity);
		if (pd != null) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
			pd = null;
		}
	}

	class ExpressAdapter extends BaseAdapter {
		ArrayList<ExpressDescribe> list = new ArrayList<ExpressDescribe>();

		public ExpressAdapter() {
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int i) {
			return list.get(i);
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		public void setList(ArrayList<ExpressDescribe> list) {
			this.list = list;
			notifyDataSetChanged();
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder vh = null;
			if (view == null) {
				view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopsdk_default_item_express,
						null);
				vh = new ViewHolder();
				vh.iv = (ImageView) view.findViewById(R.id.iv);
				vh.tvDes = (TextView) view.findViewById(R.id.tvDes);
				vh.tvTime = (TextView) view.findViewById(R.id.tvTime);
				view.setTag(vh);
			} else {
				vh = (ViewHolder) view.getTag();
			}
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) vh.tvDes.getLayoutParams();
			if (i == 0) {
				vh.iv.setImageResource(ResHelper.getBitmapRes(viewGroup.getContext(),
						"shopsdk_default_express_orange"));
				vh.tvDes.setTextColor(0xFFf6573b);
				vh.tvTime.setTextColor(0xFFf6573b);

				lp.topMargin = -ResHelper.dipToPx(viewGroup.getContext(), 2);

			} else {
				lp.topMargin = -ResHelper.dipToPx(viewGroup.getContext(), 3);
				vh.iv.setImageResource(ResHelper.getBitmapRes(viewGroup.getContext(), "shopsdk_default_express_grey"));
				vh.tvDes.setTextColor(0xFF999999);
				vh.tvTime.setTextColor(0xFF999999);
			}

			LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) vh.tvTime.getLayoutParams();
			if (i == list.size() - 1) {
				lp1.bottomMargin = ResHelper.dipToPx(viewGroup.getContext(), 15);
			} else {
				lp1.bottomMargin = ResHelper.dipToPx(viewGroup.getContext(), 30);
			}
			vh.tvDes.setText(list.get(i).getStatus());
			vh.tvTime.setText(list.get(i).getTime());
			return view;
		}

		class ViewHolder {
			ImageView iv;
			TextView tvDes;
			TextView tvTime;

		}
	}
}
