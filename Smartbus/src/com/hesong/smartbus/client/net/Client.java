package com.hesong.smartbus.client.net;

import com.hesong.smartbus.client.Callbacks;
import com.hesong.smartbus.client.FlowInvokeMode;

/**
 * @brief 和声 Smarbus 网络客户端类
 * 
 * @author tanbro
 * 
 */
public class Client {

	/**
	 * @brief 初始化
	 * 
	 * @detail 类库初始化。 在使用其它方法之前，必须调用该静态方法。
	 * 
	 * @param unitId
	 *            该网络客户端实例的单元ID，每个使用该类库的进程都有必须全局唯一单元ID。
	 */
	public static void initialize(Byte unitId) {
		JniWrapper.Init(unitId);
	}

	/**
	 * @brief 类库释放。
	 */
	public static void release() {
		JniWrapper.Release();
	}

	/**
	 * @brief Smarbus 网络客户端类构造函数
	 * 
	 * @param localClientId
	 *            客户端的ID。同一个客户端单元实例下的客户端ID必须不同。
	 * @param localClientType
	 *            客户端类型标志
	 * @param masterHost
	 *            服务器主机名
	 * @param masterport
	 *            服务端口
	 * @param slaverHost
	 *            从服务器主机名
	 * @param slaverPort
	 *            从服务器端口
	 * @param authorUser
	 *            登录用户名
	 * @param authorPassword
	 *            登录密码
	 * @param extendedInfo
	 *            附加信息
	 */
	public Client(Byte localClientId, Long localClientType, String masterHost,
			Short masterport, String slaverHost, Short slaverPort,
			String authorUser, String authorPassword, String extendedInfo) {
		this.localClientId = localClientId;
		this.localClientType = localClientType;
		this.masterHost = masterHost;
		this.masterport = masterport;
		this.slaverHost = slaverHost;
		this.slaverPort = slaverPort;
		this.authorUser = authorUser;
		this.authorPassword = authorPassword;
		this.extendedInfo = extendedInfo;
		JniWrapper.instances.put(localClientId, this);
	}

	/**
	 * @brief Smarbus 网络客户端类构造函数
	 * 
	 * @param localClientId
	 *            客户端的ID。同一个客户端实例下的客户端ID必须不同。
	 * @param localClientType
	 *            客户端类型标志
	 * @param masterHost
	 *            服务器主机名
	 * @param masterport
	 *            服务端口
	 * @param slaverHost
	 *            从服务器主机名
	 * @param slaverPort
	 *            从服务器端口
	 * @param authorUser
	 *            登录用户名
	 * @param authorPassword
	 *            登录密码
	 */
	public Client(Byte localClientId, Long localClientType, String masterHost,
			Short masterport, String slaverHost, Short slaverPort,
			String authorUser, String authorPassword) {
		this(localClientId, localClientType, masterHost, masterport,
				slaverHost, slaverPort, authorUser, authorPassword, "");
	}

	/**
	 * @brief Smarbus 网络客户端类构造函数
	 * 
	 * @param localClientId
	 *            客户端的ID。同一个客户端实例下的客户端ID必须不同。
	 * @param localClientType
	 *            客户端类型标志
	 * @param masterHost
	 *            服务器主机名
	 * @param masterport
	 *            服务端口
	 * @param slaverHost
	 *            从服务器主机名
	 * @param slaverPort
	 *            从服务器端口
	 */
	public Client(Byte localClientId, Long localClientType, String masterHost,
			Short masterport, String slaverHost, Short slaverPort) {
		this(localClientId, localClientType, masterHost, masterport,
				slaverHost, slaverPort, "", "", "");
	}

	/**
	 * 
	 * @param localClientId
	 *            客户端的ID。同一个客户端实例下的客户端ID必须不同。
	 * @param localClientType
	 *            客户端类型标志
	 * @param masterHost
	 *            服务器主机名
	 * @param masterport
	 *            服务端口
	 * @param extendedInfo
	 *            附加信息
	 */
	public Client(Byte localClientId, Long localClientType, String masterHost,
			Short masterport, String extendedInfo) {
		this(localClientId, localClientType, masterHost, masterport, "",
				(short) 0, "", "", extendedInfo);
	}

	/**
	 * @brief Smarbus 网络客户端类构造函数
	 * 
	 * @param localClientId
	 *            客户端的ID。同一个客户端实例下的客户端ID必须不同。
	 * @param localClientType
	 *            客户端类型标志
	 * @param masterHost
	 *            服务器主机名
	 * @param masterport
	 *            服务端口
	 */
	public Client(Byte localClientId, Long localClientType, String masterHost,
			Short masterport) {
		this(localClientId, localClientType, masterHost, masterport, "",
				(short) 0, "", "", "");
	}

	/**
	 * @brief 释放客户端
	 */
	public void dispose() {
		JniWrapper.instances.remove(this.localClientId);
	}

	private Byte localClientId;
	private Long localClientType;
	private String masterHost;
	private Short masterport;
	private String slaverHost;
	private Short slaverPort;
	private String authorUser;
	private String authorPassword;
	private String extendedInfo;

	private Callbacks callbacks;

	/**
	 * @brief 连接到服务器
	 * @throws ConnectError
	 *             连接错误
	 */
	public void connect() throws ConnectError {
		int result = JniWrapper.CreateConnect(this.localClientId,
				this.localClientType, this.masterHost, this.masterport,
				this.slaverHost, this.slaverPort, this.authorUser,
				this.authorPassword, this.extendedInfo);
		if (result != 0) {
			throw new ConnectError();
		}
	}

	/**
	 * @brief 发送文本数据
	 * @param cmd
	 *            命令
	 * @param cmdType
	 *            命令类型
	 * @param dstUnitId
	 *            目的单元ID
	 * @param dstClientId
	 *            目的客户端ID
	 * @param dstClientType
	 *            目的客户端类型
	 * @param txt
	 *            文本内容
	 * @throws SendDataError
	 *             发送数据错误
	 */
	public void sendText(Byte cmd, Byte cmdType, Integer dstUnitId,
			Integer dstClientId, Integer dstClientType, String txt)
			throws SendDataError {
		int result = JniWrapper.SendText(this.localClientId, cmd, cmdType,
				dstUnitId, dstClientId, dstClientType, txt);
		if (result != 0) {
			throw new SendDataError();
		}
	}

	/**
	 * @brief 调用SmartFlow上的流程
	 * 
	 * @param serverUnitId
	 *            SmartFlow处于Smartbus的实例ID
	 * @param ipscIndex
	 *            IPSC进程序号
	 * @param projectId
	 *            流程项目ID
	 * @param flowId
	 *            流程ID
	 * @param mode
	 *            流程调用方式
	 * @param timeout
	 *            流程返回结果等待超时值，单位为毫秒
	 * @param valueList
	 *            传递给流程的输入参数。
	 * @return 流程调用的ID。如果需要流程返回数据，则通过该返回值进行配对。
	 * @ref com.hesong.smartbus.client.Callbacks.onFlowReturn 与
	 * @ref com.hesong.smartbus.client.Callbacks.onFlowTimeout 回调的参数中将包含该ID.
	 * @throws SendDataError
	 *             发送数据异常
	 */
	public Integer invokeFlow(Integer serverUnitId, Integer ipscIndex,
			String projectId, String flowId, FlowInvokeMode mode,
			Integer timeout, String valueList) throws SendDataError {
		int result = JniWrapper.RemoteInvokeFlow(this.localClientId,
				serverUnitId, ipscIndex, projectId, flowId, mode.getValue(),
				timeout, valueList);
		if (result <= 0) {
			throw new SendDataError();
		}
		return result;
	}

	/**
	 * 
	 * @brief 客户端ID
	 */
	public Byte getLocalClientId() {
		return localClientId;
	}

	/**
	 * 
	 * @brief 客户端类型标志
	 */
	public Long getLocalClientType() {
		return localClientType;
	}

	/**
	 * 
	 * @brief smartbus 服务主机名
	 */
	public String getMasterHost() {
		return masterHost;
	}

	/**
	 * 
	 * @brief smartbus 服务端口
	 */
	public Short getMasterport() {
		return masterport;
	}

	/**
	 * @brief smartbus 从服务主机名
	 */
	public String getSlaverHost() {
		return slaverHost;
	}

	/**
	 * 
	 * @brief smartbus 从服务端口
	 */
	public Short getSlaverPort() {
		return slaverPort;
	}

	/**
	 * 
	 * @brief 登录名
	 */
	public String getAuthorUser() {
		return authorUser;
	}

	/**
	 * 
	 * @brief 登录密码
	 */
	public String getAuthorPassword() {
		return authorPassword;
	}

	/**
	 * 
	 * @brief 连接扩展信息
	 */
	public String getExtendedInfo() {
		return extendedInfo;
	}

	/**
	 * 
	 * @brief 客户端的回调函数实现
	 */
	public Callbacks getCallbacks() {
		return callbacks;
	}

	/**
	 * 
	 * @brief callbacks 客户端的回调函数实现
	 */
	public void setCallbacks(Callbacks callbacks) {
		this.callbacks = callbacks;
	}

	/**
	 * @brief 连接错误异常类
	 * 
	 */
	public class ConnectError extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7430664588570698582L;

		public ConnectError() {
			super();
		}
	}

	/**
	 * @brief 发送数据错误异常类
	 * 
	 */
	public class SendDataError extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8388088945163510723L;

		public SendDataError() {
			super();
		}
	}
}
