package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer3_UseThreadPoll {
	private ServerSocket serverSocket;

	ExecutorService executorService;

	private int callCount = 0;

	public BIOServer3_UseThreadPoll(int port) throws IOException {
		super();
		serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress(port));
		executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		System.out.println("BIOServer3_UseThreadPoll init ok");
	}

	public void listen() throws IOException {
		while (true) {
			Socket socket = serverSocket.accept();
			if (null != socket) {
				executorService.execute(new ServiceHandler(socket));
			}
		}
	}

	private void handerService(Socket socket) {
		InputStream in = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		OutputStream out = null;
		PrintWriter pw = null;
		try {
			// 1.读取客户端消息
			in = socket.getInputStream();
			isr = new InputStreamReader(in);
			br = new BufferedReader(isr);
			StringBuffer sb = new StringBuffer();
			String message = null;
			while (null != (message = br.readLine())) {
				// System.out.println(message);
				sb.append(message);
			}
			System.out.println("request:" + sb.toString());

			// 2.返回处理结果
			String sendMessage = "Hello client_" + callCount++;
			out = socket.getOutputStream();
			pw = new PrintWriter(out);
			pw.write(sendMessage);
			System.out.println("response:" + sendMessage);

			// 关闭IO
			pw.close();
			out.close();
			br.close();
			isr.close();
			in.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class ServiceHandler implements Runnable {

		private Socket socket;

		public ServiceHandler(Socket socket) {
			super();
			this.socket = socket;
		}

		@Override
		public void run() {
			handerService(socket);
		}

	}

	public static void main(String[] args) throws IOException {
		BIOServer3_UseThreadPoll bioServer = new BIOServer3_UseThreadPoll(8082);
		bioServer.listen();
	}

}
