
package ec.report4j.comun.report.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.poi.ss.SpreadsheetVersion;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import ec.report4j.comun.report.Report;
import ec.report4j.comun.report.excepcion.ReportException;
import ec.report4j.comun.report.util.Utilitary;

/**
 * 
 * 
 * @author fochoa
 * @version 1.0 27/12/2018 Clase para generar un reporte de excel a partir de una plantilla. </br>
 *          <b>Nota:<b> No se puede superar el numero máximo de filas: {@link SpreadsheetVersion} de version excel 97.
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

	public ExcelReport assignTemplate(byte[] plantilla, Map<String, Object> parametros) {
		this.inputStream = new ByteArrayInputStream(plantilla);
		if (parametros == null) {
			context = new Context();
			return this;
		}
		context = new Context(parametros);
		return this;
	}

	public ExcelReport buildReport() throws ReportException {
		outputStream = new ByteArrayOutputStream();
		this.outputStream = new ByteArrayOutputStream();
		try {
			JxlsHelper.getInstance().processTemplate(inputStream, outputStream, context);
		} catch (Exception e) {
			throw new ReportException("Error al contruir el reporte", e);
		}

		return this;
	}

	public byte[] exportBytes() {
		return outputStream.toByteArray();
	}

	public OutputStream exportOS() {
		return outputStream;
	}

	@Override
	public byte[] exportPdf() throws ReportException {
		ByteArrayInputStream is = new ByteArrayInputStream(exportHtml());
		ByteArrayOutputStream os = null;

		try {
			os = convertHtml2Pdf(is);
		} catch (Exception e) {
			throw new ReportException("Error al exportar el pdf", e);
		}
		return os.toByteArray();
	}

	@Override
	public byte[] exportHtml() throws ReportException {
		try {
			byte[] array = exportBytes();
			ByteArrayInputStream is = new ByteArrayInputStream(array);
			return new ExcelToHtml(is).getHTML().getBytes();
		} catch (Exception e) {

			throw new ReportException("Error al exportar a html", e);
		}

	}

	@Override
	public String exportBase64() {
		return org.apache.commons.codec.binary.Base64.encodeBase64String(exportBytes());
	}

	@Override
	public Report assignTemplate(InputStream plantilla, Map<String, Object> parametros) throws ReportException {
		assignTemplate(Utilitary.convert(plantilla), parametros);
		return this;
	}

	@Override
	public Report assignTemplate(File plantilla, Map<String, Object> parametros) throws ReportException {
		assignTemplate(Utilitary.convert(plantilla), parametros);
		return this;
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
			throw new ReportException("Error al convertir el html a pdf", e);
		}
	}

}
