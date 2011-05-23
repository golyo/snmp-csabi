/*
 *  Copyright (c) 2010 Sonrisa Informatikai Kft. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Sonrisa Informatikai Kft. ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sonrisa.
 *
 * SONRISA MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. SONRISA SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.zh.snmp.snmpweb.components;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

/**
 *
 * @author monusa
 */
public class ZhPagingNavigator extends PagingNavigator {

    public final static ResourceReference end = new ResourceReference(ZhPagingNavigator.class,
            "images/end.png");
    public final static ResourceReference forward = new ResourceReference(ZhPagingNavigator.class,
            "images/forward.png");
    public final static ResourceReference back = new ResourceReference(ZhPagingNavigator.class,
            "images/backward.png");
    public final static ResourceReference home = new ResourceReference(ZhPagingNavigator.class,
            "images/home.png");

    public ZhPagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
    }

    @Override
    public void onBeforeRender() {
        super.onBeforeRender();
        if (((WebMarkupContainer) get("last")).get("end") == null) {
            ((WebMarkupContainer) get("last")).add(new Image("end", end));
            ((WebMarkupContainer) get("next")).add(new Image("forward", forward));
            ((WebMarkupContainer) get("prev")).add(new Image("back", back));
            ((WebMarkupContainer) get("first")).add(new Image("home", home));
        }
    }
}
