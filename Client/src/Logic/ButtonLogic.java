package Logic;

import Windows.GameWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class ButtonLogic implements ActionListener
{

    private GameWindow gameWindow;

    public ButtonLogic(GameWindow gameWindow)
    {
        this.gameWindow = gameWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand()
                .equals("Zug Überspringen"))
        {
            gameWindow.sendGameFieldStatus(false);
        }
        else
        {
            if (e.getActionCommand()
                    .equals("Spielen"))
            {
                gameWindow.sendGameFieldStatus(true);
            }
        }
    }

}
