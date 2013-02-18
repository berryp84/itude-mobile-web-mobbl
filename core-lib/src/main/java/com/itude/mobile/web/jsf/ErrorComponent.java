package com.itude.mobile.web.jsf;

import java.io.IOException;
import java.io.StringWriter;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class ErrorComponent extends UIComponentBase {

	@Override
	public String getFamily() {
		return "MOBBL";
	}

	@Override
	public void encodeAll(FacesContext context) throws IOException {
		ResponseWriter original = context.getResponseWriter();
		try {
			StringWriter result = new StringWriter();
			ResponseWriter clonedWriter = context.getResponseWriter()
					.cloneWithWriter(result);
			context.setResponseWriter(clonedWriter);
			super.encodeAll(context);
			clonedWriter.flush();
			original.write(result.getBuffer().toString());
		} catch (Exception e) {
			handleError(e, original);
		} finally {
			context.setResponseWriter(original);
		}
	}

	protected void handleError(Exception e, ResponseWriter writer)
			throws IOException {
		writer.writeText("Error occurred: " + e.getMessage(), null);
	}
}
