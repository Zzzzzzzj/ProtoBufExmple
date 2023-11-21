import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LongServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                // 创建线程处理客户端连接
                Thread thread = new Thread(() -> {
                    try {
                        InputStream inputStream = clientSocket.getInputStream();
                        OutputStream outputStream = clientSocket.getOutputStream();

                        while (true) {
                            // 反序列化接收到的消息
                            UserOuterClass.User user = UserOuterClass.User.parseDelimitedFrom(inputStream);
                            if (user == null) {
                                break;
                            }
                            System.out.println("Received message from client:");
                            System.out.println("Sender: " + user.getUsername());
                            System.out.println("Content: " + user.getAge());

                            // 创建回复消息
                            UserOuterClass.User reply = UserOuterClass.User.newBuilder()
                                    .setUsername("Server")
                                    .setAge(1)
                                    .build();

                            // 序列化并发送回复消息给客户端
                            reply.writeDelimitedTo(outputStream);
                            outputStream.flush();
                        }

                        // 关闭连接
                        clientSocket.close();
                        System.out.println("Client disconnected");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                // 启动线程处理客户端连接
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
