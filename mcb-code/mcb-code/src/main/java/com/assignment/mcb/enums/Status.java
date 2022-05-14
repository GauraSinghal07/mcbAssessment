package com.assignment.mcb.enums;

public enum Status {

	COMPLETED("completed"), FAILED("failed");

	String value;

	Status(String status) {
		this.value = status;
	}

	public String getValue() {
		return value;
	}

	public static Status getStatus(String status) {
		for (Status v : Status.values()) {

			if (v.value.equals(status)) {
				return v;
			}

		}
		return null;
	}
}
