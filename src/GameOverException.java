//It now extends error rather than exception so that we can't accidentally catch it by catching exception e.
//Error is basically identical to exception but nothing will automatically catch them, since they don't extend exception.
//They are basically designed to be uncatchable exceptions, so that seems like it would be useful here.
public class GameOverException extends Error{
	
}
