package io.meduse.data;

public class HttpSuccess {

	private int code;

	public HttpSuccess() {
		this.setCode(200);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
