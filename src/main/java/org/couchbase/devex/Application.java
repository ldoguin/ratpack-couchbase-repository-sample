package org.couchbase.devex;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.repository.Repository;

import ratpack.exec.Blocking;
import ratpack.server.RatpackServer;

public class Application {

	public static void main(String... args) throws Exception {
		RatpackServer.start(server -> server.handlers(chain -> chain.path("create", ctx -> {
			Blocking.get(() -> {
				EntityDocument<User> document = EntityDocument.create(new User("ldoguin", 31, "Laurent", "Doguin"));
				Repository repo = CouchbaseCluster.create().openBucket().repository();
				return repo.upsert(document);
			}).then(entityDoc -> ctx.render("OK"));
		}).path("get", ctx -> {
			Blocking.get(() -> {
				Repository repo = CouchbaseCluster.create().openBucket().repository();
				EntityDocument<User> ldoguin = repo.get("ldoguin", User.class);
				return ldoguin;
			}).then(user -> ctx.render(user.content().toString()));
		})

		));
	}

}