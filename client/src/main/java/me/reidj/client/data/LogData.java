package me.reidj.client.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LogData {

    public final int id;
    public final String creator;
    public final String dateCreation;
    public final String category;
    public final String description;
    public final String status;

    public final String reason;

    public LogData(String creator, String dateCreation, String category, String description, String status, String reason) {
        this(0, creator, dateCreation, category, description, status, reason);
    }
}
