import javax.swing.*; // для создания графического интерфейса
import java.io.*; // для работы с вводом-выводом
import java.net.Socket; // для работы с сетевыми сокетами

public class ClientReceiver extends JFrame { // класс клиента-получателя
    private static final int PORT = 12345; // порт для подключения к серверу
    private static final String SERVER_ADDRESS = "localhost"; // адрес сервера

    public ClientReceiver() { // конструктор клиента-получателя
        setTitle("Получатель изображения"); // заголовок окна
        setSize(300, 200); // размер окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // действие при закрытии окна
        new Thread(this::receiveImageFromServer).start(); // запуск процесса приема изображения в отдельном потоке
    }

    private void receiveImageFromServer() { // метод для получения изображения от сервера
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT); // сокет для соединения с сервером
             InputStream inputStream = socket.getInputStream()) { // получение входного потока для приема данных

            // создание файла, куда будет сохранено принятое изображение
            File file = new File("received_image.jpg");
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) { // открытие потока для записи в файл
                byte[] buffer = new byte[4096]; // буфер для чтения данных
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) { // чтение данных из входного потока
                    fileOutputStream.write(buffer, 0, bytesRead); // запись данных в файл
                }
                JOptionPane.showMessageDialog(this, "Изображение получено."); // сообщение об успешном получении
                displayImage(file); // отображение изображений на экране
            }
        } catch (IOException e) { // обработка исключения на случай ошибки ввода/вывода
            e.printStackTrace();
        }
    }

    private void displayImage(File file) { // метод для отображения изображения в окне
        ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath()); // загрузка изображения из файла
        JLabel label = new JLabel(imageIcon); // создание метки для отображения изображения
        add(label); // добавление метки с изображением в окно
        validate(); // обновление компоновку компонентов
        repaint(); // перерисовка
    }

    public static void main(String[] args) { // главный метод для запуска клиента-получателя
        SwingUtilities.invokeLater(() -> new ClientReceiver().setVisible(true)); // создание экземпляра окна (видимой)
    }
}