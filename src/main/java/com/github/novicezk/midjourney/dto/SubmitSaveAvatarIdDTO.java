package com.github.novicezk.midjourney.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel("SaveAvatarId提交参数")
@EqualsAndHashCode(callSuper = true)
public class SubmitSaveAvatarIdDTO extends BaseSubmitDTO {

	@ApiModelProperty(value = "头像id，小于10个字符", required = true, example = "test1")
	private String name;

	@ApiModelProperty(value = "图片base64", required = true, example = "data:image/png;base64,xxx")
	private String base64;
}
