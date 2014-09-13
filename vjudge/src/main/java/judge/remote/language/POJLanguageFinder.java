package judge.remote.language;

import java.util.LinkedHashMap;

import org.springframework.stereotype.Component;

import judge.remote.RemoteOj;
import judge.remote.language.common.LanguageFinder;
import judge.tool.Handler;

@Component
public class POJLanguageFinder implements LanguageFinder {

	@Override
	public RemoteOj getOj() {
		return RemoteOj.POJ;
	}

	@Override
	public boolean isDiverse() {
		return false;
	}

	@Override
	public void getLanguages(String remoteProblemId, Handler<LinkedHashMap<String, String>> handler) {
		// TODO Auto-generated method stub
	}

	@Override
	public LinkedHashMap<String, String> getDefaultLanguages() {
		LinkedHashMap<String, String> languageList = new LinkedHashMap<String, String>();
		languageList.put("0", "G++");
		languageList.put("1", "GCC");
		languageList.put("2", "Java");
		languageList.put("3", "Pascal");
		languageList.put("4", "C++");
		languageList.put("5", "C");
		languageList.put("6", "Fortran");
		return languageList;
	}

}
