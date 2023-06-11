package me.reidj.client.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class CreateApplicationPackage extends CorePackage {

    public final int userId;
    public final String description;
    public final String category;
    public final String status;
}
