package judge.remote.submitter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.HttpStatusValidator;
import judge.httpclient.SimpleNameValueEntityFactory;
import judge.remote.RemoteOj;
import judge.remote.account.RemoteAccount;
import judge.remote.submitter.common.SubmissionInfo;
import judge.remote.submitter.common.CanonicalSubmitter;

import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class HYSBZSubmitter extends CanonicalSubmitter {

	@Override
	public RemoteOj getOj() {
		return RemoteOj.HYSBZ;
	}

	@Override
	protected boolean needLogin() {
		return true;
	}

	@Override
	protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
		String html = client.get("/JudgeOnline/status.php?user_id=" + info.remoteAccountId + "&problem_id=" + info.remoteProblemId).getBody();
		Matcher matcher = Pattern.compile("class='evenrow'><td>(\\d+)").matcher(html);
		return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
	}

	@Override
	protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
		HttpEntity entity = SimpleNameValueEntityFactory.create(
			"language", info.remotelanguage, //
			"id", info.remoteProblemId, //
			"source", info.sourceCode //
		);
		client.post("/JudgeOnline/submit.php", entity, HttpStatusValidator.SC_MOVED_TEMPORARILY);
		return null;
	}

}
