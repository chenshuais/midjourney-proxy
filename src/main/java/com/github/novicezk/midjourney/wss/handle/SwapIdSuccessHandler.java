package com.github.novicezk.midjourney.wss.handle;


import com.github.novicezk.midjourney.Constants;
import com.github.novicezk.midjourney.enums.MessageType;
import com.github.novicezk.midjourney.enums.TaskAction;
import com.github.novicezk.midjourney.support.Task;
import com.github.novicezk.midjourney.support.TaskCondition;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * swapid消息处理.
 */
@Slf4j
@Component
public class SwapIdSuccessHandler extends MessageHandler {

	@Override
	public void handle(MessageType messageType, DataObject message) {
		String authorName = message.optObject("author").map(a -> a.getString("username")).orElse("System");
		log.debug("{}", message.toString());
		if (!MessageType.CREATE.equals(messageType) || !authorName.equals("InsightFaceSwap")) {
			return;
		}
		String content = message.getString("content", "");
		Pattern notFoundPattern = Pattern.compile("idname (\\w+) not found");
		Matcher notFoundMatcher = notFoundPattern.matcher(content);
		if (notFoundMatcher.find()) {
			String idname = notFoundMatcher.group(1);
			TaskCondition condition = new TaskCondition()
					.setActionSet(Set.of(TaskAction.SWAPID))
					.setFinalPromptEn(idname);
			findAndFinishFaceSwapTask(condition, idname, message, false, content);
			return;
		}
		DataArray attachments = message.getArray("attachments");
		if (attachments.isEmpty()) {
			return;
		}
		String filename = attachments.getObject(0).getString("filename");
		Pattern taskIdPattern = Pattern.compile("(\\d+)_ins\\..*");
		Matcher taskIdMatcher = taskIdPattern.matcher(filename);
		if (!taskIdMatcher.find()) {
			return;
		}
		String taskId = taskIdMatcher.group(1);
		Task task = this.discordLoadBalancer.getRunningTask(taskId);
		if (task == null) {
			return;
		}
		task.setImageUrl(getImageUrl(message));
		task.success();
		task.awake();
	}
}
