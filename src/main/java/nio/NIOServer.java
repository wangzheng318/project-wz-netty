package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class NIOServer {
	private ServerSocketChannel serverSocketChannel;
	private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
	private ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
	private Selector selector;
	//发送次数
	//private AtomicInteger sendCount = new AtomicInteger(0);
	private int sendCount = 0;

	public NIOServer(int port) throws IOException {
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(port));
		serverSocketChannel.configureBlocking(false);

		selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("NIOServer init success");
	}

	public void listen() throws IOException {
		while (true) {
			int count = selector.select();
			// System.out.println("服务端有" + count + "个事件就位");
			Set<SelectionKey> selectionKeySet = selector.selectedKeys();
			Iterator<SelectionKey> it = selectionKeySet.iterator();
			while (it.hasNext()) {
				SelectionKey selectionKey = it.next();
				serviceHandle(selectionKey);
				it.remove();
			}
		}
	}

	private void serviceHandle(SelectionKey selectionKey) throws IOException {
		if (selectionKey.isAcceptable()) {
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
			SocketChannel socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);

		} else if (selectionKey.isReadable()) {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			receiveBuffer.clear();
			int count = socketChannel.read(receiveBuffer);
			// 只有读到数据才会返回相应，否则不处理
			if (0 < count) {
				String message = new String(receiveBuffer.array(), 0, count);
				System.out.println("Receive message:" + message);
				// 注册写事件
				socketChannel.register(selector, SelectionKey.OP_WRITE);
			}
		} else if (selectionKey.isWritable()) {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			sendBuffer.clear();
			sendBuffer.put(("hello,client_"+(sendCount++)).getBytes());
			sendBuffer.flip();
			socketChannel.write(sendBuffer);

			//socketChannel.register(selector, SelectionKey.OP_READ);
			//取消监听
			selectionKey.cancel();
		} else {
			System.out.println("un supported event type");
		}
	}

	public static void main(String[] args) throws IOException {
		new NIOServer(8082).listen();
	}

}
