
package ec.report4j.comun.report;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

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
//    protected ByteArrayInputStream inputStream;
//    protected ByteArrayOutputStream outputStream;
//    protected WordprocessingMLPackage wordMLPackage;
	@Getter
	private ReportConfiguration configuration;

	public Report(ReportConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Metodo para construir le reporte. Debe ser usado despues del metodo:
	 * {@link #asignarPlantilla(byte[], Map)}, {@link #asignarPlantilla(File, Map)},
	 * {@link #asignarPlantilla(InputStream, Map)}
	 * 
	 * @return {@link Report}
	 * @throws ReportException en caso de error
	 */
	public abstract OutputReportFile buildReport() throws ReportException;

}
