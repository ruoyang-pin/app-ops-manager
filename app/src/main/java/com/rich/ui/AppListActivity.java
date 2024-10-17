package com.rich.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.rich.R;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        // 获取 PackageManager 实例
        PackageManager packageManager = getPackageManager();

        // 创建用于显示应用名的列表
        List<String> appNames = new ArrayList<>();
        List<ResolveInfo> resolveInfoList;

        // 使用 Intent 查询所有主启动活动的应用
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveInfoList = packageManager.queryIntentActivities(intent, 0);

        // 添加应用信息到列表
        for (ResolveInfo resolveInfo : resolveInfoList) {
            String appLabel = resolveInfo.loadLabel(packageManager).toString();
            appNames.add(appLabel);  // 添加应用名
        }

        // 使用 ListView 显示应用
        ListView listView = findViewById(R.id.app_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appNames);
        listView.setAdapter(adapter);

        // 添加搜索框
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 根据输入的文本过滤应用列表
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        // 处理点击事件，获取选中应用的包名
        listView.setOnItemClickListener((parent, view, position, id) -> {
            ResolveInfo selectedResolveInfo = resolveInfoList.get(position);
            String packageName = selectedResolveInfo.activityInfo.packageName;
            // 启动 AppPermissionsActivity，并传递包名
            Intent appPermissionsIntent = new Intent(AppListActivity.this, AppPermissionsActivity.class);
            appPermissionsIntent.putExtra("PACKAGE_NAME", packageName);
            startActivity(appPermissionsIntent);
        });

    }
}


