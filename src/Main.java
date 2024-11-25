import java.util.Scanner; // для считывания ввода пользователя

public class Main {
    public static void main(String[] args) { // главный метод
        Scanner scanner = new Scanner(System.in); // создание сканера для чтения ввода с консоли
        System.out.println("Выберите режим работы приложения:"); // вывод сообщения для выбора режима работы
        System.out.println("1 - Запустить сервер"); // запуск сервера
        System.out.println("2 - Запустить клиент для отправки изображения"); // запуск клиента для отправки изображения
        System.out.println("3 - Запустить клиент для получения изображения"); // запуск клиента для получения изображения
        int choice = scanner.nextInt(); // чтение ввода пользователя (выбор режима)

        switch (choice) { // проверка выбора пользователя с помощью оператора switch
            case 1:
                Server.main(args); // запуск сервера
                break;
            case 2:
                ClientSender.main(args); // запуск клиента для отправки изображения
                break;
            case 3:
                ClientReceiver.main(args); // запуск клиента для получения изображения
                break;
            default:
                System.out.println("Неверный выбор. Попробуйте снова."); // сообщение об ошибке, если введен неверный выбор
                break;
        }
        scanner.close(); // закрываем сканер
    }
}