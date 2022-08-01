package edu.jphoebe.demo.netty.jphoebeChat.common;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author 蒋时华
 * @date 2022-07-29 17:43:31
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class CustomMessage implements Serializable {

    public static String TYPE_LOGIN = "login";
    public static String TYPE_LOGOUT = "logout";
    public static String TYPE_CHAT = "chat";

    @Builder.Default
    private String key = "default";

    private String type;

    /*** 消息内容 */
    private String content;

}
