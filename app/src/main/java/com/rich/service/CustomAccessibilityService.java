package com.rich.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.rich.domain.ConfigBean;
import com.rich.util.ParseConfigFile;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class CustomAccessibilityService extends AccessibilityService {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // 每3000毫秒执行一次操作
            Map<String, ConfigBean> configBeanMap = ParseConfigFile.getInstance(this);
            new CountDownTimer(2100, 700) {
                public void onTick(long millisUntilFinished) {
                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                    if (rootNode != null) {
                        ConfigBean configBean = configBeanMap.get(rootNode.getPackageName().toString());
                        if (configBean != null) {
                            boolean isExist = false;
                            if (StringUtils.isNotBlank(configBean.getViewId())) {
                                isExist = CollectionUtils.isNotEmpty(rootNode.findAccessibilityNodeInfosByViewId(configBean.getViewId()));
                            }
                            if (StringUtils.isNotBlank(configBean.getViewText())) {
                                isExist = isExist || CollectionUtils.isNotEmpty(rootNode.findAccessibilityNodeInfosByText(configBean.getViewText()));
                            }
                            if (isExist) {
                                clickAtPosition(configBean.getLocation().getX(), configBean.getLocation().getY());
                            }
                        } else if (findAndClickSkipButton(rootNode, "跳过", new int[]{1})) {
                            this.cancel(); // 成功点击后取消计时器
                        }
                    }
                }

                public void onFinish() {
                    // 计时结束后的操作（如果需要的话）
                }
            }.start();
        }
    }

    // 查找并点击包含"跳过"字样的元素
    private boolean findAndClickSkipButton(AccessibilityNodeInfo node, String str, int[] size) {
        if (node == null) return false;
        if (size[0] >= 100) {
            return false;
        }
        if ("android.widget.TextView".contentEquals(node.getClassName())) {
            // 检查当前节点的文本
            CharSequence text = node.getText();
            if (StringUtils.contains(text, str) && text.length() < 7) {
                if (node.isClickable()) {
                    // 模拟点击
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else {
                    Rect rect = new Rect();
                    node.getBoundsInScreen(rect);
                    clickAtPosition((rect.left + rect.right) / 2, (rect.top + rect.bottom) / 2);
                }
                return true; // 找到并点击后直接返回
            }
        }
        // 递归查找子节点
        for (int i = 0; i < node.getChildCount(); i++) {
            size[0]++;
            if (findAndClickSkipButton(node.getChild(i), str, size)) return true;
        }
        return false;
    }


    @Override
    public void onInterrupt() {
        // 服务中断时的处理
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }


    private void clickAtPosition(int x, int y) {
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(path, 0, 1));
        dispatchGesture(gestureBuilder.build(), null, null);
    }




}
