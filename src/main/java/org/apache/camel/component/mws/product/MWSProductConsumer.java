package org.apache.camel.component.mws.product;

import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;


/**
 * The MWSProduct producer.
 */
public class MWSProductConsumer extends DefaultConsumer {

//	private static final Logger LOG = LoggerFactory.getLogger(MWSProductProducer.class);

//	private MWSProductEndpoint endpoint;


	/**
	 * @param endpoint
	 * @param processor 
	 */
	public MWSProductConsumer(MWSProductEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
//		this.endpoint = endpoint;
		if (processor == null) {
			processor = new MWSProductProcessor();
		}
		((MWSProductProcessor)processor).setEndpoint( endpoint );
	}

}