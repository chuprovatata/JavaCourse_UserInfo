import java.util.Scanner;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UserInfo {
    // Основной метод
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Введите ФИО: ");
        String fullName = scan.nextLine();

        // Проверяем, чтобы в ФИО были только русские буквы (отсутствовали цифры, символы и английские буквы)
        if (!isValidFullName(fullName)) {
            System.err.println("ФИО содержит недопустимые символы. Проверьте, что оно состоит только из русских букв.");
            return;
        }

        // Проверяем, что введено ровно 3 слова
        String[] nameParts = fullName.split(" ");
        if (nameParts.length != 3) {
            System.err.println("Неверный формат ФИО. Введите ФИО в формате \"Фамилия Имя Отчество\".");
            return;
        }

        System.out.print("Введите дату рождения (дд.мм.гггг или дд/мм/гггг): ");
        String birthDateString = scan.nextLine();
        LocalDate birthDate;

        try {
            // Парсим и проверяем дату рождения
            birthDate = parseAndValidateDate(birthDateString);
            // Проверяем, что дата рождения не в будущем
            if (birthDate.isAfter(LocalDate.now())) {
                System.err.println("Дата рождения не может быть в будущем!");
                return;
            }
        } catch (IllegalArgumentException e) {
            // Обрабатываем исключение, если дата некорректна
            System.err.println(e.getMessage());
            return;
        }

        String initials = getInitials(nameParts);
        System.out.println("Инициалы: " + initials);
        String gender = getGender(nameParts[2]);
        System.out.println("Пол: " + gender);
        int age = getAge(birthDate);
        String ageSuffix = getAgeEnd(age);
        System.out.println("Возраст: " + age + " " + ageSuffix);

        scan.close();
    }
    // Метод для проверки, что в ФИО не содержатся недопустимые символы
    private static boolean isValidFullName(String fullName) {
        for (char c : fullName.toCharArray()) {
            if (!Character.isWhitespace(c) && (c < 'а' || c > 'я') && (c < 'А' || c > 'Я')) {
                return false;
            }
        }
        return true;
    }
    // Метод для парсинга и проверки даты рождения
    private static LocalDate parseAndValidateDate(String str) {
        DateTimeFormatter formatterDot = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter formatterSlash = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date;
        try {
            date = LocalDate.parse(str, formatterDot);
        } catch (DateTimeParseException e) {
            try {
                date = LocalDate.parse(str, formatterSlash);
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Неверный формат даты. Используйте дд.мм.гггг или дд/мм/гггг");
            }
        }
        // Проверяем на существование даты
        if (date.getDayOfMonth() != Integer.parseInt(str.split("[./]")[0])) {
            throw new IllegalArgumentException("Нереальная дата: " + str);
        }
        return date;
    }
    // Метод для генерации инициалов
    private static String getInitials(String[] nameParts) {
        return nameParts[0] + " " + nameParts[1].charAt(0) + "." + nameParts[2].charAt(0) + ".";
    }
    // Метод для получения пола по окончанию отчества
    private static String getGender(String middleName) {
        if (middleName.toLowerCase().endsWith("вич")) {
            return "Мужской";
        } else if (middleName.toLowerCase().endsWith("вна")) {
            return "Женский";
        } else {
            return "Не удалось определить.";
        }
    }
    // Метод для получения возраста по дате рождения
    private static int getAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    // Метод для получения окончания (последнего слова) для вывода возраста
    private static String getAgeEnd(int age) {
        String[] endings = {"лет", "год", "года"};
        int lastDigit = age % 10;
        int lastTwoDigits = age % 100;
        if (lastDigit == 1 && lastTwoDigits != 11) {
            return endings[1];
        } else if (lastDigit >= 2 && lastDigit <= 4 && (lastTwoDigits < 12 || lastTwoDigits > 14)) {
            return endings[2];
        } else {
            return endings[0];
        }
    }
}
