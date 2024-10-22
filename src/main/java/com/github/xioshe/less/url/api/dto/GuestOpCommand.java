package com.github.xioshe.less.url.api.dto;

import lombok.Data;

@Data
public class GuestOpCommand {
    private String guestId;

    public String toOwnerId() {
        return "g_" + guestId;
    }
}
