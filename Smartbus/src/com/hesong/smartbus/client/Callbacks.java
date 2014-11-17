package com.hesong.smartbus.client;

public interface Callbacks {
	public void onConnectSuccess();

	public void onConnectFail(Integer errorCode);
	public void onDisconnect();
	public void onGlobalConnectInfo(Byte unitId, Byte clientId,
			Byte clientType, Byte status, String addInfo);

	public void onReceiveText(PackInfo head, String txt);
	public void onFlowReturn(PackInfo head, String projectId, Integer invokeId,
			String param);

	public void onFlowTimeout(PackInfo head, String projectId, Integer invokeId);
	
}
