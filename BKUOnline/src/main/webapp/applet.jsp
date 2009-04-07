<!--
  Copyright 2008 Federal Chancellery Austria and
  Graz University of Technology

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8" 
  import="at.gv.egiz.bku.online.webapp.AppletDispatcher, org.apache.commons.lang.RandomStringUtils" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MOCCA Applet</title>
        <link rel="shortcut icon" href="img/favicon.ico" type="image/x-icon">
        <script type="text/javascript" src="js/deployJava.js"></script>
        <style type="text/css" media="all">@import "css/applet.css";</style>

        <META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
        <META HTTP-EQUIV="EXPIRES" CONTENT="Mon, 22 Jul 2002 11:12:01 GMT">
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE">
    </head>
    <%
        // min W/H (for de locale): 145px/145px with gui style 'tiny'
        // (vs. 152px on linux)
        int width = session.getAttribute("appletWidth") == null ? 190
                : (Integer) session.getAttribute("appletWidth"); //230 for workshop demo integration
        int height = session.getAttribute("appletHeight") == null ? 130
                : (Integer) session.getAttribute("appletHeight");
        String backgroundImg = (String) session.getAttribute("appletBackground");
        String guiStyle = (String) session.getAttribute("appletGuiStyle");
        String locale = (String) session.getAttribute("locale");
        String extension = (String) session.getAttribute("extension");
        String rand = AppletDispatcher.RAND_PREFIX +
                RandomStringUtils.randomAlphanumeric(16);

        String appletClass, appletArchive;
        if ("activation".equals(extension)) {
            appletArchive = "BKUAppletExt";
            appletClass = "at.gv.egiz.bku.online.applet.ActivationApplet.class";
        } else if ("pin".equals(extension)) {
            appletArchive = "BKUAppletExt";
            appletClass = "at.gv.egiz.bku.online.applet.PINManagementApplet.class";
        } else {
            appletArchive = "BKUApplet";
            appletClass = "at.gv.egiz.bku.online.applet.BKUApplet.class";
        }
    %>
    <body id="appletpage" style="width:<%=width%>">
            <script>
                if (!deployJava.versionCheck('1.6.0_04+')) {
                    document
                    .write('<b>Diese Anwendung benötigt die Java Platform Version 1.6.0_04 oder höher.</b>' + '<input type="submit" value="Java Platform 1.6.0_02 installieren" onclick="deployJava.installLatestJRE();">');
                } else {
                  // to enable applet caching, remove AppletDispatcher servlet,
                  // change codebase to 'applet',
                  // remove random suffix for appletArchive
                  // and remove '../' for all URL applet parameters
                    var attributes = {
                      codebase :'<%="applet/" + AppletDispatcher.DISPATCH_CTX %>',
                      code : '<%=appletClass%>',
                      archive : '<%=appletArchive + rand +".jar, commons-logging.jar, iaik_jce_me4se.jar"%>',
                      width : <%=width%>,
                      height :<%=height%>
                    };
                    var parameters = {
                      GuiStyle : '<%=guiStyle%>',
                      Locale : '<%=locale%>',
                      Background : '<%=backgroundImg%>',
                      WSDL_URL :'../../stal;jsessionid=<%=session.getId()%>?wsdl',
                      HelpURL : '../../help/',
                      SessionID : '<%=session.getId()%>',
                      RedirectURL : '../../bkuResult',
                      RedirectTarget: '_parent'
                    };
                    var version = '1.6.0_04';
                    deployJava.runApplet(attributes, parameters, version);
                }
            </script>
    </body>
</html>
