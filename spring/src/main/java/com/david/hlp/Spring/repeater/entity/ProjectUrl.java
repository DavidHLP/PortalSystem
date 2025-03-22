package com.david.hlp.Spring.repeater.entity;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * 项目URL实体类
 *
 * @author david
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUrl implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 项目名称，唯一
     */
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 50, message = "项目名称长度不能超过50个字符")
    @Builder.Default
    private String projectName = "";

    /**
     * 项目接口名称
     */
    @NotBlank(message = "接口名称不能为空")
    @Size(max = 100, message = "接口名称长度不能超过100个字符")
    @Builder.Default
    private String projectInterfaceName = "";

    /**
     * 项目文档
     */
    @Size(max = 500, message = "项目文档长度不能超过500个字符")
    private String description;
}