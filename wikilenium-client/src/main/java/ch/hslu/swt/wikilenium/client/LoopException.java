package ch.hslu.swt.wikilenium.client;

/**
 * Exception to be thrown when a loop is detected
 */
public class LoopException extends RuntimeException {

	/**
	 * The serial version identifier
	 */
	private static final long serialVersionUID = -2022774304198638161L;

	public LoopException() {
		super();
	}

	public LoopException(String message) {
		super(message);
	}
}
