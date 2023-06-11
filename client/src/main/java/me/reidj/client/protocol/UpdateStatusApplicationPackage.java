package me.reidj.client.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.reidj.client.data.UserDto;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdateStatusApplicationPackage extends CorePackage {

    public final int applicationId;
    public final int creatorId;
    public final String status;
    public final String reason;

    public UserDto user;
}
