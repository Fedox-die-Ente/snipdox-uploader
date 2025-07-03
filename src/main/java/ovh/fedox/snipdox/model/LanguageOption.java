package ovh.fedox.snipdox.model;

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 7:35 PM
 */

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 8:00 PM
 */
public class LanguageOption {
	private final String displayName;
	private final String value;

	public LanguageOption(String displayName, String value) {
		this.displayName = displayName;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String toString() {
		return displayName;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		LanguageOption that = (LanguageOption) obj;
		return value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
}