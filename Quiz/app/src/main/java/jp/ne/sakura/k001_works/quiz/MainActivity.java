package jp.ne.sakura.k001_works.quiz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static jp.ne.sakura.k001_works.quiz.QuizConstants.*;

/**
 * メインアクティビティクラス
 *
 * @author K
 */
public class MainActivity extends AppCompatActivity {

    /** アダプター */
    private ArrayAdapter arrayAdapter;

    /**
     * onCreateイベント
     *
     * @param savedInstanceState 状態保持情報
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // アダプタ生成
        arrayAdapter = new ArrayAdapter(this, R.layout.list_item);

        // listViewの取得
        ListView listView = (ListView) findViewById(R.id.listView);

        // アダプタの項目追加ループ
        for (int i = 0; i < quizKindName.length; i++) {

            // 追加
            arrayAdapter.add(quizKindName[i]);
        }

        // アダプタをListViewに設定
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplication(), PlayActivity.class);
                intent.putExtra("quizKind", position);
                startActivity(intent);
            }
        });
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

            // 確認ダイアログ表示
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(DIALOG_TITLE_CONFIRM);
            alertDialogBuilder.setMessage(DIALOG_MSG_CONFIRM);

            // ポジティブボタン押下時イベント
            alertDialogBuilder.setPositiveButton(DIALOG_BUTTON_POSITIVE_,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // 終了
                            finish();
                        }
                    });

            // ネガティブボタン押下時イベント
            alertDialogBuilder.setNegativeButton(DIALOG_BUTTON_NEGATIVE_,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

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
}
