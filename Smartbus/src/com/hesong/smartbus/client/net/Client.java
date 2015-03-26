package com.hesong.smartbus.client.net;

import com.hesong.smartbus.client.Callbacks;
import com.hesong.smartbus.client.FlowInvokeMode;

public class Client {

    public static void initialize(Byte unitId) {
        JniWrapper.Init(unitId);
    }
    public static void release() {
        JniWrapper.Release();
    }

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
//        System.out.println("localClientId = " + localClientId);
//        System.out.println("Instances length = " + JniWrapper.instances.size());
//        System.out.println(JniWrapper.instances);
        System.out.println("New client instance is: "+this);
    }

    public Client(Byte localClientId, Long localClientType, String masterHost,
            Short masterport, String slaverHost, Short slaverPort,
            String authorUser, String authorPassword) {
        this(localClientId, localClientType, masterHost, masterport,
                slaverHost, slaverPort, authorUser, authorPassword, "");
    }
    public Client(Byte localClientId, Long localClientType, String masterHost,
            Short masterport, String slaverHost, Short slaverPort) {
        this(localClientId, localClientType, masterHost, masterport,
                slaverHost, slaverPort, "", "", "");
    }

    public Client(Byte localClientId, Long localClientType, String masterHost,
            Short masterport, String extendedInfo) {
        this(localClientId, localClientType, masterHost, masterport, "",
                (short) 0, "", "", extendedInfo);
    }

    public Client(Byte localClientId, Long localClientType, String masterHost,
            Short masterport) {
        this(localClientId, localClientType, masterHost, masterport, "",
                (short) 0, "", "", "");
    }

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

    public void connect() throws ConnectError {
        int result = JniWrapper.CreateConnect(this.localClientId,
                this.localClientType, this.masterHost, this.masterport,
                this.slaverHost, this.slaverPort, this.authorUser,
                this.authorPassword, this.extendedInfo);
        if (result != 0) {
            throw new ConnectError();
        }
    }

    public void sendText(Byte cmd, Byte cmdType, Integer dstUnitId,
            Integer dstClientId, Integer dstClientType, String txt)
            throws SendDataError {
        int result = JniWrapper.SendText(this.localClientId, cmd, cmdType,
                dstUnitId, dstClientId, dstClientType, txt);
        if (result != 0) {
            throw new SendDataError();
        }
    }

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

    public void globalConnect(int arg, byte unitid, byte clientid,
            byte clienttype, byte status, String addinfo) {
        JniWrapper.cb_globalconnect(arg, unitid, clientid, clienttype,  status,
                addinfo);
    }

    public Byte getLocalClientId() {
        return localClientId;
    }

    public Long getLocalClientType() {
        return localClientType;
    }

    public String getMasterHost() {
        return masterHost;
    }

    public Short getMasterport() {
        return masterport;
    }

    public String getSlaverHost() {
        return slaverHost;
    }

    public Short getSlaverPort() {
        return slaverPort;
    }

    public String getAuthorUser() {
        return authorUser;
    }

    public String getAuthorPassword() {
        return authorPassword;
    }

    public String getExtendedInfo() {
        return extendedInfo;
    }

    public Callbacks getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.callbacks = callbacks;
    }

    public class ConnectError extends Exception {
        /**
		 * 
		 */
        private static final long serialVersionUID = -7430664588570698582L;

        public ConnectError() {
            super();
        }
    }

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
