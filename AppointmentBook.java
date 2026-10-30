public class AppointmentBook {

    private boolean[][] schedule = new boolean[9][60];

    public AppointmentBook() {
        for (int period = 0; period < schedule.length; period++) {
            for (int minute = 0; minute < schedule[period].length; minute++) {
                schedule[period][minute] = true;
            }
        }
    }
    
    private boolean isMinuteFree(int period, int minute) {
        if (period == 2) {
            boolean allowedMinute = (minute >= 10 && minute <= 14)
                    || (minute >= 30 && minute <= 44)
                    || (minute >= 50 && minute <= 59);
            return allowedMinute && schedule[period][minute];
        }

        return schedule[period][minute];
    }

    private void reserveBlock(int period, int startMinute, int duration) {
        for (int m = startMinute; m < startMinute + duration; m++) {
            schedule[period][m] = false;
        }
    }

    private boolean isAppointmentMinuteFree(int period, int minute) {
        if (period == 2) {
            boolean allowedMinute = (minute >= 25 && minute <= 29);
            return allowedMinute && schedule[period][minute];
        }

        if (period == 3) {
            boolean allowedMinute = (minute >= 0 && minute <= 14)
                    || (minute >= 41 && minute <= 59);
            return allowedMinute && schedule[period][minute];
        }

        if (period == 4) {
            boolean allowedMinute = (minute >= 5 && minute <= 29)
                    || (minute >= 44 && minute <= 59);
            return allowedMinute && schedule[period][minute];
        }

        return schedule[period][minute];
    }

    private int findFreeAppointmentBlock(int period, int duration) {
        int blockLength = 0;

        for (int minute = 0; minute < 60; minute++) {
            if (isAppointmentMinuteFree(period, minute)) {
                blockLength++;
                if (blockLength == duration) {
                    return minute - blockLength + 1;
                }
            }
            else {
                blockLength = 0;
            }
        }
        return -1;
    }

    public int findFreeBlock(int period, int duration) {
        int blockLength = 0;

        for (int minute = 0; minute < 60; minute++) {
            if (isMinuteFree(period, minute)) {
                blockLength++;
                if (blockLength == duration) {
                    return minute - blockLength + 1;
                }
            }
            else {
                blockLength = 0;
            }
        }
        return -1;
    }
    
    public boolean makeAppointment(int startPeriod, int endPeriod, int duration) {
        for (int period = startPeriod; period <= endPeriod; period++) {
            int minute = findFreeAppointmentBlock(period, duration);
            if (minute != -1) {
                reserveBlock(period, minute, duration);
                return true;
            }
        }
        return false;
    }
}
