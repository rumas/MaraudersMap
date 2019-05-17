package com.brunel.team28.maraudersmap.Menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brunel.team28.maraudersmap.R;

import java.util.List;

// Used to set up the adapter

class MenuContentAdapter extends RecyclerView.Adapter<MenuContentAdapter.contentViewHolder> {
	private final List<MenuContent> contentList;

	public MenuContentAdapter(List<MenuContent> contentList) {
		this.contentList = contentList;
	}

	@Override
	public contentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_my_profile, viewGroup, false);
		return new contentViewHolder(itemView);
	}

	// When the view holder is bound to the view, this will set the text to screen name and description
	@Override
	public void onBindViewHolder(contentViewHolder holder, int i) {
		MenuContent mc = contentList.get(i);
		holder.vScreenName.setText(mc.screenName);
		holder.vScreenDesc.setText(mc.screenDesc);
	}

	@Override
	public int getItemCount() {
		return contentList.size();
	}

	static class contentViewHolder extends RecyclerView.ViewHolder {
		final TextView vScreenName;
		final TextView vScreenDesc;

		contentViewHolder(View v) {
			super(v);
			vScreenName = (TextView) v.findViewById(R.id.menu_card_screenName);
			vScreenDesc = (TextView) v.findViewById(R.id.menu_card_screenDesc);
		}
	}
}
