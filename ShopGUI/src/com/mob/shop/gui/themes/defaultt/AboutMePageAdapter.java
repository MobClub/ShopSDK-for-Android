package com.mob.shop.gui.themes.defaultt;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.shop.gui.R;
import com.mob.shop.gui.pages.AboutMePage;
import com.mob.shop.gui.themes.defaultt.components.TitleView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yjin on 2017/11/14.
 */

public class AboutMePageAdapter extends DefaultThemePageAdapter<AboutMePage> {
	private RelativeLayout aboutMeTitle;
	private RelativeLayout aboutMeShow;
	private ImageView aboutIsShow;
	private TextView versionName;
	private TextView versionDate;
	private int type = 0;
	private TitleView aboutMeTitleBack;

	@Override
	public void onCreate(AboutMePage page, Activity activity) {
		super.onCreate(page, activity);
		View view = LayoutInflater.from(activity).inflate(R.layout.shopsdk_default_about_me,null);
		init(view);
		activity.setContentView(view);
		initData();
	}

	private void init(View view){
		aboutMeTitleBack = (TitleView)view.findViewById(R.id.aboutMeTitleView);
		aboutMeTitleBack.initTitleView(getPage(), "shopsdk_default_about_me_title", null, null, true);
		aboutMeTitle = (RelativeLayout)view.findViewById(R.id.about_me_version_title);
		aboutIsShow = (ImageView)view.findViewById(R.id.about_me_is_show);
		aboutMeTitle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(type == 0){
					transUpDown();
				} else if(type == 1){
					transNormal();
				}

			}
		});
		aboutMeShow = (RelativeLayout)view.findViewById(R.id.about_me_version_show);
		aboutMeShow.setVisibility(View.GONE);
		versionName = (TextView)view.findViewById(R.id.about_me_version_name);
		versionDate = (TextView)view.findViewById(R.id.about_me_version_date);
	}

	private void initData(){
		versionName.setText(getVersion());
		versionDate.setText(getVersionDate());
	}

	public String getVersion() {
		try {
			PackageManager manager = getPage().getContext().getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPage().getContext().getPackageName(), 0);
			String version = info.versionName;
			return "v" + version;
		} catch (Exception e) {
			e.printStackTrace();
			return "v0.0.1";
		}
	}

	private String getVersionDate(){
		try {
			PackageManager manager = getPage().getContext().getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPage().getContext().getPackageName(), 0);
			Date date = new Date(info.lastUpdateTime);
			String time =  new SimpleDateFormat("yyyy.MM.dd").format(date);
			return time;
		} catch (Exception e) {
			e.printStackTrace();
			return new SimpleDateFormat("yyyy.MM.dd").format(new Date());
		}
	}

	private void transNormal(){
		type = 0;
		aboutIsShow.setRotationX(0);
		aboutMeShow.setVisibility(View.GONE);
	}

	private void transUpDown(){
		type = 1;
		aboutIsShow.setRotationX(180);
		aboutMeShow.setVisibility(View.VISIBLE);
	}
}
