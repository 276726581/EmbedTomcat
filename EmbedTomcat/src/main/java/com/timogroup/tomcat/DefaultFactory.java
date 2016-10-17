package com.timogroup.tomcat;

import com.timogroup.tomcat.config.FilterConfig;
import com.timogroup.tomcat.config.InitParameter;
import com.timogroup.tomcat.config.ListenerConfig;
import com.timogroup.tomcat.config.ServletConfig;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TimoRD on 2016/7/8.
 */
public final class DefaultFactory {

    private DefaultFactory() {

    }

    public static ListenerConfig getDefaultContextLoaderListener(String xml) {
        ListenerConfig config = new ListenerConfig();
        config.setListenerClass(ContextLoaderListener.class);
        InitParameter initParameter = new InitParameter("contextConfigLocation", xml);
        config.setInitParameter(initParameter);
        return config;
    }

    public static FilterConfig getDefaultCharacterEncodingFilter(String encoding) {
        FilterConfig config = new FilterConfig();
        config.setFilterName("encoding");
        config.setFilterClass(CharacterEncodingFilter.class);
        InitParameter initParameter = new InitParameter("encoding", encoding);
        config.setInitParameter(initParameter);
        return config;
    }

    public static ServletConfig getDefaultDispatcherServlet(String xml) {
        ServletConfig config = new ServletConfig();
        config.setServletName("dispatcherServlet");
        config.setServletClass(DispatcherServlet.class);
        InitParameter initParameter = new InitParameter("contextConfigLocation", xml);
        config.setInitParameter(initParameter);
        config.setUrlPatterns("/");
        return config;
    }

    static Map<String, String> getDefaultMimeMapping() {
        Map<String, String> map = new HashMap<>();
        map.put("abs", "audio/x-mpeg");
        map.put("ai", "application/postscript");
        map.put("aif", "audio/x-aiff");
        map.put("aifc", "audio/x-aiff");
        map.put("aiff", "audio/x-aiff");
        map.put("aim", "application/x-aim");
        map.put("art", "image/x-jg");
        map.put("asf", "video/x-ms-asf");
        map.put("asx", "video/x-ms-asf");
        map.put("au", "audio/basic");
        map.put("avi", "video/x-msvideo");
        map.put("avx", "video/x-rad-screenplay");
        map.put("bcpio", "application/x-bcpio");
        map.put("bin", "application/octet-stream");
        map.put("bmp", "image/bmp");
        map.put("body", "text/html");
        map.put("cdf", "application/x-cdf");
        map.put("cer", "application/pkix-cert");
        map.put("class", "application/java");
        map.put("cpio", "application/x-cpio");
        map.put("csh", "application/x-csh");
        map.put("css", "text/css");
        map.put("dib", "image/bmp");
        map.put("doc", "application/msword");
        map.put("dtd", "application/xml-dtd");
        map.put("dv", "video/x-dv");
        map.put("dvi", "application/x-dvi");
        map.put("eps", "application/postscript");
        map.put("etx", "text/x-setext");
        map.put("exe", "application/octet-stream");
        map.put("gif", "image/gif");
        map.put("gtar", "application/x-gtar");
        map.put("gz", "application/x-gzip");
        map.put("hdf", "application/x-hdf");
        map.put("hqx", "application/mac-binhex40");
        map.put("htc", "text/x-component");
        map.put("htm", "text/html");
        map.put("html", "text/html");
        map.put("ief", "image/ief");
        map.put("jad", "text/vnd.sun.j2me.app-descriptor");
        map.put("jar", "application/java-archive");
        map.put("java", "text/x-java-source");
        map.put("jnlp", "application/x-java-jnlp-file");
        map.put("jpe", "image/jpeg");
        map.put("jpeg", "image/jpeg");
        map.put("jpg", "image/jpeg");
        map.put("js", "application/javascript");
        map.put("jsf", "text/plain");
        map.put("json", "application/json");
        map.put("jspf", "text/plain");
        map.put("kar", "audio/midi");
        map.put("latex", "application/x-latex");
        map.put("m3u", "audio/x-mpegurl");
        map.put("mac", "image/x-macpaint");
        map.put("man", "text/troff");
        map.put("mathml", "application/mathml+xml");
        map.put("me", "text/troff");
        map.put("mid", "audio/midi");
        map.put("midi", "audio/midi");
        map.put("mif", "application/x-mif");
        map.put("mov", "video/quicktime");
        map.put("movie", "video/x-sgi-movie");
        map.put("mp1", "audio/mpeg");
        map.put("mp2", "audio/mpeg");
        map.put("mp3", "audio/mpeg");
        map.put("mp4", "video/mp4");
        map.put("mpa", "audio/mpeg");
        map.put("mpe", "video/mpeg");
        map.put("mpeg", "video/mpeg");
        map.put("mpega", "audio/x-mpeg");
        map.put("mpg", "video/mpeg");
        map.put("mpv2", "video/mpeg2");
        map.put("ms", "application/x-wais-source");
        map.put("nc", "application/x-netcdf");
        map.put("oda", "application/oda");
        map.put("odb", "application/vnd.oasis.opendocument.database");
        map.put("odc", "application/vnd.oasis.opendocument.chart");
        map.put("odf", "application/vnd.oasis.opendocument.formula");
        map.put("odg", "application/vnd.oasis.opendocument.graphics");
        map.put("odi", "application/vnd.oasis.opendocument.image");
        map.put("odm", "application/vnd.oasis.opendocument.text-master");
        map.put("odp", "application/vnd.oasis.opendocument.presentation");
        map.put("ods", "application/vnd.oasis.opendocument.spreadsheet");
        map.put("odt", "application/vnd.oasis.opendocument.text");
        map.put("otg", "application/vnd.oasis.opendocument.graphics-template");
        map.put("oth", "application/vnd.oasis.opendocument.text-web");
        map.put("otp", "application/vnd.oasis.opendocument.presentation-template");
        map.put("ots", "application/vnd.oasis.opendocument.spreadsheet-template ");
        map.put("ott", "application/vnd.oasis.opendocument.text-template");
        map.put("ogx", "application/ogg");
        map.put("ogv", "video/ogg");
        map.put("oga", "audio/ogg");
        map.put("ogg", "audio/ogg");
        map.put("spx", "audio/ogg");
        map.put("flac", "audio/flac");
        map.put("anx", "application/annodex");
        map.put("axa", "audio/annodex");
        map.put("axv", "video/annodex");
        map.put("xspf", "application/xspf+xml");
        map.put("pbm", "image/x-portable-bitmap");
        map.put("pct", "image/pict");
        map.put("pdf", "application/pdf");
        map.put("pgm", "image/x-portable-graymap");
        map.put("pic", "image/pict");
        map.put("pict", "image/pict");
        map.put("pls", "audio/x-scpls");
        map.put("png", "image/png");
        map.put("pnm", "image/x-portable-anymap");
        map.put("pnt", "image/x-macpaint");
        map.put("ppm", "image/x-portable-pixmap");
        map.put("ppt", "application/vnd.ms-powerpoint");
        map.put("pps", "application/vnd.ms-powerpoint");
        map.put("ps", "application/postscript");
        map.put("psd", "image/vnd.adobe.photoshop");
        map.put("qt", "video/quicktime");
        map.put("qti", "image/x-quicktime");
        map.put("qtif", "image/x-quicktime");
        map.put("ras", "image/x-cmu-raster");
        map.put("rdf", "application/rdf+xml");
        map.put("rgb", "image/x-rgb");
        map.put("rm", "application/vnd.rn-realmedia");
        map.put("roff", "text/troff");
        map.put("rtf", "application/rtf");
        map.put("rtx", "text/richtext");
        map.put("sh", "application/x-sh");
        map.put("shar", "application/x-shar");
        map.put("sit", "application/x-stuffit");
        map.put("snd", "audio/basic");
        map.put("src", "application/x-wais-source");
        map.put("sv4cpio", "application/x-sv4cpio");
        map.put("sv4crc", "application/x-sv4crc");
        map.put("svg", "image/svg+xml");
        map.put("svgz", "image/svg+xml");
        map.put("swf", "application/x-shockwave-flash");
        map.put("t", "text/troff");
        map.put("tar", "application/x-tar");
        map.put("tcl", "application/x-tcl");
        map.put("tex", "application/x-tex");
        map.put("texi", "application/x-texinfo");
        map.put("texinfo", "application/x-texinfo");
        map.put("tif", "image/tiff");
        map.put("tiff", "image/tiff");
        map.put("tr", "text/troff");
        map.put("tsv", "text/tab-separated-values");
        map.put("txt", "text/plain");
        map.put("ulw", "audio/basic");
        map.put("ustar", "application/x-ustar");
        map.put("vxml", "application/voicexml+xml");
        map.put("xbm", "image/x-xbitmap");
        map.put("xht", "application/xhtml+xml");
        map.put("xhtml", "application/xhtml+xml");
        map.put("xls", "application/vnd.ms-excel");
        map.put("xml", "application/xml");
        map.put("xpm", "image/x-xpixmap");
        map.put("xsl", "application/xml");
        map.put("xslt", "application/xslt+xml");
        map.put("xul", "application/vnd.mozilla.xul+xml");
        map.put("xwd", "image/x-xwindowdump");
        map.put("vsd", "application/vnd.visio");
        map.put("wav", "audio/x-wav");
        map.put("wbmp", "image/vnd.wap.wbmp");
        map.put("wml", "text/vnd.wap.wml");
        map.put("wmlc", "application/vnd.wap.wmlc");
        map.put("wmls", "text/vnd.wap.wmlsc");
        map.put("wmlscriptc", "application/vnd.wap.wmlscriptc");
        map.put("wmv", "video/x-ms-wmv");
        map.put("wrl", "model/vrml");
        map.put("wspolicy", "application/wspolicy+xml");
        map.put("z", "application/x-compress");
        map.put("zip", "application/zip");

        return map;
    }
}
