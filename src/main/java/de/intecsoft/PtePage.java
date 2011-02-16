package de.intecsoft;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.CompoundPropertyModel;
import org.odlabs.wiquery.ui.core.DefaultJsScopeUiEvent;
import org.odlabs.wiquery.ui.slider.Slider;


/**
 * TODO
 */
public class PtePage extends WebPage {

    private static final long serialVersionUID = 1L;

    private AbstractDefaultAjaxBehavior sliderBehavior;

    //********* ui components ************//
    // sliders to adjust the three values
    private Slider sliderCheap;
    private Slider sliderGood;
    private Slider sliderFast;

    // checkboxes to fix one of the three values
    private CheckBox cbCheap;
    private CheckBox cbGood;
    private CheckBox cbFast;

    //********** value holder connected with the UI components **********//
    // is one of the sliders fixed?
    private boolean cheapFix = false;
    private boolean goodFix = false;
    private boolean fastFix = false;

    // current values adjusted in the sliders
    private int cheapValue = 50;
    private int fastValue = 50;
    private int goodValue = 50;

    /**
     * Constructor that is invoked when page is invoked without a session.
     *
     * @param parameters Page parameters
     */
    public PtePage(final PageParameters parameters) {
        super(parameters);

        // behaviour that will be connected to the slides
        // it defines a callback function that will be called if
        // one of the sliders changes its value
        // TODO check if its better to have a dedicated behaviour to avery slider
        sliderBehavior = new AbstractDefaultAjaxBehavior() {
            private static final long serialVersionUID = 1L;

            /* (non-Javadoc)
             * @see org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#respond(org.apache.wicket.ajax.AjaxRequestTarget)
             */
            @Override
            protected void respond(AjaxRequestTarget target) {
                int diffCheap = 0;
                int diffFast = 0;
                int diffGood = 0;

                // check from which slider the new value was generated
                if (null != this.getComponent().getRequest().getParameter("cheapValue")) {
                    diffCheap = cheapValue - Integer.parseInt(this.getComponent().getRequest().getParameter("cheapValue"));
                } else if (null != this.getComponent().getRequest().getParameter("goodValue")) {
                    diffGood = goodValue - Integer.parseInt(this.getComponent().getRequest().getParameter("goodValue"));
                } else if (null != this.getComponent().getRequest().getParameter("fastValue")) {
                    diffFast = fastValue - Integer.parseInt(this.getComponent().getRequest().getParameter("fastValue"));
                }

                // recalculate the new values
                recalcValues(diffCheap, diffFast, diffGood);

                // attach the new values to the sliders
                sliderCheap.setValue(cheapValue);
                sliderFast.setValue(fastValue);
                sliderGood.setValue(goodValue);

                // update the ui components
                target.addComponent(sliderCheap);
                target.addComponent(sliderFast);
                target.addComponent(sliderGood);
            }
        };
        add(sliderBehavior);

        // attach this class as model to the ui
        setDefaultModel(new CompoundPropertyModel<PtePage>(this));

        // define the form that contains all ui components
        Form pteForm = new Form("pte");

        // checkbox and slider for the GOOD value
        cbGood = new CheckBox("goodFix");
        cbGood.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (isGoodFix()) {
                    setCheapFix(false);
                    setFastFix(false);
                }
                target.addComponent(sliderCheap);
                target.addComponent(sliderFast);
                target.addComponent(sliderGood);
                target.addComponent(cbCheap);
                target.addComponent(cbFast);
            }
        });
        pteForm.add(cbGood);

        sliderGood = new Slider("goodValue", 0, 100);
        sliderGood.setValue(goodValue);
        sliderGood.setOutputMarkupId(true);
        sliderGood.setChangeEvent(
                new DefaultJsScopeUiEvent(
                        "wicketAjaxGet('" + sliderBehavior.getCallbackUrl(true)
                                + "&goodValue='+" + Slider.UI_VALUE
                                + ", null,null, function() {return true;})"));
        pteForm.add(sliderGood);

        // checkbox and slider for the CHEAP value
        cbCheap = new CheckBox("cheapFix");
        cbCheap.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (isCheapFix()) {
                    setGoodFix(false);
                    setFastFix(false);
                }
                target.addComponent(sliderCheap);
                target.addComponent(sliderFast);
                target.addComponent(sliderGood);
                target.addComponent(cbGood);
                target.addComponent(cbFast);
            }
        });
        pteForm.add(cbCheap);

        sliderCheap = new Slider("cheapValue", 0, 100);
        sliderCheap.setValue(cheapValue);
        sliderCheap.setOutputMarkupId(true);
        sliderCheap.setChangeEvent(
                new DefaultJsScopeUiEvent(
                        "wicketAjaxGet('" + sliderBehavior.getCallbackUrl(true)
                                + "&cheapValue='+" + Slider.UI_VALUE
                                + ", null,null, function() {return true;})"));
        pteForm.add(sliderCheap);


        // checkbox and slider for the FAST value
        cbFast = new CheckBox("fastFix");
        cbFast.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (isFastFix()) {
                    setCheapFix(false);
                    setGoodFix(false);
                }
                target.addComponent(sliderCheap);
                target.addComponent(sliderFast);
                target.addComponent(sliderGood);
                target.addComponent(cbGood);
                target.addComponent(cbCheap);
            }
        });
        pteForm.add(cbFast);

        sliderFast = new Slider("fastValue", 0, 100);
        sliderFast.setValue(fastValue);
        sliderFast.setOutputMarkupId(true);
        sliderFast.setChangeEvent(
                new DefaultJsScopeUiEvent(
                        "wicketAjaxGet('" + sliderBehavior.getCallbackUrl(true)
                                + "&fastValue='+" + Slider.UI_VALUE
                                + ", null,null, function() {return true;})"));
        pteForm.add(sliderFast);

        // attach the form to the page
        add(pteForm);

    }

    /**
     * According to the new values provided by the ui (thats what the user wants to set),
     * the current values and the fix state to any of the sliders the real new values (thats what possible
     * according to the logic behind) will be calculated.
     */
    private void recalcValues(int diffCheap, int diffFast, int diffGood) {
        System.out.println("recalcValues: diffCheap = " + diffCheap + "; diffFast = " + diffFast + "; diffGood = " + diffGood);
        goodValue = goodValue - diffGood;
        fastValue = fastValue - diffFast;
        cheapValue = cheapValue - diffCheap;

        // reset values if fix is set
        if (isCheapFix()) {
            cheapValue = cheapValue + diffCheap;
            if (diffGood != 0) {
                fastValue = fastValue + diffGood;
            } else if (diffFast != 0) {
                goodValue = goodValue + diffFast;
            }
        } else if (isGoodFix()) {
            goodValue = goodValue + diffGood;
            if (diffCheap != 0) {
                fastValue = fastValue + diffCheap;
            } else if (diffFast != 0) {
                cheapValue = cheapValue + diffFast;
            }
        } else if (isFastFix()) {
            fastValue = fastValue + diffFast;
            if (diffGood != 0) {
                cheapValue = cheapValue + diffGood;
            } else if (diffCheap != 0) {
                goodValue = goodValue + diffCheap;
            }
        }
    }

    public boolean isCheapFix() {
        return cheapFix;
    }

    public void setCheapFix(boolean cheapFix) {
        this.cheapFix = cheapFix;
    }

    public boolean isGoodFix() {
        return goodFix;
    }

    public void setGoodFix(boolean goodFix) {
        this.goodFix = goodFix;
    }

    public boolean isFastFix() {
        return fastFix;
    }

    public void setFastFix(boolean fastFix) {
        this.fastFix = fastFix;
    }

    public int getCheapValue() {
        return cheapValue;
    }

    public void setCheapValue(int cheapValue) {
        this.cheapValue = cheapValue;
    }

    public int getGoodValue() {
        return goodValue;
    }

    public void setGoodValue(int goodValue) {
        this.goodValue = goodValue;
    }

    public int getFastValue() {
        return fastValue;
    }

    public void setFastValue(int fastValue) {
        this.fastValue = fastValue;
    }

}
