 private static LocalDateTime getDateTimeInput(String prompt) {
        LocalDateTime dateTime = null;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                dateTime = LocalDateTime.parse(input, formatter);
                valid = true;
            } catch (Exception e) {
                System.out.println("Invalid format. Please use yyyy-MM-dd HH:mm");
            }
        }

        return dateTime;
    }
