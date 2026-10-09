package com.spzx.auth.form;

import io.swagger.v3.oas.annotations.media.Schema;

public class RegisterBody extends LoginBody {

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "验证码")
    private String code;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}