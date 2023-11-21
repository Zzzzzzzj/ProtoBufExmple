
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server started. Listening on port 12345...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                while (true) {
                    UserOuterClass.User user = UserOuterClass.User.parseDelimitedFrom(clientSocket.getInputStream());
                    System.out.println("Received user details:");
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Age: " + user.getAge());

                    // 创建回复消息
                    UserOuterClass.User reply = UserOuterClass.User.newBuilder()
                            .setUsername("Server")
                            .setAge(1)
                            .build();

                    // 发送回复消息给客户端
                    OutputStream outputStream = clientSocket.getOutputStream();
                    reply.writeTo(outputStream);
                    outputStream.flush();
                }


             //   clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
