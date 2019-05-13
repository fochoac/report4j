
package ec.eppec.comun.reporte.word;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Before;
import org.junit.Test;

import ec.report4j.comun.report.Report;

public class WordReportContractTest {
	private static final String TEMPLATE_NAME_CONTRACT = "contract.docx";
	private static final String TEMPLATE_NAME_TEST = "test.docx";

	@Before
	public void setUp() throws Exception {
	}

	

	@Test
	public final void testContractExportPdf() throws IOException, Throwable {
		byte[] pdf = Report.getWordInstance()
				.assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME_CONTRACT), null)
				.buildReport().exportPdf();
		File archivo = saveFile(pdf, "report-word-pdf-contract", ".pdf");
		assertNotNull(archivo);
	}
	@Test
	public final void testTestExportPdf() throws IOException, Throwable {
		byte[] pdf = Report.getWordInstance()
				.assignTemplate(getClass().getClassLoader().getResourceAsStream(TEMPLATE_NAME_TEST), null)
				.buildReport().exportPdf();
		File archivo = saveFile(pdf, "report-word-pdf-test", ".pdf");
		assertNotNull(archivo);
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
