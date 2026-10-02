package com.keyence.bttokyu.scanner;

import android.content.Context;
import com.keyence.bttokyu.util.FileLogger;

/**
 * X-N10 ScanSettingsのキーストローク出力をアプリへ渡すラッパー。
 */
public class ScannerManager {

    private static final String TAG = "BtScanner";
    public static final String INTENT_ACTION   = "com.keyence.bttokyu.SCAN_DATA";
    public static final String INTENT_KEY_DATA = "data";

    @FunctionalInterface
    public interface ScanCallback {
        void onScanData(String rawData);
    }

    private final Context appContext;
    private ScanCallback callback;
    private boolean isEnabled;

    public ScannerManager(Context context) {
        this.appContext = context.getApplicationContext();
        FileLogger.getInstance(appContext).section("ScannerManager init");
        FileLogger.getInstance(appContext).log("Scanner", "X-N10 ScanSettings key input mode");
    }

    public void setCallback(ScanCallback cb) { this.callback = cb; }

    /** enable(): サンプルの onResume 相当 */
    public void enable() {
        isEnabled = true;
    }

    /** disable(): サンプルの onPause 相当 */
    public void disable() {
        isEnabled = false;
    }

    public void release() {
        isEnabled = false;
        callback = null;
    }

    /** ウェジェット経由のスキャンデータ配信 (DDモデルフォールバック) */
    public void deliverWedgeScan(String data) {
        FileLogger.getInstance(appContext).log("Wedge",
            "deliverWedgeScan: [" + data + "] callback=" + (callback!=null?"set":"NULL"));
        if (isEnabled && callback != null && data != null && !data.isEmpty()) {
            callback.onScanData(data.trim());
        }
    }

    public boolean isEnabled() { return isEnabled; }
}
