package com.mcx.hoveringlist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mcx.hoveringlist.R;

/**
 * 列表Fragment
 * Created by machenxi on 2016/11/28.
 */
public class SimpleListFragment extends Fragment {

	private View mBackButton;
	private View.OnClickListener mBackButtonClickListener;
	private ListView mListView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mBackButton = view.findViewById(R.id.backBtn);
		mListView = (ListView) view.findViewById(R.id.listView);
		mListView.setAdapter(new ListAdapter(getContext()));

		mBackButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mBackButtonClickListener != null) {
					mBackButtonClickListener.onClick(view);
				}
			}
		});
	}

	public void setBackButtonClickListener(View.OnClickListener backButtonClickListener) {
		mBackButtonClickListener = backButtonClickListener;
	}

	public void setShowBackButton(boolean show) {
		mBackButton.setVisibility(show ? View.VISIBLE : View.GONE);
	}

	public ListView getListView() {
		return mListView;
	}

	class ListAdapter extends BaseAdapter {

		private int mDrawableRes[] = {R.mipmap.img_1, R.mipmap.img_2, R.mipmap.img_3, R.mipmap.img_4, R.mipmap.img_5, R.mipmap.img_6, R.mipmap.img_7,};
		private Context mContext;

		public ListAdapter(Context context) {
			mContext = context;
		}

		@Override
		public int getCount() {
			return mDrawableRes.length * 3;
		}

		@Override
		public Object getItem(int i) {
			return mDrawableRes[i];
		}

		@Override
		public long getItemId(int i) {
			return i;
		}

		@Override
		public View getView(int i, View view, ViewGroup viewGroup) {
			ViewHolder holder;
			if (view == null) {
				view = LayoutInflater.from(mContext).inflate(R.layout.list_item, viewGroup, false);
				holder = new ViewHolder();
				holder.txt = (TextView) view.findViewById(R.id.list_item_txt);
				holder.img = (ImageView) view.findViewById(R.id.list_item_img);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.txt.setText("List Item " + i);
			holder.img.setBackgroundResource(mDrawableRes[i % mDrawableRes.length]);
			return view;
		}

		class ViewHolder {
			TextView txt;
			ImageView img;
		}

	}
}
