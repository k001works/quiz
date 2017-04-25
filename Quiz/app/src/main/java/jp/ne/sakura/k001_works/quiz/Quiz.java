package jp.ne.sakura.k001_works.quiz;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * クイズ情報クラス
 */
public class Quiz {

    // 定数
    /** クイズ選択肢の最大数 */
    private final static int QUIZ_NUM_MAX = 4;

    /** ID */
    private int id;
    /** ジャンル */
    private int kind;
    /** 問題 */
    private String question;
    /** 選択肢 */
    private String[] choice = new String[QUIZ_NUM_MAX];
    /** 解答 */
    private String answer;
    /** 解説 */
    private String comment;

    /**
     * コンストラクタ
     */
    public Quiz(int id, int kind, String question, String[] choice, String answer, String comment) {

        // 問題情報を設定
        this.id = id;
        this.kind = kind;
        this.question = question;
        this.choice = shuffleList(choice);
        this.answer = answer;
        this.comment = comment;
    }

    /**
     * IDの取得
     *
     * @return 取得したID
     */
    public int getId() {
        return id;
    }

    /**
     * ジャンルの取得
     *
     * @return 取得したジャンル
     */
    public int getKind() {
        return kind;
    }

    /**
     * 問題の取得
     *
     * @return 取得した問題
     */
    public String getQuestion() {
        return this.question;
    }

    /**
     * 選択肢の取得
     *
     * @return 取得した選択肢
     */
    public String[] getChoice() {
        return this.choice;
    }

    /**
     * 解答の取得
     *
     * @return 取得した解答
     */
    public String getAnswer() {
        return this.answer;
    }

    /**
     * 解説の取得
     *
     * @return 取得した解説
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * 指定配列の文字列を並べ替えて返却
     *
     * @param list 並び替えたい配列情報
     * @return 並び替えた配列情報
     */
    private String[] shuffleList(String[] list) {

        //　配列の項目をシャッフル
        List<String> shuffleList = Arrays.asList(list.clone());
        Collections.shuffle(shuffleList);
        String[] shuffle = shuffleList.toArray(new String[shuffleList.size()]);
        return shuffle;
    }
}
