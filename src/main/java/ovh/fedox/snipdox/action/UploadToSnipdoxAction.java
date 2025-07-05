package ovh.fedox.snipdox.action;

import com.intellij.lang.Language;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import ovh.fedox.snipdox.SnipdoxPlugin;
import ovh.fedox.snipdox.dialog.UploadDialog;
import ovh.fedox.snipdox.settings.SnipdoxSettingsConfigurable;
import ovh.fedox.snipdox.settings.SnipdoxSettingsState;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 7:11 PM
 */
public class UploadToSnipdoxAction extends DumbAwareAction {

	private static final Set<String> SUPPORTED_LANGUAGES = new HashSet<>();
	private static final Map<String, String> LANGUAGE_MAPPING = new HashMap<>();
	private static final Map<String, String> EXTENSION_MAPPING = new HashMap<>();

	static {
		SUPPORTED_LANGUAGES.add("javascript");
		SUPPORTED_LANGUAGES.add("typescript");
		SUPPORTED_LANGUAGES.add("python");
		SUPPORTED_LANGUAGES.add("java");
		SUPPORTED_LANGUAGES.add("csharp");
		SUPPORTED_LANGUAGES.add("cpp");
		SUPPORTED_LANGUAGES.add("c");
		SUPPORTED_LANGUAGES.add("go");
		SUPPORTED_LANGUAGES.add("rust");
		SUPPORTED_LANGUAGES.add("php");
		SUPPORTED_LANGUAGES.add("ruby");
		SUPPORTED_LANGUAGES.add("swift");
		SUPPORTED_LANGUAGES.add("kotlin");
		SUPPORTED_LANGUAGES.add("scala");
		SUPPORTED_LANGUAGES.add("dart");
		SUPPORTED_LANGUAGES.add("r");
		SUPPORTED_LANGUAGES.add("perl");
		SUPPORTED_LANGUAGES.add("lua");
		SUPPORTED_LANGUAGES.add("html");
		SUPPORTED_LANGUAGES.add("css");
		SUPPORTED_LANGUAGES.add("sql");
		SUPPORTED_LANGUAGES.add("json");
		SUPPORTED_LANGUAGES.add("xml");
		SUPPORTED_LANGUAGES.add("yaml");
		SUPPORTED_LANGUAGES.add("markdown");
		SUPPORTED_LANGUAGES.add("shell");
		SUPPORTED_LANGUAGES.add("bash");
		SUPPORTED_LANGUAGES.add("powershell");
		SUPPORTED_LANGUAGES.add("dockerfile");
		SUPPORTED_LANGUAGES.add("plaintext");
	}

	static {
		// Java
		LANGUAGE_MAPPING.put("JAVA", "java");

		// JavaScript/TypeScript
		LANGUAGE_MAPPING.put("JavaScript", "javascript");
		LANGUAGE_MAPPING.put("TypeScript", "typescript");
		LANGUAGE_MAPPING.put("ECMAScript 6", "javascript");

		// Python
		LANGUAGE_MAPPING.put("Python", "python");

		// C/C++
		LANGUAGE_MAPPING.put("C", "c");
		LANGUAGE_MAPPING.put("C++", "cpp");
		LANGUAGE_MAPPING.put("ObjectiveC", "c");

		// C#
		LANGUAGE_MAPPING.put("C#", "csharp");

		// Other languages
		LANGUAGE_MAPPING.put("Go", "go");
		LANGUAGE_MAPPING.put("Rust", "rust");
		LANGUAGE_MAPPING.put("PHP", "php");
		LANGUAGE_MAPPING.put("Ruby", "ruby");
		LANGUAGE_MAPPING.put("Swift", "swift");
		LANGUAGE_MAPPING.put("Kotlin", "kotlin");
		LANGUAGE_MAPPING.put("Scala", "scala");
		LANGUAGE_MAPPING.put("Dart", "dart");
		LANGUAGE_MAPPING.put("R", "r");
		LANGUAGE_MAPPING.put("Perl", "perl");
		LANGUAGE_MAPPING.put("Lua", "lua");

		// Web languages
		LANGUAGE_MAPPING.put("HTML", "html");
		LANGUAGE_MAPPING.put("CSS", "css");
		LANGUAGE_MAPPING.put("SCSS", "css");
		LANGUAGE_MAPPING.put("SASS", "css");
		LANGUAGE_MAPPING.put("LESS", "css");

		// Data formats
		LANGUAGE_MAPPING.put("JSON", "json");
		LANGUAGE_MAPPING.put("XML", "xml");
		LANGUAGE_MAPPING.put("YAML", "yaml");
		LANGUAGE_MAPPING.put("Properties", "plaintext");

		// Documentation
		LANGUAGE_MAPPING.put("Markdown", "markdown");

		// Shell
		LANGUAGE_MAPPING.put("Shell Script", "bash");
		LANGUAGE_MAPPING.put("Bash", "bash");
		LANGUAGE_MAPPING.put("PowerShell", "powershell");

		// Database
		LANGUAGE_MAPPING.put("SQL", "sql");

		// Docker
		LANGUAGE_MAPPING.put("Dockerfile", "dockerfile");

		// Vue/React/etc
		LANGUAGE_MAPPING.put("Vue", "javascript");
		LANGUAGE_MAPPING.put("Svelte", "javascript");
	}

	static {
		EXTENSION_MAPPING.put("java", "java");
		EXTENSION_MAPPING.put("js", "javascript");
		EXTENSION_MAPPING.put("mjs", "javascript");
		EXTENSION_MAPPING.put("jsx", "javascript");
		EXTENSION_MAPPING.put("ts", "typescript");
		EXTENSION_MAPPING.put("tsx", "typescript");
		EXTENSION_MAPPING.put("py", "python");
		EXTENSION_MAPPING.put("pyw", "python");
		EXTENSION_MAPPING.put("cpp", "cpp");
		EXTENSION_MAPPING.put("cc", "cpp");
		EXTENSION_MAPPING.put("cxx", "cpp");
		EXTENSION_MAPPING.put("c", "c");
		EXTENSION_MAPPING.put("cs", "csharp");
		EXTENSION_MAPPING.put("go", "go");
		EXTENSION_MAPPING.put("rs", "rust");
		EXTENSION_MAPPING.put("php", "php");
		EXTENSION_MAPPING.put("rb", "ruby");
		EXTENSION_MAPPING.put("swift", "swift");
		EXTENSION_MAPPING.put("kt", "kotlin");
		EXTENSION_MAPPING.put("scala", "scala");
		EXTENSION_MAPPING.put("dart", "dart");
		EXTENSION_MAPPING.put("r", "r");
		EXTENSION_MAPPING.put("pl", "perl");
		EXTENSION_MAPPING.put("lua", "lua");
		EXTENSION_MAPPING.put("html", "html");
		EXTENSION_MAPPING.put("htm", "html");
		EXTENSION_MAPPING.put("css", "css");
		EXTENSION_MAPPING.put("scss", "css");
		EXTENSION_MAPPING.put("sass", "css");
		EXTENSION_MAPPING.put("less", "css");
		EXTENSION_MAPPING.put("sql", "sql");
		EXTENSION_MAPPING.put("json", "json");
		EXTENSION_MAPPING.put("xml", "xml");
		EXTENSION_MAPPING.put("yml", "yaml");
		EXTENSION_MAPPING.put("yaml", "yaml");
		EXTENSION_MAPPING.put("md", "markdown");
		EXTENSION_MAPPING.put("sh", "bash");
		EXTENSION_MAPPING.put("bash", "bash");
		EXTENSION_MAPPING.put("ps1", "powershell");
		EXTENSION_MAPPING.put("dockerfile", "dockerfile");
		EXTENSION_MAPPING.put("vue", "javascript");
		EXTENSION_MAPPING.put("svelte", "javascript");
	}

	@Override
	public @NotNull ActionUpdateThread getActionUpdateThread() {
		return ActionUpdateThread.EDT;
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e) {
		Project p = e.getProject();
		if (p == null) {
			return;
		}

		SnipdoxSettingsState settings = SnipdoxSettingsState.getInstance();
		if (!settings.isApiKeyConfigured()) {
			int result = SnipdoxPlugin.showYesNoDialog(p, "API Key Required", "No API key configured. Would you like to open the settings to configure it?");

			if (result == 0) { // Yes
				ShowSettingsUtil.getInstance().showSettingsDialog(p, SnipdoxSettingsConfigurable.class);
			}
			return;
		}

		DataContext context = e.getDataContext();
		Editor editor = PlatformDataKeys.EDITOR.getData(context);
		if (editor == null) {
			SnipdoxPlugin.error(p, "This action requires an editor to be open");
			return;
		}
		if (!editor.getSelectionModel().hasSelection()) {
			SnipdoxPlugin.error(p, "Select some code to upload it");
			return;
		}

		String fileName = null;
		String language = "plaintext";

		VirtualFile currentFile = FileEditorManager.getInstance(p)
				.getSelectedFiles().length > 0 ? FileEditorManager.getInstance(p).getSelectedFiles()[0] : null;

		if (currentFile != null) {
			fileName = currentFile.getName();
			if (settings.isAutoDetectLanguage()) {
				language = detectLanguage(p, currentFile);
			}
		}

		UploadDialog dialog = new UploadDialog(p, fileName, language);
		if (dialog.showAndGet()) {
			String selectedExpiration = dialog.getSelectedExpiration();
			String title = dialog.getTitle();
			String selectedLanguage = dialog.getSelectedLanguage();
			String selectedCode = editor.getSelectionModel().getSelectedText();

			uploadToSnipdox(p, selectedCode, title, selectedExpiration, selectedLanguage, fileName);
		}
	}


	private String detectLanguage(Project project, VirtualFile file) {
		try {
			PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
			if (psiFile != null) {
				Language language = psiFile.getLanguage();
				String langName = language.getDisplayName();

				String mappedLang = LANGUAGE_MAPPING.get(langName);
				if (mappedLang != null && SUPPORTED_LANGUAGES.contains(mappedLang)) {
					return mappedLang;
				}
			}

			FileType fileType = file.getFileType();
			String typeName = fileType.getName();

			String mappedType = LANGUAGE_MAPPING.get(typeName);
			if (mappedType != null && SUPPORTED_LANGUAGES.contains(mappedType)) {
				return mappedType;
			}

			String extension = file.getExtension();
			if (extension != null) {
				String mappedExt = EXTENSION_MAPPING.get(extension.toLowerCase());
				if (mappedExt != null && SUPPORTED_LANGUAGES.contains(mappedExt)) {
					return mappedExt;
				}
			}

		} catch (Exception ex) {
			SnipdoxPlugin.error(project, "Error detecting language: " + ex.getMessage());
		}

		return "plaintext";
	}

	private void uploadToSnipdox(Project project, String code, String title, String expiration, String language, String fileName) {
		SnipdoxSettingsState settings = SnipdoxSettingsState.getInstance();

		ProgressManager.getInstance().run(new Task.Backgroundable(project, "Uploading to Snipdox...", true) {
			@Override
			public void run(@NotNull ProgressIndicator indicator) {
				try {
					indicator.setText("Preparing upload...");
					indicator.setFraction(0.1);

					String apiUrl = settings.getApiUrl();
					if (!apiUrl.endsWith("/")) {
						apiUrl += "/";
					}
					apiUrl += "paste/upload";

					indicator.setText("Connecting to Snipdox...");
					indicator.setFraction(0.3);

					String jsonBody = createJsonBody(code, title, expiration, language, fileName);

					indicator.setText("Uploading code...");
					indicator.setFraction(0.5);

					String response = makeHttpRequest(apiUrl, settings.getApiKey(), jsonBody);

					indicator.setText("Processing response...");
					indicator.setFraction(0.9);

					handleResponse(project, response, settings.isShowNotifications());

					indicator.setFraction(1.0);

				} catch (Exception ex) {
					ApplicationManager.getApplication().invokeLater(() -> {
						SnipdoxPlugin.error(project, "Upload failed: " + ex.getMessage());
					});
				}
			}
		});
	}

	private String createJsonBody(String code, String title, String expiration, String language, String fileName) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"paste_title\":").append(escapeJson(title)).append(",");
		json.append("\"content\":").append(escapeJson(code)).append(",");
		json.append("\"language\":").append(escapeJson(language)).append(",");
		json.append("\"expiration\":").append(escapeJson(expiration));

		if (fileName != null && !fileName.trim().isEmpty()) {
			json.append(",\"filename\":").append(escapeJson(fileName));
		}

		json.append("}");
		return json.toString();
	}

	private String escapeJson(String value) {
		if (value == null) {
			return "null";
		}

		StringBuilder escaped = new StringBuilder("\"");
		for (char c : value.toCharArray()) {
			switch (c) {
				case '"':
					escaped.append("\\\"");
					break;
				case '\\':
					escaped.append("\\\\");
					break;
				case '\b':
					escaped.append("\\b");
					break;
				case '\f':
					escaped.append("\\f");
					break;
				case '\n':
					escaped.append("\\n");
					break;
				case '\r':
					escaped.append("\\r");
					break;
				case '\t':
					escaped.append("\\t");
					break;
				default:
					if (c < ' ') {
						escaped.append(String.format("\\u%04x", (int) c));
					} else {
						escaped.append(c);
					}
					break;
			}
		}
		escaped.append("\"");
		return escaped.toString();
	}

	private String makeHttpRequest(String apiUrl, String apiKey, String jsonBody) throws IOException {
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		try {
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("x-api-key", apiKey);
			connection.setRequestProperty("User-Agent", "Snipdox-IntelliJ-Plugin/1.0");
			connection.setDoOutput(true);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(30000);

			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			int responseCode = connection.getResponseCode();

			InputStream inputStream;
			if (responseCode >= 200 && responseCode < 300) {
				inputStream = connection.getInputStream();
			} else {
				inputStream = connection.getErrorStream();
			}

			StringBuilder response = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
				String responseLine;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
			}

			if (responseCode >= 200 && responseCode < 300) {
				return response.toString();
			} else {
				throw new IOException("HTTP " + responseCode + ": " + response);
			}

		} finally {
			connection.disconnect();
		}
	}

	private void handleResponse(Project project, String response, boolean showNotifications) {
		ApplicationManager.getApplication().invokeLater(() -> {
			try {
				String pasteUrl = extractPasteUrl(response);

				if (showNotifications) {
					if (pasteUrl != null) {
						NotificationGroupManager.getInstance()
								.getNotificationGroup("Snipdox Uploader")
								.createNotification("Code uploaded successfully!", "Your paste is available at: " + pasteUrl, NotificationType.INFORMATION)
								.setTitle("Snipdox Uploader")
								.addAction(new CopyUrlAction(pasteUrl))
								.addAction(new OpenUrlAction(pasteUrl))
								.notify(project);
					} else {
						NotificationGroupManager.getInstance()
								.getNotificationGroup("Snipdox Uploader")
								.createNotification("Code uploaded successfully!", "Upload completed successfully.", NotificationType.INFORMATION)
								.setTitle("Snipdox Uploader")
								.notify(project);
					}
				}

			} catch (Exception ex) {
				SnipdoxPlugin.error(project, "Failed to process response: " + ex.getMessage());
			}
		});
	}

	private String extractPasteUrl(String jsonResponse) {
		try {
			int urlStart = jsonResponse.indexOf("\"url\":");
			if (urlStart != -1) {
				urlStart = jsonResponse.indexOf("\"", urlStart + 6);
				if (urlStart != -1) {
					int urlEnd = jsonResponse.indexOf("\"", urlStart + 1);
					if (urlEnd != -1) {
						return jsonResponse.substring(urlStart + 1, urlEnd);
					}
				}
			}

			urlStart = jsonResponse.indexOf("\"paste_url\":");
			if (urlStart != -1) {
				urlStart = jsonResponse.indexOf("\"", urlStart + 12);
				if (urlStart != -1) {
					int urlEnd = jsonResponse.indexOf("\"", urlStart + 1);
					if (urlEnd != -1) {
						return jsonResponse.substring(urlStart + 1, urlEnd);
					}
				}
			}
		} catch (Exception ex) {
			// Ignore parsing errors
		}

		return null;
	}

	@Override
	public void update(@NotNull AnActionEvent e) {
		Project project = e.getProject();
		if (project == null) {
			return;
		}

		DataContext context = e.getDataContext();
		Editor editor = PlatformDataKeys.EDITOR.getData(context);
		e.getPresentation().setEnabled(editor != null && editor.getSelectionModel().hasSelection());
	}
}