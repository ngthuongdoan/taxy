package com.example.taxy;

import java.io.File;

public class SignatureWrapper {
	private File signature;

	public SignatureWrapper(File signature) {
		this.signature = signature;
	}
	public File getSignature() {
		return signature;
	}
	public void setSignature(File signature) {
		this.signature = signature;
	}
}
