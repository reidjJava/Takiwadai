package me.reidj.client.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class UpdateUserDataPackage extends CorePackage {

    public final int userId;
    public final String surname;
    public final String name;
    public final String patronymic;
    public final String password;
}
