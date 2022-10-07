package com.game.mj.engine;

public class Engine implements EngineBase{

    @Override
    public Leaf process(QueryMap queryMap) {
        TreeMapVo treeMapVo = TreeContext.getTreeMapVoById(queryMap.getTreeId());
        if (treeMapVo == null){
            return null;
        }
        RuleContext context = TreeContext.getContextById(queryMap.getTreeId());
        RuleNode node = context.getNodeByNodeId(queryMap.getStartNodeId());
        BaseFilter baseFilter = new DefaultBaseFilter();
        while (node.getNodeType() == 2){
//            if (node.isSpecial()){
//
//                baseFilter =  FilterContext.getInstance().getFilterByKey(node.getKey());
//            }
            if(!queryMap.getDecesionMap().containsKey(node.getKey())){
                throw new IllegalArgumentException("don't have key ---------------------");
            }
            Object filterValue = queryMap.getDecesionMap().get(node.getKey());
            Long nextNodeId = baseFilter.filter((RuleNodeComposite) node,filterValue);
            node = context.getNodeByNodeId(nextNodeId);


        }

        return (Leaf) node;
    }
}
