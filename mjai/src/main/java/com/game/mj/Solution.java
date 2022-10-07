package com.game.mj;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zheng
 */
public class Solution {
    /**
     * 回溯算法的思路
     */
    List<List<Integer>> ans = new ArrayList<>();
    public List<List<Integer>> combile(int n,int k){
        List<Integer> path = new ArrayList<>();
        dfs(1,n,k,path);
        return ans;
    }

    private void dfs(int index, int n, int k, List<Integer> path) {
        if (path.size() == k){
            ans.add(new ArrayList<>(path));
            return;
        }
        for (int i = index;i<= n;i++){
            path.add(i);
            dfs(i+1,n,k,path);
            path.remove(path.size()-1);
        }
    }
}
