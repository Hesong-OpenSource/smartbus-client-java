package com.hesong.smartbus.client;

/**
 * Smartbus 数据包信息。
 * 
 * 该信息包含在每一个收到的数据包中，记录的数据的发送者与接收者等信息。
 * 
 * @author tanbro
 * 
 */
public class PackInfo {
	public PackInfo(byte flag, byte cmd, byte cmdType, byte srcUnitId,
			byte srcClientId, byte srcClientType, byte dstUnitid,
			byte dstClientId, byte dstClientType) {
		this.flag = flag;
		this.cmd = cmd;
		this.cmdType = cmdType;
		this.srcUnitId = srcUnitId;
		this.srcClientId = srcClientId;
		this.srcClientType = srcClientType;
		this.dstUnitid = dstUnitid;
		this.dstClientId = dstClientId;
		this.dstClientType = dstClientType;
	}

	private byte flag;
	private byte cmd;
	private byte cmdType;
	private byte srcUnitId;
	private byte srcClientId;
	private byte srcClientType;
	private byte dstUnitid;
	private byte dstClientId;
	private byte dstClientType;

	/**
	 * 标志
	 */
	public byte getFlag() {
		return flag;
	}

	/**
	 * 命令
	 */
	public byte getCmd() {
		return cmd;
	}

	/**
	 * 命令类型
	 */
	public byte getCmdType() {
		return cmdType;
	}

	/**
	 * 源单元ID
	 */
	public byte getSrcUnitId() {
		return srcUnitId;
	}

	/**
	 * 源客户端ID
	 */
	public byte getSrcClientId() {
		return srcClientId;
	}

	/**
	 * 源客户端类型
	 */
	public byte getSrcClientType() {
		return srcClientType;
	}

	/**
	 * 目的单元ID
	 */
	public byte getDstUnitid() {
		return dstUnitid;
	}

	/**
	 * 目的客户端ID
	 */
	public byte getDstClientId() {
		return dstClientId;
	}

	/**
	 * 目的客户端类型
	 */
	public byte getDstClientType() {
		return dstClientType;
	}

}