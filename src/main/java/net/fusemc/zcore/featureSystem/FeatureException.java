package net.fusemc.zcore.featureSystem;

/**
 * A Exception for multiple purposes
 *
 * @author michidk
 */
public class FeatureException extends Exception
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -839105512060117351L;

	public FeatureException()
    {

    }

    public FeatureException(String message)
    {
        super("A error occured in Z_Core: " + message);
    }

    public FeatureException(Throwable cause)
    {
        super(cause);
    }

    public FeatureException(String message, Throwable cause)
    {
        super("A error occured in Z_Core: " + message, cause);
    }

}
