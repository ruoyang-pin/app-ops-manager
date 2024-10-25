package com.rich.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.rich.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AppListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        // 获取 PackageManager 实例
        PackageManager packageManager = getPackageManager();

        // 使用 Intent 查询所有主启动活动的应用
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

        Map<String, ResolveInfo> resolveInfoMap = resolveInfoList.stream()
                .collect(Collectors.toMap(f -> f.loadLabel(packageManager).toString(), Function.identity(), (x, y) -> x));

        //使用 ListView 显示应用
        ListView listView = findViewById(R.id.app_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>(resolveInfoMap.keySet()));
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
            String name = adapter.getItem(position);
            ResolveInfo selectedResolveInfo = resolveInfoMap.get(name);
            if (selectedResolveInfo == null) {
                return;
            }
            String packageName = selectedResolveInfo.activityInfo.packageName;
            openOtherApp(packageName);
        });
    }

    public void openOtherApp(String packageName) {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            // 处理未找到应用的情况
            Toast.makeText(this, "应用未安装", Toast.LENGTH_SHORT).show();
        }
    }

}


