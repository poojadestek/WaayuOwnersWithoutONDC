package com.waayu.owner.adepter;

import static com.waayu.owner.retrofit.APIClient.baseUrl;
import static com.waayu.owner.utils.SessionManager.curruncy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waayu.owner.R;
import com.waayu.owner.activity.ProductActivity;
import com.waayu.owner.model.HeaderModel;
import com.waayu.owner.model.MenuitemDataItem;
import com.waayu.owner.utils.SessionManager;

import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<HeaderModel> expandableListTitle;
    private HashMap<String, List<MenuitemDataItem>> expandableListDetail;
    private List<HeaderModel> filteredListHeader;
    private HashMap<String, List<MenuitemDataItem>> filteredListChild;

    public CustomExpandableListAdapter(Context context, List<HeaderModel> expandableListTitle,
                                       HashMap<String, List<MenuitemDataItem>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition).getGetTitle())
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    public void filterData(List<HeaderModel> expandableListTitles, HashMap<String, List<MenuitemDataItem>> query) {
        expandableListTitle = expandableListTitles;
        expandableListDetail = query;
        notifyDataSetChanged();
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final MenuitemDataItem expandedListText = (MenuitemDataItem) getChild(listPosition, expandedListPosition);


        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
        TextView txt_price = (TextView) convertView.findViewById(R.id.txt_price);
        TextView txt_strikeprice = (TextView) convertView.findViewById(R.id.txt_strikeprice);
        TextView txt_percentageoffprice = (TextView) convertView.findViewById(R.id.txt_percentageoffprice);
        ImageView txtPtype = (ImageView) convertView.findViewById(R.id.txt_ptype);
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.img_icon);
        Switch switch_ = (Switch) convertView.findViewById(R.id.switch_);
        RelativeLayout lvlClicl = (RelativeLayout) convertView.findViewById(R.id.lvl_clicl);
        SessionManager sessionManager = new SessionManager(context);
        txtTitle.setText("" + expandedListText.getTitle());
        txt_price.setText(sessionManager.getStringData(curruncy) + "" + expandedListText.getPrice());
        txt_strikeprice.setText(sessionManager.getStringData(curruncy) + expandedListText.getPrice());
        txt_percentageoffprice.setText(sessionManager.getStringData(curruncy) + expandedListText.getDisc_perc() + "%OFF");

        if (expandedListText.getStatus().equals("1")) {
            switch_.setChecked(true);
        } else {
            switch_.setChecked(false);
        }

        if (expandedListText.getIsVeg() == 0) {
            txtPtype.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nonveg));
        } else if (expandedListText.getIsVeg() == 1) {

            txtPtype.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_veg));

        } else if (expandedListText.getIsVeg() == 2) {
            txtPtype.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_egg));
        }
        Glide.with(context).load(baseUrl + expandedListText.getItemImg()).placeholder(R.drawable.slider).into(imgIcon);

        lvlClicl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ProductActivity.class).putExtra("pid", expandedListText.getId()));
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(expandableListTitle.get(listPosition).getGetTitle())
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        HeaderModel listTitle = (HeaderModel) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        TextView listTotalTitle = (TextView) convertView
                .findViewById(R.id.listTotalTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle.getGetTitle());
        listTotalTitle.setText("Item Total : " + listTitle.getGetSize());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}