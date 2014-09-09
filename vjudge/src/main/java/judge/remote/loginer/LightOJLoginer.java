package judge.remote.loginer;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.HttpBodyValidator;
import judge.httpclient.SimpleNameValueEntityFactory;
import judge.remote.RemoteOj;
import judge.remote.account.RemoteAccount;
import judge.remote.loginer.common.RetentiveLoginer;

import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class LightOJLoginer extends RetentiveLoginer {

	@Override
	public RemoteOj getOj() {
		return RemoteOj.LightOJ;
	}

	@Override
	public void loginEnforce(RemoteAccount account, DedicatedHttpClient client) {
		if (!client.get("/index.php").getBody().contains("<script>location.href=")) {
			return;
		}
		
		HttpEntity entity = SimpleNameValueEntityFactory.create(
			"mypassword", account.getPassword(),
			"myrem", "1",
			"myuserid", account.getAccountId()
		);
		client.post("/login_check.php", entity, new HttpBodyValidator("login_success.php"));
	}

}
