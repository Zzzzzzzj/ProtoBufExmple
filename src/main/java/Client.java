
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);

            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
           while (true) {
                UserOuterClass.User user = UserOuterClass.User.newBuilder()
                        .setUsername("John")
                        .setAge(25)
                        .build();

                user.writeDelimitedTo(outputStream);
                outputStream.flush();

                // 接收服务器回复的消息
            //    InputStream inputStream = socket.getInputStream();
                UserOuterClass.User reply = UserOuterClass.User.parseFrom(inputStream);
                if (reply == null) {
                    break;
                }
                System.out.println(reply.getUsername());
                Thread.sleep(2000);
            }
           socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
