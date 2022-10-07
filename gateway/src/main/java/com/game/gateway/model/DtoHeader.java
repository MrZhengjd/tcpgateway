package com.game.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author zheng
 */
@Getter
@Setter
@Component
//@AllArgsConstructor
public class DtoHeader {
    private String clientIp;
    private Long playerId;

    public DtoHeader() {

    }
}
