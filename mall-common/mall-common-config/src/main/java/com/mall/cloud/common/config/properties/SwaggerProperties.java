package com.mall.cloud.common.config.properties;

import lombok.Data;

@Data
public class SwaggerProperties {

	private String title;

	private String description;

	private String version = "1.0-SNAPSHOT";

	private String license = "Apache License 2.0";

	private String licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0";

	private String contactName = "无痕";

	private String contactUrl = "127.0.0.1";

	private String contactEmail = "779043593@qq.com";
}
