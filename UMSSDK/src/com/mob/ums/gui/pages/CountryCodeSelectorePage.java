package com.mob.ums.gui.pages;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;

import com.mob.jimu.gui.Page;
import com.mob.jimu.gui.Theme;
import com.mob.tools.utils.Hashon;
import com.mob.tools.utils.ResHelper;
import com.mob.ums.OperationCallback;
import com.mob.ums.UMSSDK;
import com.mob.ums.gui.pages.dialog.ErrorDialog;
import com.mob.ums.gui.pages.dialog.ProgressDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.GZIPInputStream;

/** country selector, for sending verify code */
public class CountryCodeSelectorePage extends Page<CountryCodeSelectorePage> {
	private static HashMap<String, String> countryRules;
	private String code;
	private ProgressDialog pd;

	public CountryCodeSelectorePage(Theme theme) {
		super(theme);
	}

	public void setLoginCode(String code) {
		this.code = code;
	}

	public String getLoginCode() {
		return code;
	}

	public void onCreate() {
		super.onCreate();
	}

	public void prepareData(final Callback cb) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = new ProgressDialog.Builder(getContext(), getTheme()).show();
		SearchEngine.prepare(activity, new Runnable() {
			public void run() {
				if (countryRules == null || countryRules.size() <= 0) {
					UMSSDK.getAvailableVCodeCountries(new OperationCallback<ArrayList<HashMap<String,Object>>>() {
						public void onSuccess(ArrayList<HashMap<String,Object>> data) {
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}
							countryRules = onCountryListGot(data);
							Message msg = new Message();
							msg.what = 1;
							cb.handleMessage(msg);
						}

						public void onFailed(Throwable t) {
//							t.printStackTrace();
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}
							ErrorDialog.Builder builder = new ErrorDialog.Builder(getContext(), getTheme());
							int resId = ResHelper.getStringRes(getContext(), "umssdk_default_country");
							builder.setTitle(getContext().getString(resId));
							builder.setThrowable(t);
							builder.setMessage(t.getMessage());
							builder.setOnDismissListener(new OnDismissListener() {
								public void onDismiss(DialogInterface dialog) {
									Message msg = new Message();
									msg.what = 0;
									cb.handleMessage(msg);
								}
							});
							builder.show();
						}
					});
				} else {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					Message msg = new Message();
					msg.what = 1;
					cb.handleMessage(msg);
				}
			}
		});
	}

	private HashMap<String, String> onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
		// 解析国家列表
		HashMap<String, String> countryRules = new HashMap<String, String>();
		for (HashMap<String, Object> country : countries) {
			String code = (String) country.get("zone");
			String rule = (String) country.get("rule");
			if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
				continue;
			}
			countryRules.put(code, rule);
		}
		return countryRules;
	}

	public HashMap<String, String> getCountries() {
		return countryRules;
	}

	public static class SearchEngine {
		private static final String DB_FILE = "smssdk_pydb";
		private static HashMap<String, Object> hanzi2Pinyin;
		private boolean caseSensitive;
		private ArrayList<SearchIndex> index;

		public static void prepare(final Context context, final Runnable afterPrepare) {
			Runnable act = new Runnable() {
				public void run() {
					synchronized (DB_FILE) {
						if (hanzi2Pinyin == null || hanzi2Pinyin.size() <= 0) {
							try {
								int resId = ResHelper.getRawRes(context, DB_FILE);
								if (resId <= 0) {
									hanzi2Pinyin = new HashMap<String, Object>();
								} else {
									InputStream is = context.getResources().openRawResource(resId);
									GZIPInputStream gzis = new GZIPInputStream(is);
									InputStreamReader isr = new InputStreamReader(gzis);
									BufferedReader br = new BufferedReader(isr);
									String json = br.readLine();
									br.close();
									hanzi2Pinyin = new Hashon().fromJson(json);
								}
							} catch (Throwable t) {
//								t.printStackTrace();
								hanzi2Pinyin = new HashMap<String, Object>();
							}
						}

						if (afterPrepare != null) {
							afterPrepare.run();
						}
					}
				}
			};
			if (afterPrepare != null) {
				new Thread(act).start();
			} else {
				act.run();
			}
		}

		public void setCaseSensitive(boolean caseSensitive) {
			this.caseSensitive = caseSensitive;
		}

		public void setIndex(ArrayList<String> index) {
			this.index = new ArrayList<SearchIndex>();
			for (String i : index) {
				this.index.add(new SearchIndex(i, hanzi2Pinyin));
			}
		}

		public ArrayList<String> match(String token) {
			ArrayList<String> res = new ArrayList<String>();
			if (index == null) {
				return res;
			}

			for (SearchIndex si : index) {
				if (si.match(token, caseSensitive)) {
					res.add(si.getText());
				}
			}
			return res;
		}

		public static void destory() {
			if (hanzi2Pinyin != null) {
				hanzi2Pinyin.clear();
				hanzi2Pinyin = null;
			}
		}

		private static class SearchIndex {
			private String text;
			private ArrayList<String> pinyin;
			private ArrayList<String> firstLatters;

			public SearchIndex(String text, HashMap<String, Object> hanzi2Pinyin) {
				this.text = text;
				this.pinyin = new ArrayList<String>();
				firstLatters = new ArrayList<String>();
				createPinyinList(hanzi2Pinyin);
			}

			private void createPinyinList(HashMap<String, Object> hanzi2Pinyin) {
				if (hanzi2Pinyin != null && hanzi2Pinyin.size() > 0) {
					char[] cArr = text.toCharArray();
					ArrayList<String[]> pinyin = new ArrayList<String[]>();
					for (char c : cArr) {
						String s = String.valueOf(c);
						@SuppressWarnings("unchecked")
						ArrayList<HashMap<String, Object>> yins
								= (ArrayList<HashMap<String, Object>>) hanzi2Pinyin.get(s);
						int size = yins == null ? 0 : yins.size();
						String[] py = new String[size];
						for (int i = 0; i < size; i++) {
							String yin = (String) yins.get(i).get("yin");
							if ("none".equals(yin)) {
								yin = "";
							}
							py[i] = yin;
						}
						pinyin.add(py);
					}

					HashSet<String> pyRes = new HashSet<String>();
					HashSet<String> flRes = new HashSet<String>();
					toPinyinArray("", "", pyRes, flRes, pinyin);
					this.pinyin.addAll(pyRes);
					firstLatters.addAll(flRes);
				}
			}

			private void toPinyinArray(String base, String firstLatter, HashSet<String> pyRes,
					HashSet<String> flRes, ArrayList<String[]> pys) {
				if (pys.size() > 0) {
					String[] py = pys.get(0);
					ArrayList<String[]> cpys = new ArrayList<String[]>();
					cpys.addAll(pys);
					cpys.remove(0);
					for (String y : py) {
						if (y.length() > 0) {
							toPinyinArray(base + y, firstLatter + y.charAt(0), pyRes, flRes, cpys);
						} else {
							toPinyinArray(base, firstLatter, pyRes, flRes, cpys);
						}
					}
				} else {
					pyRes.add(base);
					flRes.add(firstLatter);
				}
			}

			public String getText() {
				return text;
			}

			private boolean match(String token, boolean caseSensitive) {
				if (token == null || token.trim().length() <= 0) {
					return true;
				}

				String keyToSearch = token;
				if (!caseSensitive) {
					keyToSearch = token.toLowerCase();
				}

				if (text != null && text.toLowerCase().contains(keyToSearch)) {
					return true;
				}

				for (String py : pinyin) {
					if (py.contains(keyToSearch)) {
						return true;
					}
				}

				for (String fl : firstLatters) {
					if (fl.contains(keyToSearch)) {
						return true;
					}
				}

				return false;
			}

			public String toString() {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("text", text);
				map.put("pinyin", pinyin);
				map.put("firstLatters", firstLatters);
				return map.toString();
			}

		}
	}

}
