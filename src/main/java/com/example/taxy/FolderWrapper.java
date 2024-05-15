package com.example.taxy;

import java.io.File;

public class FolderWrapper {
	private File folder;

	public FolderWrapper(File folder) {
		this.folder = folder;
	}
	public File getFolder() {
		return folder;
	}
	public void setFolder(File folder) {
		this.folder = folder;
	}
}
