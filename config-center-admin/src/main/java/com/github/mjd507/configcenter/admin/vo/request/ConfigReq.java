package com.github.mjd507.configcenter.admin.vo.request;

import lombok.Data;

/**
 * Created by mjd on 2020/4/17 17:16
 */
@Data
public class ConfigReq {
    private Integer appId;
    private String key;
    private String val;
    private Boolean isAdd = false;
}
