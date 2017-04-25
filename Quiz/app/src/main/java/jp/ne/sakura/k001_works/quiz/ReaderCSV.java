package jp.ne.sakura.k001_works.quiz;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * クイズ定数クラス
 *
 * @author K
 */
public class ReaderCSV {

    /**
     * CSVファイルから情報読み込みし、クイズ情報をリストに格納し返却
     *
     * @param activity アクティビティ
     * @param fileName ファイル名
     * @return クイズ情報リスト(CSV読込)
     */
    static public List<Quiz>[] ReadCSV(Activity activity, String fileName) {

        // ファイル読込用変数宣言
        InputStream is = null;
        BufferedReader br = null;
        List<Quiz>[] quizList = new List[4];

        // 返却値の初期化
        for (int i = 0; i < 4; i++) {
            quizList[i] = new ArrayList<>();
        }

        try {
            try {
                // assetsフォルダ内のCSVファイルをオープン
                is = activity.getAssets().open(fileName);
                br = new BufferedReader(new InputStreamReader(is));

                //　1行目はタイトルなので読み捨て
                br.readLine();

                // 行単位読み込みループ
                String line;
                while ((line = br.readLine()) != null) {

                    // カンマ区切りの値を分割
                    StringTokenizer st = new StringTokenizer(line, ",");

                    // クイズ情報に設定
                    Quiz q = new Quiz(
                            Integer.parseInt(st.nextToken().trim()),
                            Integer.parseInt(st.nextToken().trim()),
                            st.nextToken().trim(),
                            new String[]{
                                    st.nextToken().trim(),
                                    st.nextToken().trim(),
                                    st.nextToken().trim(),
                                    st.nextToken().trim(),
                            },
                            st.nextToken().trim(),
                            st.nextToken().trim()
                    );

                    // クイズリストに追加
                    quizList[q.getKind()].add(q);
                }

            } catch (Exception e) {

                e.printStackTrace();


            } finally {

                // クローズ処理
                if (is != null) {
                    is.close();
                }
                if (br != null) {
                    br.close();
                }
            }

        // エラー発生時
        } catch (Exception e){

            e.printStackTrace();
        }

        return quizList;
    }
}
