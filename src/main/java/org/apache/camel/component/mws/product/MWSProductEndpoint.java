package org.apache.camel.component.mws.product;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;


/**
 * Represents a MWSProduct endpoint.
 */
@UriEndpoint(scheme = "mws-product", title = "MWSProduct", syntax = "mws-product:marketplaceId", label = "MWSProduct", producerOnly = true)
public class MWSProductEndpoint extends DefaultEndpoint {

	private static final Map<String, String> marketplaceMap = new HashMap<String, String>();

	@UriPath()
	@Metadata(required = "true", defaultValue = "DE")
	private String marketplaceId;

	@UriParam(description = "")
	private String mwsUrl = "";

	@UriParam(description = "")
	@Metadata(required = "true")
	private String merchantId = "";

	@UriParam(description = "")
	@Metadata(required = "true")
	private String accessKey = "";

	@UriParam(description = "")
	@Metadata(required = "true")
	private String secretAccessKey = "";


	public MWSProductEndpoint() {
		this(null, null);
	}

	public MWSProductEndpoint(String uri, MWSProductComponent component) {
		super(uri, component);
		marketplaceMap.put("DE", "A1PA6795UKMFR9");
	}

//	public MWSProductEndpoint(String endpointUri) {
//		super(endpointUri);
//	}

	@Override
	public Producer createProducer() throws Exception {
		return new MWSProductProducer(this);
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		throw new RuntimeCamelException("Cannot consume from a MWSProduct endpoint: " + getEndpointUri());
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Some description of this option, and what it does
	 */
	public void setMarketplaceId(String marketplaceId) {
		this.marketplaceId = marketplaceId;
	}
	public String getMarketplaceId() {
//		return marketplaceId;
		return marketplaceMap.get( marketplaceId );
	}

	/**
	 * Some description of this option, and what it does
	 */
	public void setMwsUrl(String mwsUrl) {
		this.mwsUrl = mwsUrl;
	}
	public String getMwsUrl() {
		return mwsUrl;
	}

	/**
	 * Some description of this option, and what it does
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * Some description of this option, and what it does
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * Some description of this option, and what it does
	 */
	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}
	public String getSecretAccessKey() {
		return secretAccessKey;
	}
}