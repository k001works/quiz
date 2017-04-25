package jp.ne.sakura.k001_works.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static jp.ne.sakura.k001_works.quiz.QuizConstants.*;

/**
 * クイズアクティビティクラス
 *
 * @author K
 */
public class PlayActivity extends AppCompatActivity {

    /** 回答数 */
    private int count = 0;
    /** 成績(正解数) */
    private int record = 0;
    /** 回答状態 */
    private int ansState = ANSWER_STATUS_INIT;
    /** キャンセルフラグ(一時停止判定用) */
    private boolean cancelFlg = false;
    /** タイマーカウンタ */
    private int timerCount = 0;

    /** アダプタ */
    private ArrayAdapter arrayAdapter;
    /** クイズ情報 */
    private List<Quiz>[] quiz;
    /** 出題中クイズ */
    private List<Quiz> playQuiz;
    /** ダイアログレイアウト */
    private View dialogLayout;
    /** ダイアログ */
    private AlertDialog dialog;
    /** プログレスバー */
    private ProgressBar progressBar;
    /** タイマータスク */
    private ProgressBarTimer timerTask;
    /** タイマー */
    private Timer timer;
    /** ハンドラ */
    private Handler handler = new Handler();

    /**
     * onCreateイベント
     *
     * @param savedInstanceState 状態保持情報
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // -------------------------------------------------------
        // クイズの初期化処理
        // -------------------------------------------------------
        quizInit();

        // -------------------------------------------------------
        // 表示する問題の取り出し
        // -------------------------------------------------------
        // 回答回数に合わせた問題情報を取得
        Quiz q = playQuiz.get(count);

        // -------------------------------------------------------
        // 回答数表示
        // -------------------------------------------------------
        dispCount(getCount() + 1);

        // -------------------------------------------------------
        // 問題文表示
        // -------------------------------------------------------
        dispQuestion(q.getQuestion());

        // -------------------------------------------------------
        // 問題の選択肢表示
        // -------------------------------------------------------
        dispChoice(q.getChoice());

        // -------------------------------------------------------
        // プログレスバーの表示
        // -------------------------------------------------------
        // タイマー初期化/スタート
        timerInit();
        setTimerCount(0);
        timerStart();

        // プログレスバーの設定
        progressBar = (ProgressBar) findViewById(R.id.timer);
        progressBar.setMax(PROGRESS_NUM_MAX);
        progressBar.setProgress(PROGRESS_NUM_INIT);
    }

    /**
     * onResumeイベント
     */
    @Override
    protected void onResume() {

        // タイマーが回答中かつ、キャンセル中の場合(一時停止中)
        if ((getAnsState() == ANSWER_STATUS_ANSWER) && isCancelFlg()) {

            // タイマー再開
            timerStart();
        }
        super.onResume();
    }

    /**
     * onPauseイベント
     */
    @Override
    protected void onPause() {

        // タイマー一時停止
        timerPause();
        super.onPause();
    }

    /**
     * キー押下イベント
     *
     * @param keyCode キー種別
     * @param event イベント種別
     * @return 処理結果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 戻るボタンの場合
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            // タイマー一時停止
            timerPause();

            // 確認ダイアログ表示
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("確認");
            alertDialogBuilder.setMessage("この画面から移動しますか？\n回答中の成績は保存されません。");
            alertDialogBuilder.setPositiveButton("はい",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // タイマ停止
                            timerStop();

                            // 終了
                            finish();
                        }
                    });
            alertDialogBuilder.setNegativeButton("いいえ",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // タイマー再開
                            timerStart();
                        }
                    });

            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return false;

            // 戻るボタン以外
        } else {

            // そのまま処理
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * クイズ初期化
     */
    private void quizInit() {

        // -------------------------------------------------------
        // 初期化処理
        // -------------------------------------------------------
        // 回答数の初期化
        setCount(0);

        // 成績の初期化
        setRecord(0);

        // クイズ情報
        quiz = new List[4];

        // -------------------------------------------------------
        // 遷移元画面からの情報受け取り
        // -------------------------------------------------------
        // クイズ種別情報を取得
        Intent intent = getIntent();
        int quizKind = intent.getIntExtra("quizKind", 0);


        // -------------------------------------------------------
        // アプリのタイトルバーの設定
        // -------------------------------------------------------
        if ((quizKind >= QUIZ_KIND_NUM00) && (quizKind <= QUIZ_KIND_NUM03)) {

            // 実行中のクイズジャンルを設定
            setTitle(quizKindName[quizKind]);

        } else {

            // アプリ名を設定
            setTitle(getResources().getString(R.string.app_name));
        }

        // -------------------------------------------------------
        // 回答状態の初期化
        // -------------------------------------------------------
        setAnsState(ANSWER_STATUS_INIT);

        // -------------------------------------------------------
        // 問題生成
        // -------------------------------------------------------
        // 問題生成
        createQuiz();

        // 出題内容をシャッフル
        playQuiz = shuffleList(quiz[quizKind]);

        // -------------------------------------------------------
        // ダイアログ生成
        // -------------------------------------------------------
        createDialog();
    }

    /**
     * 回答数表示
     *
     * @param countNum　回答数
     */
    private void dispCount(int countNum) {

        // 回答数表示用のTextViewの取得
        TextView text = (TextView) findViewById(R.id.state);

        // 残り問題数を表示
        text.setText(countNum + "/" + COUNT_NUM_MAX);
    }

    /**
     * 問題文表示
     *
     * @param questionText 問題文
     */
    private void dispQuestion(String questionText) {

        // 問題表示用TextViewの取得
        TextView question = (TextView) findViewById(R.id.question);

        // 問題文の設定
        question.setText(questionText);
    }

    /**
     * 選択肢表示
     *
     * @param choice 選択肢情報
     */
    private void dispChoice(String[] choice) {

        // -------------------------------------------------------
        // 選択肢用のlistView設定
        // -------------------------------------------------------
        // アダプタ生成
        arrayAdapter = new ArrayAdapter(this, R.layout.list_item);

        // 選択肢用のlistViewの取得
        ListView listView = (ListView) findViewById(R.id.quizListView);

        // アダプタの項目追加ループ
        for (String choiceItem : choice) {

            // クイズの選択肢をアダプタに追加
            arrayAdapter.add(choiceItem);
        }

        // アダプタをListViewに設定
        listView.setAdapter(arrayAdapter);

        // -------------------------------------------------------
        // 問題の選択肢クリック時処理
        // -------------------------------------------------------
        // 選択肢のクリックリスナー
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // 選択肢クリック処理
                onClickChoice(position);
            }
        });
    }

    /**
     * 問題の選択肢クリック時処理
     *
     * @param position クリック位置情報
     */
    private void onClickChoice(int position) {

        // -------------------------------------------------------
        // 正解判定
        // -------------------------------------------------------
        // 選択肢情報取得
        String[] choice = playQuiz.get(getCount()).getChoice();

        // ダイアログのTextView取得
        TextView result = (TextView)dialogLayout.findViewById(R.id.result);
        TextView msg = (TextView)dialogLayout.findViewById(R.id.message);

        // 正解の場合
        if (choice[position].equals(playQuiz.get(getCount()).getAnswer())) {

            // ダイアログにテキスト設定
            result.setText("正解");

            // 成績更新
            addRecord();

        // 不正解の場合
        } else {

            // ダイアログにテキスト設定
            result.setText("不正解");
        }

        // 回答状態を回答済み状態に更新
        setAnsState(ANSWER_STATUS_FINISHED);

        // タイマー停止
        timerStop();

        // -------------------------------------------------------
        // 結果をダイアログ表示
        // -------------------------------------------------------
        // 解説を設定
        msg.setText("正解：" + playQuiz.get(getCount()).getAnswer());

        // ダイアログ表示
        dialog.show();
    }

    /**
     * 次のクイズへ遷移
     */
    private void nextQuiz() {

        // 最後の問題の場合
        if (getCount() >= (COUNT_NUM_MAX - 1)) {

            // -------------------------------------------------------
            // 結果画面へ遷移
            // -------------------------------------------------------
            Intent intent = new Intent(getApplication(), ResultActivity.class);
            intent.putExtra("record", getRecord());
            intent.putExtra("recordMax", COUNT_NUM_MAX);
            startActivity(intent);

            // アクティビティ終了
            finish();

        // 最後の問題以外の場合
        } else {

            // -------------------------------------------------------
            // 回答数更新
            // -------------------------------------------------------
            // 回答回数をインクリメント
            addCount();

            // -------------------------------------------------------
            // 次の問題表示
            // -------------------------------------------------------
            // クイズ表示更新
            renewQuiz(getCount());
        }
    }

    /**
     * クイズの表示更新
     *
     * @param countNum 回答回数
     */
    private void renewQuiz(int countNum) {

        // -------------------------------------------------------
        // 表示する問題の取り出し
        // -------------------------------------------------------
        // 回答回数に合わせた問題情報を取得
        Quiz q = playQuiz.get(countNum);

        // -------------------------------------------------------
        // 回答数表示
        // -------------------------------------------------------
        dispCount(getCount() + 1);

        // -------------------------------------------------------
        // 問題文表示
        // -------------------------------------------------------
        dispQuestion(q.getQuestion());

        // -------------------------------------------------------
        // 問題の選択肢表示
        // -------------------------------------------------------
        dispChoice(q.getChoice());

        // -------------------------------------------------------
        // プログレスバーの表示
        // -------------------------------------------------------
        // タイマースタート
        setTimerCount(0);
        timerStart();

        // プログレスバーの設定
        progressBar = (ProgressBar) findViewById(R.id.timer);
        progressBar.setMax(PROGRESS_NUM_MAX);
        progressBar.setProgress(PROGRESS_NUM_INIT);
    }

    /**
     * クイズの生成
     */
    private void createQuiz() {

        // CSVからクイズ情報の読込
        ReaderCSV readCsv = new ReaderCSV();
        quiz = readCsv.ReadCSV(this, "quiz.csv");
    }

    /**
     * 指定配列の文字列を並べ替えてリストとして返却
     *
     * @param list 並び替えたい配列情報
     * @return 並び替えたリスト情報
     */
    private List<Quiz> shuffleList(List<Quiz> list) {

        //　シャッフル
        List<Quiz> shuffle = new ArrayList<>(list);
        Collections.shuffle(shuffle);
        return shuffle;
    }

    /**
     * ダイアログ生成
     */
    private void createDialog() {

        // カスタムビューを設定
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        dialogLayout = inflater.inflate(R.layout.layout_dialog, (ViewGroup) findViewById(R.id.diaogLayout));

        // ダイアログ生成
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setView(dialogLayout);
        builder.setCancelable(false);
        Button nextBtn = (Button)dialogLayout.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 閉じる
                dialog.dismiss();

                // 次のクイズへ切替
                nextQuiz();
            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

    }

    /**
     * タイマー初期化
     */
    private void timerInit() {

        // タイマー初期化(稼働中の場合は止める）
        if(timer != null){

            timer.cancel();
            timer = null;
            timerTask = null;
        }

        // キャンセルフラグを初期化
        setCancelFlg(false);

        // タイマータスクインスタンスを作成
        timerTask = new ProgressBarTimer();

        // カウンタを初期化して設定
        setTimerCount(0);
    }

    /**
     * タイマースタート
     */
    private void timerStart() {

        // タイマー起動中
        if((getTimerCount() > 0) && (timer != null)){

            // そのまま終了
            return;
        }

        // キャンセルフラグを初期化
        setCancelFlg(false);

        // タイマータスクがnullでない場合
        if (timerTask == null) {

            // タイマータスクインスタンスを作成
            timerTask = new ProgressBarTimer();
        }

        // 回答状態を回答中状態に更新
        setAnsState(ANSWER_STATUS_ANSWER);

        // タイマースケジュールを設定
        timer = new Timer();
        timer.schedule(timerTask, 0, TIMER_NUM_INTERVAL);
    }

    /**
     * タイマー一時停止
     */
    private void timerPause() {

        // キャンセルフラグをON
        setCancelFlg(true);

        // タイマー初期化
        if(timer != null){

            timer.cancel();
            timer = null;
            timerTask = null;
        }
    }

    /**
     * タイマー停止
     */
    private void timerStop() {

        // キャンセルフラグを初期化
        setCancelFlg(false);

        // タイマー初期化
        if(timer != null){

            timer.cancel();
            timer = null;
            timerTask = null;
        }

        // 回答状態を完了状態に更新
        setAnsState(ANSWER_STATUS_FINISHED);

        // カウンタを初期化
        setTimerCount(0);

        // プログレスバーの初期化
        progressBar.setProgress(PROGRESS_NUM_INIT);
    }


    /**
     * タイマータスククラス(プログレスバー用)
     */
    class ProgressBarTimer extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {

                    // タイマーがnullの場合、即終了
                    if (timer == null) {
                        return;
                    }

                    // タイマーカウントを更新(インクリメント）
                    setTimerCount(getTimerCount() + 1);

                    // プログレスバーの進捗状況を更新
                    progressBar.setProgress(timerCount);

                    // 制限時間の場合
                    if (getTimerCount() >= progressBar.getMax()) {

                        // タイマー初期化
                        if(timer != null){
                            timer.cancel();
                            timer = null;
                            timerTask = null;
                        }

                        // 回答状態を回答済み状態に更新
                        setAnsState(ANSWER_STATUS_FINISHED);

                        // -------------------------------------------------------
                        // 時間切れをダイアログ表示(不正解)
                        // -------------------------------------------------------
                        TextView result = (TextView)dialogLayout.findViewById(R.id.result);
                        TextView msg = (TextView)dialogLayout.findViewById(R.id.message);
                        result.setText("不正解");
                        msg.setText("正解：" + playQuiz.get(getCount()).getAnswer());
                        dialog.show();
                    }
                }
            });
        }
    }

    /**
     * 回答数の設定
     *
     * @param count 回答数
     */
    private void setCount(int count) {
        this.count = count;
    }

    /**
     * 回答数の更新(インクリメント)
     */
    private void addCount() {
        this.count++;
    }

    /**
     * 回答数の取得
     *
     * @return 回答数
     */
    private int getCount() {
        return this.count;
    }

    /**
     * 成績の設定
     *
     * @param record 成績
     */
    private void setRecord(int record) {
        this.record = record;
    }

    /**
     * 成績の更新(インクリメント)
     */
    private void addRecord() {
        this.record++;
    }

    /**
     * 成績の取得
     *
     * @return 成績
     */
    private int getRecord() {
        return this.record;
    }

    /**
     * 回答状態の取得
     *
     * @return 回答状態
     */
    public int getAnsState() {
        return this.ansState;
    }

    /**
     * キャンセルフラグ(一時停止)の状態取得
     *
     * @return キャンセルフラグ状態
     */
    public boolean isCancelFlg() {
        return this.cancelFlg;
    }

    /**
     * タイマーカウント値の取得
     *
     * @return タイマーカウント値
     */
    public int getTimerCount() {
        return this.timerCount;
    }

    /**
     * 回答状態の設定
     *
     * @param ansState 回答状態
     */
    public void setAnsState(int ansState) {
        this.ansState = ansState;
    }

    /**
     * キャンセルフラグ(一時停止)の設定
     *
     * @param cancelFlg キャンセルフラグ
     */
    public void setCancelFlg(boolean cancelFlg) {
        this.cancelFlg = cancelFlg;
    }

    /**
     * タイマーカウント値の設定
     *
     * @param timerCount タイマーカウント値
     */
    public void setTimerCount(int timerCount) {
        this.timerCount = timerCount;
    }
}
