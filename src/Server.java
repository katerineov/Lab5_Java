import java.io.*; // для работы с потоками ввода-вывода
import java.net.*; // для работы с сетевыми соединениями
import java.util.ArrayList; // для хранения списка клиентов
import java.util.List; // для работы с динамическими списками

public class Server {
    private static final int PORT = 12345; // порт для подключения клиентов
    private List<Socket> clients = new ArrayList<>(); // список подключенных клиентов

    public static void main(String[] args) {
        new Server().startServer(); // запуск сервера
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { // создание серверного сокета на указанном порту
            System.out.println("Сервер запущен и ожидает подключения...");
            while (true) {
                Socket clientSocket = serverSocket.accept(); // ожидание подключения клиента
                clients.add(clientSocket); // добавление клиента в список
                System.out.println("Клиент подключен: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start(); // запуск нового потока для обработки клиента
            }
        } catch (IOException e) {
            e.printStackTrace(); // обработка исключения, если возникла ошибка ввода/вывода
        }
    }

    private class ClientHandler implements Runnable { // для обработки клиентов в отдельных потоках
        private Socket clientSocket;

        public ClientHandler(Socket socket) { // конструктор; принимает сокет клиента
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = clientSocket.getInputStream(); // получение входного потока от клиента
                byte[] buffer = new byte[4096]; // буфер для чтения данных
                int bytesRead;

                // пересылка изображения всем клиентам
                for (Socket client : clients) { // проходимся по всем подключенным клиентам
                    OutputStream outputStream = client.getOutputStream(); // для отправки данных клиенту
                    while ((bytesRead = inputStream.read(buffer)) != -1) { // чтение данных из входного потока
                        outputStream.write(buffer, 0, bytesRead); // отправка данных клиенту
                    }
                    outputStream.flush(); // завершение отправки данных
                }

                inputStream.close(); // закрытие входного потока
                clientSocket.close(); // закрытие соединения с клиентом
            } catch (IOException e) {
                e.printStackTrace(); // обработка исключения, если возникла ошибка ввода/вывода
            }
        }
    }
}