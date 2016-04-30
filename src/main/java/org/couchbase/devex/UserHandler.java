package org.couchbase.devex;

import com.couchbase.client.java.AsyncBucket;
import com.couchbase.client.java.document.EntityDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.AsyncN1qlQueryResult;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.repository.AsyncRepository;

import ratpack.exec.Promise;
import ratpack.func.Action;
import ratpack.handling.Chain;
import ratpack.jackson.Jackson;
import ratpack.rx.RxRatpack;
import rx.Observable;

public class UserHandler implements Action<Chain> {

	@Override
	public void execute(Chain chain) throws Exception {
		chain.path(":userId", ctx -> {
			AsyncRepository repo = ctx.get(AsyncRepository.class);
			String userId = ctx.getPathTokens().get("userId");
			ctx.byMethod(methodSpec -> {
				methodSpec.get(() -> {
					Observable<EntityDocument<User>> user = repo.get(userId, User.class);
					RxRatpack.promise(user).then(u -> ctx.render(u.get(0).content()));
				}).put(() -> {
					Promise<User> promise = ctx.parse(Jackson.fromJson(User.class));
					Observable<EntityDocument<User>> observable = RxRatpack.observe(promise)
							.map(user -> EntityDocument.create(user)).flatMap(doc -> repo.upsert(doc));
					RxRatpack.promise(observable).then(doc -> ctx.render("OK"));
				}).delete(() -> {
					Observable<EntityDocument<User>> user = repo.remove(userId, User.class);
					RxRatpack.promise(user).then(u -> ctx.render("OK"));
				});
			});
		});
		chain.all(ctx -> {
			AsyncBucket bucket = ctx.get(AsyncBucket.class);
			AsyncRepository repo = ctx.get(AsyncRepository.class);
			ctx.byMethod(methodSpec -> {
				methodSpec.get(() -> {
					N1qlQuery query = N1qlQuery.simple("SELECT Meta().id as username,age,fName,lName FROM default WHERE type = 'user'");
					Observable<User> observable = bucket.query(query)
							.flatMap(qRes -> qRes.rows()).map(row -> {
								JsonObject jo = row.value();
								String username = jo.getString("username");
								Integer age = jo.getInt("age");
								String fName = jo.getString("fName");
								String lName = jo.getString("lName");
								return new User(username, age, fName, lName);
							});
					RxRatpack.promise(observable).then(users -> ctx.render(Jackson.json(users)));
				}).post(() -> {
					Promise<User> promise = ctx.parse(Jackson.fromJson(User.class));
					Observable<EntityDocument<User>> observable = RxRatpack.observe(promise)
							.map(user -> EntityDocument.create(user)).flatMap(doc -> repo.upsert(doc));
					RxRatpack.promise(observable).then(doc -> ctx.render("OK"));
				});
			});
		});
	}

}
