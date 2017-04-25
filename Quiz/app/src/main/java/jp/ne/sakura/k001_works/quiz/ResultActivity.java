package jp.ne.sakura.k001_works.quiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static jp.ne.sakura.k001_works.quiz.QuizConstants.*;

/**
 * 結果表示アクティビティクラス
 *
 * @author K
 */
public class ResultActivity extends AppCompatActivity {

    /**
     * onCreateイベント
     *
     * @param savedInstanceState 状態保持情報
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // -------------------------------------------------------
        // 遷移元画面からの情報取得
        // -------------------------------------------------------
        // クイズ画面からの情報を取得
        Intent intent = getIntent();
        int record = intent.getIntExtra(INTENT_KEY_RECORD, 0);
        int recordMax = intent.getIntExtra(INTENT_KEY_RECORD_MAX, 0);

        // -------------------------------------------------------
        // アプリのタイトルバーの設定
        // -------------------------------------------------------
        setTitle(TITLE_NAME_RESULT);

        // -------------------------------------------------------
        // 成績ランク表示
        // -------------------------------------------------------
        // 正答率を算出
        int rateVal = (int)(((float)record / recordMax) * 100);

        // 正答率テキストの設定
        String rateText = RECORD_RATE_UPPER + rateVal + RECORD_RATE_LOWER;

        // 正解数テキストの設定
        String scoreText = recordMax + RECORD_SCORE_UPPER + record + RECORD_SCORE_LOWER;

        // ランク、メッセージ格納用変数宣言
        String rankText;
        String messageText;

        // 正答率が100％の場合
        if (rateVal >= RECORD_RATE_S) {

            rankText = RECORD_RANK_S;
            messageText = RECORD_MSG_S;
        // 正答率が80％以上の場合
        } else if (rateVal >= RECORD_RATE_A) {

            rankText = RECORD_RANK_A;
            messageText = RECORD_MSG_A;

        // 正答率が60％以上の場合
        } else if (rateVal >= RECORD_RATE_B) {

            rankText = RECORD_RANK_B;
            messageText = RECORD_MSG_B;

        // 正答率が40％以上の場合
        } else if (rateVal >= RECORD_RATE_C) {

            rankText = RECORD_RANK_C;
            messageText = RECORD_MSG_C;

        // 正答率が20％以上の場合
        } else if (rateVal >= RECORD_RATE_D) {

            rankText = RECORD_RANK_D;
            messageText = RECORD_MSG_D;

        // 正答率が19％以下の場合
        } else {

            rankText = RECORD_RANK_E;
            messageText = RECORD_MSG_E;
        }

        // -------------------------------------------------------
        // 成績ランク表示
        // -------------------------------------------------------
        // 成績ランク表示用TextViewの取得
        TextView rank = (TextView) findViewById(R.id.rank);

        // 成績ランクの設定
        rank.setText(rankText);

        // -------------------------------------------------------
        // 成績メッセージ表示
        // -------------------------------------------------------
        // 成績メッセージ表示用TextViewの取得
        TextView message = (TextView) findViewById(R.id.message);

        // 成績メッセージの設定
        message.setText(messageText);

        // -------------------------------------------------------
        // 正答率表示
        // -------------------------------------------------------
        // 正答率表示用TextViewの取得
        TextView rate = (TextView) findViewById(R.id.rate);

        // 正答率の設定
        rate.setText(rateText);

        // -------------------------------------------------------
        // 正解数表示
        // -------------------------------------------------------
        // 正解数表示用TextViewの取得
        TextView score = (TextView) findViewById(R.id.score);

        // 正解数の設定
        score.setText(scoreText);

        // -------------------------------------------------------
        // 戻るボタンクリックイベント
        // -------------------------------------------------------
        // 成績表示用TextViewの取得
        Button backBtn = (Button) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // アクティビティ終了
                finish();
            }
        });
    }
}
