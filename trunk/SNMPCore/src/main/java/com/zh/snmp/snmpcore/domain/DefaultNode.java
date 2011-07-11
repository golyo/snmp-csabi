/*
 *   Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 * 
 *  This software is the confidential and proprietary information of
 *  Sonrisa Informatikai Kft. ("Confidential Information").
 *  You shall not disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into
 *  with Sonrisa.
 * 
 *  SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 *  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 *  ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 *  DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
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
