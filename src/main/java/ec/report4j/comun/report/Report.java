
package ec.report4j.comun.report;

import ec.report4j.comun.report.excepcion.ReportException;
import lombok.Getter;

/**
 *
 * @author fochoa
 *
 * @version 1.0
 *
 *          16/04/2019
 *
 *          La clase {@link Report} sirve para generar reportes acorde a lo que
 *          se necesita.
 */
public abstract class Report {

	@Getter
	private ReportConfiguration configuration;

	public Report(ReportConfiguration configuration) {
		this.configuration = configuration;
	}

	public abstract OutputReportFile buildReport() throws ReportException;

}
