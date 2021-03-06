package com.gh.mygreen.xlsmapper.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 処理をする際のヒントを指定するためのアノテーションです。
 * 
 * 
 * @author T.TSUCHIE
 *
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XlsHint {
    
    /**
     * オブジェクト内でのフィールドの処理順序を定義します。
     * 
     * <p>セルに対するマッピング情報を指定したアノテーションが付与された、
     *    フィールドまたはメソッドの処理順序は、Javaの実行環境に依存するため、
     *    処理順序を一定にしたいときに付与します。
     * </p>
     * <p>そのため、書き込み時に、アノテーション{@link XlsHorizontalRecords}などでマッピングし、
     *    行の追加、削除を伴う操作が行われるときには、アノテーションで指定したセルの位置がずれ正しく処理できない場合があります。
     *    <br>また、フィールド{@literal Map<String, Point> positions}などで保持するセルの位置情報もずれます。
     * </p>
     * 
     * <p>このような時は、アノテーション{@link XlsHint}を付与し、処理順序は属性{@link #order()}で指定します。
     * </p>
     * <p>{@link XlsHint} を付与しないフィールドは、付与しているフィールドよりも後から処理が実行されます。
     *    <br>属性{@link #order()}が同じ値を設定されているときは、 フィールド名の昇順で優先度を決めて処理されます。
     * </p>
     * 
     * <pre class="highlight"><code class="java">
     * {@literal @XlsSheet(name="Users")}
     * public class SampleSheet {
     *     
     *     // セルの位置情報
     *     private {@literal Map<String, Point>} positions;
     *     
     *     {@literal @XlsHint(order=1)}
     *     {@literal @XlsHorizontalRecords(tableLabel="ユーザ一覧", terminal=RecordTerminal.Border,
     *             overRecord=OverRecordOperate.Insert, remainedRecord=RemainedRecordOperate.Delete)}
     *     private {@literal List<UserRecord>} records;
     *     
     *     {@literal @XlsHint(order=2)}
     *     {@literal @XlsLabelledCell(label="更新日", type=LabelledCellType.Right)}
     *     private Date updateTime;
     *
     * }
     * </code></pre>
     * 
     * <div class="picture">
     *    <img src="doc-files/Hint.png">
     *    <p>書き込み時にセルの位置がずれる場合</p>
     * </div>
     * 
     * <ul>
     *  <li>-1以下の場合は無視します。</li>
     *  <li>同じ値を設定した場合は、第2処理順としてフィールド名の昇順を使用します。</li>
     *  <li>このアノテーションの付与、または属性の値が-1以下の場合、付与しているいフィールドよりも処理順序は後になります。</li>
     * </ul>
     * @return
     */
    int order() default -1;
}
