
package ec.eppec.comun.reporte.word;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ec.report4j.comun.report.InputReportFile;
import ec.report4j.comun.report.OutputReportFile;
import ec.report4j.comun.report.Report;
import ec.report4j.comun.report.ReportConfiguration;
import ec.report4j.comun.report.enumeration.OutputReportTypeEnum;
import ec.report4j.comun.report.word.TableWord;
import ec.report4j.comun.report.word.WordReport;

public class WordReportTest {
	private static final String TEMPLATE_NAME = "empleados.docx";
	private static final Path TEMPORAL_FOLDER = Paths.get(System.getProperty("java.io.tmpdir"));
	private InputReportFile inputReportFile;

	@Before
	public void setUp() throws Exception {
		inputReportFile = new InputReportFile(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME),
				getData());
	}

	@Test
	public final void testExportPdf() throws IOException, Throwable {
		OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testWord2Pdf",
				OutputReportTypeEnum.PDF);
		ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
		Report report = new WordReport(configuration);
		OutputReportFile outReportFile = report.buildReport();
		outReportFile.saveFile();

		assertNotNull(outReportFile.getFileBase64());
	}

	@Test
	public final void testExportHtml() throws IOException, Throwable {
		OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testWord2Html",
				OutputReportTypeEnum.HTML);
		ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
		Report report = new WordReport(configuration);
		OutputReportFile outReportFile = report.buildReport();
		outReportFile.saveFile();

		assertNotNull(outReportFile.getFileBase64());
	}

	@Test
	public final void testExportWord() throws IOException, Throwable {
		OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testWord2Word",
				OutputReportTypeEnum.DOC);
		ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
		Report report = new WordReport(configuration);
		OutputReportFile outReportFile = report.buildReport();
		outReportFile.saveFile();

		assertNotNull(outReportFile.getFileBase64());
	}

	@Test
	public final void testExportPdfWithTables() throws IOException, Throwable {
		OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testWord2PdfWithTables",
				OutputReportTypeEnum.PDF);
		ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
		Report report = new WordReport(configuration, generateTable());
		OutputReportFile outReportFile = report.buildReport();
		outReportFile.saveFile();

		assertNotNull(outReportFile.getFileBase64());
	}

	@Test
	public final void testExportHtmlWithTables() throws IOException, Throwable {
		OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testWord2HtmlWithTables",
				OutputReportTypeEnum.HTML);
		ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
		Report report = new WordReport(configuration, generateTable());
		OutputReportFile outReportFile = report.buildReport();
		outReportFile.saveFile();

		assertNotNull(outReportFile.getFileBase64());
	}

	@Test
	public final void testExportWordWithTables() throws IOException, Throwable {
		OutputReportFile outputReportFile = new OutputReportFile(TEMPORAL_FOLDER, "testWord2WordWithTables",
				OutputReportTypeEnum.DOC);
		ReportConfiguration configuration = new ReportConfiguration(inputReportFile, outputReportFile);
		Report report = new WordReport(configuration, generateTable());
		OutputReportFile outReportFile = report.buildReport();
		outReportFile.saveFile();

		assertNotNull(outReportFile.getFileBase64());
	}

	private Map<String, Object> getData() {
		String fecha = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		Map<String, Object> mappings = new HashMap<String, Object>();
		mappings.put("nombre", "Pancho");
		mappings.put("fechaCorte", fecha);
		mappings.put("mail", "mail@hotmail.comc");
		mappings.put("fechaNacimiento", fecha);
		mappings.put("salario", "100.25");
		return mappings;
	}

	private List<TableWord> generateTable() {
		List<Map<String, String>> textToAdd = new ArrayList<>();
		Map<String, String> fila = new HashMap<>();
		fila.put("name", "1");
		fila.put("email", "2");
		fila.put("dateOfBirth", "3");
		fila.put("salary", "4");
		textToAdd.add(fila);
		return Arrays.asList(new TableWord(new String[] { "name", "email", "dateOfBirth", "salary" }, textToAdd));
	}

}
