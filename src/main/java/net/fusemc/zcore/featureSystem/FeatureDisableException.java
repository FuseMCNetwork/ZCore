package net.fusemc.zcore.featureSystem;

/**
 * @author michidk
 */

public class FeatureDisableException extends FeatureException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4733078888446071761L;

	public FeatureDisableException()
    {

    }

    public FeatureDisableException(String message)
    {
        super("A error in Z_Core occurred while disabling a feature: " + message);
    }

    public FeatureDisableException(Throwable cause)
    {
        super(cause);
    }

    public FeatureDisableException(String message, Throwable cause)
    {
        super("A error in Z_Core occurred while disabling a feature: " + message, cause);
    }

}
