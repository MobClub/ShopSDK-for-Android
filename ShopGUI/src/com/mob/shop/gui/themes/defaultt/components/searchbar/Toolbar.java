package com.mob.shop.gui.themes.defaultt.components.searchbar;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


/**
 * Created by yjin on 2017/10/10.
 */

public class Toolbar extends LinearLayout {
	public Toolbar(Context context) {
		super(context);
	}

	public Toolbar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

//	public Toolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//	}

//	private int mTitleTextAppearance;
//	private int mSubtitleTextAppearance;
//	private int mNavButtonStyle;
//
//	private int mButtonGravity;
//
//	private int mMaxButtonHeight;
//
//	private int mTitleMarginStart;
//	private int mTitleMarginEnd;
//	private int mTitleMarginTop;
//	private int mTitleMarginBottom;
//
//	private int mContentInsetStartWithNavigation;
//	private int mContentInsetEndWithActions;
//
//	private int mGravity = Gravity.START | Gravity.CENTER_VERTICAL;
//
//	private Drawable mCollapseIcon;
//	private CharSequence mCollapseDescription;
//	private ImageButton mCollapseButtonView;
//	View mExpandedActionView;
//
//	/**
//	 * Context against which to inflate popup menus.
//	 */
//	private Context mPopupContext;
//
//	/**
//	 * Theme resource against which to inflate popup menus.
//	 */
//	private int mPopupTheme;
//
//	private boolean mCollapsible;
//	private final int[] mTempMargins = new int[2];
//
//	private TextView mTitleTextView;
//	private TextView mSubtitleTextView;
//	private ImageButton mNavButtonView;
//	private ImageView mLogoView;
//
//	private final ArrayList<View> mTempViews = new ArrayList<View>();
//
//	public Toolbar(Context context) {
//		super(context);
//	}
//
//	public Toolbar(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
//		super(context, attrs, defStyleAttr);
//	}
//
//	public Toolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Toolbar,
//				defStyleAttr, defStyleRes);
//
//		mTitleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
//		mSubtitleTextAppearance = a.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
//		mNavButtonStyle = a.getResourceId(R.styleable.Toolbar_navigationButtonStyle, 0);
//		mGravity = a.getInteger(R.styleable.Toolbar_gravity, mGravity);
//		mButtonGravity = a.getInteger(R.styleable.Toolbar_buttonGravity, Gravity.TOP);
//		mTitleMarginStart = mTitleMarginEnd = mTitleMarginTop = mTitleMarginBottom =
//				a.getDimensionPixelOffset(R.styleable.Toolbar_titleMargin, 0);
//
//		final int marginStart = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
//		if (marginStart >= 0) {
//			mTitleMarginStart = marginStart;
//		}
//
//		final int marginEnd = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
//		if (marginEnd >= 0) {
//			mTitleMarginEnd = marginEnd;
//		}
//
//		final int marginTop = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
//		if (marginTop >= 0) {
//			mTitleMarginTop = marginTop;
//		}
//
//		final int marginBottom = a.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom,
//				-1);
//		if (marginBottom >= 0) {
//			mTitleMarginBottom = marginBottom;
//		}
//
//		mMaxButtonHeight = a.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
//
//
//		mContentInsetStartWithNavigation = a.getDimensionPixelOffset(
//				R.styleable.Toolbar_contentInsetStartWithNavigation, Integer.MIN_VALUE);
//		mContentInsetEndWithActions = a.getDimensionPixelOffset(
//				R.styleable.Toolbar_contentInsetEndWithActions, Integer.MIN_VALUE);
//
//		mCollapseIcon = a.getDrawable(R.styleable.Toolbar_collapseIcon);
//		mCollapseDescription = a.getText(R.styleable.Toolbar_collapseContentDescription);
//
//		final Drawable navIcon = a.getDrawable(R.styleable.Toolbar_navigationIcon);
//		final CharSequence navDesc = a.getText(
//				R.styleable.Toolbar_navigationContentDescription);
//		final CharSequence logoDesc = a.getText(R.styleable.Toolbar_logoDescription);
//		a.recycle();
//	}
//
//	private boolean shouldLayout(View view) {
//		return view != null && view.getParent() == this && view.getVisibility() != GONE;
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int width = 0;
//		int height = 0;
//		int childState = 0;
//
//		final int[] collapsingMargins = mTempMargins;
//		final int marginStartIndex;
//		final int marginEndIndex;
//		marginStartIndex = 1;
//		marginEndIndex = 0;
//
//		int navWidth = 0;
//
//		final int contentInsetStart = getCurrentContentInsetStart();
//		width += Math.max(contentInsetStart, navWidth);
//		collapsingMargins[marginStartIndex] = Math.max(0, contentInsetStart - navWidth);
//
//		int menuWidth = 0;
//
//		final int contentInsetEnd = getCurrentContentInsetEnd();
//		width += Math.max(contentInsetEnd, menuWidth);
//		collapsingMargins[marginEndIndex] = Math.max(0, contentInsetEnd - menuWidth);
//
//		if (shouldLayout(mExpandedActionView)) {
//			width += measureChildCollapseMargins(mExpandedActionView, widthMeasureSpec, width,
//					heightMeasureSpec, 0, collapsingMargins);
//			height = Math.max(height, mExpandedActionView.getMeasuredHeight() +
//					getVerticalMargins(mExpandedActionView));
//			childState = combineMeasuredStates(childState, mExpandedActionView.getMeasuredState());
//		}
//
//		final int childCount = getChildCount();
//		for (int i = 0; i < childCount; i++) {
//			final View child = getChildAt(i);
//			width += measureChildCollapseMargins(child, widthMeasureSpec, width,
//					heightMeasureSpec, 0, collapsingMargins);
//			height = Math.max(height, child.getMeasuredHeight() + getVerticalMargins(child));
//			childState = combineMeasuredStates(childState, child.getMeasuredState());
//		}
//
//		int titleWidth = 0;
//		int titleHeight = 0;
//
//		width += titleWidth;
//		height = Math.max(height, titleHeight);
//
//		width += getPaddingLeft() + getPaddingRight();
//		height += getPaddingTop() + getPaddingBottom();
//
//		final int measuredWidth = resolveSizeAndState(
//				Math.max(width, getSuggestedMinimumWidth()),
//				widthMeasureSpec, childState & MEASURED_STATE_MASK);
//		final int measuredHeight = resolveSizeAndState(
//				Math.max(height, getSuggestedMinimumHeight()),
//				heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT);
//
//		setMeasuredDimension(measuredWidth, shouldCollapse() ? 0 : measuredHeight);
//	}
//
//	/**
//	 * Gets the content inset that will be used on the ending side of the bar in the current
//	 * toolbar configuration.
//	 *
//	 * @return the current content inset end in pixels
//	 */
//	public int getCurrentContentInsetEnd() {
//		return Math.max(getContentInsetEnd(), Math.max(mContentInsetEndWithActions, 0));
//
//	}
//
//	public int getContentInsetEnd() {
//		return 0;
//	}
//
//	public int getCurrentContentInsetStart() {
//		return 0;
//	}
//
//	private int getVerticalMargins(View v) {
//		return v.getTop() + v.getBottom();
//	}
//
//	/**
//	 * Returns the width + uncollapsed margins
//	 */
//	private int measureChildCollapseMargins(View child,
//											int parentWidthMeasureSpec, int widthUsed,
//											int parentHeightMeasureSpec, int heightUsed, int[] collapsingMargins) {
//		final ViewGroup.LayoutParams lp = child.getLayoutParams();
//
//		final int leftDiff = child.getLeft() - collapsingMargins[0];
//		final int rightDiff = child.getRight() - collapsingMargins[1];
//		final int leftMargin = Math.max(0, leftDiff);
//		final int rightMargin = Math.max(0, rightDiff);
//		final int hMargins = leftMargin + rightMargin;
//		collapsingMargins[0] = Math.max(0, -leftDiff);
//		collapsingMargins[1] = Math.max(0, -rightDiff);
//
//		final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
//				0 + 0 + hMargins + widthUsed, lp.width);
//		final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
//				0 + 0 + child.getTop() + child.getBottom()
//						+ heightUsed, lp.height);
//
//		child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//		return child.getMeasuredWidth() + hMargins;
//	}
//
//	/**
//	 * Returns true if the Toolbar is collapsible and has no child views with a measured size > 0.
//	 */
//	private boolean shouldCollapse() {
//		if (!mCollapsible) return false;
//
//		final int childCount = getChildCount();
//		for (int i = 0; i < childCount; i++) {
//			final View child = getChildAt(i);
//			if (shouldLayout(child) && child.getMeasuredWidth() > 0 &&
//					child.getMeasuredHeight() > 0) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		final boolean isRtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
//		final int width = getWidth();
//		final int height = getHeight();
//		final int paddingLeft = getPaddingLeft();
//		final int paddingRight = getPaddingRight();
//		final int paddingTop = getPaddingTop();
//		final int paddingBottom = getPaddingBottom();
//		int left = paddingLeft;
//		int right = width - paddingRight;
//
//		final int[] collapsingMargins = mTempMargins;
//		collapsingMargins[0] = collapsingMargins[1] = 0;
//
//		final int alignmentHeight = getMinimumHeight();
//
//		if (shouldLayout(mNavButtonView)) {
//			if (isRtl) {
//				right = layoutChildRight(mNavButtonView, right, collapsingMargins,
//						alignmentHeight);
//			} else {
//				left = layoutChildLeft(mNavButtonView, left, collapsingMargins,
//						alignmentHeight);
//			}
//		}
//
//		if (shouldLayout(mCollapseButtonView)) {
//			if (isRtl) {
//				right = layoutChildRight(mCollapseButtonView, right, collapsingMargins,
//						alignmentHeight);
//			} else {
//				left = layoutChildLeft(mCollapseButtonView, left, collapsingMargins,
//						alignmentHeight);
//			}
//		}
//
//		final int contentInsetLeft = getCurrentContentInsetLeft();
//		final int contentInsetRight = 0;
//		collapsingMargins[0] = Math.max(0, contentInsetLeft - left);
//		collapsingMargins[1] = Math.max(0, contentInsetRight - (width - paddingRight - right));
//		left = Math.max(left, contentInsetLeft);
//		right = Math.min(right, width - paddingRight - contentInsetRight);
//
//		if (shouldLayout(mExpandedActionView)) {
//			if (isRtl) {
//				right = layoutChildRight(mExpandedActionView, right, collapsingMargins,
//						alignmentHeight);
//			} else {
//				left = layoutChildLeft(mExpandedActionView, left, collapsingMargins,
//						alignmentHeight);
//			}
//		}
//
//		if (shouldLayout(mLogoView)) {
//			if (isRtl) {
//				right = layoutChildRight(mLogoView, right, collapsingMargins,
//						alignmentHeight);
//			} else {
//				left = layoutChildLeft(mLogoView, left, collapsingMargins,
//						alignmentHeight);
//			}
//		}
//
//		final boolean layoutTitle = shouldLayout(mTitleTextView);
//		final boolean layoutSubtitle = shouldLayout(mSubtitleTextView);
//		int titleHeight = 0;
//		if (layoutTitle) {
//			final android.widget.Toolbar.LayoutParams lp = (android.widget.Toolbar.LayoutParams) mTitleTextView.getLayoutParams();
//			titleHeight += lp.topMargin + mTitleTextView.getMeasuredHeight() + lp.bottomMargin;
//		}
//		if (layoutSubtitle) {
//			final android.widget.Toolbar.LayoutParams lp = (android.widget.Toolbar.LayoutParams) mSubtitleTextView.getLayoutParams();
//			titleHeight += lp.topMargin + mSubtitleTextView.getMeasuredHeight() + lp.bottomMargin;
//		}
//
//		if (layoutTitle || layoutSubtitle) {
//			int titleTop;
//			final View topChild = layoutTitle ? mTitleTextView : mSubtitleTextView;
//			final View bottomChild = layoutSubtitle ? mSubtitleTextView : mTitleTextView;
//			final android.widget.Toolbar.LayoutParams toplp = (android.widget.Toolbar.LayoutParams) topChild.getLayoutParams();
//			final android.widget.Toolbar.LayoutParams bottomlp = (android.widget.Toolbar.LayoutParams) bottomChild.getLayoutParams();
//			final boolean titleHasWidth = layoutTitle && mTitleTextView.getMeasuredWidth() > 0
//					|| layoutSubtitle && mSubtitleTextView.getMeasuredWidth() > 0;
//
//			switch (mGravity & Gravity.VERTICAL_GRAVITY_MASK) {
//				case Gravity.TOP:
//					titleTop = getPaddingTop() + toplp.topMargin + mTitleMarginTop;
//					break;
//				default:
//				case Gravity.CENTER_VERTICAL:
//					final int space = height - paddingTop - paddingBottom;
//					int spaceAbove = (space - titleHeight) / 2;
//					if (spaceAbove < toplp.topMargin + mTitleMarginTop) {
//						spaceAbove = toplp.topMargin + mTitleMarginTop;
//					} else {
//						final int spaceBelow = height - paddingBottom - titleHeight -
//								spaceAbove - paddingTop;
//						if (spaceBelow < toplp.bottomMargin + mTitleMarginBottom) {
//							spaceAbove = Math.max(0, spaceAbove -
//									(bottomlp.bottomMargin + mTitleMarginBottom - spaceBelow));
//						}
//					}
//					titleTop = paddingTop + spaceAbove;
//					break;
//				case Gravity.BOTTOM:
//					titleTop = height - paddingBottom - bottomlp.bottomMargin - mTitleMarginBottom -
//							titleHeight;
//					break;
//			}
//			if (isRtl) {
//				final int rd = (titleHasWidth ? mTitleMarginStart : 0) - collapsingMargins[1];
//				right -= Math.max(0, rd);
//				collapsingMargins[1] = Math.max(0, -rd);
//				int titleRight = right;
//				int subtitleRight = right;
//
//				if (layoutTitle) {
//					final android.widget.Toolbar.LayoutParams lp = (android.widget.Toolbar.LayoutParams) mTitleTextView.getLayoutParams();
//					final int titleLeft = titleRight - mTitleTextView.getMeasuredWidth();
//					final int titleBottom = titleTop + mTitleTextView.getMeasuredHeight();
//					mTitleTextView.layout(titleLeft, titleTop, titleRight, titleBottom);
//					titleRight = titleLeft - mTitleMarginEnd;
//					titleTop = titleBottom + lp.bottomMargin;
//				}
//				if (layoutSubtitle) {
//					final android.widget.Toolbar.LayoutParams lp = (android.widget.Toolbar.LayoutParams) mSubtitleTextView.getLayoutParams();
//					titleTop += lp.topMargin;
//					final int subtitleLeft = subtitleRight - mSubtitleTextView.getMeasuredWidth();
//					final int subtitleBottom = titleTop + mSubtitleTextView.getMeasuredHeight();
//					mSubtitleTextView.layout(subtitleLeft, titleTop, subtitleRight, subtitleBottom);
//					subtitleRight = subtitleRight - mTitleMarginEnd;
//					titleTop = subtitleBottom + lp.bottomMargin;
//				}
//				if (titleHasWidth) {
//					right = Math.min(titleRight, subtitleRight);
//				}
//			} else {
//				final int ld = (titleHasWidth ? mTitleMarginStart : 0) - collapsingMargins[0];
//				left += Math.max(0, ld);
//				collapsingMargins[0] = Math.max(0, -ld);
//				int titleLeft = left;
//				int subtitleLeft = left;
//
//				if (layoutTitle) {
//					final android.widget.Toolbar.LayoutParams lp = (android.widget.Toolbar.LayoutParams) mTitleTextView.getLayoutParams();
//					final int titleRight = titleLeft + mTitleTextView.getMeasuredWidth();
//					final int titleBottom = titleTop + mTitleTextView.getMeasuredHeight();
//					mTitleTextView.layout(titleLeft, titleTop, titleRight, titleBottom);
//					titleLeft = titleRight + mTitleMarginEnd;
//					titleTop = titleBottom + lp.bottomMargin;
//				}
//				if (layoutSubtitle) {
//					final android.widget.Toolbar.LayoutParams lp = (android.widget.Toolbar.LayoutParams) mSubtitleTextView.getLayoutParams();
//					titleTop += lp.topMargin;
//					final int subtitleRight = subtitleLeft + mSubtitleTextView.getMeasuredWidth();
//					final int subtitleBottom = titleTop + mSubtitleTextView.getMeasuredHeight();
//					mSubtitleTextView.layout(subtitleLeft, titleTop, subtitleRight, subtitleBottom);
//					subtitleLeft = subtitleRight + mTitleMarginEnd;
//					titleTop = subtitleBottom + lp.bottomMargin;
//				}
//				if (titleHasWidth) {
//					left = Math.max(titleLeft, subtitleLeft);
//				}
//			}
//		}
//		addCustomViewsWithGravity(mTempViews, Gravity.LEFT);
//		final int leftViewsCount = mTempViews.size();
//		for (int i = 0; i < leftViewsCount; i++) {
//			left = layoutChildLeft(mTempViews.get(i), left, collapsingMargins,
//					alignmentHeight);
//		}
//
//		addCustomViewsWithGravity(mTempViews, Gravity.RIGHT);
//		final int rightViewsCount = mTempViews.size();
//		for (int i = 0; i < rightViewsCount; i++) {
//			right = layoutChildRight(mTempViews.get(i), right, collapsingMargins,
//					alignmentHeight);
//		}
//		addCustomViewsWithGravity(mTempViews, Gravity.CENTER_HORIZONTAL);
//		final int centerViewsWidth = getViewListMeasuredWidth(mTempViews, collapsingMargins);
//		final int parentCenter = paddingLeft + (width - paddingLeft - paddingRight) / 2;
//		final int halfCenterViewsWidth = centerViewsWidth / 2;
//		int centerLeft = parentCenter - halfCenterViewsWidth;
//		final int centerRight = centerLeft + centerViewsWidth;
//		if (centerLeft < left) {
//			centerLeft = left;
//		} else if (centerRight > right) {
//			centerLeft -= centerRight - right;
//		}
//
//		final int centerViewsCount = mTempViews.size();
//		for (int i = 0; i < centerViewsCount; i++) {
//			centerLeft = layoutChildLeft(mTempViews.get(i), centerLeft, collapsingMargins,
//					alignmentHeight);
//		}
//
//		mTempViews.clear();
//	}
//
//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//	private void addCustomViewsWithGravity(List<View> views, int gravity) {
//		final boolean isRtl = getLayoutDirection() == LAYOUT_DIRECTION_RTL;
//		final int childCount = getChildCount();
//		views.clear();
//
//		if (isRtl) {
//			for (int i = childCount - 1; i >= 0; i--) {
//				final View child = getChildAt(i);
//				views.add(child);
//			}
//		} else {
//			for (int i = 0; i < childCount; i++) {
//				final View child = getChildAt(i);
//				views.add(child);
//				//	}
//			}
//		}
//	}
//
//	private int getViewListMeasuredWidth(List<View> views, int[] collapsingMargins) {
//		int collapseLeft = collapsingMargins[0];
//		int collapseRight = collapsingMargins[1];
//		int width = 0;
//		final int count = views.size();
//		for (int i = 0; i < count; i++) {
//			final View v = views.get(i);
//			final int l = v.getLeft() - collapseLeft;
//			final int r = v.getRight() - collapseRight;
//			final int leftMargin = Math.max(0, l);
//			final int rightMargin = Math.max(0, r);
//			collapseLeft = Math.max(0, -l);
//			collapseRight = Math.max(0, -r);
//			width += leftMargin + v.getMeasuredWidth() + rightMargin;
//		}
//		return width;
//	}
//
//	private int layoutChildLeft(View child, int left, int[] collapsingMargins,
//								int alignmentHeight) {
//		final int l = child.getLeft() - collapsingMargins[0];
//		left += Math.max(0, l);
//		collapsingMargins[0] = Math.max(0, -l);
//		final int top = getChildTop(child, alignmentHeight);
//		final int childWidth = child.getMeasuredWidth();
//		child.layout(left, top, left + childWidth, top + child.getMeasuredHeight());
//		left += childWidth + child.getRight();
//		return left;
//	}
//
//	private int getChildTop(View child, int alignmentHeight) {
//		final ViewGroup.LayoutParams lp = child.getLayoutParams();
//		final int childHeight = child.getMeasuredHeight();
//		final int alignmentOffset = alignmentHeight > 0 ? (childHeight - alignmentHeight) / 2 : 0;
//		switch (getChildVerticalGravity(Gravity.BOTTOM)) {
//			case Gravity.TOP:
//				return getPaddingTop() - alignmentOffset;
//			case Gravity.BOTTOM:
//				return getHeight() - getPaddingBottom() - childHeight
//						- child.getBottom() - alignmentOffset;
//			default:
//			case Gravity.CENTER_VERTICAL:
//				final int paddingTop = getPaddingTop();
//				final int paddingBottom = getPaddingBottom();
//				final int height = getHeight();
//				final int space = height - paddingTop - paddingBottom;
//				int spaceAbove = (space - childHeight) / 2;
//				if (spaceAbove < child.getTop()) {
//					spaceAbove = child.getTop();
//				} else {
//					final int spaceBelow = height - paddingBottom - childHeight -
//							spaceAbove - paddingTop;
//					if (spaceBelow < child.getBottom()) {
//						spaceAbove = Math.max(0, spaceAbove - (child.getBottom() - spaceBelow));
//					}
//				}
//				return paddingTop + spaceAbove;
//		}
//	}
//
//	private int getChildVerticalGravity(int gravity) {
//		final int vgrav = gravity & Gravity.VERTICAL_GRAVITY_MASK;
//		switch (vgrav) {
//			case Gravity.TOP:
//			case Gravity.BOTTOM:
//			case Gravity.CENTER_VERTICAL:
//				return vgrav;
//			default:
//				return mGravity & Gravity.VERTICAL_GRAVITY_MASK;
//		}
//	}
//
//	private int layoutChildRight(View child, int right, int[] collapsingMargins,
//								 int alignmentHeight) {
//		final ViewGroup.LayoutParams lp = child.getLayoutParams();
//		final int r = child.getRight() - collapsingMargins[1];
//		right -= Math.max(0, r);
//		collapsingMargins[1] = Math.max(0, -r);
//		final int top = getChildTop(child, alignmentHeight);
//		final int childWidth = child.getMeasuredWidth();
//		child.layout(right - childWidth, top, right, top + child.getMeasuredHeight());
//		right -= childWidth + child.getLeft();
//		return right;
//	}
//
//	public int getCurrentContentInsetLeft() {
//		return getCurrentContentInsetStart();
//	}
}
