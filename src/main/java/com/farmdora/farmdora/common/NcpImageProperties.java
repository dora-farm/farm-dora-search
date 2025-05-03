package com.farmdora.farmdora.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ncp.image")
public class NcpImageProperties {
    private String path;
    private String type;
}
