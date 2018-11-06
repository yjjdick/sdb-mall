package io.sdb.form;

import io.sdb.common.validator.group.AddGroup;
import io.sdb.common.validator.group.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by IntelliJ IDEA.
 * User: quendi
 * Date: 2018/6/10
 */
@Data
public class SysNewsForm {

    private Long id;
    @NotBlank(message = "新闻标题不能为空",groups = {AddGroup.class,UpdateGroup.class})
    private String title;
    private String img;
    @NotBlank(message = "新闻内容不能为空",groups = {AddGroup.class,UpdateGroup.class})
    private String content;
    private Integer head;
}
