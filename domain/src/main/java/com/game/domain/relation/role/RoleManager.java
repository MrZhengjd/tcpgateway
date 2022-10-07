package com.game.domain.relation.role;


/**
 * @author zheng
 */
public  class RoleManager<T extends BaseRole> {
    private T role;

    protected RoleManager(T role) {
        this.role = role;
    }

    public T getRole() {
        return role;
    }

    public void setRole(T role) {
        this.role = role;
    }
}
