import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Паттерн Наблюдатель
interface TaskObserver {
    void onTaskStatusChanged(Task task);
}

class TaskStatusNotifier {
    private List<TaskObserver> observers = new ArrayList<>();

    // Метод для добавления наблюдателя
    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    // Уведомление всех наблюдателей об изменении статуса задачи
    public void notifyObservers(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskStatusChanged(task);
        }
    }
}

// Базовый класс Задача
abstract class Task {
    private String title;
    private String description;
    private TaskStatusNotifier notifier;
    private String status;
    private String priority; // Приоритет задачи

    // Конструктор задачи
    public Task(String title, String description, String priority, TaskStatusNotifier notifier) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.notifier = notifier;
    }

    // Получение названия задачи
    public String getTitle() {
        return title;
    }

    // Получение описания задачи
    public String getDescription() {
        return description;
    }

    // Получение статуса задачи
    public String getStatus() {
        return status;
    }

    // Установка статуса задачи и уведомление наблюдателей
    public void setStatus(String status) {
        this.status = status;
        notifier.notifyObservers(this);
    }

    // Получение приоритета задачи
    public String getPriority() {
        return priority;
    }

    // Абстрактный метод для выполнения задачи
    public abstract void executeTask();
}

// Конкретные типы задач: Работа и Личное
class WorkTask extends Task {
    public WorkTask(String title, String description, String priority, TaskStatusNotifier notifier) {
        super(title, description, priority, notifier);
    }

    // Выполнение рабочей задачи
    @Override
    public void executeTask() {
        setStatus("In Progress"); // Установка статуса "Выполняется"
        System.out.println("Executing work task: " + getTitle());
        setStatus("Completed"); // Установка статуса "Завершена"
    }
}

class PersonalTask extends Task {
    public PersonalTask(String title, String description, String priority, TaskStatusNotifier notifier) {
        super(title, description, priority, notifier);
    }

    // Выполнение личной задачи
    @Override
    public void executeTask() {
        setStatus("In Progress"); // Установка статуса "Выполняется"
        System.out.println("Executing personal task: " + getTitle());
        setStatus("Completed"); // Установка статуса "Завершена"
    }
}

// Фабрика для создания задач
class TaskFactory {
    // Метод создания задачи в зависимости от типа
    public static Task createTask(String type, String title, String description, String priority, TaskStatusNotifier notifier) {
        if (type.equalsIgnoreCase("work")) {
            return new WorkTask(title, description, priority, notifier);
        } else if (type.equalsIgnoreCase("personal")) {
            return new PersonalTask(title, description, priority, notifier);
        } else {
            throw new IllegalArgumentException("Unknown task type: " + type);
        }
    }
}

// Фасад для управления задачами
class TaskManagerFacade {
    private List<Task> tasks = new ArrayList<>();
    private TaskStatusNotifier notifier = new TaskStatusNotifier();

    // Конструктор, который добавляет наблюдателя для задач
    public TaskManagerFacade() {
        notifier.addObserver(task -> System.out.println("Task '" + task.getTitle() + "' status changed to: " + task.getStatus()));
    }

    // Метод для добавления задачи
    public void addTask(String type, String title, String description, String priority) {
        Task task = TaskFactory.createTask(type, title, description, priority, notifier);
        tasks.add(task);
        System.out.println("Task added: " + title);
    }

    // Метод для выполнения всех задач
    public void executeAllTasks() {
        for (Task task : tasks) {
            task.executeTask();
        }
    }

    // Метод для отображения всех задач и их статусов
    public void showTasks() {
        for (Task task : tasks) {
            System.out.println("Task: " + task.getTitle() + ", Priority: " + task.getPriority() + ", Status: " + task.getStatus());
        }
    }
}

// Главное приложение
public class TaskManagementApp {
    public static void main(String[] args) {
        TaskManagerFacade taskManager = new TaskManagerFacade();
        Scanner scanner = new Scanner(System.in);

        // Добавление задач через консоль
        System.out.println("Добавьте задачу (тип, название, описание, приоритет): ");
        while (true) {
            System.out.print("Введите тип задачи (work/personal) или 'exit' для завершения: ");
            String type = scanner.nextLine();
            if (type.equalsIgnoreCase("exit")) break;

            System.out.print("Название задачи: ");
            String title = scanner.nextLine();

            System.out.print("Описание задачи: ");
            String description = scanner.nextLine();

            System.out.print("Приоритет задачи (низкий/средний/высокий): ");
            String priority = scanner.nextLine();

            taskManager.addTask(type, title, description, priority);
        }

        // Отображение всех задач
        taskManager.showTasks();

        // Выполнение всех задач
        System.out.println("\nВыполнение всех задач...");
        taskManager.executeAllTasks();

        // Показать статус задач после выполнения
        System.out.println("\nСтатусы задач после выполнения:");
        taskManager.showTasks();

        scanner.close();
    }
}
