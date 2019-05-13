
package ec.report4j.comun.report.word;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import ec.report4j.comun.report.Report;
import ec.report4j.comun.report.excepcion.ReportException;
import ec.report4j.comun.report.util.Utilitary;
import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.document.DocumentKind;

/**
 *
 * @author fochoa
 *
 * @version 1.0
 *
 *          16/04/2019
 *
 *          La clase {@link WordReport} sirve para generar reportes de word con
 *          tablas.
 */
public class WordReport extends Report {

	private HashMap<String, String> parametros;
	private List<TableWord> tablas;

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

	@Override
	public WordReport assignTemplate(byte[] plantilla, Map<String, Object> parametros) {
		this.inputStream = new ByteArrayInputStream(plantilla);
		parsearParametros(parametros);
		return this;
	}

	public WordReport assignTemplate(byte[] plantilla, Map<String, Object> parametros, List<TableWord> tablas) {
		this.inputStream = new ByteArrayInputStream(plantilla);
		this.tablas = tablas;
		parsearParametros(parametros);
		return this;
	}

	public WordReport assignTemplate(InputStream plantilla, Map<String, Object> parametros, List<TableWord> tablas)
			throws ReportException {
		assignTemplate(plantilla, parametros);
		this.tablas = tablas;
		return this;
	}

	public WordReport assignTemplate(File plantilla, Map<String, Object> parametros, List<TableWord> tablas)
			throws ReportException {
		assignTemplate(plantilla, parametros);
		this.tablas = tablas;
		return this;
	}

	@Override
	public WordReport buildReport() throws ReportException {

		try {
			wordMLPackage = WordprocessingMLPackage.load(inputStream);
			if (!this.parametros.isEmpty()) {
				replaceFields();
			}
			this.outputStream = new ByteArrayOutputStream();
			wordMLPackage.save(outputStream);
		} catch (Exception e) {
			throw new ReportException("Error al contruir el reporte", e);

		}
		return this;
	}

	@Override
	public byte[] exportBytes() {

		return outputStream.toByteArray();
	}

	@Override
	public OutputStream exportOS() {

		return outputStream;
	}

	private void replaceFields() throws Exception {
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
		VariablePrepare.prepare(wordMLPackage);

		if (tablas != null && !tablas.isEmpty()) {
			for (TableWord tabla : tablas) {
				WMLPackageUtil.replaceTable(tabla.getColumnas(), tabla.getFilas(), wordMLPackage);
			}
		}

		documentPart.variableReplace(this.parametros);
	}

	private void parsearParametros(Map<String, Object> parametros) {
		this.parametros = new HashMap<>();
		if (parametros == null) {
			return;
		}
		for (Map.Entry<String, Object> item : parametros.entrySet()) {
			if (item.getValue() instanceof String) {
				this.parametros.put(item.getKey(), item.getValue().toString());
			}

		}
	}

	@Override
	public byte[] exportPdf() throws ReportException {
		ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(exportBytes());
		Options options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF);
		IConverter conversor = ConverterRegistry.getRegistry().getConverter(options);
		try {
			conversor.convert(is, pdfStream, options);
		} catch (XDocConverterException e) {
			throw new ReportException("Error generando el pdf", e);
		}

		return pdfStream.toByteArray();
	}

	@Override
	public byte[] exportHtml() throws ReportException {

		ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(exportBytes());
		Options options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.XHTML);
		IConverter conversor = ConverterRegistry.getRegistry().getConverter(options);
		try {
			conversor.convert(is, htmlStream, options);
		} catch (XDocConverterException e) {
			throw new ReportException("Error generando el html", e);
		}

		return htmlStream.toByteArray();
	}

	@Override
	public String exportBase64() {
		return org.apache.commons.codec.binary.Base64.encodeBase64String(exportBytes());

	}

}
