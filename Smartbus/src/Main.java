import java.io.Console;

import com.hesong.smartbus.client.Callbacks;
import com.hesong.smartbus.client.FlowInvokeMode;
import com.hesong.smartbus.client.PackInfo;
import com.hesong.smartbus.client.net.Client;
import com.hesong.smartbus.client.net.Client.ConnectError;
import com.hesong.smartbus.client.net.Client.SendDataError;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Console sh = System.console();

		byte unitid = 15;
		Client.initialize(unitid);
		Client client = new Client((byte) 15, (long) 15, "192.168.3.21",
				(short) 8089);

		class MyCallbacks implements Callbacks {
			public void onConnectSuccess() {
				System.out.println("onConnectSuccess");
			}

			public void onConnectFail(Integer errorCode) {
				System.out.println("onConnectFail");

			}

			public void onDisconnect() {
				System.out.println("onDisconnect");

			}

			public void onReceiveText(PackInfo head, String txt) {
				System.out.println(String.format("onReceiveText: %s", txt));

			}

			public void onFlowReturn(PackInfo head, String projectid,
					Integer invokeId, String param) {
				System.out.println(String.format("onFlowReturn: %s %d : %s",
						projectid, invokeId, param));

			}

			public void onFlowTimeout(PackInfo head, String projectid,
					Integer invokeId) {
				System.out.println("onFlowTimeout");

			}

		}

		client.setCallbacks(new MyCallbacks());

		try {
			client.connect();
		} catch (ConnectError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (true) {
			String s = sh.readLine();
			if (s == "quit") {
				sh.printf("quit...\n");
				break;
			} else {
				try {
					client.invokeFlow(0, 0, "Project1", "Flow1",
							FlowInvokeMode.HASRESULT, 30,
							String.format("[\'%s\']", s));
				} catch (SendDataError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		System.out.println("end of the program!");

	}
}
