package com.hesong.smartbus.client;

/**
 * @brief Smartbus客户端的回调函数接口类型
 * 
 * @detail 每个客户端实例都应该被提供一个该类型的实现类实例。实例中的各个接口函数的实现将接收客户端实例的回调函数事件通知。
 * 
 * @author tanbro
 * 
 */
public interface Callbacks {
	/**
	 * @brief 连接成功
	 */
	public void onConnectSuccess();

	/**
	 * @brief 连接失败
	 * 
	 * @param errorCode
	 *            失败错误编码
	 */
	public void onConnectFail(Integer errorCode);

	/**
	 * @brief 连接断开
	 */
	public void onDisconnect();

	/**
	 * @brief 收到了文本
	 * 
	 * @param head
	 *            消息头
	 * @param txt
	 *            文本
	 */
	public void onReceiveText(PackInfo head, String txt);

	/**
	 * @brief 流程返回
	 * 
	 * @param head
	 *            消息头
	 * @param projectId
	 *            流程所属的项目ID
	 * @param invokeId
	 *            该流程返回消息所对应的流程调用的ID。流程调用的ID由
	 *            com.hesong.smartbus.client.net.Client.invokeFlow 返回
	 * @param param
	 *            流程返回的数据
	 */
	public void onFlowReturn(PackInfo head, String projectId, Integer invokeId,
			String param);

	/**
	 * @brief 流程返回超时
	 * 
	 * @param head
	 *            消息头
	 * @param projectId
	 *            流程所属的项目ID
	 * @param invokeId
	 *            该流程返回消息所对应的流程调用的ID。流程调用的ID由
	 *            com.hesong.smartbus.client.net.Client.invokeFlow 返回
	 */
	public void onFlowTimeout(PackInfo head, String projectId, Integer invokeId);
}
