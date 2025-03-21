package com.david.hlp.Spring.repeater.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 路由URL实体类
 *
 * @author david
 */
@Schema(description = "路由URL实体")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouterUrl {
    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "1")
    private Integer id;

    /**
     * URL路径（支持RESTful参数，如/api/v1/:id）
     */
    @Schema(description = "URL路径，支持RESTful参数，如/api/v1/:id", example = "/api/v1/users/:id")
    @NotBlank(message = "路径不能为空")
    @Pattern(regexp = "^/.*", message = "路径必须以/开头")
    private String path;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
}