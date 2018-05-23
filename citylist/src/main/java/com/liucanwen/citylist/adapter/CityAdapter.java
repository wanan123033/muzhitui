package com.liucanwen.citylist.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


import com.liucanwen.citylist.R;
import com.liucanwen.citylist.widget.ContactItemInterface;
import com.liucanwen.citylist.widget.ContactListAdapter;

import java.util.List;

public class CityAdapter extends ContactListAdapter {
    private String cityName;

    public CityAdapter(Context _context, int _resource,
                       List<ContactItemInterface> _items, String cityName) {

        super(_context, _resource, _items);
        this.cityName = cityName;
    }

    public void populateDataForRow(View parentView, ContactItemInterface item,
                                   int position) {
        View infoView = parentView.findViewById(R
                .id.infoRowContainer);
        TextView nicknameView = (TextView) infoView
                .findViewById(R.id.cityName);
        TextView tvCheck = (TextView) infoView
                .findViewById(R.id.tvCheck);
        if (item.getDisplayInfo().equals(cityName)) {
            tvCheck.setVisibility(View.VISIBLE);
        } else {
            tvCheck.setVisibility(View.GONE);
        }
        nicknameView.setText(item.getDisplayInfo());
    }

}
