
package ec.report4j.comun.report.word;

import java.util.List;
import java.util.Map;

import lombok.Getter;

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

public class TableWord {
	@Getter
	private String[] columns;
	@Getter
	private List<Map<String, String>> rows;

	public TableWord(String[] columns, List<Map<String, String>> rows) {

		this.columns = columns;
		this.rows = rows;
	}

}
