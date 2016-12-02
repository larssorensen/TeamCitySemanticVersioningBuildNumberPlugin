<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>
<%@ taglib prefix="authz" tagdir="/WEB-INF/tags/authz" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<c:set var="releaseBranch" value="${propertiesBean.properties['versionUpdater.releaseBranch']}" />

<tr>
  <td colspan="2">
    <em>Updates %build.number% with a Semantic Versioning formatted version number based on configuration parameters MajorVersion, MinorVersion, PatchVersion and build counter.</em><br/>
    <em>If branch being built matches Release Branch Name below, the version number is formatted according to a Semantic Versioning release number (i.e. "1.2.3"), omitting build count number, and then PatchVersion is incremented in build configuration.</em><br/>
    <em>Otherwise, the version number is formatted according to a Semantic Versioning pre-release number (i.e. "1.2.3-DEV004").</em>
  </td>
</tr>

<tr class="noBorder">
  <th>Release branch name:</th>
  <td>
      <props:textProperty name="versionUpdater.releaseBranch" />
  </td>
</tr>
