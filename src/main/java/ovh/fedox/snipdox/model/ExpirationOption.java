package ovh.fedox.snipdox.model;

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 7:25 PM
 */
public class ExpirationOption {
	private final String displayName;
	private final String value;

	public ExpirationOption(String displayName, String value) {
		this.displayName = displayName;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return displayName;
	}
}