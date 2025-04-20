// In the getDateTimeInput() method
private static LocalDateTime getDateTimeInput(String prompt) {
    LocalDateTime dateTime = null;
    boolean valid = false;

    while (!valid) {
        try {
            System.out.print(prompt);
            String input = scanner.nextLine();
            dateTime = LocalDateTime.parse(input, formatter);
            valid = true;
        } catch (DateTimeParseException e) {
            try {
                throw new InvalidDateFormatException("Invalid format. Please use yyyy-MM-dd HH:mm");
            } catch (InvalidDateFormatException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    return dateTime;
}
