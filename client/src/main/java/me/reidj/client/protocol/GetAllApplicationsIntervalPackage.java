package me.reidj.client.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.reidj.client.data.ApplicationData;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@RequiredArgsConstructor
public class GetAllApplicationsIntervalPackage extends CorePackage {

    public final String firstDate;
    public final String secondDate;
    public final String status;

    public Set<ApplicationData> applicationDataSet = new HashSet<>();
}
