package com.github.novicezk.midjourney.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@ApiModel("DeleteAvatarId提交参数")
@EqualsAndHashCode(callSuper = true)
public class SubmitDeleteAvatarIdDTO extends BaseSubmitDTO {

	@ApiModelProperty(value = "头像id，小于10个字符", required = true, example = "test1")
	private String name;
}
