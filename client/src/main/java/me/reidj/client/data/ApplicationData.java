package me.reidj.client.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApplicationData {

    public final int id;
    public final int creatorId;
    public final String creator;
    public final String dateCreation;
    public final String category;
    public final String description;
    public final String status;
    public final String reason;

    public ApplicationData(String creator, int creatorId, String dateCreation, String category, String description, String status, String reason) {
        this(0, creatorId, creator, dateCreation, category, description, status, reason);
    }

    public ApplicationData(String dateCreation, int creatorId, String category, String description, String status, String reason) {
        this(0, creatorId, null, dateCreation, category, description, status, reason);
    }

    public ApplicationData(String creator, String dateChange, String description) {
        this(creator, 0, dateChange, null, description, null, null);
    }
}
