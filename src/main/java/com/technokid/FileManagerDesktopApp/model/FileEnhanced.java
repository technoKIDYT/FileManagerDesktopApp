package com.technokid.FileManagerDesktopApp.model;

import java.io.File;

public class FileEnhanced {
	FileType fileType;
	File file;

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return getFile().getAbsolutePath();
	}
}
