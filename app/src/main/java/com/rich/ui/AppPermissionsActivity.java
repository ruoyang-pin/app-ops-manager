package com.rich.ui;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.rich.Adapter.PermissionAdapter;
import com.rich.R;

import java.util.ArrayList;
import java.util.List;

public class AppPermissionsActivity extends AppCompatActivity {

    private final List<String> permissionsList = new ArrayList<>();

    private final List<String> permissionsStatus = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_app_permissions);
            ListView permissionsListView = findViewById(R.id.permissions_list_view);

            // 获取传递的包名
            String packageName = getIntent().getStringExtra("PACKAGE_NAME");

            // 获取应用的权限列表
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = packageInfo.requestedPermissions;

            // 将权限添加到列表及其状态
            if (requestedPermissions != null) {
                for (String permission : requestedPermissions) {
                    permissionsList.add(permission);
                    permissionsStatus.add(isPermissionGranted(packageName, permission) ? "已启用" : "已禁用");
                }
            }

            // 使用自定义适配器显示权限和状态
            PermissionAdapter adapter = new PermissionAdapter(this, permissionsList, permissionsStatus);
            permissionsListView.setAdapter(adapter);

            // 处理点击事件，弹出启用/禁用对话框
            permissionsListView.setOnItemClickListener((parent, view, position, id) -> {
                String selectedPermission = permissionsList.get(position);
                String selectedStatus = permissionsStatus.get(position);
                showPermissionDialog(selectedPermission, packageName, selectedStatus);
            });
        } catch (Exception e) {
            Toast.makeText(this, "发生错误" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPermissionGranted(String packageName, String permission) {
        PackageManager pm = getPackageManager();
        return pm.checkPermission(permission, packageName) == PackageManager.PERMISSION_GRANTED;
    }

    private void showPermissionDialog(String permission, String packageName, String currentStatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择操作");
        builder.setMessage("当前状态: " + currentStatus + "\n是否要更改权限: " + permission);
        builder.setPositiveButton("启用", (dialog, which) -> {
            requestPermission(permission);
        });
        builder.setNegativeButton("禁用", (dialog, which) -> {
            openAppSettings();
        });
        builder.setNeutralButton("取消", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void requestPermission(String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{permission}, 1);
        } else {
            Toast.makeText(this, "权限已启用: " + permission, Toast.LENGTH_SHORT).show();
        }
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

}
