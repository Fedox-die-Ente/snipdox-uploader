package ovh.fedox.snipdox.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 7:43 PM
 */
@State( name = "ovh.fedox.snipdox.settings.SnipdoxSettingsState", storages = @Storage( "SnipdoxSettings.xml" ) )
public class SnipdoxSettingsState implements PersistentStateComponent<SnipdoxSettingsState> {

	public String apiKey = "";
	public String apiUrl = "https://snipdox.fedox.ovh/api";
	public boolean showNotifications = true;
	public boolean autoDetectLanguage = true;

	public static SnipdoxSettingsState getInstance() {
		return ApplicationManager.getApplication().getService(SnipdoxSettingsState.class);
	}

	@Override
	public @Nullable SnipdoxSettingsState getState() {
		return this;
	}

	@Override
	public void loadState(@NotNull SnipdoxSettingsState state) {
		XmlSerializerUtil.copyBean(state, this);
	}

	// Getter und Setter
	public String getApiKey() {
		return apiKey != null ? apiKey : "";
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getApiUrl() {
		return apiUrl != null ? apiUrl : "https://snipdox.fedox.ovh/api";
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public boolean isShowNotifications() {
		return showNotifications;
	}

	public void setShowNotifications(boolean showNotifications) {
		this.showNotifications = showNotifications;
	}

	public boolean isAutoDetectLanguage() {
		return autoDetectLanguage;
	}

	public void setAutoDetectLanguage(boolean autoDetectLanguage) {
		this.autoDetectLanguage = autoDetectLanguage;
	}

	public boolean isApiKeyConfigured() {
		return apiKey != null && !apiKey.trim().isEmpty();
	}
}