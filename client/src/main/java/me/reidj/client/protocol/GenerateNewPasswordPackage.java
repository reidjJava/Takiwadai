package me.reidj.client.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.reidj.client.exception.Exceptions;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@RequiredArgsConstructor
public class GenerateNewPasswordPackage extends CorePackage {

    public final String email;

    public String name;
    public String newPassword;
    public Exceptions exception;
}
