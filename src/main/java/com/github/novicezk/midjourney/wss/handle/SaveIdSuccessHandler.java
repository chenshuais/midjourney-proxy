package com.github.novicezk.midjourney.wss.handle;


import com.github.novicezk.midjourney.enums.MessageType;
import com.github.novicezk.midjourney.enums.TaskAction;
import com.github.novicezk.midjourney.support.TaskCondition;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * saveid消息处理.
 */
@Component
public class SaveIdSuccessHandler extends MessageHandler {

	@Override
	public void handle(MessageType messageType, DataObject message) {
		String authorName = message.optObject("author").map(a -> a.getString("username")).orElse("System");
		if (!MessageType.CREATE.equals(messageType) || !authorName.equals("InsightFaceSwap")) {
			return;
		}
		String content = message.getString("content", "");
		Pattern pattern = Pattern.compile("idname (\\w+) created");
		Matcher matcher = pattern.matcher(content);
		if (!matcher.find()) {
			return;
		}
		String idname = matcher.group(1);
		TaskCondition condition = new TaskCondition()
				.setActionSet(Set.of(TaskAction.SAVEID))
				.setFinalPromptEn(idname);
		findAndFinishFaceSwapTask(condition, idname, message, true, "");
	}
}
