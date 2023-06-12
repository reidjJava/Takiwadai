package me.reidj.client.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class AddToChangelogPackage extends CorePackage {

    public final long dateChange;
    public final int userId;
    public final String description;
}
