public class AppointmentBook {

    private static final int minutesPeriod = 60;
    private static final int numPeriod = 8;

    private final boolean[][] schedule = new boolean[numPeriod + 1][minutesPeriod];

    public AppointmentBook() {
        for (int period = 1; period <= numPeriod; period++) {
            for (int minute = 0; minute < minutesPeriod; minute++) {
                schedule[period][minute] = true;
            }
        }
    }

    public AppointmentBook(boolean[][] availability) {
        if (availability.length != numPeriod) {
            throw new IllegalArgumentException("A schedule must contain exactly 8 periods.");
        }

        for (int period = 0; period < numPeriod; period++) {
            if (availability[period].length != minutesPeriod) {
                throw new IllegalArgumentException("Each period must contain exactly 60 minutes.");
            }
            System.arraycopy(availability[period], 0, schedule[period + 1], 0, minutesPeriod);
        }
    }

    private void reserveBlock(int period, int startMinute, int duration) {
        for (int minute = startMinute; minute < startMinute + duration; minute++) {
            schedule[period][minute] = false;
        }
    }

    public int findFreeBlock(int period, int duration) {
        if (period < 1 || period > numPeriod || duration < 1 || duration > minutesPeriod) {
            return -1;
        }

        int blockLength = 0;
        for (int minute = 0; minute < minutesPeriod; minute++) {
            if (schedule[period][minute]) {
                blockLength++;
                if (blockLength == duration) {
                    return minute - blockLength + 1;
                }
            } else {
                blockLength = 0;
            }
        }
        return -1;
    }

    public boolean makeAppointment(int startPeriod, int endPeriod, int duration) {
        if (startPeriod < 1 || endPeriod > numPeriod || startPeriod > endPeriod
                || duration < 1 || duration > minutesPeriod) {
            return false;
        }

        for (int period = startPeriod; period <= endPeriod; period++) {
            int minute = findFreeBlock(period, duration);
            if (minute != -1) {
                reserveBlock(period, minute, duration);
                return true;
            }
        }
        return false;
    }
}
