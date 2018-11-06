package io.sdb.common.utils;

import com.alibaba.fastjson.JSON;
import io.sdb.common.entity.Node;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造目录JSON树
 * Created by fukang on 2017/5/26 0026.
 */
public class TreeBuilder<T extends Node> {

    List<T> nodes = new ArrayList<>();

    public List<T> buildTree(List<T> nodes) {
        if(nodes == null || nodes.size() == 0){
            return null;
        }

        TreeBuilder treeBuilder = new TreeBuilder(nodes);

        return treeBuilder.buildTree();
    }

    public String buildTreeJson(List<T> nodes) {
        if(nodes == null || nodes.size() == 0){
            return null;
        }

        TreeBuilder treeBuilder = new TreeBuilder(nodes);

        return treeBuilder.buildJSONTree();
    }

    public TreeBuilder() {
    }

    public TreeBuilder(List<T> nodes) {
        super();
        this.nodes = nodes;
    }

    // 构建JSON树形结构
    public String buildJSONTree() {
        List<T> nodeTree = buildTree();
        String jsonString = JSON.toJSONString(nodeTree);
        return jsonString;
    }

    // 构建树形结构
    public List<T> buildTree() {
        List<T> treeNodes = new ArrayList<>();
        List<T> rootNodes = getRootNodes();
        for (T rootNode : rootNodes) {
            buildChildNodes(rootNode);
            treeNodes.add(rootNode);
        }

        return treeNodes;
    }

    // 递归子节点
    public void buildChildNodes(T node) {
        List<T> children = getChildNodes(node);
        if (!children.isEmpty()) {
            for (T child : children) {
                buildChildNodes(child);
            }
            node.setChildren(children);
        }
    }

    // 获取父节点下所有的子节点
    public List<T> getChildNodes(T pnode) {
        List<T> childNodes = new ArrayList<>();
        for (T n : nodes) {
            if (pnode.getId().equals(n.getParentId())) {
                childNodes.add(n);
            }
        }
        return childNodes;
    }

    // 判断是否为根节点
    public boolean rootNode(T node) {
        boolean isRootNode = true;
        for (T n : nodes) {
            if (node.getParentId() != null && node.getParentId().equals(n.getId())) {
                isRootNode = false;
                break;
            }
        }
        return isRootNode;
    }

    // 获取集合中所有的根节点
    public List<T> getRootNodes() {
        List<T> rootNodes = new ArrayList<>();
        for (T n : nodes) {
            if (rootNode(n)) {
                rootNodes.add(n);
            }
        }
        return rootNodes;
    }
}
