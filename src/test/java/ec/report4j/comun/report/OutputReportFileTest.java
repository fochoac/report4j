package ec.report4j.comun.report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import ec.report4j.comun.report.enumeration.OutputReportTypeEnum;
import ec.report4j.comun.report.excepcion.ReportException;

public class OutputReportFileTest {

	@Test(expected = ReportException.class)
	public void testOutputReportFileWithNullParameters() throws ReportException {

		new OutputReportFile(null, null, null);
	}

	@Test(expected = ReportException.class)
	public void testOutputReportFileWithNameNull() throws ReportException, IOException {
		Path path = Files.createTempDirectory("test");
		new OutputReportFile(path, null, OutputReportTypeEnum.DOC);
	}

	@Test(expected = ReportException.class)
	public void testOutputReportFileWithReportTypeNull() throws ReportException, IOException {
		Path path = Files.createTempDirectory("test");
		new OutputReportFile(path, "filename", null);
	}

	@Test(expected = ReportException.class)
	public void testOutputReportFileWithPathIsNotADirectory() throws ReportException, IOException {
		Path path = Files.createTempFile("test", ".tmp");
		new OutputReportFile(path, "file", OutputReportTypeEnum.DOC);
	}

	@Test
	public void testOutputReportFileWithCorrectParameters() throws ReportException, IOException {
		Path path = Files.createTempDirectory("test");
		OutputReportFile orf = new OutputReportFile(path, "file", OutputReportTypeEnum.DOC);

		assertEquals(OutputReportTypeEnum.DOC, orf.getOutputReportTypeEnum());

	}

	@Test
	public void testGetFileBase64() throws IOException, ReportException {
		Path path = Files.createTempDirectory("test");
		OutputReportFile orf = new OutputReportFile(path, "file", OutputReportTypeEnum.DOC);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write("Hello".getBytes());
		orf.setOutputFile(os);
		assertEquals("SGVsbG8=", orf.getFileBase64());
	}

	@Test
	public void testSaveFile() throws IOException, ReportException {
		Path path = Files.createTempDirectory("test");
		OutputReportFile orf = new OutputReportFile(path, "test", OutputReportTypeEnum.DOC);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		os.write("Hello".getBytes());
		orf.setOutputFile(os);
		orf.saveFile();
		File file = new File(path.toFile(), "test" + OutputReportTypeEnum.DOC.getExtension());
		assertTrue(file.exists());
	}

}
