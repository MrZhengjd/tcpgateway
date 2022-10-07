package com.game.domain.repository.token;

/**
 * @author zheng
 */
public interface TokenRepository {

    String getToken(byte[] data);
}
