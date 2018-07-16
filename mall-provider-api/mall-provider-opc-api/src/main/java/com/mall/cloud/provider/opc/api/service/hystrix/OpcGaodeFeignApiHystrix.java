package com.mall.cloud.provider.opc.api.service.hystrix;

import com.mall.cloud.provider.opc.api.model.dto.gaode.GaodeLocation;
import com.mall.cloud.provider.opc.api.service.OpcGaodeFeignApi;
import com.mall.cloud.uac.common.util.wrapper.Wrapper;
import org.springframework.stereotype.Component;

@Component
public class OpcGaodeFeignApiHystrix extends OpcGaodeFeignApi {

	@Override
    public Wrapper<GaodeLocation> getLocationByIpAddr(final String ipAddr) {
		return null;
	}
}
