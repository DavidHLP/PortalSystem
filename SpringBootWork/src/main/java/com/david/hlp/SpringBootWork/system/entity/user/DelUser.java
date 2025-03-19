package com.david.hlp.SpringBootWork.system.entity.user;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.david.hlp.SpringBootWork.system.util.annotation.emptystring.NotEmptyString;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DelUser {
    private Long id;
    @NotEmptyString(message = "密码不能为空")
    private String password;
}
