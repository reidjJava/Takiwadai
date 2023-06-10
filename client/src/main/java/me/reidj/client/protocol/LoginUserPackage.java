package me.reidj.client.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
public class LoginUserPackage extends CorePackage {

    private final String email;
    private final String password;
}
