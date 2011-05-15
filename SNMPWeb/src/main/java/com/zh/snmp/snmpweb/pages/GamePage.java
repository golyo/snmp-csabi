/*
 *  Copyright 2010 Tomi.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.zh.snmp.snmpweb.pages;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 *
 * @author golyo
 */
public class GamePage extends WebPage {
    private static final long serialVersionUID = 1L;

    public GamePage(final PageParameters parameters) {
        super(parameters);
        add(new Label("lb", "szar"));
        add(new AjaxLink("testLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                int i = 0;
                int j = i;
            }
        });
    }
}
