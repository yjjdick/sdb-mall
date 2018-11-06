package io.sdb.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * Created by IntelliJ IDEA.
 * User: quendi
 * Date: 2018/6/4
 */
@ApiModel(value = "新闻表单")
public class NewsAddForm {
    @ApiModelProperty(value = "标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty(value = "是否头条")
    @NotBlank(message = "是否头条")
    private Integer head;

    @ApiModelProperty(value = "新闻图片")
    private String img;

    @ApiModelProperty(value = "新闻内容")
    @NotBlank(message = "新闻内容不能为空")
    private String content;
}
