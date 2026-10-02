package com.keyence.bttokyu.hardware;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;

/**
 * KEYENCE BT-A シリーズ ブザー制御ラッパー。
 *
 * リファレンス: BTA2000_1000Reference.md — Notification クラス
 *   startBuzzer(tone, onPeriod, offPeriod, repeatCount)
 *     tone        : 1〜16（音のトーン）
 *     onPeriod    : 1〜5000ms（鳴動時間）
 *     offPeriod   : 1〜5000ms（消音時間）
 *     repeatCount : 1〜10（繰り返し回数）
 *
 * 使い分け:
 *   beepOk()    → 正常完了: 短い1音 (高め)
 *   beepError() → エラー: 3連短音 (低め) ← DBエラー・スキャンNG時
 *   beepAlert() → 警告: 2回長音 (中程度)
 */
public class BuzzerHelper {

    private static final String TAG = "BuzzerHelper";

    // ブザートーン番号（1〜16: 数値が大きいほど高音）
    private static final int TONE_OK    = 12;   // 高め：正常
    private static final int TONE_ERROR = 4;    // 低め：エラー
    private static final int TONE_ALERT = 8;    // 中程度：警告

    private final ToneGenerator toneGenerator;

    public BuzzerHelper(Context context) {
        ToneGenerator generator = null;
        try {
            generator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
        } catch (Exception e) {
            android.util.Log.e(TAG, "BuzzerHelper init error", e);
        }
        toneGenerator = generator;
    }

    /**
     * 正常完了音: 短い1音（高め）
     * 読取成功・登録完了など
     */
    public void beepOk() {
        beep(TONE_OK, 150, 0, 1);
    }

    /**
     * エラー音: 3連短音（低め）
     * DB通信エラー・スキャンNG・品番不一致など
     */
    public void beepError() {
        beep(TONE_ERROR, 200, 100, 3);
    }

    /**
     * 警告音: 2回長音（中程度）
     * 保留品・前工程未完了など注意が必要な場合
     */
    public void beepAlert() {
        beep(TONE_ALERT, 400, 150, 2);
    }

    /** ブザー停止 */
    public void stop() {
        if (toneGenerator != null) toneGenerator.stopTone();
    }

    private void beep(int tone, int onMs, int offMs, int repeat) {
        if (toneGenerator != null) toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, onMs);
    }

    /** リソース解放（MainActivity.onDestroy() で呼ぶ） */
    public void release() {
        if (toneGenerator != null) toneGenerator.release();
    }
}
