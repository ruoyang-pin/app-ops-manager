package com.rich.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PermissionAdapter extends ArrayAdapter<String> {
    private final List<String> permissionsStatus;

    public PermissionAdapter(Context context, List<String> permissions, List<String> statuses) {
        super(context, android.R.layout.simple_list_item_1, permissions);
        this.permissionsStatus = statuses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        String permission = getItem(position);
        String status = permissionsStatus.get(position);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(String.format("%s%s%s%s", permission, " (", status, ")"));  // 显示权限及其状态
        return view;
    }
}
