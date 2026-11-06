import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final int numPeriod = 8;
    private static final int minutesPeriod = 60;

    public static void main(String[] args) throws IOException {
        if (args.length != 0 && args.length != 2) {
            throw new IllegalArgumentException("Usage: java Main [schedules-file requests-file]");
        }

        Path schedulesPath = Paths.get(args.length == 2 ? args[0] : "schedules.txt");
        Path requestsPath = Paths.get(args.length == 2 ? args[1] : "requests.txt");
        List<AppointmentBook> teachers = loadTeachers(schedulesPath);
        int successfulRequests = processRequests(requestsPath, teachers);

        System.out.println(successfulRequests);
    }

    private static List<AppointmentBook> loadTeachers(Path path) throws IOException {
        List<String> rows = readNonEmptyLines(path);
        if (rows.size() % numPeriod != 0) {
            throw new IllegalArgumentException("Schedule file must contain 8 rows per teacher.");
        }

        List<AppointmentBook> teachers = new ArrayList<>();
        for (int row = 0; row < rows.size(); row += numPeriod) {
            boolean[][] availability = new boolean[numPeriod][minutesPeriod];
            for (int period = 0; period < numPeriod; period++) {
                String[] values = rows.get(row + period).trim().split("\\s+");
                if (values.length != minutesPeriod) {
                    throw new IllegalArgumentException("Each schedule row must contain 60 values.");
                }
                for (int minute = 0; minute < minutesPeriod; minute++) {
                    if (!values[minute].equals("true") && !values[minute].equals("false")) {
                        throw new IllegalArgumentException("Schedule values must be true or false.");
                    }
                    availability[period][minute] = values[minute].equals("true");
                }
            }
            teachers.add(new AppointmentBook(availability));
        }
        return teachers;
    }

    private static int processRequests(Path path, List<AppointmentBook> teachers) throws IOException {
        int successfulRequests = 0;
        List<String> requests = readNonEmptyLines(path);
        for (int lineNumber = 0; lineNumber < requests.size(); lineNumber++) {
            String[] values = requests.get(lineNumber).trim().split("\\s+");
            if (values.length != 4) {
                throw new IllegalArgumentException("Request line " + (lineNumber + 1)
                        + " must contain teacher, start period, end period, and duration.");
            }

            int teacherIndex = Integer.parseInt(values[0]);
            int startPeriod = Integer.parseInt(values[1]);
            int endPeriod = Integer.parseInt(values[2]);
            int duration = Integer.parseInt(values[3]);
            if (teacherIndex < 0 || teacherIndex >= teachers.size()) {
                throw new IllegalArgumentException("Invalid teacher on request line " + (lineNumber + 1) + ".");
            }
            if (teachers.get(teacherIndex).makeAppointment(startPeriod, endPeriod, duration)) {
                successfulRequests++;
            }
        }
        return successfulRequests;
    }

    private static List<String> readNonEmptyLines(Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        for (String line : Files.readAllLines(path)) {
            if (!line.trim().isEmpty()) {
                lines.add(line);
            }
        }
        return lines;
    }
}
