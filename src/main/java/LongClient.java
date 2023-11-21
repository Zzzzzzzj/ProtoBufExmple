import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LongClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080);
            System.out.println("Connected to server");

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            while (true) {
                // 创建消息
                UserOuterClass.User user = UserOuterClass.User.newBuilder()
                        .setUsername("Client")
                        .setAge(25)
                        .build();

                // 序列化并发送消息给服务器
                user.writeDelimitedTo(outputStream);
                outputStream.flush();

                // 反序列化接收服务器回复的消息
                UserOuterClass.User reply = UserOuterClass.User.parseDelimitedFrom(inputStream);
                if (reply == null) {
                    break;
                }

                System.out.println("Received message from server:");
                System.out.println("Sender: " + reply.getUsername());
                System.out.println("Content: " + reply.getAge());

                // 延时一段时间再发送下一条消息
                Thread.sleep(2000);
            }

            // 关闭连接
            socket.close();
            System.out.println("Connection closed");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
