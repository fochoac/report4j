
package ec.report4j.comun.report.word;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 *
 * @author fochoa
 *
 * @version 1.0
 *
 *          16/04/2019
 *
 *          The {@link TableWord} class is used for generate word tables.
 */
@Data
public class TableWord {

	private String[] columns;
	private List<Map<String, String>> rows;

	public TableWord() {
		super();
	}

	public TableWord(String[] columns, List<Map<String, String>> rows) {

		this.columns = columns;
		this.rows = rows;
	}

}
