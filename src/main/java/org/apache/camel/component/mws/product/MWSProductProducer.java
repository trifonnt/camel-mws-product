package org.apache.camel.component.mws.product;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsAsyncClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsConfig;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsException;
import com.amazonservices.mws.products.model.ListMatchingProductsRequest;
import com.amazonservices.mws.products.model.ListMatchingProductsResponse;
import com.amazonservices.mws.products.model.ResponseHeaderMetadata;


/**
 * The MWSProduct producer.
 */
public class MWSProductProducer extends DefaultProducer {

	private static final Logger LOG = LoggerFactory.getLogger(MWSProductProducer.class);

	private static final String APP_NAME = "Trifon-Camel-MWS-Product-Component"; //TODO - MUST be configurable!

	private static final String APP_VERSION = "1.0.0";

	/** The client, lazy initialized. Async client is also a sync client. */
	private static MarketplaceWebServiceProductsAsyncClient client = null;

	private MWSProductEndpoint endpoint;


	public MWSProductProducer(MWSProductEndpoint endpoint) {
		super(endpoint);
		this.endpoint = endpoint;
	}

	@Override
	public void process(Exchange exchange) throws Exception {
		LOG.debug("marketplaceId={}, merchantId={}", endpoint.getMarketplaceId(), endpoint.getMerchantId());
		Message msg = exchange.getOut();

		// Get the searchString from the HTTP Request Parameters(Camel Exchange Header)
		String searchString = (String) exchange.getIn().getHeader("mwsSearchString");
		LOG.debug("searchString={}", searchString);
		if (searchString == null || searchString.isEmpty()) {
//			throw new IllegalArgumentException("SearchString is mandatory!");
			msg.setFault( true );
			msg.setBody("SearchString is mandatory!");
			return;
		}

		String searchContext = (String) exchange.getIn().getHeader("mwsSearchContext");
		LOG.debug("searchCntext={}", searchContext);
		if (searchContext == null || searchContext.isEmpty()) {
			searchContext = "All"; // All, Books, ...
		}

		// Get a client connection.
		MarketplaceWebServiceProductsClient client = getClient(endpoint.getAccessKey(), endpoint.getSecretAccessKey(), endpoint.getMwsUrl());

		// Create a request.
		ListMatchingProductsRequest request = new ListMatchingProductsRequest();
		request.setSellerId( endpoint.getMerchantId() );
//		request.setMWSAuthToken( endpoint.getSecretAccessKey() );
		request.setMarketplaceId( endpoint.getMarketplaceId() );

		request.setQuery( searchString );
		request.setQueryContextId( searchContext );

		try {
			ListMatchingProductsResponse response = client.listMatchingProducts( request );

			ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();

			exchange.getOut().setHeader("MwsRequestId", rhmd.getRequestId());
			exchange.getOut().setHeader("MwsTimestamp", rhmd.getTimestamp());
			exchange.getOut().setHeader("MwsQuotaMax", rhmd.getQuotaMax());
			exchange.getOut().setHeader("MwsQuotaRemaining", rhmd.getQuotaRemaining());
			exchange.getOut().setHeader("MwsQuotaResetsAt", rhmd.getQuotaResetsAt());

			exchange.getOut().setHeader(Exchange.CONTENT_TYPE, "text/xml");
			String responseXml = response.toXML();
			exchange.getOut().setBody( responseXml );
		} catch (MarketplaceWebServiceProductsException ex) {
			// @Trifon - TODO - proper Camel route Exception handling!
			// Exception properties are important for diagnostics.
			System.out.println("Service Exception:");
			ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
			if (rhmd != null) {
				System.out.println("RequestId: " + rhmd.getRequestId());
				System.out.println("Timestamp: " + rhmd.getTimestamp());
			}
			System.out.println("Message: " + ex.getMessage());
			System.out.println("StatusCode: " + ex.getStatusCode());
			System.out.println("ErrorCode: " + ex.getErrorCode());
			System.out.println("ErrorType: " + ex.getErrorType());
			throw ex;
		}
	}

	public static MarketplaceWebServiceProductsAsyncClient getClient(String accessKey, String secretKey, String serviceURL) {
		return getAsyncClient( accessKey, secretKey, serviceURL );
	}

	public static synchronized MarketplaceWebServiceProductsAsyncClient getAsyncClient(String accessKey, String secretKey, String serviceURL) {
		if (client == null) {
			MarketplaceWebServiceProductsConfig config = new MarketplaceWebServiceProductsConfig();
			config.setServiceURL(serviceURL);

			client = new MarketplaceWebServiceProductsAsyncClient(accessKey, secretKey, APP_NAME, APP_VERSION, config, null);
		}
		return client;
	}
}