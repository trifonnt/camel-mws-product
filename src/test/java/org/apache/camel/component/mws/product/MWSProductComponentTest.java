package org.apache.camel.component.mws.product;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MWSProductComponentTest extends CamelTestSupport {

	@Produce(uri = "direct:start")
	protected ProducerTemplate producerTemplate;

	@Override
	public boolean isDumpRouteCoverage() {
		return true;
	}

	@Test
	public void testMWSProduct() throws Exception {
		Map<String, Object> headers = new HashMap<String, Object>();
		headers.put("mwsSearchString", "OSGi");
		headers.put("mwsSearchContext", "All");
		producerTemplate.sendBodyAndHeaders("Sample Message Body", headers);

		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedMinimumMessageCount( 1 );

		assertMockEndpointsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			public void configure() {
// - http://localhost:8080/camel/mws/product
//				from("servlet:///mws/product?matchOnUriPrefix=true")
//					.to("vm:direct-mws-product");

				from("direct:start")
//					.setHeader("mwsSearchString", constant("OSGi"))
//					.setHeader("mwsSearchContext", constant("All"))
					.to("vm:direct-mws-product")
				;

				from("vm:direct-mws-product")
					.to("mws-product://DE?mwsUrl=RAW({{MWS_URL}})&merchantId=RAW({{MWS_MERCHANT_ID}})&accessKey=RAW({{MWS_ACCESS_KEY}})&secretAccessKey=RAW({{MWS_SECRET_ACCESS_KEY}})")
					.to("log:out")
//					.to("file://target/amazon-product/?fileName=products.xml")
					.setHeader(Exchange.FILE_NAME, constant("products.xml")).to( "file:target/amazon-product")
					.to("mock:result")
				;
			}
		};
	}

	@Override
	protected CamelContext createCamelContext() throws Exception {
//		Properties props = new Properties();
//		props.setProperty("test.route.out", "mock:result");

		CamelContext context = super.createCamelContext();

		PropertiesComponent pc = context.getComponent("properties", PropertiesComponent.class);
		pc.setLocation("classpath:application.properties");
//		pc.setOverrideProperties( props );

		return context;
	}
}