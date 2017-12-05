package com.mob.shop.gui.themes.defaultt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class ProductGridViewAdapter extends ProductListBaseAdapter {
	private static final String TAG = ProductGridViewAdapter.class.getSimpleName();
	private int ivWidth;

	public ProductGridViewAdapter(PullToRequestView view, MainPageAdapter mainPageAdapter) {
		super(view, mainPageAdapter);
		// Disable divider of ListView
		getListView().setDivider(null);
	}

	@Override
	public View getView(final int i, View convertView, ViewGroup viewGroup) {
		final ViewHolder vh;
		if (convertView == null) {
			convertView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shopsdk_default_page_all_grid_item_wrapper,
					null);
			vh = new ViewHolder();
			vh.left = (LinearLayout)convertView.findViewById(R.id.shopsdk_all_product_left);
			vh.imageViewLeft = (AsyncImageView) vh.left.findViewById(R.id.goodImgGrid);
			vh.nameLeft = (TextView) vh.left.findViewById(R.id.goodNameGrid);
			vh.priceLeft = (TextView) vh.left.findViewById(R.id.goodPricesGrid);
			vh.buyerLeft = (TextView) vh.left.findViewById(R.id.buyerGrid);
			vh.right = (LinearLayout)convertView.findViewById(R.id.shopsdk_all_product_right);
			vh.imageViewRight = (AsyncImageView) vh.right.findViewById(R.id.goodImgGrid);
			vh.nameRight = (TextView) vh.right.findViewById(R.id.goodNameGrid);
			vh.priceRight = (TextView) vh.right.findViewById(R.id.goodPricesGrid);
			vh.buyerRight = (TextView) vh.right.findViewById(R.id.buyerGrid);

			if (ivWidth == 0) {
				ViewGroup.LayoutParams lp = vh.imageViewLeft.getLayoutParams();
				ivWidth = lp.width;
			}

			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();

		}

		if (list != null && !list.isEmpty()) {
			final int left = 2 * i;
			final Product product = list.get(left);
			vh.nameLeft.setText(product.getProductName());
			List<Commodity> commoditys = product.getCommodityList();
			vh.priceLeft.setText(MoneyConverter.conversionMoneyStr(commoditys.get(0).getCurrentCost()));
			String buyerStr = String.format(this.salesFormatter, commoditys.get(0).getCommoditySales());
			vh.buyerLeft.setText(buyerStr);
			vh.imageViewLeft.setCompressOptions(ivWidth, ivWidth, 80, 10240L);
			vh.imageViewLeft.execute(commoditys.get(0).getImgUrl().getSrc(), ResHelper.getColorRes(getParent().getContext(),
					"order_bg"));
			if (vh.left != null) {
				vh.left.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (onItemClickListener != null) {
							onItemClickListener.onItemClick(left);
						}
						gotoDetailPage(product);
					}
				});
			}
			if (vh.right != null) {
				vh.right.setVisibility(View.INVISIBLE);
			}
			final int right = 2 * i + 1;
			if (list.size() > right) {
				final Product product2 = list.get(right);
				vh.nameRight.setText(product2.getProductName());
				List<Commodity> commoditys2 = product2.getCommodityList();
				vh.priceRight.setText(MoneyConverter.conversionMoneyStr(commoditys2.get(0).getCurrentCost()));
				String buyerStrRight = String.format(this.salesFormatter, commoditys2.get(0).getCommoditySales());
				vh.buyerRight.setText(buyerStrRight);
				vh.imageViewRight.setCompressOptions(ivWidth, ivWidth, 80, 10240L);
				vh.imageViewRight.execute(commoditys2.get(0).getImgUrl().getSrc(), ResHelper.getColorRes(getParent().getContext(),
						"order_bg"));
				if (vh.right != null) {
					vh.right.setVisibility(View.VISIBLE);
					vh.right.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (onItemClickListener != null) {
								onItemClickListener.onItemClick(right);
							}
							gotoDetailPage(product2);
						}
					});
				}
			}
		}

		return convertView;
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public Product getItem(int i) {
//		List<Product> rst = new ArrayList<Product>();
//		rst.add(list.get(2* i));
//		if (list.size() - 1 >= 2 * i + 1) {
//			rst.add(list.get(2 * i + 1));
//		}
//		return rst;
		return list == null ? null : list.get(i);
	}

	@Override
	public int getCount() {
		double size = list == null ? 0 : list.size();
		double i = size / 2;
		// 有小数时一律进位
		return (int)Math.ceil(i);
	}

	static class ViewHolder {
		LinearLayout left;
		AsyncImageView imageViewLeft;
		TextView nameLeft;
		TextView priceLeft;
		TextView buyerLeft;
		LinearLayout right;
		AsyncImageView imageViewRight;
		TextView nameRight;
		TextView priceRight;
		TextView buyerRight;
	}

	private void gotoDetailPage(Product product) {
		CommodityDetailShowPage detailShowPage = new CommodityDetailShowPage(mainPageAdapter.getPage().getTheme(), product);
		detailShowPage.show(getContext(), null);
	}
}
