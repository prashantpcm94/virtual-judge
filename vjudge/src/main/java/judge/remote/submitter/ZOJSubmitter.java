package judge.remote.submitter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import judge.httpclient.DedicatedHttpClient;
import judge.httpclient.SimpleNameValueEntityFactory;
import judge.remote.RemoteOj;
import judge.remote.account.RemoteAccount;
import judge.remote.submitter.common.SubmissionInfo;
import judge.remote.submitter.common.CanonicalSubmitter;
import judge.tool.Tools;

import org.apache.http.HttpEntity;
import org.springframework.stereotype.Component;

@Component
public class ZOJSubmitter extends CanonicalSubmitter {

	@Override
	public RemoteOj getOj() {
		return RemoteOj.ZOJ;
	}

	@Override
	protected boolean needLogin() {
		return true;
	}

	@Override
	protected Integer getMaxRunId(SubmissionInfo info, DedicatedHttpClient client, boolean submitted) {
		String html = client.get(
				"/onlinejudge/showRuns.do?contestId=1&problemCode=" + info.remoteProblemId + "&handle=" + info.remoteAccountId)
				.getBody();
		Matcher matcher = Pattern.compile("<td class=\"runId\">(\\d+)</td>").matcher(html);
		return matcher.find() ? Integer.parseInt(matcher.group(1)) : -1;
	}

	@Override
	protected String submitCode(SubmissionInfo info, RemoteAccount remoteAccount, DedicatedHttpClient client) {
		String html = client.get("/onlinejudge/showProblem.do?problemCode=" + info.remoteProblemId).getBody();
		String realProblemId = Tools.regFind(html, "problemId=([\\s\\S]*?)\"><font");

		HttpEntity entity = SimpleNameValueEntityFactory.create( //
				"languageId", info.remotelanguage, //
				"problemId", realProblemId, //
				"source", info.sourceCode);
		client.post("/onlinejudge/submit.do", entity);
		return null;
	}

}
