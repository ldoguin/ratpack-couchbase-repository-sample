package org.couchbase.devex;

import ratpack.handling.Context;
import ratpack.jackson.Jackson;
import ratpack.render.RendererSupport;

public class UserRenderer extends RendererSupport<User> {

	@Override
	public void render(Context context, User user) throws Exception {
		context.byContent(byContentSpec -> byContentSpec
				.json(() -> context.render(Jackson.json(user)))
				.plainText(() -> context.render(user.toString()))
				);
	}

}
