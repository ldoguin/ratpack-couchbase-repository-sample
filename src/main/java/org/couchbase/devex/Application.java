package org.couchbase.devex;

import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.repository.AsyncRepository;

import ratpack.guice.Guice;
import ratpack.rx.RxRatpack;
import ratpack.server.RatpackServer;

public class Application {

	public static void main(String... args) throws Exception {
		RxRatpack.initialize();
		RatpackServer.start(server -> server.registry(Guice.registry(b -> b.module(Config.class)))
				.handlers(chain -> chain.path("create", ctx -> {
					EntityDocument<User> document = EntityDocument.create(new User("ldoguin", 31, "Laurent", "Doguin"));
					AsyncRepository repo = ctx.get(AsyncRepository.class);
					RxRatpack.promise(repo.upsert(document)).then(entityDoc -> ctx.render("OK"));
				}).path("get", ctx -> {
					AsyncRepository repo = ctx.get(AsyncRepository.class);
					RxRatpack.promise(repo.get("ldoguin", User.class))
							.then(user -> ctx.render(user.get(0).content().toString()));
				})));
	}

}