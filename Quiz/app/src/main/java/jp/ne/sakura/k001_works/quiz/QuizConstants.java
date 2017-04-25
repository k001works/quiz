package jp.ne.sakura.k001_works.quiz;

/**
 * クイズ定数クラス
 *
 * @author K
 */
public class QuizConstants {

    // -------------------------------------------------------
    // メインアクティビティ用定数
    // -------------------------------------------------------
    /** クイズジャンル値(ジャンル0) */
    public static final int QUIZ_KIND_NUM00 = 0;
    /** クイズジャンル値(ジャンル1) */
    public static final int QUIZ_KIND_NUM01 = 1;
    /** クイズジャンル値(ジャンル2) */
    public static final int QUIZ_KIND_NUM02 = 2;
    /** クイズジャンル値(ジャンル3) */
    public static final int QUIZ_KIND_NUM03 = 3;
    /** クイズジャンル名(ジャンル0) */
    public static final String QUIZ_KIND_NAME00 = "雑学クイズ(生活雑学)";
    /** クイズジャンル名(ジャンル1) */
    public static final String QUIZ_KIND_NAME01 = "雑学クイズ(動物雑学)";
    /** クイズジャンル名(ジャンル2) */
    public static final String QUIZ_KIND_NAME02 = "雑学クイズ(食べ物雑学)";
    /** クイズジャンル名(ジャンル3) */
    public static final String QUIZ_KIND_NAME03 = "雑学クイズ(歴史雑学)";
    /** クイズジャンル名 */
    public static final String[] quizKindName = {QUIZ_KIND_NAME00, QUIZ_KIND_NAME01, QUIZ_KIND_NAME02, QUIZ_KIND_NAME03};

    /** タイトル(結果画面用) */
    public final static String DIALOG_TITLE_CONFIRM = "確認";
    public final static String DIALOG_MSG_CONFIRM = "アプリケーションを終了しますか？";
    public final static String DIALOG_BUTTON_POSITIVE_ = "はい";
    public final static String DIALOG_BUTTON_NEGATIVE_ = "いいえ";

    // -------------------------------------------------------
    // クイズアクティビティ用定数
    // -------------------------------------------------------
    /** タイマー周期 */
    public static final int TIMER_NUM_INTERVAL = 10;

    /** プログレスバー初期値 */
    public static final int PROGRESS_NUM_INIT = 0;
    /** プログレスバー最大値 */
    public static final int PROGRESS_NUM_MAX = 2000;

    /** 回答最大数(出題数) */
    public static final int COUNT_NUM_MAX = 5;

    // 回答状態
    /** 回答状態(初期状態) */
    public static final int ANSWER_STATUS_INIT = -1;
    /** 回答状態(回答中状態) */
    public static final int ANSWER_STATUS_ANSWER = 0;
    /** 回答状態(回答済み状態) */
    public static final int ANSWER_STATUS_FINISHED = 1;

    // -------------------------------------------------------
    // 結果表示アクティビティ用定数
    // -------------------------------------------------------
    // インテントキー
    /** インテントキー(正答数) */
    public final static String INTENT_KEY_RECORD = "record";
    /** インテントキー(正答数最大値) */
    public final static String INTENT_KEY_RECORD_MAX = "recordMax";

    // タイトルバー表示用
    /** タイトル(結果画面用) */
    public final static String TITLE_NAME_RESULT = "成績発表";

    // 成績表示用
    /** ランクS */
    public final static String RECORD_RANK_S = "Sランク";
    /** ランクA */
    public final static String RECORD_RANK_A = "Aランク";
    /** ランクB */
    public final static String RECORD_RANK_B = "Bランク";
    /** ランクC */
    public final static String RECORD_RANK_C = "Cランク";
    /** ランクD */
    public final static String RECORD_RANK_D = "Dランク";
    /** ランクE */
    public final static String RECORD_RANK_E = "Eランク";

    /** メッセージ(ランクS用) */
    public final static String RECORD_MSG_S = "全問正解！よくできました！";
    /** メッセージ(ランクA用) */
    public final static String RECORD_MSG_A = "おめでとうございます！\n次はSランクを目指しましょう。";
    /** メッセージ(ランクB用) */
    public final static String RECORD_MSG_B = "よくできました！\n次はAランクを目指しましょう。";
    /** メッセージ(ランクC用) */
    public final static String RECORD_MSG_C = "お疲れさまでした。\n次はBランクを目指しましょう。";
    /** メッセージ(ランクD用) */
    public final static String RECORD_MSG_D = "残念！次からはもう少し頑張って下さい。";
    /** メッセージ(ランクE用) */
    public final static String RECORD_MSG_E = "全然だめです。もっと精進しましょう。";

    /** 正答数(上部) */
    public final static String RECORD_SCORE_UPPER = "問中";
    /** 正答数(下部) */
    public final static String RECORD_SCORE_LOWER = "問正解";
    /** 正答率(上部) */
    public final static String RECORD_RATE_UPPER = "正答率 ";
    /** 正答率(下部) */
    public final static String RECORD_RATE_LOWER = "%";

    // 正答率表示用
    /** 正答率判定値(Sランク) */
    public final static int RECORD_RATE_S = 100;
    /** 正答率判定値(Sランク) */
    public final static int RECORD_RATE_A = 80;
    /** 正答率判定値(Sランク) */
    public final static int RECORD_RATE_B = 60;
    /** 正答率判定値(Sランク) */
    public final static int RECORD_RATE_C = 40;
    /** 正答率判定値(Sランク) */
    public final static int RECORD_RATE_D = 20;

    /** コンストラクタ */
    private QuizConstants() {}
}
