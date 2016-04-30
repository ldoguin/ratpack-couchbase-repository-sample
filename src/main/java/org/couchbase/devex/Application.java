package org.couchbase.devex;

import ratpack.guice.Guice;
import ratpack.rx.RxRatpack;
import ratpack.server.RatpackServer;

public class Application {

	public static void main(String... args) throws Exception {
		RxRatpack.initialize();
		RatpackServer.start(server -> server.registry(Guice.registry(b -> b.module(Config.class)))
				.handlers(chain -> chain.prefix("user", UserHandler.class)));
	}

}