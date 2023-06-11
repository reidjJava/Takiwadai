package me.reidj.client.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginUserPackage extends CorePackage {

    public final String email;
    public final String password;

    public int userId;
    public String name;
    public String surname;
    public String patronymic;
}
