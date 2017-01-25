package engine.type;

import engine.type.youtube.NetSubtitle;
import engine.type.youtube.Settings;
import engine.type.youtube.Video;
import org.apache.commons.io.IOUtils;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utils.Logs;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pavelkuzmin on 06/10/2016.
 */
public class YouTube {

    static void process(Document pageDocument) {

        Elements youtube = pageDocument.body().select("iframe[src=*youtube.com*]");
        Logs.debug("YouTube: " + youtube.size());
    }

    public static void read(String url) throws Video.NoDocId, Video.NoSubs, Video.NoYouTubeParamV, Video.NoQuery,
            Video.HostNoGV, Video.InvalidDocId, JDOMException, IOException {

        List<List<NetSubtitle>> sub = getSubtitlesWithTranslations(url);

        Logs.debug(getTargetURL(sub.get(0).get(0)));

        Logs.debug(sub.get(0).toString());
    }

    public static List<List<NetSubtitle>> getSubtitlesWithTranslations(String link)

            throws MalformedURLException, Video.HostNoGV, Video.NoQuery, Video.NoDocId, Video.InvalidDocId,
            UnsupportedEncodingException, JDOMException, IOException, Video.NoSubs, Video.NoYouTubeParamV {

        String urlList;
        URL url;
        org.jdom.Document xmlDoc;
        List<NetSubtitle> lTracks;
        List<List<NetSubtitle>> result;

        url = new URL(link);
        result = new ArrayList<List<NetSubtitle>>();

        if (url.getHost() == null) {
            throw new Video.HostNoGV();

        } else if (url.getHost().indexOf("video.google.com") != -1) {

            HashMap<String, String> _params = getURLParams(link);
            urlList = NetSubtitle.getListURL(NetSubtitle.Method.Google, _params);
            xmlDoc = readListURL(urlList);
            lTracks = getListSubs(xmlDoc, _params);
            result = new ArrayList<List<NetSubtitle>>();
            result.add(lTracks);
            result.add(new ArrayList<NetSubtitle>());

        } else if (url.getHost().indexOf("youtube.com") != -1 || url.getHost().indexOf("youtu.be") != -1) {

            if (url.getHost().indexOf("youtu.be") != -1) {

                // http://youtu.be/c8RGPpcenZY => https://www.youtube.com/watch?v=c8RGPpcenZY
                String s;
                try { s = url.getFile(); }
                catch (Exception e) { s = " "; }

                url = new URL("https://www.youtube.com/watch?v=" + s.substring(1, s.length()));
                link = url.toString();

            } else {

                // http://www.youtube.com/watch?v=c8RGPpcenZY => https://www.youtube.com/watch?v=c8RGPpcenZY

                url = new URL(url.toString().replace("http://", "https://"));
                link = url.toString();
            }

//            if (Settings.)
//                System.out.println("(DEBUG) Final video URL: " + link);

            try {

                String _magicURL = retrieveMagicURL(link);
//                String _title = Video.retrieveVideoTitle();
                HashMap<String, String> _params = getURLParams(_magicURL);
//                setMethod();
                urlList = NetSubtitle.getListURL(NetSubtitle.Method.YouTubeSignature, _params);
                xmlDoc = readListURL(urlList);
                result = getListSubsWithTranslations(xmlDoc, _params, NetSubtitle.Method.YouTubeSignature);

            } catch (Exception ex) {

//                if (Settings.DEBUG)
//                    System.out.println("(DEBUG) Exception reading via Signature mode. Switching to Legacy mode...");

                String _magicURL = "";
                String _title = "";
                HashMap<String, String> _params = getURLParams(link);
//                setMethod();
                urlList = NetSubtitle.getListURL(NetSubtitle.Method.YouTubeLegacy, _params);
                xmlDoc = readListURL(urlList);
                result = getListSubsWithTranslations(xmlDoc, _params, NetSubtitle.Method.YouTubeLegacy);
            }

        } else
            throw new Video.HostNoGV();

        return result;
    }

    private static List<List<NetSubtitle>> getListSubsWithTranslations(org.jdom.Document xml,
                                                                       HashMap<String, String> params,
                                                                       NetSubtitle.Method method)
            throws Video.NoSubs, UnsupportedEncodingException {

        String _id;

        Element arrel, track;
        List<Element> tracks;
        int tam, i, tmpInt;
        Attribute tmpAtt;
        String tmpS, sName, sLang, sLangOrig, sLangTrans;
        List<NetSubtitle> lTracks = new ArrayList<NetSubtitle>();
        List<NetSubtitle> lTargets = new ArrayList<NetSubtitle>();
        List<List<NetSubtitle>> resultat;
        NetSubtitle tNS;

        if (xml == null)
            throw new Video.NoSubs();

        arrel = xml.getRootElement();
        tmpAtt = arrel.getAttribute("docid");
        if (tmpAtt == null)
            throw new Video.NoSubs();

        tracks = arrel.getChildren();
        tam = tracks.size();
        if (tam == 0)
            return null;
        i = 0;
        while (i < tam) {
            track = tracks.get(i);
            if (track != null) {
                tmpAtt = track.getAttribute("id");
                if (tmpAtt != null) {
                    tmpS = tmpAtt.getValue();
                    tmpInt = Integer.valueOf(tmpS);

                    //<track id="0" name="" lang_code="ca" lang_original="Català" lang_translated="Catalan" cantran="true"/>
                    //<target id="42" lang_code="ca" lang_original="Català" lang_translated="Catalan"/>

                    tmpAtt = track.getAttribute("lang_code");
                    sLang = tmpAtt.getValue();
                    tmpAtt = track.getAttribute("lang_original");
                    sLangOrig = tmpAtt.getValue();
                    tmpAtt = track.getAttribute("lang_translated");
                    sLangTrans = tmpAtt.getValue();

                    tNS = new NetSubtitle(null);

                    switch (method)
                    {
                        case Google:
                            _id = params.get("docid"); //tNS.setId(params.get("docid"));
                            break;
                        case YouTubeLegacy:
                        case YouTubeSignature:
                            _id = params.get("v"); // tNS.setId(params.get("v"));
                            break;
                        default:
                            _id = ""; // tNS.setId("");
                    }
                    tNS.setIdXML(tmpInt);
                    tNS.setLang(sLang);
                    tNS.setLangOriginal(sLangOrig);
                    tNS.setLangTranslated(sLangTrans);

                    tmpS = track.getName();
                    if ("track".equals(tmpS))
                    {
                        if ((tmpAtt = track.getAttribute("kind")) != null &&
                                (tmpS = tmpAtt.getValue()) != null &&
                                "asr".equals(tmpS))
                        {
                            tNS.setType(NetSubtitle.Tipus.YouTubeASRTrack);
                        } else
                        {
                            tmpAtt = track.getAttribute("name");
                            sName = tmpAtt.getValue();
                            tNS.setName(sName);
                            tNS.setType(NetSubtitle.Tipus.YouTubeTrack);
                        }
                        tNS.setTrack(true);
                        lTracks.add(tNS);
                    } else if ("target".equals(tmpS))
                    {
                        tNS.setType(NetSubtitle.Tipus.YouTubeTarget);
                        lTargets.add(tNS);
                    }
                }
            }
            i++;
        }

        resultat = new ArrayList<List<NetSubtitle>>();
        resultat.add(lTracks);
        resultat.add(lTargets);

        return resultat;
    }

    public static org.jdom.Document readListURL(String url) throws MalformedURLException, JDOMException, IOException {
        SAXBuilder parser = new SAXBuilder();
        InputStreamReader isr;

        isr = readURL(url);
        return parser.build(isr);
    }

    public static String readURL(InputStreamReader isr) throws IOException {
        String s;

        StringWriter writer = new StringWriter();
        IOUtils.copy(isr, writer);
        s = writer.toString();

        return s;
    }

    public static InputStreamReader readURL(String s) throws MalformedURLException, IOException {

        URL url;
        InputStreamReader isr;
        String appName, appVersion;
        URLConnection urlconn;

        appName = "xuy";
        appVersion = "0";

        url = new URL(s);

        urlconn = url.openConnection();

        urlconn.setRequestProperty("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        urlconn.setRequestProperty("Accept-Charset",
                "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
        urlconn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (compatible; " + appName + "/" + appVersion + ")");
        urlconn.connect();

        isr = new InputStreamReader(urlconn.getInputStream(), "UTF-8");
        return isr;
    }

    public static List<NetSubtitle> getListSubs(org.jdom.Document xml, HashMap<String, String> params)
            throws Video.NoSubs, UnsupportedEncodingException {

        return getListSubsWithTranslations(xml, params, NetSubtitle.Method.Google).get(0);
    }

    public static HashMap<String, String> getURLParams(String URL) throws MalformedURLException {

        URL url;
        String[] sparams;
        HashMap<String, String> mparams;
        String name, value;
        String[] as;

        url = new URL(URL);
        sparams = url.getQuery().split("&");
        mparams = new HashMap<String, String>();

        for (String param : sparams)
        {
            as = param.split("=");
            if (as.length > 1)
            {
                name = as[0];
                value = as[1];
            }
            else if (as.length > 0)
            {
                name = as[0];
                value = "";
            } else
            {
                name = "";
                value = "";
            }

            mparams.put(name, value);
        }

        return mparams;
    }

    public static String retrieveMagicURL(String YouTubeURL) throws MalformedURLException, IOException {

        String magicURL;
        InputStreamReader isr;

        isr = readURL(YouTubeURL);
        String YouTubeWebSource = readURL(isr);
        magicURL = NetSubtitle.getMagicURL(YouTubeWebSource);


//        if (Settings.DEBUG) System.out.println("(DEBUG) *Magic* URL: " + magicURL);
        return magicURL;
    }

    public static String getTargetURL(NetSubtitle source) throws UnsupportedEncodingException {
        String s;

        try
        {
            //Network.setMethod(Method.YouTubeSignature);
            s = getTargetURL(NetSubtitle.Method.YouTubeSignature, source);
            return s;
        }
        catch (Exception e) {
//            video.setMethod();
            s = getTargetURL(NetSubtitle.Method.YouTubeLegacy, source);
            return s;
        }
    }

    public static String getTargetURL(NetSubtitle.Method method, NetSubtitle source) throws UnsupportedEncodingException {
        String s;
        HashMap<String,String> params;

        switch (method) {
            // Other cases are unsupported
            case YouTubeLegacy:
                s = "http://video.google.com/timedtext?type=track&" +
                        "name=" + URLEncoder.encode(source.getName(), "UTF-8") +
                        "&lang=" + URLEncoder.encode(source.getLang(), "UTF-8") +
                        "&tlang=" + URLEncoder.encode(source.getLang(), "UTF-8") +
                        "&v=" + URLEncoder.encode(source.getId(), "UTF-8");
//                if (Settings.DEBUG) System.out.println("(DEBUG) Target URL (Legacy): " + s);
                return s;
            case YouTubeSignature:
                /* *** IMPORTANT NOTE ***
                 * Targets DO NOT use its parent video to generate the URL.
                 * Current implementation (v0.7) generates all targets (translations/languages) for ONE requested video.
                 * Therefore, these targets (translations/languages) are reused to get subtitles for other videos.
                 *
                */
                params = source.getVideo().getParams(); // due to the notes above this is WRONG: params = video.getParams();
                s = "https://www.youtube.com/api/timedtext" +
                        "?key=" + params.get("key") +
                        "&expire=" + params.get("expire") +
                        "&sparams=" + params.get("sparams") +
                        "&signature=" + params.get("signature") +
                        "&caps=" + params.get("caps") +
                        "&asr_langs=" + params.get("asr_langs") +

                        "&name=" + URLEncoder.encode(source.getName(), "UTF-8") +
                        "&lang=" + URLEncoder.encode(source.getLang(), "UTF-8") +
                        "&tlang=" + URLEncoder.encode(source.getLang(), "UTF-8") +
                        "&v=" + URLEncoder.encode(source.getId(), "UTF-8") +

                        "&type=track";

                if (NetSubtitle.Tipus.YouTubeASRTrack.equals(source.getType()))
                {
                    s += "&kind=asr";
//                    if (Settings.DEBUG) System.out.println("(DEBUG) Target ASR URL (Signature): " + s);
                }
                else
                {
//                    if (Settings.DEBUG) System.out.println("(DEBUG) Target URL (Signature): " + s);
                }

                return s;
        }
        return null;
    }
}
