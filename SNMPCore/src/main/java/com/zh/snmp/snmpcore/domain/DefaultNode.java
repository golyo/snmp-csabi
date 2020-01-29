package com.zh.snmp.snmpcore.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Golyo
 */
public class DefaultNode implements TreeNode {

    protected DefaultNode parent;
    protected List<? extends DefaultNode> children;
    
    public DefaultNode() {
        children = new ArrayList<DefaultNode>();
    }
    
    @Override
    public Enumeration children() {
        return Collections.enumeration(children);
    }

    @Override
    public boolean getAllowsChildren() {
        return Boolean.TRUE;
    }

    @Override
    public DefaultNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public DefaultNode getParent() {
        return parent;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
    
    public void setupParents() {
        for (DefaultNode child: children) {
            child.parent = this;
            child.setupParents();
        }
    }
}
