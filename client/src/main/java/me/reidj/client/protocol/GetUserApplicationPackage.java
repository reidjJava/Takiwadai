package me.reidj.client.protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.reidj.client.data.LogData;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class GetUserApplicationPackage extends CorePackage {

    public final int userId;

    public Set<LogData> logDataSet = new HashSet<>();
}
