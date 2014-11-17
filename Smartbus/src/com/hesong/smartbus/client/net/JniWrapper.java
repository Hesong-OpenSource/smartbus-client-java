package com.hesong.smartbus.client.net;

import com.hesong.smartbus.client.PackInfo;

import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class JniWrapper {
    public native static int Init(byte unitid);

    public native static void Release();

    public native static int CreateConnect(byte local_clientid,
            long local_clienttype, String masterip, short masterport,
            String slaverip, short slaverport, String author_username,
            String author_pwd, String add_info);

    public native static int SendText(byte local_clientid, byte cmd,
            byte cmdtype, int dst_unitid, int dst_clientid, int dst_clienttype,
            String txt);

    public native static int RemoteInvokeFlow(byte local_clientid,
            int server_unitid, int ipscindex, String projectid, String flowid,
            int mode, int timeout, String in_valuelist);

    static {
        System.loadLibrary("smartbus_net_cli_jni");
    }

    public static Map<Byte, Client> instances = new ConcurrentHashMap<Byte, Client>();

    protected static void cb_connection(int arg, byte local_clientid,
            int accesspoint_unitid, int ack) {
        Client inst = JniWrapper.instances.get(local_clientid);
        if (inst != null) {
            if (ack == 0) {
                inst.getCallbacks().onConnectSuccess();
            } else {
                inst.getCallbacks().onConnectFail(ack);
            }
        }
    }

    protected static void cb_disconnect(int arg, byte local_clientid) {
        Client inst = instances.get(local_clientid);
        if (inst != null) {
            inst.getCallbacks().onDisconnect();
        }
    }

    protected static void cb_recvdata(int arg, byte local_clientid, byte cmd, byte cmdtype,
            byte src_unit_id, byte src_unit_client_id,
            byte src_unit_client_type, byte dest_unit_id,
            byte dest_unit_client_id, byte dest_unit_client_type, String txt) {
        System.out.println("Recv clientId = " + dest_unit_client_id);
        Client inst = instances.get(local_clientid);
        PackInfo head = new PackInfo((byte) arg, cmd, cmdtype, src_unit_id,
                src_unit_client_id, src_unit_client_type, dest_unit_id,
                dest_unit_client_id, dest_unit_client_type);
        if (inst != null) {
            inst.getCallbacks().onReceiveText(head, txt);
        }
    }

    protected static void cb_invokeflowret(int arg, byte local_clientid,
            byte head_flag, byte cmd, byte cmdtype, byte src_unit_id,
            byte src_unit_client_id, byte src_unit_client_type,
            byte dest_unit_id, byte dest_unit_client_id,
            byte dest_unit_client_type, String projectid, int invoke_id,
            int ret, String param) {
        Client inst = instances.get(local_clientid);
        if (inst != null) {
            PackInfo head = new PackInfo(head_flag, cmd, cmdtype, src_unit_id,
                    src_unit_client_id, src_unit_client_type, dest_unit_id,
                    dest_unit_client_id, dest_unit_client_type);
            if (ret == 1) {
                inst.getCallbacks().onFlowReturn(head, projectid, invoke_id,
                        param);
            } else {
                inst.getCallbacks().onFlowTimeout(head, projectid, invoke_id);
            }
        }
    }

    protected static void cb_globalconnect(int arg, byte unitid, byte clientid,
            byte clienttype, byte accesspoint_unit, byte status, String addinfo) {
        System.out.println(instances);
        Iterator<Entry<Byte, Client>> iter = instances.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Byte, Client> pair = (Map.Entry<Byte, Client>) iter
                    .next();
            Client inst = pair.getValue();
            inst.getCallbacks().onGlobalConnectInfo(unitid, clientid,
                    clienttype, status, addinfo);
        }
    }

}
