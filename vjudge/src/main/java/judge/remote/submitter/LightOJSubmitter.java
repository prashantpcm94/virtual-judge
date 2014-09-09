package judge.remote.submitter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.HttpBodyValidator;
import judge.httpclient.SimpleNameValueEntityFactory;
import judge.remote.RemoteOj;
import judge.remote.account.RemoteAccount;
import judge.remote.submitter.common.SubmissionInfo;
import judge.remote.submitter.common.CanonicalSubmitter;

import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class LightOJSubmitter extends CanonicalSubmitter {

	@Override
	public RemoteOj getOj() {
		return RemoteOj.LightOJ;
	}

	@Override
	protected boolean needLogin() {
		return true;
	}

	@Override
	protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
		String html = client.get("/volume_usersubmissions.php").getBody();
		Matcher matcher = Pattern.compile("sub_id=(\\d+)(?:[\\s\\S](?!/tr))*problem=" + info.remoteProblemId).matcher(html);
		return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
	}

	@Override
	protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
		HttpEntity entity = SimpleNameValueEntityFactory.create(
			"language", info.remotelanguage, //
			"sub_problem", info.remoteProblemId, //
			"code", info.sourceCode //
		);
		client.post("/volume_submit.php", entity, new HttpBodyValidator("location.href='volume_usersubmissions.php'"));
		return null;
	}

}
