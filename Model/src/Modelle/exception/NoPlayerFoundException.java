package Modelle.exception;

/**
 * <strong>Project:</strong> NetzwerkKartenspielModelle <br>
 * <strong>File:</strong> NoPlayerFoundException <br>
 *
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class NoPlayerFoundException extends RuntimeException
{

    ////////////////////////////////////////////////////////////////////////////
    // Constances
    ////////////////////////////////////////////////////////////////////////////
    private final static String FILE_NAME = "NoPlayerFoundException";

    ////////////////////////////////////////////////////////////////////////////
    // Fields
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////////////////////////
    
   /**
    * Default-Constructor
    */
    public NoPlayerFoundException() 
    {   
        super();
    }

    public NoPlayerFoundException(String message)
    {
        super(message);
    }


}