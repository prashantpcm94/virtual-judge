package judge.tool;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;


/**
 * 销毁垃圾httpSession
 * @author Isun
 *
 */
public class TempFileCleaner {
	
	public void clean() throws IOException {
		String relativePath = (String) ApplicationContainer.sc.getAttribute("DataPath");
		String path = ApplicationContainer.sc.getRealPath(relativePath);
		FileUtils.deleteDirectory(new File(path));
	}

}
