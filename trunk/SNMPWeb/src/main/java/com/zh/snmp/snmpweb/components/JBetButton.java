
package com.zh.snmp.snmpweb.components;

import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPostprocessingCallDecorator;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;

/**
 *
 * @author cserepj
 */
public abstract class JBetButton extends IndicatingAjaxButton {

    public JBetButton(String id, IModel<String> model, Form<?> form) {
        super(id, model, form);
    }

    public JBetButton(String id, Form<?> form) {
        super(id, form);
    }

    public JBetButton(String id, IModel<String> model) {
        super(id, model);
    }

    public JBetButton(String id) {
        super(id);
    }

       @Override
    protected IAjaxCallDecorator getAjaxCallDecorator() {
        return new AjaxPostprocessingCallDecorator(super.getAjaxCallDecorator()) {
            private static final long serialVersionUID = 1L;

            @Override
            public CharSequence postDecorateScript(CharSequence script) {
                return script + "this.disabled = 'disabled';";
            }

            @Override
            public CharSequence postDecorateOnFailureScript(CharSequence script) {
                return script +"this.removeAttribute('disabled');";
            }

            @Override
            public CharSequence postDecorateOnSuccessScript(CharSequence script) {
                  return script +"this.removeAttribute('disabled');";
           }

        };
    }

}
