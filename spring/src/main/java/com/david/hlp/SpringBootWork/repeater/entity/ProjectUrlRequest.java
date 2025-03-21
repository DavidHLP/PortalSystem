package com.david.hlp.SpringBootWork.repeater.entity;

import java.util.List;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUrlRequest {
    private Integer projectId;
    private String projectName;
    private String projectInterfaceName;
    private String projectDescription;
    private Integer postCount;
    private Integer getCount;
    private Integer putCount;
    private Integer deleteCount;
    private Integer httpCount;
    private Integer httpsCount;
    private List<Url> urls;
}