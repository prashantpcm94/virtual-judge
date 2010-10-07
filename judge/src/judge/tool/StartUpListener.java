package judge.tool;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import judge.service.StatService;


public class StartUpListener implements ServletContextListener {

	/* 监听服务器启动 */
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("系统启动");
		
		ServletContext sc = event.getServletContext();
		Map<String, String> languageList;
		
		languageList = new HashMap<String, String>();
		languageList.put("0", "G++");
		languageList.put("1", "GCC");
		languageList.put("2", "Java");
		languageList.put("3", "Pascal");
		languageList.put("4", "C++");
		languageList.put("5", "C");
		languageList.put("6", "Fortran");
		sc.setAttribute("POJ", languageList);
		
		languageList = new HashMap<String, String>();
		languageList.put("1", "C (gcc 4.4.5)");
		languageList.put("2", "C++ (g++ 4.4.5)");
		languageList.put("3", "FPC (fpc 2.4.0)");
		languageList.put("4", "Java (java 1.6.0)");
		languageList.put("5", "Python (Python 2.6.6)");
		languageList.put("6", "Perl (Perl 5.10.1)");
		languageList.put("7", "Scheme (Guile 1.8.7)");
		languageList.put("8", "PHP (PHP 5.3.2)");
		sc.setAttribute("ZOJ", languageList);
	
		languageList = new HashMap<String, String>();
		languageList.put("C", "C");
		languageList.put("C++", "C++");
		languageList.put("Pascal", "Pascal");
		languageList.put("Java", "Java");
		sc.setAttribute("UVALive", languageList);
		
		languageList = new HashMap<String, String>();
		languageList.put("GNU C (MinGW, GCC 4)", "GNU C (MinGW, GCC 4)");
		languageList.put("GNU CPP (MinGW, GCC 4)", "GNU CPP (MinGW, GCC 4)");
		languageList.put("Visual Studio 8 C++", "Visual Studio 8 C++");
		languageList.put("C# (Mono gmcs 2.4)", "C# (Mono gmcs 2.4)");
		languageList.put("Visual Studio 8 C", "Visual Studio 8 C");
		languageList.put("JAVA 1.6", "JAVA 1.6");
		languageList.put("Delphi 7.0", "Delphi 7.0");
		sc.setAttribute("SGU", languageList);
		
		languageList = new HashMap<String, String>();
		languageList.put("9", "C");
		languageList.put("10", "C++");
		languageList.put("3", "Pascal");
		languageList.put("7", "Java");
		languageList.put("8", "C#");
		sc.setAttribute("URAL", languageList);

		languageList = new HashMap<String, String>();
		languageList.put("0", "C");
		languageList.put("1", "C++");
		languageList.put("2", "Pascal");
		languageList.put("3", "Java");
		sc.setAttribute("HUST", languageList);

		languageList = new HashMap<String, String>();
		languageList.put("11", "C (gcc 4.3.2)");
		languageList.put("34", "C99 strict (gcc 4.3.2)");
		languageList.put("1", "C++ (g++ 4.0.0-8)");
		languageList.put("41", "C++ (g++ 4.3.2)");
		languageList.put("2", "Pascal (gpc 20070904)");
		languageList.put("22", "Pascal (fpc 2.2.4)");
		languageList.put("38", "Tcl (tclsh 8.5.3)");
		languageList.put("39", "Scala (Scalac 2.7.4)");
		languageList.put("10", "Java (JavaSE 6)");
		languageList.put("25", "Nice (nicec 0.9.6)");
		languageList.put("24", "JAR (JavaSE 6)");
		languageList.put("27", "C# (gmcs 2.0.1)");
		languageList.put("30", "Nemerle (ncc 0.9.3)");
		languageList.put("23", "Smalltalk (gst 3.0.3)");
		languageList.put("13", "Assembler (nasm 2.03.01)");
		languageList.put("20", "D (gdc 4.1.3)");
		languageList.put("5", "Fortran 95 (gfortran 4.3.2)");
		languageList.put("7", "ADA 95 (gnat 4.3.2)");
		languageList.put("28", "Bash (bash 3.2.29)");
		languageList.put("3", "Perl (perl 5.10.0)");
		languageList.put("44", "Python (python 2.6.2)");
		languageList.put("4", "Python (python 2.5)");
		languageList.put("17", "Ruby (ruby 1.9.0)");
		languageList.put("26", "Lua (luac 5.1.3)");
		languageList.put("16", "Icon (iconc 9.4.3)");
		languageList.put("19", "Pike (pike 7.6.112)");
		languageList.put("29", "PHP (php 5.2.6)");
		languageList.put("33", "Scheme (guile 1.8.5)");
		languageList.put("18", "Scheme (stalin 0.11)");
		languageList.put("31", "Common Lisp (sbcl 1.0.18)");
		languageList.put("32", "Common Lisp (clisp 2.44.1)");
		languageList.put("21", "Haskell (ghc 6.10.4)");
		languageList.put("36", "Erlang (erl 5.6.3)");
		languageList.put("8", "Ocaml (ocamlopt 3.10.2)");
		languageList.put("14", "Clips (clips 6.24)");
		languageList.put("15", "Prolog (swipl 5.6.58)");
		languageList.put("6", "Whitespace (wspace 0.3)");
		languageList.put("12", "Brainf**k (bff 1.0.3.1)");
		languageList.put("9", "Intercal (ick 0.28-4)");
		languageList.put("62", "Text (plain text)");
		languageList.put("35", "JavaScript (rhino 1.7R1-2)");
		sc.setAttribute("SPOJ", languageList);

		languageList = new HashMap<String, String>();
		languageList.put("0", "G++");
		languageList.put("1", "GCC");
		languageList.put("2", "C++");
		languageList.put("3", "C");
		languageList.put("4", "Pascal");
		languageList.put("5", "Java");
		sc.setAttribute("HDU", languageList);
		
		MyFilter.setStatService((StatService) SpringBean.getBean("statService", sc));
		SessionListener.setStatService((StatService) SpringBean.getBean("statService", sc));
		
        Properties prop = new Properties();
        FileInputStream in;
		try {
			in = new FileInputStream(sc.getRealPath("WEB-INF" + File.separator + "web.properties"));
            prop.load(in);
            String basePath = prop.getProperty("basePath").trim();
            sc.setAttribute("basePath", basePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* 监听服务器关闭 */
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("系统关闭");
	}
}