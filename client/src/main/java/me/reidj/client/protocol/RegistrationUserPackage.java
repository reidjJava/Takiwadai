package me.reidj.client.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.reidj.client.exception.Exceptions;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class RegistrationUserPackage extends CorePackage {

    public final String email;
    public final String password;
    public final String name;
    public final String surname;
    public final String patronymic;

    public Exceptions exception;
}
