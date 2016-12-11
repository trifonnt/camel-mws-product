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


	/**
	 * Default Constructor
	 */
	public MWSProductEndpoint() {
		this(null, null);
	}

	/**
	 * @param uri
	 * @param component
	 */
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
		throw new RuntimeCamelException("Can not consume from a MWSProduct endpoint: " + getEndpointUri());
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Some description of this option, and what it does
	 * @param marketplaceId 
	 */
	public void setMarketplaceId(String marketplaceId) {
		this.marketplaceId = marketplaceId;
	}
	/**
	 * @return Marketplace Id
	 */
	public String getMarketplaceId() {
//		return marketplaceId;
		return marketplaceMap.get( marketplaceId );
	}

	/**
	 * Some description of this option, and what it does
	 * @param mwsUrl 
	 */
	public void setMwsUrl(String mwsUrl) {
		this.mwsUrl = mwsUrl;
	}
	/**
	 * @return MWS URL
	 */
	public String getMwsUrl() {
		return mwsUrl;
	}

	/**
	 * Some description of this option, and what it does
	 * @param merchantId 
	 */
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	/**
	 * @return Merchant Id
	 */
	public String getMerchantId() {
		return merchantId;
	}

	/**
	 * Some description of this option, and what it does
	 * @param accessKey 
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	/**
	 * @return Access Key
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * Some description of this option, and what it does
	 * @param secretAccessKey 
	 */
	public void setSecretAccessKey(String secretAccessKey) {
		this.secretAccessKey = secretAccessKey;
	}
	/**
	 * @return Secret Access Key
	 */
	public String getSecretAccessKey() {
		return secretAccessKey;
	}
}