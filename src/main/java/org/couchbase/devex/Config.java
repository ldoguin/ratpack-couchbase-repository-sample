package org.couchbase.devex;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.repository.AsyncRepository;
import com.google.inject.AbstractModule;

public class Config extends AbstractModule {

	protected void configure() {
		CouchbaseCluster cc = CouchbaseCluster.create();
		Bucket bucket = cc.openBucket();
		bind(AsyncBucket.class).toInstance(bucket.async());
		bind(AsyncRepository.class).toInstance(bucket.repository().async());
		bind(UserHandler.class);
		bind(UserRenderer.class);
	}
}
