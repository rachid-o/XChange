package org.knowm.xchange.okex.v5.service;

import org.knowm.xchange.service.BaseParamsDigest;
import si.mazi.rescu.RestInvocation;

import javax.crypto.Mac;
import java.util.Base64;

/** Author: Max Gao (gaamox@tutanota.com) Created: 08-06-2021 */
public class OkexDigest extends BaseParamsDigest {
  private OkexDigest(String secretKeyBase64) {
    super(secretKeyBase64, HMAC_SHA_256);
  }

  public static OkexDigest createInstance(String secretKeyBase64) {
    return secretKeyBase64 == null ? null : new OkexDigest(secretKeyBase64);
  }

  /** https://www.okex.com/docs-v5/en/#rest-api-authentication-signature * */
  @Override
  public String digestParams(RestInvocation restInvocation) {

    StringBuilder sb = new StringBuilder();
    sb.append(restInvocation.getHttpHeadersFromParams().getOrDefault("OK-ACCESS-TIMESTAMP", null));
    sb.append(restInvocation.getHttpMethod().toUpperCase());
    sb.append(restInvocation.getPath());
    sb.append(restInvocation.getRequestBody());

    Mac mac = getMac();
    mac.update(sb.toString().getBytes());

    return Base64.getEncoder().encodeToString(mac.doFinal());
  }
}
