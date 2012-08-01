//It now extends throwable rather than exception so that we can't accidentally catch it by catching exception e.
//Throwable is basically identical to exception but nothing will automatically catch them, since it's a subclass of exception.
//They are basically designed subclass exceptions and errors, but they can be useful for non-caught exceptions.
public class GameOverException extends Throwable{}