package ovh.fedox.snipdox.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 7:45 PM
 */
public class SnipdoxSettingsConfigurable implements Configurable {

	private SnipdoxSettingsComponent mySettingsComponent;

	@Nls( capitalization = Nls.Capitalization.Title )
	@Override
	public String getDisplayName() {
		return "Snipdox";
	}

	@Override
	public JComponent getPreferredFocusedComponent() {
		return mySettingsComponent.getPreferredFocusedComponent();
	}

	@Nullable
	@Override
	public JComponent createComponent() {
		mySettingsComponent = new SnipdoxSettingsComponent();
		return mySettingsComponent.getPanel();
	}

	@Override
	public boolean isModified() {
		SnipdoxSettingsState settings = SnipdoxSettingsState.getInstance();
		boolean modified = false;

		modified |= !mySettingsComponent.getApiKey().equals(settings.getApiKey());
		modified |= !mySettingsComponent.getApiUrl().equals(settings.getApiUrl());
		modified |= mySettingsComponent.getShowNotifications() != settings.isShowNotifications();
		modified |= mySettingsComponent.getAutoDetectLanguage() != settings.isAutoDetectLanguage();

		return modified;
	}

	@Override
	public void apply() {
		SnipdoxSettingsState settings = SnipdoxSettingsState.getInstance();
		settings.setApiKey(mySettingsComponent.getApiKey());
		settings.setApiUrl(mySettingsComponent.getApiUrl());
		settings.setShowNotifications(mySettingsComponent.getShowNotifications());
		settings.setAutoDetectLanguage(mySettingsComponent.getAutoDetectLanguage());
	}

	@Override
	public void reset() {
		SnipdoxSettingsState settings = SnipdoxSettingsState.getInstance();
		mySettingsComponent.setApiKey(settings.getApiKey());
		mySettingsComponent.setApiUrl(settings.getApiUrl());
		mySettingsComponent.setShowNotifications(settings.isShowNotifications());
		mySettingsComponent.setAutoDetectLanguage(settings.isAutoDetectLanguage());
	}

	@Override
	public void disposeUIResources() {
		mySettingsComponent = null;
	}
}