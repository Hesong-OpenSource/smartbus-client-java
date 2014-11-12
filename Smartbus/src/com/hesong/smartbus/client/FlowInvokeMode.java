package com.hesong.smartbus.client;

public enum FlowInvokeMode {
	NORESULT(1);

	private int value;

	private FlowInvokeMode(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
