package ovh.fedox.snipdox.action;

import com.intellij.ide.BrowserUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 8:17 PM
 */
public class OpenUrlAction extends NotificationAction {
	private final String url;

	public OpenUrlAction(String url) {
		super("Open in Browser");
		this.url = url;
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
		BrowserUtil.browse(url);
		notification.expire();
	}
}