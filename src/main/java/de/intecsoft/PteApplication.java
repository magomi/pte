package de.intecsoft;

import org.odlabs.wiquery.utils.WiQueryWebApplication;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see de.intecsoft.Start#main(String[])
 */
public class PteApplication extends WiQueryWebApplication {    
    /**
     * Constructor
     */
	public PteApplication()
	{
	}

    /**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	public Class<PtePage> getHomePage()
	{
		return PtePage.class;
	}

}
