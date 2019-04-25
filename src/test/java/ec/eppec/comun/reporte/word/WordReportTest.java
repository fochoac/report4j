
package ec.eppec.comun.reporte.word;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ec.report4j.comun.report.Report;
import ec.report4j.comun.report.word.WordReport;
import ec.report4j.comun.report.word.TableWord;

public class WordReportTest {
	private static final String TEMPLATE_NAME = "empleados.docx";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void testExportBytes() throws IOException, Throwable {
		byte[] resultado = Report.getWordInstance()
				.assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME), getData())
				.buildReport().exportBytes();
		assertNotNull(resultado);
	}

	@Test
	public final void testExportOS() throws IOException, Throwable {
		ByteArrayOutputStream resultado = (ByteArrayOutputStream) Report.getWordInstance()
				.assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME), getData())
				.buildReport().exportOS();
		assertNotNull(resultado);
	}

	@Test
	public final void testExportBase64() throws IOException, Throwable {
		String cadena = Report.getWordInstance()
				.assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME), getData())
				.buildReport().exportBase64();
		assertNotNull(cadena);
	}

	@Test
	public final void testExportPdf() throws IOException, Throwable {
		byte[] pdf = Report.getWordInstance()
				.assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME), getData())
				.buildReport().exportPdf();
		File archivo = saveFile(pdf, "report-word-pdf", ".pdf");
		assertNotNull(archivo);
	}

	@Test
	public final void testExportHtml() throws IOException, Throwable {
		byte[] html = Report.getWordInstance()
				.assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME), getData())
				.buildReport().exportHtml();
		File archivo = saveFile(html, "report-word-html", ".html");
		assertNotNull(archivo);
	}

	@Test
	public final void testExportPdfConTabla() throws IOException, Throwable {
		WordReport reporte = (WordReport) Report.getWordInstance();
		byte[] pdf = reporte.assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME),
				getData(), generateTable()).buildReport().exportPdf();
		File archivo = saveFile(pdf, "report-word-pdf-tabla", ".pdf");
		assertNotNull(archivo);
	}

	@Test
	public final void testExportHtmlConTabla() throws IOException, Throwable {
		WordReport reporte = (WordReport) Report.getWordInstance();
		byte[] html = reporte.assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME),
				getData(), generateTable()).buildReport().exportHtml();
		File archivo = saveFile(html, "report-word-html-tabla", ".html");
		assertNotNull(archivo);
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

	private File saveFile(byte[] contenido, String nombre, String extension)
			throws FileNotFoundException, IOException {
		File archivo = Files.createTempFile(nombre, extension).toFile();
		try (FileOutputStream out = new FileOutputStream(archivo)) {
			out.write(contenido);
		}
		return archivo;
	}

}
