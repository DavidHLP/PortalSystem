package com.david.hlp.Spring.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 用户上下文工具类
 * 用于存储和获取当前请求的用户信息
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserContext {

    private static final ThreadLocal<String> EMAIL_HOLDER = new ThreadLocal<>();

    /**
     * 设置当前用户邮箱
     *
     * @param email 用户邮箱
     */
    public static void setCurrentEmail(String email) {
        EMAIL_HOLDER.set(email);
    }

    /**
     * 获取当前用户邮箱
     *
     * @return 当前用户邮箱
     */
    public static String getCurrentEmail() {
        return EMAIL_HOLDER.get();
    }

    /**
     * 清除当前用户邮箱信息
     * 防止内存泄漏，应在请求结束时调用
     */
    public static void clear() {
        EMAIL_HOLDER.remove();
    }
}