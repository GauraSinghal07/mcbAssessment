package com.assignment.mcb.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "files")
public class FileDetails {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String fileId;
	private String fileName;
	private Long customerId;
	@Lob
	private byte[] data;
	private String documentType;
	private long size;
	private String status;
	private Timestamp timestamp;

	public FileDetails() {
		super();
	}

	public FileDetails(String fileName, Long customerId, byte[] data, String documentType, long size, String status,
			Timestamp timestamp) {
		super();
		this.fileName = fileName;
		this.customerId = customerId;
		this.data = data;
		this.documentType = documentType;
		this.size = size;
		this.status = status;
		this.timestamp = timestamp;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
