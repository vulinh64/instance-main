void main() {
    int accumulatedDaysOfMonth = daysOfMonth(1);
    int currentDayOfSunday = 6; // Jan 6 2019

    List<Integer> monthsWithFiveSundays = new ArrayList<>();

    for (int i = 1; i <= 12; i++) {
        int sundayCount = 0;

        while (currentDayOfSunday <= accumulatedDaysOfMonth) {
            sundayCount++;
            currentDayOfSunday += 7;
        }

        if (i < 12) {
            accumulatedDaysOfMonth += daysOfMonth(i + 1);
        }

        if (sundayCount == 5) {
            monthsWithFiveSundays.add(i);
        }
    }

    println(monthsWithFiveSundays);
}

int daysOfMonth(int month) {
    if (month < 1 || month > 12) {
        throw new IllegalArgumentException("Invalid month: " + month);
    }

    return switch (month) {
        case 2 -> 28;
        case 4, 6, 9, 11 -> 30;
        default -> 31;
    };
}

int daysOfMonth(int month, int year) {
    if (month < 1 || month > 12) {
        throw new IllegalArgumentException("Invalid month: " + month);
    }

    return switch (month) {
        case 2 -> year % 400 == 0 || year % 4 == 0 && year % 100 != 0 ? 29 : 28;
        case 4, 6, 9, 11 -> 30;
        default -> 31;
    };
}