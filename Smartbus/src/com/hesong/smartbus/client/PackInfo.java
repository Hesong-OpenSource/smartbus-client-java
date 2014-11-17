package com.hesong.smartbus.client;

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

	public byte getFlag() {
		return flag;
	}
	public byte getCmd() {
		return cmd;
	}

	public byte getCmdType() {
		return cmdType;
	}

	public byte getSrcUnitId() {
		return srcUnitId;
	}

	public byte getSrcClientId() {
		return srcClientId;
	}

	public byte getSrcClientType() {
		return srcClientType;
	}

	public byte getDstUnitid() {
		return dstUnitid;
	}

	public byte getDstClientId() {
		return dstClientId;
	}

	public byte getDstClientType() {
		return dstClientType;
	}

}