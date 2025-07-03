package ovh.fedox.snipdox.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 7:44 PM
 */
public class SnipdoxSettingsComponent {
	private final JPanel myMainPanel;
	private final JBPasswordField myApiKeyField = new JBPasswordField();
	private final JBTextField myApiUrlField = new JBTextField();
	private final JBCheckBox myShowNotificationsCheckBox = new JBCheckBox("Show upload notifications");
	private final JBCheckBox myAutoDetectLanguageCheckBox = new JBCheckBox("Auto-detect programming language");

	public SnipdoxSettingsComponent() {
		myMainPanel = FormBuilder.createFormBuilder()
				.addLabeledComponent(new JBLabel("API Key: "), myApiKeyField, 1, false)
				.addComponentFillVertically(new JPanel(), 0)
				.addLabeledComponent(new JBLabel("API URL: "), myApiUrlField, 1, false)
				.addComponentFillVertically(new JPanel(), 0)
				.addComponent(myShowNotificationsCheckBox, 0)
				.addComponent(myAutoDetectLanguageCheckBox, 0)
				.addComponentFillVertically(new JPanel(), 0)
				.addSeparator(5)
				.addComponent(createInfoPanel(), 0)
				.addComponentFillVertically(new JPanel(), 0)
				.getPanel();

		myShowNotificationsCheckBox.setSelected(true);
		myAutoDetectLanguageCheckBox.setSelected(true);
		myApiUrlField.setText("https://snipdox.fedox.ovh/api");
	}

	private JPanel createInfoPanel() {
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

		JBLabel infoLabel = new JBLabel("<html><body style='width: 400px'>" + "<b>How to get your API Key:</b><br>" + "1. Visit <a href='https://snipdox.fedox.ovh'>snipdox.fedox.ovh</a><br>" + "2. Sign in to your account<br>" + "3. Go to Settings → API Keys<br>" + "4. Generate a new API key<br>" + "5. Copy and paste it above<br><br>" + "<b>Note:</b> Your API key is stored securely in IntelliJ's settings." + "</body></html>");

		infoPanel.add(infoLabel);
		return infoPanel;
	}

	public JPanel getPanel() {
		return myMainPanel;
	}

	public JComponent getPreferredFocusedComponent() {
		return myApiKeyField;
	}

	@NotNull
	public String getApiKey() {
		return new String(myApiKeyField.getPassword());
	}

	public void setApiKey(@NotNull String newText) {
		myApiKeyField.setText(newText);
	}

	@NotNull
	public String getApiUrl() {
		return myApiUrlField.getText();
	}

	public void setApiUrl(@NotNull String newText) {
		myApiUrlField.setText(newText);
	}

	public boolean getShowNotifications() {
		return myShowNotificationsCheckBox.isSelected();
	}

	public void setShowNotifications(boolean newStatus) {
		myShowNotificationsCheckBox.setSelected(newStatus);
	}

	public boolean getAutoDetectLanguage() {
		return myAutoDetectLanguageCheckBox.isSelected();
	}

	public void setAutoDetectLanguage(boolean newStatus) {
		myAutoDetectLanguageCheckBox.setSelected(newStatus);
	}
}