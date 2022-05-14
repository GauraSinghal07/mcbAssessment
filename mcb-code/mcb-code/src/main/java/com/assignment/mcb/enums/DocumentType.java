package com.assignment.mcb.enums;

public enum DocumentType {

	PERSONAL("personal"), ADDRESS("address");

	String value;

	DocumentType(String documentType) {
		this.value = documentType;
	}

	public String getValue() {
		return value;
	}

	public static DocumentType getDocumentType(String documentType) {
		for (DocumentType v : DocumentType.values()) {

			if (v.value.equals(documentType)) {
				return v;
			}

		}
		return null;
	}

}
