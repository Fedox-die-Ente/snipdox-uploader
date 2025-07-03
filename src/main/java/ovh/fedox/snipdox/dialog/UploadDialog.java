package ovh.fedox.snipdox.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ovh.fedox.snipdox.model.ExpirationOption;
import ovh.fedox.snipdox.model.LanguageOption;

import javax.swing.*;
import java.awt.*;

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 7:26 PM
 */
public class UploadDialog extends DialogWrapper {
	private final String defaultFileName;
	private final String detectedLanguage;
	private JBTextField titleField;
	private ComboBox<ExpirationOption> expirationComboBox;
	private ComboBox<LanguageOption> languageComboBox;

	public UploadDialog(@Nullable Project project, String fileName, String language) {
		super(project);
		this.defaultFileName = fileName;
		this.detectedLanguage = language;
		setTitle("Upload to Snipdox");
		init();
	}

	@Override
	protected @Nullable JComponent createCenterPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// Title Field
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(5, 5, 5, 5);
		panel.add(new JBLabel("Title:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		titleField = new JBTextField();
		if (defaultFileName != null) {
			titleField.setText(defaultFileName);
		}
		panel.add(titleField, gbc);

		// Language ComboBox
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;
		panel.add(new JBLabel("Language:"), gbc);

		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		languageComboBox = new ComboBox<>(new LanguageOption[]{ new LanguageOption("Auto-detected: " + getLanguageDisplayName(detectedLanguage), detectedLanguage), new LanguageOption("Plain Text", "plaintext"), new LanguageOption("Java", "java"), new LanguageOption("JavaScript", "javascript"), new LanguageOption("TypeScript", "typescript"), new LanguageOption("Python", "python"), new LanguageOption("C++", "cpp"), new LanguageOption("C", "c"), new LanguageOption("C#", "csharp"), new LanguageOption("Go", "go"), new LanguageOption("Rust", "rust"), new LanguageOption("PHP", "php"), new LanguageOption("Ruby", "ruby"), new LanguageOption("Swift", "swift"), new LanguageOption("Kotlin", "kotlin"), new LanguageOption("Scala", "scala"), new LanguageOption("Dart", "dart"), new LanguageOption("R", "r"), new LanguageOption("Perl", "perl"), new LanguageOption("Lua", "lua"), new LanguageOption("HTML", "html"), new LanguageOption("CSS", "css"), new LanguageOption("SQL", "sql"), new LanguageOption("JSON", "json"), new LanguageOption("XML", "xml"), new LanguageOption("YAML", "yaml"), new LanguageOption("Markdown", "markdown"), new LanguageOption("Shell/Bash", "bash"), new LanguageOption("PowerShell", "powershell"), new LanguageOption("Dockerfile", "dockerfile") });
		languageComboBox.setSelectedIndex(0); // Auto-detected
		panel.add(languageComboBox, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(new JBLabel("Expiration:"), gbc);

		gbc.gridx = 1;
		expirationComboBox = new ComboBox<>(new ExpirationOption[]{ new ExpirationOption("1 Hour", "1h"), new ExpirationOption("1 Day", "1d"), new ExpirationOption("1 Week", "1w"), new ExpirationOption("1 Month", "1m"), new ExpirationOption("Never", "never") });
		expirationComboBox.setSelectedIndex(1); // Default: 1 Day
		panel.add(expirationComboBox, gbc);

		return panel;
	}

	private String getLanguageDisplayName(String language) {
		return switch (language) {
			case "javascript" -> "JavaScript";
			case "typescript" -> "TypeScript";
			case "python" -> "Python";
			case "java" -> "Java";
			case "csharp" -> "C#";
			case "cpp" -> "C++";
			case "c" -> "C";
			case "go" -> "Go";
			case "rust" -> "Rust";
			case "php" -> "PHP";
			case "ruby" -> "Ruby";
			case "swift" -> "Swift";
			case "kotlin" -> "Kotlin";
			case "scala" -> "Scala";
			case "dart" -> "Dart";
			case "r" -> "R";
			case "perl" -> "Perl";
			case "lua" -> "Lua";
			case "html" -> "HTML";
			case "css" -> "CSS";
			case "sql" -> "SQL";
			case "json" -> "JSON";
			case "xml" -> "XML";
			case "yaml" -> "YAML";
			case "markdown" -> "Markdown";
			case "bash" -> "Shell/Bash";
			case "shell" -> "Shell/Bash";
			case "powershell" -> "PowerShell";
			case "dockerfile" -> "Dockerfile";
			default -> "Plain Text";
		};
	}

	public String getSelectedExpiration() {
		ExpirationOption selected = (ExpirationOption) expirationComboBox.getSelectedItem();
		return selected != null ? selected.getValue() : "1d";
	}

	public String getTitle() {
		String title = titleField.getText().trim();
		return title.isEmpty() ? "Untitled" : title;
	}

	public String getSelectedLanguage() {
		LanguageOption selected = (LanguageOption) languageComboBox.getSelectedItem();
		return selected != null ? selected.getValue() : detectedLanguage;
	}

	@Override
	protected Action @NotNull [] createActions() {
		return new Action[]{ getOKAction(), getCancelAction() };
	}

	@Override
	public @Nullable JComponent getPreferredFocusedComponent() {
		return titleField;
	}
}