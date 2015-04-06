package net.fusemc.zcore.featureSystem;

/**
 * @author michidk
 */

public class FeatureEnableException extends FeatureException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4075685534413666692L;

	public FeatureEnableException()
    {

    }

    public FeatureEnableException(String message)
    {
        super("A error in Z_Core occurred while enabling a feature: " + message);
    }

    public FeatureEnableException(Throwable cause)
    {
        super(cause);
    }

    public FeatureEnableException(String message, Throwable cause)
    {
        super("A error in Z_Core occurred while enabling a feature: " + message, cause);
    }

}
