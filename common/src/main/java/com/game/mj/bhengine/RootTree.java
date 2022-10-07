package com.game.mj.bhengine;


import java.util.Map;

/**
 * @author zheng
 */
public class RootTree extends BaseBTree  {
    private CompositeBTree root;



    public RootTree(CompositeBTree root) {
        super();
        this.root = root;
    }

    public EStatus handleRequest(Map<String ,Object> request){
        return root.handleRequest(request);
    }

}
