module me.reidj.client {
    requires io.nats.jnats;
    requires com.google.gson;
    requires static lombok;

    exports me.reidj.client.protocol;
    exports me.reidj.client.network;
    exports me.reidj.client.exception;
    exports me.reidj.client.util;
}