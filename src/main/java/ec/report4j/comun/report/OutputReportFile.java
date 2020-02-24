package ec.report4j.comun.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import ec.report4j.comun.report.enumeration.OutputReportTypeEnum;
import ec.report4j.comun.report.excepcion.ReportException;
import lombok.Getter;
import lombok.Setter;

public class OutputReportFile {

	private ByteArrayOutputStream outputFile;
	@Setter
	private String filename;
	@Setter
	private Path path;
	@Setter
	@Getter
	private OutputReportTypeEnum outputReportTypeEnum;

	public OutputReportFile(Path path, String filename, OutputReportTypeEnum outputType) throws ReportException {
		validateInput(path, filename, outputType);
		this.path = path;
		this.filename = filename;
		this.outputReportTypeEnum = outputType;
	}

	public void saveFile() throws IOException {
		File file = new File(path.toFile(), filename + outputReportTypeEnum.getExtension());
		Files.newOutputStream(file.toPath()).write(outputFile.toByteArray());
	}

	public String getFileBase64() {
		return org.apache.commons.codec.binary.Base64.encodeBase64String(outputFile.toByteArray());
	}

	private void validateInput(Path path, String filename, OutputReportTypeEnum outputType) throws ReportException {
		Objects.requireNonNull(path, "Error, Path can't null");
		Objects.requireNonNull(filename, "Error, filename can't null");
		Objects.requireNonNull(outputType, "Error, outputType can't null");
		if (!path.toFile().isDirectory()) {
			throw new ReportException("The enter path doesn't exist");
		}
	}

	public void setOutputFile(ByteArrayOutputStream outputStream) {
		this.outputFile = outputStream;
	}
}
