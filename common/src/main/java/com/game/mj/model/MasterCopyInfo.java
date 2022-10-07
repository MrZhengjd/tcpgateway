package com.game.mj.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author zheng
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MasterCopyInfo {
    private String hostName;
    private long startCopyId;
    private long endCopyId;
}
