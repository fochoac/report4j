
package ec.report4j.comun.report.excel;

import static java.util.Objects.nonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.ss.SpreadsheetVersion;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import ec.report4j.comun.report.OutputReportFile;
import ec.report4j.comun.report.Report;
import ec.report4j.comun.report.ReportConfiguration;
import ec.report4j.comun.report.excepcion.ReportException;

/**
 * 
 * 
 * @author fochoa
 * @version 1.0 27/12/2018 Clase para generar un reporte de excel a partir de
 *          una plantilla. </br>
 *          <b>Nota:<b> No se puede superar el numero máximo de filas:
 *          {@link SpreadsheetVersion} de version excel 97.
 * 
 *          Excel97 format aka BIFF8
 *          <ul>
 *          <li>The total number of available rows is 64k (2^16)</li>
 *          <li>The total number of available columns is 256 (2^8)</li>
 *          <li>The maximum number of arguments to a function is 30</li>
 *          <li>Number of conditional format conditions on a cell is 3</li>
 *          <li>Number of cell styles is 4000</li>
 *          <li>Length of text cell contents is 32767</li>
 *          </ul>
 */
public final class ExcelReport extends Report {

	private Context context;
	private ByteArrayOutputStream outputStream;

	public ExcelReport(ReportConfiguration configuration) {
		super(configuration);
		if (nonNull(configuration.getInputReportFile().getParameterValues())
				&& !configuration.getInputReportFile().getParameterValues().isEmpty()) {
			context = new Context(configuration.getInputReportFile().getParameterValues());
		} else {
			context = new Context();
		}
	}

	public OutputReportFile buildReport() throws ReportException {
		outputStream = new ByteArrayOutputStream();

		try {
			JxlsHelper.getInstance().processTemplate(getConfiguration().getInputReportFile().getTemplate(),
					outputStream, context);
			return exportReport();

		} catch (Exception e) {
			throw new ReportException("Error to build the report", e);
		}

	}

	private OutputReportFile exportReport() throws ReportException, IOException {
		switch (getConfiguration().getOutputReportFile().getOutputReportTypeEnum()) {
		case DOC:
			throw new ReportException("Convert Excel to Word doesn't support");

		case XLS:
			getConfiguration().getOutputReportFile().setOutputFile(outputStream);
			return getConfiguration().getOutputReportFile();
		case HTML:
			byte[] htmlArray = exportHtml();
			return export(htmlArray);

		default:
			byte[] pdfArray = exportPdf();
			return export(pdfArray);

		}
	}

	private OutputReportFile export(byte[] htmlArray) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write(htmlArray);
		getConfiguration().getOutputReportFile().setOutputFile(os);
		return getConfiguration().getOutputReportFile();
	}

	private byte[] exportPdf() throws ReportException {
		ByteArrayInputStream is = new ByteArrayInputStream(exportHtml());
		ByteArrayOutputStream os = null;

		try {
			os = convertHtml2Pdf(is);
		} catch (Exception e) {
			throw new ReportException("Error to export XLS to PDF", e);
		}
		return os.toByteArray();
	}

	private byte[] exportHtml() throws ReportException {
		try {
			byte[] array = outputStream.toByteArray();
			ByteArrayInputStream is = new ByteArrayInputStream(array);
			return new ExcelToHtml(is).getHTML().getBytes();

		} catch (Exception e) {

			throw new ReportException("Error to export XLS to HTML", e);
		}

	}

	private ByteArrayOutputStream convertHtml2Pdf(ByteArrayInputStream inputStream) throws ReportException {
		try {
			Document document = new Document();
			ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
			PdfWriter writer = PdfWriter.getInstance(document, arrayOutputStream);
			document.open();
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, inputStream);
			document.close();
			return arrayOutputStream;
		} catch (Exception e) {
			throw new ReportException("Error to export HTML to PDF", e);
		}
	}

}
