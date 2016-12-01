package com.mcx.hoveringlist.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.mcx.hoveringlist.R;
import com.mcx.hoveringlist.fragments.SimpleListFragment;
import com.mcx.hoveringlist.widgets.ScrollViewWithList;

public class MainActivity extends AppCompatActivity implements ScrollViewWithList.OnAttachStateChangedListener, ScrollViewWithList.OnScrollListener {

	private SimpleListFragment mListFragment;
	private ScrollViewWithList mScrollView;
	private View mListContainer;
	private View mHeaderView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mHeaderView = findViewById(R.id.header_img);
		mScrollView = (ScrollViewWithList) findViewById(R.id.scrollView);
		mListContainer = findViewById(R.id.list_container);
		mScrollView.setOnScrollListener(this);
		mScrollView.setListContainer(mListContainer);
		mScrollView.setAttachStateChangedListener(this);
		mScrollView.post(new Runnable() {
			@Override
			public void run() {
				mScrollView.setScrollableView(mListFragment.getListView());
				ViewGroup.LayoutParams params = mListContainer.getLayoutParams();
				params.height = findViewById(R.id.root).getMeasuredHeight();
				mListContainer.setLayoutParams(params);
			}
		});
		mListFragment = new SimpleListFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.list_container, mListFragment).commitAllowingStateLoss();
		mListFragment.setBackButtonClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onAttachStateChanged(boolean attached) {
		mListFragment.setShowBackButton(attached);
	}

	@Override
	public void onBackPressed() {
		if (mScrollView.isInterceptTouchEvent()) {
			super.onBackPressed();
		} else {
			mScrollView.backToTop();
		}

	}

	@Override
	public void onScroll(int scrollY) {
		mHeaderView.setTranslationY(scrollY * 0.4f);
	}
}
