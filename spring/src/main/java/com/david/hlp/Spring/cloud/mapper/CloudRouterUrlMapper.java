package com.david.hlp.Spring.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.david.hlp.Spring.cloud.module.dto.CloudDTO;
import com.david.hlp.Spring.cloud.module.dto.CloudData;
import java.util.HashMap;

@Mapper
public interface CloudRouterUrlMapper {
    CloudDTO<CloudData<HashMap<String, Object>>> selectByUniqueIdAndUserId(String uniqueId, String userId);
}
