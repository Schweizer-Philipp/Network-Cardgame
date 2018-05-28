package Modelle;

import java.util.stream.Stream;

/**
 *
 * @author Philipp Schweizer
 */
public enum Role
{
    // Constanten
    CHEF(1),
    VITZE_CHEF(2),
    MITTELMANN(3),
    VIZE_ARSCH(4),
    ARSCH(5);

    // Attribute
    private final int position;

    private Role(int position)
    {
        this.position = position;
    }

    
    public int getPosition()
    {
        return position;
    }
    
    public static Role getRoleByValue(int value)
    {
        return Stream.of(values()).filter(e -> e.getPosition()==value).findFirst().orElseThrow(IndexOutOfBoundsException::new);        
    }

    @Override
    public String toString()
    {
        return String.format("Sie sind Platz %d geworden und haben damit die Role %s erreicht. ", position, name());
    }

}
