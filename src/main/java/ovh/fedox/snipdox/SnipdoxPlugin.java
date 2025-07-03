package ovh.fedox.snipdox;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 7:05 PM
 */
public class SnipdoxPlugin {

	public static void error(Project project, String message) {
		NotificationGroupManager.getInstance()
				.getNotificationGroup("Snipdox Uploader")
				.createNotification("Error", message, NotificationType.ERROR)
				.notify(project);
	}

	public static void info(Project project, String message) {
		NotificationGroupManager.getInstance()
				.getNotificationGroup("Snipdox Uploader")
				.createNotification("Info", message, NotificationType.INFORMATION)
				.notify(project);
	}

	public static int showYesNoDialog(Project project, String title, String message) {
		return Messages.showYesNoDialog(project, message, title, Messages.getQuestionIcon());
	}

}
