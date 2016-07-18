package org.apache.camel.components.mws.product;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.UriEndpointComponent;
import org.apache.camel.util.ObjectHelper;


/**
 * Represents the component that manages {@link MWSProductEndpoint}.
 */
public class MWSProductComponent extends UriEndpointComponent {

//	private static final Logger LOG = LoggerFactory.getLogger(MWSProductComponent.class);


	public MWSProductComponent() {
		super(MWSProductEndpoint.class);
	}

	public MWSProductComponent(CamelContext context) {
		super(context, MWSProductEndpoint.class);
	}

	@Override
	protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
		String marketplaceId = remaining;
		if (!isMarketplaceValid(marketplaceId)) {
			throw new IllegalArgumentException( "MarketplaceId value is not valid!" );
		}

		MWSProductEndpoint endpoint = new MWSProductEndpoint(uri, this);
		setProperties(endpoint, parameters);
		endpoint.setMarketplaceId( marketplaceId );
		return endpoint;
	}

	private boolean isMarketplaceValid(String marketplaceId) {
		ObjectHelper.notEmpty(marketplaceId, "marketplaceId");
		boolean result = false;
		if (marketplaceId.equals("DE")) {
			result = true;
		}
		return result;
	}
}