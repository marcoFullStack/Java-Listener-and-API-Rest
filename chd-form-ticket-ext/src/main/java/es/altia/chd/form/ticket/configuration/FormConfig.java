package es.altia.chd.form.ticket.configuration;

public class FormConfig {
    private String contactField;
    private String contactNameField;
    private String subjectField;
    private String grupo;
    private String endpoint;

    public FormConfig(String contactField, String contactNameField, String subjectField, String grupo, String endpoint) {
        this.contactField = contactField;
        this.contactNameField = contactNameField;
        this.subjectField = subjectField;
        this.grupo = grupo;
        this.endpoint = endpoint;
    }

	public String getContactField() {
		return contactField;
	}

	public void setContactField(String contactField) {
		this.contactField = contactField;
	}

	public String getContactNameField() {
		return contactNameField;
	}

	public void setContactNameField(String contactNameField) {
		this.contactNameField = contactNameField;
	}

	public String getSubjectField() {
		return subjectField;
	}

	public void setSubjectField(String subjectField) {
		this.subjectField = subjectField;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
}