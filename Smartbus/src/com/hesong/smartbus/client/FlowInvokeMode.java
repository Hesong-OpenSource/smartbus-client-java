package com.hesong.smartbus.client;

/**
 * @brief 流程调用模式
 * @detail @ref com.hesong.smartbus.client.net.Client.invikeFlow 需要指定流程调用的方式。
 *         分为需要返回值的流程调用与不需要返回值的流程调用。
 * @author tanbro
 * 
 */
public enum FlowInvokeMode {
	/**
	 * @brief 需要返回值
	 */
	HASRESULT(0),
	/**
	 * @brief 不需要返回值
	 */
	NORESULT(1);

	private int value;

	private FlowInvokeMode(int value) {
		this.value = value;
	}

	/**
	 * @brief 返回该枚举所对应的整数值。
	 */
	public int getValue() {
		return this.value;
	}
}
