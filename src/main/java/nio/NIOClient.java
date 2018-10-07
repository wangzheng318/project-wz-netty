package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClient {
	private SocketChannel socketChannel;
	private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
	private ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
	private Selector selector;

	public static int sendCount = 0;

	public NIOClient(String hostName, int port) throws IOException {
		socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		selector = Selector.open();

		// ��ע�ᣬ������
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		socketChannel.connect(new InetSocketAddress(hostName, port));
		System.out.println("NIOClient init success");
	}

	public void clientListener() throws IOException {
		while (true) {
			// ѡ��һ���������Ӧ��ͨ����Ϊ I/O ����׼���������˷���ִ�д�������ģʽ��ѡ�����
			int count = selector.select();
			// System.out.println("Client �����¼�����" + count);
			Set<SelectionKey> selectionKeySet = selector.selectedKeys();
			Iterator<SelectionKey> it = selectionKeySet.iterator();
			while (it.hasNext()) {
				SelectionKey selectionKey = it.next();
				serviceHandle(selectionKey);
				it.remove();
			}
			selectionKeySet.clear();
		}
	}

	private void serviceHandle(SelectionKey selectionKey) throws IOException {
		if (selectionKey.isConnectable()) {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

			/**
			 * �׽���ͨ��֧�ַ��������ӣ��ɴ���һ���׽���ͨ��������ͨ�� connect �������Է���Զ���׽��ֵ����ӣ�֮��ͨ�� finishConnect
			 * ������ɸ����ӡ���ͨ������ isConnectionPending ������ȷ���Ƿ����ڽ������Ӳ�����
			 */
			if (socketChannel.isConnectionPending()) {
				socketChannel.finishConnect();
				// ������Ϣ
				sendBuffer.clear();
				sendBuffer.put(("hello,server_" + sendCount++).getBytes());
				sendBuffer.flip();
				socketChannel.write(sendBuffer);
			}

			socketChannel.register(selector, SelectionKey.OP_READ);

			// System.out.println("acceptable");
		} else if (selectionKey.isReadable()) {
			// System.out.println("isReadable");
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			receiveBuffer.clear();
			int count = socketChannel.read(receiveBuffer);
			if (0 < count) {
				String message = new String(receiveBuffer.array(), 0, count);
				System.out.println("receive: " + message);
				// ע��д�¼�
				//socketChannel.register(selector, SelectionKey.OP_WRITE);
			}
		} else if (selectionKey.isWritable()) {
			// System.out.println("isWritable");
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			sendBuffer.clear();
			sendBuffer.put(("hello,server2_" + sendCount++).getBytes());
			sendBuffer.flip();
			socketChannel.write(sendBuffer);
		} else {
			System.out.println("un supported event type");
		}
	}

	public static void main(String[] args) throws Exception {
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					NIOClient client;
					try {
						client = new NIOClient("127.0.0.1", 8082);
						client.clientListener();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

			}).start();
		}

	}

}
