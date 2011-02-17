package de.intecsoft;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.odlabs.wiquery.ui.core.DefaultJsScopeUiEvent;
import org.odlabs.wiquery.ui.slider.Slider;


/**
 * TODO
 */
public class SliderDemoPage extends WebPage {

    private static final long serialVersionUID = 1L;

    /**
     * TODO
     */
    private AbstractDefaultAjaxBehavior sliderBehavior;

    /**
     * The slider.
     */
    private Slider valueSl;

    /**
     * The text input field.
     */
    private TextField valueTf;

    /**
     * The value that is connected both to the slider and the input field
     */
    private int value = 50;

    /**
     * Constructor that is invoked when page is invoked without a session.
     */
    public SliderDemoPage() {
        super();

        sliderBehavior = new AbstractDefaultAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            /* (non-Javadoc)
             * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
             */
            @Override
            protected void respond(AjaxRequestTarget target) {
                value = Integer.parseInt(this.getComponent().getRequest().getParameter("value"));
                target.addComponent(valueTf);
            }
        };
        add(sliderBehavior);

        // attach this class as model to the ui
        setDefaultModel(new CompoundPropertyModel<SliderDemoPage>(this));

        // define the form that contains all ui components
        Form form = new Form("form");

        // slider
        valueSl = new Slider("valueSl", 0, 100);
        valueSl.setValue(value);
        valueSl.setOutputMarkupId(true);
        valueSl.setChangeEvent(
                new DefaultJsScopeUiEvent(
                        "wicketAjaxGet('" + sliderBehavior.getCallbackUrl(true)
                                + "&value='+" + Slider.UI_VALUE
                                + ", null,null, function() {return true;})"));
        form.add(valueSl);

        // textfield
        valueTf = new TextField("valueTf", new PropertyModel(this, "value"));
        valueTf.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                valueSl.setValue(value);
                target.addComponent(valueSl);
            }
        });
        form.add(valueTf);

        // attach the form to the page
        add(form);

    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
