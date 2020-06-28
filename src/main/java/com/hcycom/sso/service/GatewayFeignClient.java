package com.hcycom.sso.service;



import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hcycom.sso.client.AuthorizedUserFeignClient;



@AuthorizedUserFeignClient(name = "gateway")
public interface GatewayFeignClient {
  @RequestMapping(value = "/auth/login", method = RequestMethod.POST, consumes = MediaType
	        .APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OAuth2AccessToken> authenticate( @RequestBody
	        Map<String, String> params);
  
}


