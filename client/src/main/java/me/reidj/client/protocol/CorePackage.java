package me.reidj.client.protocol;

import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class CorePackage {

    private final String id = UUID.randomUUID().toString();
}
