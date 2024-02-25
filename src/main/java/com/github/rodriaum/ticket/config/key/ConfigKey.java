package com.github.rodriaum.ticket.config.key;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigKey {

    TOKEN("token", ""),
    GUILD_ID("guild-id", ""),
    CHANNEL_TICKET("channel.ticket", ""),
    CHANNEL_LOG("channel.log", ""),
    CATEGORY_TICKET("category.ticket-received", ""),
    ONLINE_STATUS("online.status", "dnd");

    private final String key;
    private final Object value;
}
