package ovh.fedox.snipdox.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ide.CopyPasteManager;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.StringSelection;

/**
 * @author Florian Ohldag (Fedox)
 * @version 1.0
 * @since 7/3/2025, 8:16 PM
 */
public class CopyUrlAction extends NotificationAction {
	private final String url;

	public CopyUrlAction(String url) {
		super("Copy URL");
		this.url = url;
	}

	@Override
	public void actionPerformed(@NotNull AnActionEvent e, @NotNull Notification notification) {
		CopyPasteManager.getInstance().setContents(new StringSelection(url));
		notification.expire();
	}
}