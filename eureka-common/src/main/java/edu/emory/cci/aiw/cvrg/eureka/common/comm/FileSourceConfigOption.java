package edu.emory.cci.aiw.cvrg.eureka.common.comm;

/**
 *
 * @author Andrew Post
 */
public class FileSourceConfigOption extends SourceConfigOption {

	private String[] acceptedMimetypes;

	public String[] getAcceptedMimetypes() {
		return acceptedMimetypes;
	}

	public void setAcceptedMimetypes(String[] acceptedMimetypes) {
		this.acceptedMimetypes = acceptedMimetypes;
	}

}
