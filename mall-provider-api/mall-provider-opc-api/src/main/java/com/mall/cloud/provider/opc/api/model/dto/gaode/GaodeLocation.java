package com.mall.cloud.provider.opc.api.model.dto.gaode;

import com.mall.cloud.provider.uac.common.base.dto.GaodeBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GaodeLocation extends GaodeBaseDto {
    private String province;
    private String city;
    private String adcode;
    private String rectangle;
}