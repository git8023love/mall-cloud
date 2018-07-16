package com.mall.cloud.provider.opc.api.service;

import com.mall.cloud.common.security.feign.OAuth2FeignAutoConfiguration;
import com.mall.cloud.provider.opc.api.model.dto.gaode.GaodeLocation;
import com.mall.cloud.provider.opc.api.service.hystrix.OpcGaodeFeignApiHystrix;
import com.mall.cloud.uac.common.util.wrapper.Wrapper;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mall-provider-opc", configuration = OAuth2FeignAutoConfiguration.class, fallback = OpcGaodeFeignApiHystrix.class)
public abstract class OpcGaodeFeignApi {

	/**
     * IP定位.
	 *
	 * @param ipAddr the ip addr
	 *
	 * @return the location by ip addr
	 */
	@PostMapping(value = "/api/auth/getLocationByIpAddr")
	public abstract Wrapper<GaodeLocation> getLocationByIpAddr(@RequestParam("ipAddr") String ipAddr);
}
