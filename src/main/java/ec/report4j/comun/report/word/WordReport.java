
package ec.report4j.comun.report.word;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;

import ec.report4j.comun.report.OutputReportFile;
import ec.report4j.comun.report.Report;
import ec.report4j.comun.report.ReportConfiguration;
import ec.report4j.comun.report.excepcion.ReportException;
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

	private WordprocessingMLPackage wordMLPackage;
	private List<TableWord> tablesToReplace;
	private ByteArrayOutputStream outputStream;

	public WordReport(ReportConfiguration configuration) {
		super(configuration);

	}

	public WordReport(ReportConfiguration configuration, List<TableWord> tablesToReplace) {
		this(configuration);

		this.tablesToReplace = tablesToReplace;
	}

	@Override
	public OutputReportFile buildReport() throws ReportException {

		try {
			wordMLPackage = WordprocessingMLPackage.load(getConfiguration().getInputReportFile().getTemplate());
			if (!getWordParametersMap().isEmpty()) {
				replaceFields();
			}
			this.outputStream = new ByteArrayOutputStream();
			wordMLPackage.save(outputStream);
			return exportReport();

		} catch (Exception e) {
			throw new ReportException("Error al contruir el reporte", e);

		}

	}

	private OutputReportFile exportReport() throws ReportException, IOException {
		switch (getConfiguration().getOutputReportFile().getOutputReportTypeEnum()) {
		case DOC:
			getConfiguration().getOutputReportFile().setOutputFile(outputStream);
			return getConfiguration().getOutputReportFile();

		case XLS:
			throw new ReportException("Convert Word to Excel doesn't support");

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

	private void replaceFields() throws Exception {
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
		VariablePrepare.prepare(wordMLPackage);

		if (nonNull(tablesToReplace) && !tablesToReplace.isEmpty()) {
			tablesToReplace
					.forEach(table -> WMLPackageUtil.replaceTable(table.getColumns(), table.getRows(), wordMLPackage));

		}

		documentPart.variableReplace(new HashMap<>(getWordParametersMap()));
	}

	private Map<String, String> getWordParametersMap() {

		if (isNull(getConfiguration().getInputReportFile().getParameterValues())) {
			return new HashMap<>();
		}

		return getConfiguration().getInputReportFile().getParameterValues().entrySet().stream()
				.collect(Collectors.toMap((Map.Entry<String, Object> i) -> i.getKey(),
						(Map.Entry<String, Object> i) -> i.getValue().toString()));

	}

	public byte[] exportPdf() throws ReportException {
		ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(outputStream.toByteArray());
		Options options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF);
		IConverter conversor = ConverterRegistry.getRegistry().getConverter(options);
		try {
			conversor.convert(is, pdfStream, options);
		} catch (XDocConverterException e) {
			throw new ReportException("Error generando el pdf", e);
		}

		return pdfStream.toByteArray();
	}

	public byte[] exportHtml() throws ReportException {

		ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
		ByteArrayInputStream is = new ByteArrayInputStream(outputStream.toByteArray());
		Options options = Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.XHTML);
		IConverter conversor = ConverterRegistry.getRegistry().getConverter(options);
		try {
			conversor.convert(is, htmlStream, options);
		} catch (XDocConverterException e) {
			throw new ReportException("Error generando el html", e);
		}

		return htmlStream.toByteArray();
	}

}
