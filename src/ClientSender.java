import javax.swing.*; // для создания графического интерфейса
import java.awt.*; // для управления графическими компонентами
import java.awt.event.ActionEvent; // для работы с событиями кнопок
import java.awt.event.ActionListener; // для обработки событий
import java.io.*; // для работы с вводом-выводом
import java.net.Socket; // для работы с сетевыми сокетами

public class ClientSender extends JFrame { // класс клиента-отправителя
    private static final int PORT = 12345; // порт для подключения к серверу
    private static final String SERVER_ADDRESS = "localhost"; // адрес сервера
    private File selectedFile; // для хранения выбранного файла изображения

    public ClientSender() { // конструктор клиента-отправителя
        setTitle("Отправитель изображения"); // заголовок окна
        setSize(300, 200); // размер окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // действие при закрытии окна
        JButton selectButton = new JButton("Выбрать изображение"); // кнопка для выбора изображения
        JButton sendButton = new JButton("Отправить изображение"); // кнопка для отправки изображения

        selectButton.addActionListener(new ActionListener() { // обработчик события для кнопки выбора
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(); // диалоговое окно для выбора файла
                int result = fileChooser.showOpenDialog(ClientSender.this); // отображение диалогового окна
                if (result == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileChooser.getSelectedFile(); // сохранение выбранного файла
                    JOptionPane.showMessageDialog(ClientSender.this, "Изображение выбрано: " + selectedFile.getName()); // подтверждение
                }
            }
        });

        sendButton.addActionListener(new ActionListener() { // обработчик события для кнопки отправки
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFile != null) {
                    sendImageToServer(selectedFile); // вызов метода для отправки файла на сервер
                } else {
                    JOptionPane.showMessageDialog(ClientSender.this, "Сначала выберите изображение."); // вывод сообщения (если файл не выбран)
                }
            }
        });

        setLayout(new FlowLayout()); // установка компоновки элементов
        add(selectButton); // кнопка выбора изображения
        add(sendButton); // кнопка отправки изображения
    }

    private void sendImageToServer(File file) { // метод для отправки изображения на сервер
        try (Socket socket = new Socket(SERVER_ADDRESS, PORT); // сокет для соединения с сервером
             FileInputStream fileInputStream = new FileInputStream(file); // открытие потока для чтения файла
             OutputStream outputStream = socket.getOutputStream()) { // получение выходного потока для отправки данных

            byte[] buffer = new byte[4096]; // буфер для передачи данных
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) { // чтение данных из файла в буфер
                outputStream.write(buffer, 0, bytesRead); // отправка данных из буфера на сервер
            }

            JOptionPane.showMessageDialog(this, "Изображение отправлено."); // сообщение об успешной отправке
        } catch (IOException e) { // обработка исключения на случай ошибки ввода/вывода
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { // главный метод для запуска клиента-отправителя
        SwingUtilities.invokeLater(() -> new ClientSender().setVisible(true)); // создание экземпляра окна (видимой)
    }
}