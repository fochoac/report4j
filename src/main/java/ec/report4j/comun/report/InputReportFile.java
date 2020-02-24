package ec.report4j.comun.report;

import static ec.report4j.comun.report.util.Utilitary.convert;
import static java.util.Objects.requireNonNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

import ec.report4j.comun.report.excepcion.ReportException;
import lombok.Getter;
import lombok.Setter;

public class InputReportFile {
	@Getter
	private ByteArrayInputStream template;
	@Getter
	@Setter
	private Map<String, Object> parameterValues;

	public InputReportFile() {
		super();
	}

	public InputReportFile(ByteArrayInputStream template, Map<String, Object> parameterValues) {
		requireNonNull(template, "Template can't be null");

		this.template = template;
		this.parameterValues = parameterValues;

	}

	public InputReportFile(byte[] template, Map<String, Object> parameterValues) {
		this(new ByteArrayInputStream(template), parameterValues);
	}

	public InputReportFile(File template, Map<String, Object> parameterValues) throws ReportException {
		this(convert(template), parameterValues);
	}

	public InputReportFile(InputStream template, Map<String, Object> parameterValues) throws ReportException {
		this(convert(template), parameterValues);
	}

}
