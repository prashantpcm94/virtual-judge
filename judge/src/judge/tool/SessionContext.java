package judge.tool;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("unchecked")
public class SessionContext {
	private static SessionContext instance;
	private HashMap mymap;

	private SessionContext() {
		mymap = new HashMap();
	}

	public static SessionContext getInstance() {
		if (instance == null) {
			instance = new SessionContext();
		}
		return instance;
	}

	public synchronized void AddSession(HttpSession session) {
		if (session != null) {
			mymap.put(session.getId(), session);
		}
	}

	public synchronized void DelSession(HttpSession session) {
		if (session != null) {
			mymap.remove(session.getId());
		}
	}

	public synchronized void DelSession(String sessionId) {
		mymap.remove(sessionId);
	}

	public synchronized HttpSession getSession(String sessionId) {
		if (sessionId == null)
			return null;
		return (HttpSession) mymap.get(sessionId);
	}
	
	public synchronized List getSessionList() {
		if (mymap == null){
			return null;
		}
		List result = new ArrayList();
		for (Iterator i = mymap.values().iterator(); i.hasNext();) {
			result.add(i.next());
		}
		return result;
	}

}