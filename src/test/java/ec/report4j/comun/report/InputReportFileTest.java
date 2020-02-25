package ec.report4j.comun.report;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ec.report4j.comun.report.excepcion.ReportException;

public class InputReportFileTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test(expected = ReportException.class)
	public void testInputReportFileWithNullByteArrayInput() throws ReportException {
		byte[] array = null;
		new InputReportFile(array, null);
	}

	@Test(expected = ReportException.class)
	public void testInputReportFileWithNullByteArrayInputStream() throws ReportException {
		ByteArrayInputStream array = null;
		new InputReportFile(array, null);
	}

	@Test(expected = ReportException.class)
	public void testInputReportFileWithNullFileInput() throws ReportException {
		File file = null;
		new InputReportFile(file, null);
	}

	@Test(expected = ReportException.class)
	public void testInputReportFileWithNullInputStream() throws ReportException {
		InputStream io = null;
		new InputReportFile(io, null);
	}

	@Test
	public void testInputReportFileWithByteArray() throws ReportException {
		byte[] array = "Hello word".getBytes();
		InputReportFile is = new InputReportFile(array, null);
		assertNull(is.getParameterValues());
		assertNotNull(is.getTemplate());
	}

	@Test
	public void testInputReportFileWithFileTemplate() throws ReportException, IOException {
		Path file = Files.createTempFile("test", ".tmp");
		Map<String, Object> parameteres = new HashMap<>();
		parameteres.put("key", "remappingFunction");
		InputReportFile is = new InputReportFile(file.toFile(), parameteres);
		assertNotNull(is.getParameterValues());
		assertNotNull(is.getTemplate());
	}

	@Test
	public void testInputReportFileWithInputStreamTemplate() throws ReportException, IOException {
		Path file = Files.createTempFile("test", ".tmp");
		Map<String, Object> parameteres = new HashMap<>();
		parameteres.put("key", "value");
		InputReportFile is = new InputReportFile(new FileInputStream(file.toFile()), parameteres);
		assertNotNull(is.getParameterValues());
		assertNotNull(is.getTemplate());
	}
}
