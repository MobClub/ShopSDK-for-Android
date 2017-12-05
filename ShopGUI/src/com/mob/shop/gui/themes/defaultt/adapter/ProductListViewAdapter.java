package com.mob.shop.gui.themes.defaultt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mob.shop.datatype.entity.Commodity;
import com.mob.shop.datatype.entity.Product;
import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.CommodityDetailShowPage;
import com.mob.shop.gui.themes.defaultt.MainPageAdapter;
import com.mob.shop.gui.utils.MoneyConverter;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.ResHelper;

import java.util.List;

/**
 * Created by weishj on 2017/10/21.
 */

public class ProductListViewAdapter extends ProductListBaseAdapter {
	private static final String TAG = ProductListViewAdapter.class.getSimpleName();
	private int ivWidth;

	public ProductListViewAdapter(PullToRequestView view, MainPageAdapter mainPageAdapter) {
		super(view, mainPageAdapter);
	}

	@Override
	public View getView(final int i, View convertView, ViewGroup viewGroup) {
		final ViewHolder vh;
		if (convertView == null) {
			convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopsdk_default_page_all_list_item,
					null);
			vh = new ViewHolder();
			vh.imageView = (AsyncImageView) convertView.findViewById(R.id.goodImg);
			vh.goodName = (TextView) convertView.findViewById(R.id.goodName);
			vh.goodPrices = (TextView) convertView.findViewById(R.id.goodPrices);
			vh.buyers = (TextView) convertView.findViewById(R.id.buyer);

			if (ivWidth == 0) {
				ViewGroup.LayoutParams lp = vh.imageView.getLayoutParams();
				ivWidth = lp.width;
			}

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		if (list != null && !list.isEmpty()) {
			Product product = list.get(i);
			vh.goodName.setText(product.getProductName());
			List<Commodity> commoditys = product.getCommodityList();
			vh.goodPrices.setText(MoneyConverter.conversionMoneyStr(commoditys.get(0).getCurrentCost()));
			String buyerStr = String.format(this.salesFormatter, commoditys.get(0).getCommoditySales());
			vh.buyers.setText(buyerStr);
			vh.imageView.setCompressOptions(ivWidth, ivWidth, 80, 10240L);
			vh.imageView.execute(commoditys.get(0).getImgUrl().getSrc(), ResHelper.getColorRes(getParent().getContext(),
					"order_bg"));
		}

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onItemClick(i);
				}
				CommodityDetailShowPage detailShowPage = new CommodityDetailShowPage(mainPageAdapter.getPage().getTheme(), list.get(i));
				detailShowPage.show(getContext(), null);
			}
		});

		return convertView;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public Object getItem(int i) {
		return list.get(i);
	}

	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	static class ViewHolder {
		AsyncImageView imageView;
		TextView goodName;
		TextView goodPrices;
		TextView buyers;
	}
}
