package com.akina.domain.os;

import lombok.Getter;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * OsEnum 枚举：封装操作系统类型及相关行为。
 *
 * <p>职责：
 * - 识别操作系统类型（Windows、Mac、Linux 等）
 * - 提供对应系统信息命令
 * - 静态方法 detect() 根据系统名称返回匹配枚举对象</p>
 *
 * <p>设计原则：数据与行为绑定，便于扩展新的操作系统类型。</p>
 */


@Getter
public enum OsEnum {

    WIN("win", "systeminfo"),
    MAC("mac", "system_profiler SPHardwareDataType"),
    LINUX("nix|nux", "lshw -short");

    private final Pattern match;
    private final String cmd;

    OsEnum(String keywordRegex, String cmd) {
        this.match = Pattern.compile(keywordRegex, Pattern.CASE_INSENSITIVE);
        this.cmd = cmd;
    }

    public boolean matches(String osName) {
        return match.matcher(osName).find();
    }

    public String cmd() { return cmd; }

    public static OsEnum detect(String osName) {
        return Arrays.stream(values())
                .filter(e -> e.matches(osName))
                .findFirst()
                .orElse(null);
    }
}
