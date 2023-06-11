package me.reidj.application.misc;

public enum StatusType {
    SEND("Отправлено"),
    IN_WORK("В работе"),
    CORRECTED("Выполнено"),
    DENIED("Отказ")
    ;

    private final String title;

    StatusType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
