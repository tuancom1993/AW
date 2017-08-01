package com.amway.lms.backend.common;

public class ResponseBody {

	private ResponseStatus error;
	private Object data;

	public ResponseBody(ResponseStatus error, Object data) {
		super();
		this.error = error;
		this.data = data;
	}

	public ResponseStatus getError() {
		return error;
	}

	public void setError(ResponseStatus error) {
		this.error = error;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
